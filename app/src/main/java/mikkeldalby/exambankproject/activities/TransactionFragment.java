package mikkeldalby.exambankproject.activities;

import android.app.ProgressDialog;
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
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.CustomerService;

public class TransactionFragment extends Fragment {
    private static final String TAG = "TransactionFragment";
    private View view;
    private CustomerService customerService = new CustomerService(this);

    public Customer customer;

    public Button btnSelf, btnOther, btnBill;

    public ProgressDialog mProgressDialog;

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
            showSelf();
        });

        btnOther.setOnClickListener(v -> {
            // Set other view
            showOther();
        });

        btnBill.setOnClickListener(v -> {
            // Set pay bill view
            showPayBill();
        });

        customerService.doInBackground(TAG);
    }

    public void showSelf(){
        showProgressDialog();
        TransferSelfFragment transferSelfFragment = new TransferSelfFragment();
        try {
            if (customer != null) {
                transferSelfFragment.customer = customer;
                hideProgressDialog();
                getFragmentManager().beginTransaction().replace(R.id.transaction_frame, transferSelfFragment).commit();
            }
        } catch (NullPointerException e){
            showSelf();
        }
    }
    public void showOther(){
        showProgressDialog();
        TransferOtherFragment transferOtherFragment = new TransferOtherFragment();
        try {
            if (customer != null) {
                transferOtherFragment.customer = customer;
                hideProgressDialog();
                getFragmentManager().beginTransaction().replace(R.id.transaction_frame, transferOtherFragment).addToBackStack(null).commit();
            }
        } catch (NullPointerException e){
            showSelf();
        }
    }
    public void showPayBill(){
        showProgressDialog();
        PayBillFragment payBillFragment = new PayBillFragment();
        try {
            if (customer != null) {
                payBillFragment.customer = customer;
                hideProgressDialog();
                getFragmentManager().beginTransaction().replace(R.id.transaction_frame, payBillFragment).addToBackStack(null).commit();
            }
        } catch (NullPointerException e){
            showSelf();
        }
    }

    /**
     * Show and hide progressDialog in the activity defined in this class
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}