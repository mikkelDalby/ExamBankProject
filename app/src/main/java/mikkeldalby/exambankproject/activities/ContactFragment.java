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

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want toSpinner inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        view = inflater.inflate(R.layout.fragment_contact, null);
        setRetainInstance(true);
        init();
        return view;
    }

    public void init() {

    }
}
