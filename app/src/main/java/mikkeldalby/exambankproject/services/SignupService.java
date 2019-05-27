package mikkeldalby.exambankproject.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.activities.LoginActivity;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;

public class SignupService {
    private static final String TAG = "SignupService";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public ProgressDialog mProgressDialog;

    public String uId;

    public void signupUser(String email, String password, Customer customer, Activity activity){
        showProgressDialog(activity);
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    hideProgressDialog();
                    Toast.makeText(activity.getBaseContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    return;
                }

                uId = task.getResult().getUser().getUid();

                // Create account number
                db.collection("customers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int largestCustomerNumber = 0;
                        for (QueryDocumentSnapshot q: task.getResult()){
                            Customer c = q.toObject(Customer.class);
                            if (largestCustomerNumber < Integer.parseInt(c.getCustomernumber())){
                                largestCustomerNumber = Integer.parseInt(c.getCustomernumber());
                            }
                        }
                        customer.setCustomernumber(String.valueOf(largestCustomerNumber+1));

                        if (customer.getZipcode() > 5000){
                            customer.setRegistrationnumber("2222");
                        } else {
                            customer.setRegistrationnumber("1111");
                        }

                        List<Account> accounts = new ArrayList<>();
                        accounts.add(new Account(1+customer.getCustomernumber(), true, 0.0, "budget"));
                        accounts.add(new Account(2+customer.getCustomernumber(), false, 0.0, "business"));
                        accounts.add(new Account(3+customer.getCustomernumber(), true, 0.0, "default"));
                        accounts.add(new Account(4+customer.getCustomernumber(), true, 0.0, "pension"));
                        accounts.add(new Account(5+customer.getCustomernumber(), true, 0.0, "savings"));

                        Map<String, Object> nemid = new HashMap<>();
                        nemid.put("value", 123456);
                        nemid.put("used", false);

                        Map<String, Object> cust = new HashMap<>();
                        cust.put("adress", customer.getAdress());
                        cust.put("city", customer.getCity());
                        cust.put("cpr", customer.getCpr());
                        cust.put("customernumber", customer.getCustomernumber());
                        cust.put("firstname", customer.getFirstname());
                        cust.put("lastname", customer.getLastname());
                        cust.put("registrationnumber", customer.getRegistrationnumber());
                        cust.put("zipcode", customer.getZipcode());

                        db.collection("customers").document(uId).set(cust);

                        for (Account a: accounts) {
                            db.collection("customers").document(uId).collection("accounts").document(a.getCustomname()).set(a);
                        }

                        db.collection("customers").document(uId).collection("nemid").document("1234").set(nemid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideProgressDialog();
                                Toast.makeText(activity.getBaseContext(), "User created please login", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(activity, LoginActivity.class);
                                activity.startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
    public void showProgressDialog(Activity activity) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Creating user...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}