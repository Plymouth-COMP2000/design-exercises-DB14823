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

public class CustomerCreateAccountActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private ImageView btnBack;
    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnCreateAccount;
    private TextView txtLoginRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_customer);

        btnBack = findViewById(R.id.btnBack);
        editTextFName = findViewById(R.id.editTextFName);
        editTextLName = findViewById(R.id.editTextLName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        txtLoginRedirect = findViewById(R.id.txtLoginRedirect);

        btnBack.setOnClickListener(v -> finish());

        txtLoginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(this, CustomerLoginActivity.class));
            finish();
        });

        btnCreateAccount.setOnClickListener(v -> {
            String Fname = text(editTextFName);
            String Lname = text(editTextLName);
            String email = text(editTextEmail);
            String password = text(editTextPassword);

            if (TextUtils.isEmpty(Fname) || TextUtils.isEmpty(Lname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.username = email;
            newUser.password = password;
            newUser.firstname = Fname;
            newUser.lastname = Lname;
            newUser.email = email;
            newUser.contact = "";
            newUser.usertype = "customer";

            createAccount(newUser);
        });
    }

    private void createAccount(User newUser) {
        executor.execute(() -> {
            try {
                UserApi api = new UserApi();

                try {
                    api.createStudent();
                } catch (Exception ignored) {
                }

                User existing = api.readUser(newUser.username);
                if (existing != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                api.createUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Account created! Please log in.", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, CustomerLoginActivity.class);
                    i.putExtra("PREFILL_USERNAME", newUser.username);
                    startActivity(i);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Create account error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private static String text(EditText e) {
        return e == null ? "" : e.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
