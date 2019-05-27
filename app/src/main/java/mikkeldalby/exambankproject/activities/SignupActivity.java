package mikkeldalby.exambankproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.models.Customer;
import mikkeldalby.exambankproject.services.SignupService;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    public EditText email, password, passwordAgain, firstName, lastName, adress, zipcode, city, cpr;
    public Button createAccountBtn, loginBtn;

    private SignupService signupService = new SignupService();

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
        firstName = findViewById(R.id.signup_firstname);
        lastName = findViewById(R.id.signup_lastname);
        adress = findViewById(R.id.signup_adress);
        zipcode = findViewById(R.id.signup_zipcode);
        city = findViewById(R.id.signup_city);
        cpr = findViewById(R.id.signup_cpr);
        createAccountBtn = findViewById(R.id.create_account_btn);
        loginBtn = findViewById(R.id.login_existing_account_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()){
                    Customer c = new Customer();
                    c.setFirstname(firstName.getText().toString());
                    c.setLastname(lastName.getText().toString());
                    c.setAdress(adress.getText().toString());
                    c.setZipcode(Integer.parseInt(zipcode.getText().toString()));
                    c.setCity(city.getText().toString());
                    c.setCpr(cpr.getText().toString());
                    signupService.signupUser(email.getText().toString(), password.getText().toString(), c, SignupActivity.this);
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

        if (firstName.getText().toString().isEmpty()){
            firstName.setError(getString(R.string.error_required));
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (lastName.getText().toString().isEmpty()){
            lastName.setError(getString(R.string.error_required));
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (adress.getText().toString().isEmpty()){
            adress.setError(getString(R.string.error_required));
            valid = false;
        } else {
            adress.setError(null);
        }

        if (zipcode.getText().toString().isEmpty()){
            zipcode.setError(getString(R.string.error_required));
            valid = false;
        } else {
            zipcode.setError(null);
        }

        if (city.getText().toString().isEmpty()){
            city.setError(getString(R.string.error_required));
            valid = false;
        } else {
            city.setError(null);
        }

        if (cpr.getText().toString().isEmpty()){
            cpr.setError(getString(R.string.error_required));
            valid = false;
        } else {
            cpr.setError(null);
        }

        return valid;
    }
}