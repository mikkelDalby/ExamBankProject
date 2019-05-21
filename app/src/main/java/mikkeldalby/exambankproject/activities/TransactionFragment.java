package mikkeldalby.exambankproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.activities.subfragments.PayBillFragment;
import mikkeldalby.exambankproject.activities.subfragments.TransferOtherFragment;
import mikkeldalby.exambankproject.activities.subfragments.TransferSelfFragment;

public class TransactionFragment extends Fragment {
    private static final String TAG = "TransactionFragment";
    private View view;

    public Button btnSelf, btnOther, btnBill;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, null);
        init();
        return view;
    }

    public void init() {
        btnSelf = view.findViewById(R.id.transaction_self);
        btnOther = view.findViewById(R.id.transaction_others);
        btnBill = view.findViewById(R.id.transaction_pay_bill);

        btnSelf.setOnClickListener(v -> {
            // Set self view
            TransferSelfFragment transferSelfFragment = new TransferSelfFragment();
            getFragmentManager().beginTransaction().replace(R.id.transaction_frame, transferSelfFragment).commit();
        });

        btnOther.setOnClickListener(v -> {
            // Set other view
            TransferOtherFragment transferOtherFragment = new TransferOtherFragment();
            getFragmentManager().beginTransaction().replace(R.id.transaction_frame, transferOtherFragment).commit();
        });

        btnBill.setOnClickListener(v -> {
            // Set pay bill view
            PayBillFragment payBillFragment = new PayBillFragment();
            getFragmentManager().beginTransaction().replace(R.id.transaction_frame, payBillFragment).commit();
        });
    }
}