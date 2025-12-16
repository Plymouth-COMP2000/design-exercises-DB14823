package com.example.restaurantapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.data.api.UserApi;
import com.example.restaurantapp.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Single background thread for API calls
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private EditText editUsername;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // These IDs must exist in activity_main.xml
        editUsername = findViewById(R.id.editUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = editUsername.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(MainActivity.this, "Enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            attemptLogin(username);
        });
    }

    /**
     * Attempts login by checking if the user exists on the API.
     * Passwords are NOT validated because the API does not provide
     * a proper authentication endpoint.
     */
    private void attemptLogin(String username) {
        executor.execute(() -> {
            try {
                UserApi api = new UserApi();
                User user = api.readUser(username);

                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(
                            MainActivity.this,
                            "Logged in as: " + user.usertype,
                            Toast.LENGTH_SHORT
                    ).show();

                    // NEXT STEP:
                    // role routing + session storage
                    // if ("staff".equalsIgnoreCase(user.usertype)) {
                    //     startActivity(new Intent(this, StaffHomeActivity.class));
                    // } else {
                    //     startActivity(new Intent(this, GuestHomeActivity.class));
                    // }

                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(
                                MainActivity.this,
                                "API error: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
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
