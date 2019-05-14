package mikkeldalby.exambankproject.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.services.AuthService;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private AuthService authService = new AuthService(this);

    public EditText email, password;
    public Button loginBtn, createAccountBtn;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Initialization method of this activity
     * Responsible for mapping views to variables and setup onclick methods
     */
    private void init(){
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        createAccountBtn = findViewById(R.id.login_create_account_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (validateForm()) {
                    authService.signIn(email.getText().toString(), password.getText().toString());
                }
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        Log.d(TAG, "Islogged in: " + authService.isLoggedIn());
    }

    /**
     * Validates the login form
     * TODO: Setup more validation, Check if email is a valid email
     */
    private boolean validateForm(){
        boolean valid = true;

        if (email.getText().toString().isEmpty()){
            email.setError(getString(R.string.error_required));
            valid = false;
        } else {
            email.setError(null);
        }

        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.error_required));
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}