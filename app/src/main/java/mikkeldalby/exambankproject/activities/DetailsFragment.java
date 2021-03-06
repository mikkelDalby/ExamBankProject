package mikkeldalby.exambankproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Account;


public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    private View view;

    public Account account;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, null);
        init();
        Log.d(TAG, account.getCustomname());
        return view;
    }

    public void init() {

    }
}