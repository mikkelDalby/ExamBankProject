package mikkeldalby.exambankproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.services.ResetService;

public class ResetActivity extends AppCompatActivity {

    public EditText email;
    public Button reset, login;

    private ResetService resetService = new ResetService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        init();
    }

    private void init() {
        email = findViewById(R.id.reset_email);
        reset = findViewById(R.id.reset_button);
        login = findViewById(R.id.reset_login);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){
                    email.setError(getString(R.string.error_required));
                } else {
                    email.setError(null);
                    resetService.resetPwd(email.getText().toString(), ResetActivity.this);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
