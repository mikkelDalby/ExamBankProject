package mikkeldalby.exambankproject.activities.subfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mikkeldalby.exambankproject.R;
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
    }
}
