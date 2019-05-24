package mikkeldalby.exambankproject.activities.subfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Account;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.TransactionService;

public class PayBillFragment extends Fragment {
    private static final String TAG = "PayBillFragment";
    private View view;

    public Customer customer;
    private List<String> fromAccounts = new ArrayList<>();
    private TransactionService transactionService = new TransactionService();

    public Spinner fromSpinner;
    public EditText billType, payId, creditor, amount;
    public DatePicker datePicker;
    public Button payBtn;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_bill, null);
        init();
        return view;
    }

    public void init() {
        fromSpinner = view.findViewById(R.id.pay_from_spinner);
        billType = view.findViewById(R.id.pay_bill_type);
        payId = view.findViewById(R.id.pay_payid);
        creditor = view.findViewById(R.id.pay_creditor);
        amount = view.findViewById(R.id.pay_amount);
        datePicker = view.findViewById(R.id.pay_date_picker);
        payBtn = view.findViewById(R.id.pay_button);

        datePicker.setMinDate(System.currentTimeMillis());

        fromAccounts = customer.getPossibleAccounts();

        ArrayAdapter fromAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, fromAccounts);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if (billType.getText().toString().isEmpty()){
                    billType.setError("Please enter billtype");
                    valid = false;
                } else {
                    billType.setError(null);
                }
                if (payId.getText().toString().isEmpty()){
                    payId.setError("Please enter Pay id");
                    valid = false;
                } else {
                    payId.setError(null);
                }
                if (creditor.getText().toString().isEmpty()){
                    creditor.setError("Please enter creditor");
                    valid = false;
                } else {
                    creditor.setError(null);
                }
                if (amount.getText().toString().isEmpty()){
                    amount.setError("Please enter Amount to transfer");
                    valid = false;
                } else {
                    amount.setError(null);
                }

                if (valid) {
                    String from = "";
                    for (Account a : customer.getAccounts()) {
                        if (fromAccounts.get(fromSpinner.getSelectedItemPosition()).equals(a.getCustomname())) {
                            from = a.getAccountType();
                        }
                    }
                    String type = billType.getText().toString();
                    String id = payId.getText().toString();
                    String cred = creditor.getText().toString();
                    String amountDKK = amount.getText().toString();

                    Calendar date = Calendar.getInstance();
                    date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                    transactionService.payBill(from, type, id, cred, amountDKK, date, view.getContext());

                    resetFields();
                }
            }
        });
    }

    public void resetFields(){
        billType.setText("");
        payId.setText("");
        creditor.setText("");
        amount.setText("");
    }
}