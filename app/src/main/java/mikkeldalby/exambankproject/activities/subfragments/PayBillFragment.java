package mikkeldalby.exambankproject.activities.subfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mikkeldalby.exambankproject.R;

public class PayBillFragment extends Fragment {
    private static final String TAG = "PayBillFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_bill, null);
        init();
        return view;
    }

    public void init() {

    };
}
