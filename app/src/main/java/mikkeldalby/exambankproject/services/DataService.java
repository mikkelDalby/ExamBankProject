package mikkeldalby.exambankproject.services;

import android.app.Activity;

import com.google.firebase.firestore.FirebaseFirestore;

public class DataService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Activity activity;

    public DataService(Activity activity){
        this.activity = activity;
    }
}