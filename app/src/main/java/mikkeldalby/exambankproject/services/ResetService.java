package mikkeldalby.exambankproject.services;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import mikkeldalby.exambankproject.activities.LoginActivity;

public class ResetService {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void resetPwd(String email, Activity activity){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(activity.getBaseContext(), "An email is sent to you", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }
}
