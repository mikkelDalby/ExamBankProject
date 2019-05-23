package mikkeldalby.exambankproject.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;

public class TransactionService {
    private static final String TAG = "TransactionService";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AlertDialog dialog;

    public String mText = "";
    private Context context;

    private String typeOfTransfer = "";

    // To use in transfer to other
    String otherFromAccount = "";
    int otherRegNumber = 0;
    int otherAccountNumber = 0;
    double otherAmount = 0.0;
    Customer toCustomer = new Customer();
    List<Account> accounts = new ArrayList<>();

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

    /**
     * Should ask for nemid before transfering
     */
    public void transferOther(String fromAccount, int regNumber, int accountNumber, double amountDkk, Context thisContext){
        context = thisContext;
        typeOfTransfer = "other";
        showDialog();
        otherFromAccount = fromAccount;
        otherRegNumber = regNumber;
        otherAccountNumber = accountNumber;
        otherAmount = amountDkk;

        nemidVerification();
    }
    private void processTransferToOther(){
        // Find customer to transfer to
        db.collection("customers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Customer> customers = new ArrayList<>();
                    for (QueryDocumentSnapshot q: task.getResult()){
                        Customer c = q.toObject(Customer.class);
                        c.setCustomerId(q.getId());
                        customers.add(c);
                    }
                    int customerNumber = getCustomerNumberFromAccountNumber();

                    boolean foundCustomer = false;
                    for (Customer c: customers){
                        if (c.getCustomernumber().equals(String.valueOf(customerNumber)) && c.getRegistrationnumber().equals(String.valueOf(otherRegNumber))){
                            foundCustomer = true;
                            toCustomer = c;
                        }
                    }

                    if (foundCustomer){
                        Log.d(TAG, "Customer found");
                        Log.d(TAG, "Customer id: "+ toCustomer.getCustomerId());

                        // Check if account is active
                        db.collection("customers").document(toCustomer.getCustomerId()).collection("accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                for (QueryDocumentSnapshot q: task.getResult()){
                                    Account a = q.toObject(Account.class);
                                    a.setAccountType(q.getId());
                                    accounts.add(a);
                                }

                                String accountType = "";
                                Account toAccount = new Account();
                                boolean accountActive = false;
                                for (Account a: accounts){
                                    if (a.isActive() && a.getAccountnumber().equals(String.valueOf(otherAccountNumber))){
                                        Log.d(TAG, a.getAccountnumber()+"AccountNumber");
                                        accountType = a.getAccountType();
                                        toAccount = a;
                                        accountActive = true;
                                    }
                                }

                                if (accountActive){
                                    // Update to customer account
                                    double newBalance = toAccount.getBalance() + otherAmount;
                                    Map<String, Object> toCustomerBalanceUpdate = new HashMap<>();
                                    toCustomerBalanceUpdate.put("balance", newBalance);
                                    db.collection("customers").document(toCustomer.getCustomerId())
                                            .collection("accounts").document(accountType).update(toCustomerBalanceUpdate);

                                    // Update from account
                                    db.collection("customers").document(auth.getCurrentUser().getUid())
                                            .collection("accounts").document(otherFromAccount).get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()){
                                            Log.d(TAG, task2.getResult().get("balance").toString());
                                            double currentBal = Double.parseDouble(task2.getResult().get("balance").toString());
                                            currentBal = currentBal - otherAmount;

                                            Map<String, Object> balanceUpdate = new HashMap<>();
                                            balanceUpdate.put("balance", currentBal);

                                            db.collection("customers").document(auth.getCurrentUser().getUid())
                                                    .collection("accounts").document(otherFromAccount).update(balanceUpdate);
                                            Toast.makeText(context, "Transfer successfull", Toast.LENGTH_LONG).show();

                                        } else {
                                            Log.d(TAG, "Something went wrong");
                                        }

                                    });

                                } else {
                                    Log.d(TAG, "Account not active");
                                    Toast.makeText(context, "Account not active, please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "No customer found");
                        Toast.makeText(context, "No customer found with provided registration and account number, please try again", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(context, "Transfer unsuccesfull, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Gets a random nemid key from firebase and calls showNemidPopup method with the key
     */
    public void nemidVerification(){
        db.collection("customers").document(auth.getCurrentUser().getUid()).collection("nemid").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Random r = new Random();
                            int i = r.nextInt(task.getResult().size());
                            Log.d(TAG, "Nemid: "+task.getResult().getDocuments().get(i).getId());
                            Log.d(TAG, "Nemid value: " + task.getResult().getDocuments().get(i).getLong("value"));
                            int key = Integer.parseInt(task.getResult().getDocuments().get(i).getId());
                            showNemidPopup(String.valueOf(key));
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * Validates the entered nemid value for a specific key
     */
    public void validateNemidKey(int key, int value){
        db.collection("customers").document(auth.getCurrentUser().getUid())
                .collection("nemid").document(String.valueOf(key)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, "Validating nemid");
                if (task.isSuccessful()) {
                    Log.d(TAG, "Nemid: "+task.getResult().getId());
                    Log.d(TAG, "Nemid value: " + task.getResult().getLong("value"));
                    int val = Integer.parseInt(task.getResult().getLong("value").toString());
                    if (val == value){
                        Log.d(TAG, "Valid nemid value");
                        switch (typeOfTransfer){
                            case "other":
                                processTransferToOther();
                                break;
                        }
                    } else {
                        Log.d(TAG, "Invalid nemid value");
                        showNemidPopup(String.valueOf(key));
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }


    /**
     * Returns the customernumber from the account number
     * Removes the first digit in the variable other otherAccountNumber and returns result
     */
    private int getCustomerNumberFromAccountNumber(){
        return Integer.parseInt(String.valueOf(otherAccountNumber).substring(1));
    }

    /**
     * Different dialog methods
     */
    public void showNemidPopup(String key){
        hideDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nemid");
        builder.setMessage("Write value for key: " + key);

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validateNemidKey(Integer.parseInt(key), Integer.parseInt(input.getText().toString()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();
        dialog.show();
    }
    public void hideDialog(){
        dialog.dismiss();
    }
}
