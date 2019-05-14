package mikkeldalby.exambankproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mikkeldalby.exambankproject.R;

public class AccountsFragment extends Fragment {
    private View view;


    public TableLayout accountsTable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        view = inflater.inflate(R.layout.fragment_accounts, null);
        init();
        return view;
    }

    public void init(){
        accountsTable = view.findViewById(R.id.account_table);

        List<String> stringList = new ArrayList<>();
        stringList.add("Test");
        stringList.add("Test2");

        for (String i: stringList){
            LayoutInflater li = LayoutInflater.from(view.getContext());
            View tr = li.inflate(R.layout.account_row, null);
            TextView accountName = tr.findViewById(R.id.account_name);
            TextView accountBalance = tr.findViewById(R.id.account_balance);
            accountName.setText(i);
            accountBalance.setText(i);
            accountsTable.addView(tr);
        }
    }
}