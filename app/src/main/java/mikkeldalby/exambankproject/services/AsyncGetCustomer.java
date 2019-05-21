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
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.models.Department;

public class AsyncGetCustomer {
    private static final String TAG = "AsyncGetCustomer";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AccountsFragment accountsFragment;
    private NavigationActivity navigationActivity;
    private Customer c;

    public AsyncGetCustomer(AccountsFragment accountsFragment){
        this.accountsFragment = accountsFragment;
    }

    public AsyncGetCustomer(NavigationActivity navigationActivity) {
        this.navigationActivity = navigationActivity;
    }

    public void doInBackground() {
        db.collection("customers").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "getCustomer Success");
                    c = task.getResult().toObject(Customer.class);
                    c.setCustomerId(task.getResult().getId());
                    getAccounts();
                } else {
                    Log.d(TAG, "getCustomer Fail");
                }
            }
        });
    }
    private void getAccounts(){
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
                    getDepartment();
                } else {
                    Log.d(TAG, "Get customers failed");
                }
            }
        });
    }
    private void getDepartment(){
        db.collection("departments").document(c.getRegistrationnumber()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Department department = task.getResult().toObject(Department.class);
                    department.setRegistrationNumber(task.getResult().getId());
                    c.setDepartment(department);
                    Log.d(TAG, c.toString());
                    accountsFragment.updateUi(c);
                } else {
                    Log.d(TAG, "Get department failed");
                }
            }
        });
    }

    public void snapshotListener(){
        db.collection("customers").document(auth.getCurrentUser().getUid()).collection("accounts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                doInBackground();
            }
        });
    }

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