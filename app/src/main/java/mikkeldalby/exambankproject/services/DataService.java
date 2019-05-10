package mikkeldalby.exambankproject.services;

import com.google.firebase.firestore.FirebaseFirestore;

import mikkeldalby.exambankproject.activities.LoginActivity;

public class DataService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LoginActivity activity;

    public DataService(LoginActivity activity){
        this.activity = activity;
    }
}
