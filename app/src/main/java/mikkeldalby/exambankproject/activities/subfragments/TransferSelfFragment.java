package mikkeldalby.exambankproject.activities.subfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.TransactionService;

public class TransferSelfFragment extends Fragment {
    private static final String TAG = "TransferSelfFragment";
    private View view;

    public Spinner from, to;
    public EditText amount;
    public Button transferBtn;

    public Customer customer;
    private List<String> fromAccounts = new ArrayList<>();
    private List<String> toAccounts = new ArrayList<>();

    private TransactionService transactionService = new TransactionService();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transfer_to_self, null);
        init();
        return view;
    }

    public void init() {
        from = view.findViewById(R.id.self_spinner_from);
        to = view.findViewById(R.id.self_spinner_to);
        amount = view.findViewById(R.id.self_amount);
        transferBtn = view.findViewById(R.id.self_button_transfer);
        fromAccounts.clear();

        // TODO: Check if old enough to transfer from pension account
        for (Account a: customer.getAccounts()){
            if (a.isActive()) {
                fromAccounts.add(a.getCustomname());
            }
        }

        ArrayAdapter aa = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, fromAccounts);
        ArrayAdapter bb = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, toAccounts);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        from.setAdapter(aa);
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, fromAccounts.get(position));

                toAccounts.clear();
                for (Account a: customer.getAccounts()){
                    if (a.isActive() && !a.getCustomname().equals(fromAccounts.get(position))) {
                        toAccounts.add(a.getCustomname());
                    }
                }

                bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                to.setAdapter(bb);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()){
                    amount.setError("Type in amount");
                } else {
                    amount.setError(null);
                    Log.d(TAG, "Transfer in progress...");
                    String fromAccount = "";
                    String toAccount = "";
                    double value = Double.parseDouble(amount.getText().toString());

                    String selectedFrom = fromAccounts.get(from.getSelectedItemPosition());
                    String selectedTo = toAccounts.get(to.getSelectedItemPosition());
                    for (Account a: customer.getAccounts()){
                        if (a.getCustomname().equals(selectedFrom)){
                            fromAccount = a.getAccountType();
                        }
                        if (a.getCustomname().equals(selectedTo)){
                            toAccount = a.getAccountType();
                        }
                    }
                    Log.d(TAG, "From: "+fromAccount);
                    transactionService.transferSelf(fromAccount, toAccount, value, view);
                    amount.setText("");
                }
            }
        });
    }
}