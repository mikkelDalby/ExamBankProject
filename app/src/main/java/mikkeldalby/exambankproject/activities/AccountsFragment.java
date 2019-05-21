package mikkeldalby.exambankproject.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.AsyncGetCustomer;

public class AccountsFragment extends Fragment {
    private static final String TAG = "AccountsFragment";
    private View view;

    public TableLayout accountsTable;
    private AsyncGetCustomer asyncGetCustomer = new AsyncGetCustomer(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_accounts, null);
        init();
        asyncGetCustomer.doInBackground();
        return view;
    }

    public void init(){
        asyncGetCustomer.snapshotListener();
    }

    public void updateUi(Customer customer){
        Log.d(TAG, "Update UI");
        accountsTable = view.findViewById(R.id.account_table);

        accountsTable.removeAllViewsInLayout();

        Log.d(TAG, customer.toString());
        Collections.sort(customer.getAccounts(), new Comparator<Account>() {
            @Override
            public int compare(Account abc1, Account abc2) {
                return Boolean.compare(abc2.isActive(),abc1.isActive());
            }
        });
        for (final Account a: customer.getAccounts()){
            LayoutInflater li = LayoutInflater.from(view.getContext());
            View tr = li.inflate(R.layout.account_row, null);

            TextView accountName = tr.findViewById(R.id.account_name);
            TextView accountBalance = tr.findViewById(R.id.account_balance);
            accountName.setText(a.getCustomname());

            if (a.isActive()) {
                accountBalance.setText(String.valueOf(a.getBalance()));
            } else {
                accountBalance.setText("Not active");
            }

            tr.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Log.d(TAG, a.getCustomname());
                    v.setBackgroundColor(R.color.design_default_color_primary_dark);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.account = a;
                    fragmentTransaction.replace(R.id.content_frame, detailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            accountsTable.addView(tr);
        }
    }
}