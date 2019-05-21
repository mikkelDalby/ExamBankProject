package mikkeldalby.exambankproject.services;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TransactionService {
    private static final String TAG = "TransactionService";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void transferSelf(String fromAccount, String toAccount, double value, View view) {

        // From this account
        db.collection("customers").document(auth.getCurrentUser().getUid())
                .collection("accounts").document(fromAccount).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, task.getResult().get("balance").toString());
                        double currentBal = Double.parseDouble(task.getResult().get("balance").toString());
                        currentBal = currentBal - value;

                        Map<String, Object> balanceUpdate = new HashMap<>();
                        balanceUpdate.put("balance", currentBal);

                        db.collection("customers").document(auth.getCurrentUser().getUid())
                                .collection("accounts").document(fromAccount).update(balanceUpdate);

                    } else {
                        Log.d(TAG, "Something went wrong");
                    }

        });
        // To this account
        db.collection("customers").document(auth.getCurrentUser().getUid())
                .collection("accounts").document(toAccount).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, task.getResult().get("balance").toString());
                double currentBal = Double.parseDouble(task.getResult().get("balance").toString());
                currentBal = currentBal + value;

                Map<String, Object> balanceUpdate = new HashMap<>();
                balanceUpdate.put("balance", currentBal);

                db.collection("customers").document(auth.getCurrentUser().getUid())
                        .collection("accounts").document(toAccount).update(balanceUpdate);

                Toast.makeText(view.getContext(), "Transfer success", Toast.LENGTH_LONG).show();

            } else {
                Log.d(TAG, "Something went wrong");
            }

        });
    }
}
