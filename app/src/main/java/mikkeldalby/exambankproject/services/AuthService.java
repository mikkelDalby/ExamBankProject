package mikkeldalby.exambankproject.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.activities.NemidActivity;

public class AuthService {
    private static final String TAG = "AuthService";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Activity activity;
    public ProgressDialog mProgressDialog;

    public AuthService(Activity activity){
        this.activity = activity;
    }

    public void signIn(String email, String password){
        Log.d(TAG, "signIn()");
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    nemidVerification();
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(activity, activity.getString(R.string.error_authentication_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void nemidVerification(){
        showProgressDialog();
        db.collection("customers").document(firebaseAuth.getCurrentUser().getUid()).collection("nemid").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Random r = new Random();
                            int i = r.nextInt(task.getResult().size());
                            Log.d(TAG, "Nemid: "+task.getResult().getDocuments().get(i).getId());
                            Log.d(TAG, "Nemid value: " + task.getResult().getDocuments().get(i).getLong("value"));
                            int key = Integer.parseInt(task.getResult().getDocuments().get(i).getId());

                            Intent intent = new Intent(activity, NemidActivity.class);
                            intent.putExtra("key", key);
                            activity.startActivity(intent);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public boolean validateNemidKey(int key, int value){
        boolean valid = true;
        return valid;
    }

    /**
     * Show and hide progressDialog in the activity defined in this class
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setMessage("Logging in...");
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