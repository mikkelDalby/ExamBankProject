package mikkeldalby.exambankproject.activities.subfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.activities.TransactionFragment;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.TransactionService;

public class TransferOtherFragment extends Fragment {
    private static final String TAG = "TransferOtherFragment";
    private View view;

    public Spinner fromSpinner;
    public EditText reg, account, amount;
    public Button transferBtn;

    public Customer customer;
    private List<String> fromAccounts = new ArrayList<>();

    private TransactionService transactionService = new TransactionService();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transfer_other, null);
        init();
        return view;
    }

    public void init() {
        fromSpinner = view.findViewById(R.id.other_spinner_from);
        reg = view.findViewById(R.id.other_reg_number);
        account = view.findViewById(R.id.other_account_number);
        amount = view.findViewById(R.id.other_amount);
        transferBtn = view.findViewById(R.id.other_transfer_button);

        fromAccounts = customer.getPossibleAccounts();

        ArrayAdapter fromAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, fromAccounts);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                if (reg.getText().toString().isEmpty()){
                    reg.setError("Please enter regNumber");
                    valid = false;
                } else {
                    reg.setError(null);
                }
                if (account.getText().toString().isEmpty()){
                    account.setError("Please enter Account number");
                    valid = false;
                } else {
                    account.setError(null);
                }
                if (amount.getText().toString().isEmpty()){
                    amount.setError("Please enter amount to transfer");
                    valid = false;
                } else {
                    amount.setError(null);
                }

                if (valid) {
                    String fromAccount = "";
                    int regNumber = Integer.parseInt(reg.getText().toString());
                    int accountNumber = Integer.parseInt(account.getText().toString());
                    double amountDkk = Double.parseDouble(amount.getText().toString());

                    // Finds the from account
                    String selectedFrom = fromAccounts.get(fromSpinner.getSelectedItemPosition());
                    for (Account a : customer.getAccounts()) {
                        if (a.getCustomname().equals(selectedFrom)) {
                            fromAccount = a.getAccountType();
                        }
                    }

                    transactionService.transferOther(fromAccount, regNumber, accountNumber, amountDkk, view.getContext());
                    reg.setText("");
                    account.setText("");
                    amount.setText("");
                }
            }
        });
    }
}
