package com.example.restaurantapp.ui.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.api.UserApi;
import com.example.restaurantapp.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerLoginActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private ImageView btnBack;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnContinue;
    private TextView txtCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        btnBack = findViewById(R.id.btnBack);
        edtEmail = findViewById(R.id.edtCustomerEmail);
        edtPassword = findViewById(R.id.edtCustomerPassword);
        btnContinue = findViewById(R.id.btnCustomerContinue);
        txtCreateAccount = findViewById(R.id.txtCreateAccount);

        btnBack.setOnClickListener(v -> finish());

        String prefill = getIntent().getStringExtra("PREFILL_USERNAME");
        if (prefill != null) {
            edtEmail.setText(prefill);
        }

        txtCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, CustomerCreateAccountActivity.class));
        });

        btnContinue.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            attemptLogin(email, password);
        });
    }

    private void attemptLogin(String username, String enteredPassword) {
        executor.execute(() -> {
            try {
                UserApi api = new UserApi();
                User user = api.readUser(username);

                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("DEBUG usertype = " + user.usertype);

                    if (!"customer".equalsIgnoreCase(user.usertype)) {
                        Toast.makeText(this, "That account is not a customer account", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (!TextUtils.isEmpty(user.password) && !enteredPassword.equals(user.password)) {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, CustomerDashboardActivity.class);
                    i.putExtra("USERNAME", user.username);
                    i.putExtra("DISPLAY_NAME", user.firstname);
                    startActivity(i);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "API error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
