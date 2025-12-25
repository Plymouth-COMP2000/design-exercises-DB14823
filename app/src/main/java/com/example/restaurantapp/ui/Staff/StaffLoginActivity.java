package com.example.restaurantapp.ui.Staff;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.api.UserApi;
import com.example.restaurantapp.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaffLoginActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText edtStaffNumber;
    private EditText edtStaffPassword;
    private Button btnLogin;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        edtStaffNumber = findViewById(R.id.edtStaffNumber);
        edtStaffPassword = findViewById(R.id.edtStaffPassword);
        btnLogin = findViewById(R.id.btnStaffContinue);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> {
            String staffNumber = edtStaffNumber.getText().toString().trim();
            String password = edtStaffPassword.getText().toString();

            if (TextUtils.isEmpty(staffNumber)) {
                Toast.makeText(this, "Enter your staff number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (staffNumber.length() < 6) {
                Toast.makeText(this, "Invalid staff number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            attemptLogin(staffNumber, password);
        });
    }

    private void attemptLogin(String staffNumber, String enteredPassword) {
        executor.execute(() -> {
            try {
                UserApi api = new UserApi();
                User user = api.readUser(staffNumber);

                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(this, "Staff account not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String type = (user.usertype == null) ? "" : user.usertype.trim();
                    if (!"staff".equalsIgnoreCase(type)) {
                        Toast.makeText(this, "That account is not a staff account", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String serverPassword = (user.password == null) ? "" : user.password;
                    if (!enteredPassword.equals(serverPassword)) {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(this, "Staff login successful", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(this, StaffDashboardActivity.class);
                    i.putExtra("USERNAME", user.username);
                    startActivity(i);
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "API error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
