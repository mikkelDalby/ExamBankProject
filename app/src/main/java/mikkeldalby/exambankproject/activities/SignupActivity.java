package mikkeldalby.exambankproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mikkeldalby.exambankproject.R;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    public EditText email, password, passwordAgain;
    public Button createAccountBtn, loginBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    public void init(){
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        passwordAgain = findViewById(R.id.signup_password_again);
        createAccountBtn = findViewById(R.id.create_account_btn);
        loginBtn = findViewById(R.id.login_existing_account_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()){

                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }


    /**
     * Validates signup form
     * TODO: make validation better
     */
    private boolean validateForm(){
        boolean valid = true;

        if (email.getText().toString().isEmpty()){
            email.setError(getString(R.string.error_required));
            valid = false;
        } else {
            email.setError(null);
        }

        if (password.getText().toString().isEmpty()){
            password.setError(getString(R.string.error_required));
            valid = false;
        } else {
            password.setError(null);
        }

        if (passwordAgain.getText().toString().isEmpty()){
            passwordAgain.setError(getString(R.string.error_required));
            valid = false;
        } else {
            passwordAgain.setError(null);
        }

        return valid;
    }
}
