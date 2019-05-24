package mikkeldalby.exambankproject.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Payment;

public class BillService {
    private static final String TAG = "BillService";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    /**
     * Checks if any bills need to be paid and process them
     */
    public void checkBillsToPay(){
        db.collection("customers").document(auth.getCurrentUser().getUid())
                .collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Sucess");
                    List<Payment> payments = new ArrayList<>();
                    for(QueryDocumentSnapshot q: task.getResult()){
                        Payment p = q.toObject(Payment.class);
                        p.setPaymentDocumentId(q.getId());
                        payments.add(p);
                    }

                    for (Payment p: payments){
                        if (p.getPaydate().before(new Date(System.currentTimeMillis()))
                                || p.getPaydate().equals(new Date(System.currentTimeMillis()))){
                            // Subtract money from account and delete payment from firebase
                            db.collection("customers").document(auth.getCurrentUser().getUid())
                                    .collection("accounts").document(p.getFromaccount()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Account a = task.getResult().toObject(Account.class);
                                        a.setAccountType(task.getResult().getId());
                                        double newBalance = a.getBalance() - p.getAmount();
                                        Map<String, Object> updateBalance = new HashMap<>();
                                        updateBalance.put("balance", newBalance);
                                        db.collection("customers").document(auth.getCurrentUser().getUid())
                                                .collection("accounts").document(a.getAccountType()).update(updateBalance);

                                        db.collection("customers").document(auth.getCurrentUser().getUid())
                                                .collection("payments").document(p.getPaymentDocumentId()).delete();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}