package mikkeldalby.exambankproject.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import mikkeldalby.exambankproject.activities.AccountsFragment;
import mikkeldalby.exambankproject.activities.NavigationActivity;
import mikkeldalby.exambankproject.activities.TransactionFragment;
import mikkeldalby.exambankproject.activities.subfragments.TransferSelfFragment;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.models.Department;

public class CustomerService {
    private static final String TAG = "CustomerService";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AccountsFragment accountsFragment;
    private NavigationActivity navigationActivity;
    private TransactionFragment transactionFragment;
    private Customer c;

    public CustomerService(AccountsFragment accountsFragment){
        this.accountsFragment = accountsFragment;
    }

    public CustomerService(NavigationActivity navigationActivity) {
        this.navigationActivity = navigationActivity;
    }

    public CustomerService(TransactionFragment transactionFragment) {
        this.transactionFragment = transactionFragment;
    }

    public void doInBackground(String frag) {
        db.collection("customers").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "getCustomer Success");
                    c = task.getResult().toObject(Customer.class);
                    c.setCustomerId(task.getResult().getId());
                    getAccounts(frag);
                } else {
                    Log.d(TAG, "getCustomer Fail");
                }
            }
        });
    }
    private void getAccounts(String frag){
        db.collection("customers").document(auth.getCurrentUser().getUid()).collection("accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Account> accounts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Account account = document.toObject(Account.class);
                        account.setAccountType(document.getId());
                        accounts.add(account);
                    }
                    c.setAccounts(accounts);
                    getDepartment(frag);
                } else {
                    Log.d(TAG, "Get customers failed");
                }
            }
        });
    }
    private void getDepartment(String frag){
        db.collection("departments").document(c.getRegistrationnumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Department department = task.getResult().toObject(Department.class);
                    department.setRegistrationNumber(task.getResult().getId());
                    c.setDepartment(department);
                    Log.d(TAG, c.toString());

                    // If from AccountsFragment
                    switch (frag){
                        case "AccountsFragment":
                            try {
                                accountsFragment.updateUi(c);
                            } catch (NullPointerException e){
                                Log.d(TAG, "Failed to updateUI.. Trying again");
                                doInBackground(frag);
                            }
                            break;
                        case "TransactionFragment":
                            try {
                                transactionFragment.customer = c;
                            } catch (NullPointerException e){
                                Log.d(TAG, "Failed to updateUI.. Trying again");
                                doInBackground(frag);
                            }
                    }
                } else {
                    Log.d(TAG, "Get department failed");
                }
            }
        });
    }

    public void snapshotListener(String frag){
        db.collection("customers").document(auth.getCurrentUser().getUid()).collection("accounts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                Log.d(TAG, "new snapshot");
                doInBackground(frag);
            }
        });
    }

    // Used in navigationActivity for navigation header
    public void getCustomerLoggedIn(){
        db.collection("customers").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Customer customer = task.getResult().toObject(Customer.class);
                navigationActivity.name.setText(customer.getFirstname() + " " + customer.getLastname());
                navigationActivity.accountNumber.setText("Customer number: " + customer.getCustomernumber());
            }
        });
    }
}