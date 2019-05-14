package mikkeldalby.exambankproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mikkeldalby.exambankproject.R;
import mikkeldalby.exambankproject.services.AuthService;

public class NemidActivity extends AppCompatActivity {
    private static final String TAG = "NemidActivity";

    private AuthService authService = new AuthService(this);

    public TextView nemidKey;
    public EditText nemidValue;
    public Button loginButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nemid);
        init();
    }

    public void init(){
        Log.d(TAG, "Inside init");
        nemidKey = findViewById(R.id.txt_nemid_key);
        nemidValue = findViewById(R.id.txt_nemid_value);
        loginButton = findViewById(R.id.nemid_login_btn);
        backButton = findViewById(R.id.back_loginpage_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NemidActivity.this, NavigationActivity.class);
                authService.validateNemidKey(Integer.parseInt(nemidKey.getText().toString()), nemidValue, intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authService.logout();
                Intent i = new Intent(NemidActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        int k = getIntent().getIntExtra("key", 0);
        if (k != 0){
            nemidKey.setText(String.valueOf(k));
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
