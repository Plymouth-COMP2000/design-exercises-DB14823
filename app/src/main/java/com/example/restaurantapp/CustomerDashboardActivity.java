package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // IMPORTANT: this must match the XML filename
        setContentView(R.layout.activity_customer_dashboard);

        // Get logged-in user
        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        // Bind views (IDs taken directly from your XML)
        ImageView imgNotification = findViewById(R.id.imgNotification);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        Button btnViewMenu = findViewById(R.id.btnViewMenu);
        Button btnViewBookings = findViewById(R.id.btnViewBookings);
        TextView txtLogout = findViewById(R.id.txtLogout);

        // Personalise welcome message (optional but nice)
        if (!username.isEmpty()) {
            String displayName = getIntent().getStringExtra("DISPLAY_NAME");
            if (displayName != null) {
                txtWelcome.setText("Welcome, " + displayName);
            }

        }

        // View Menu → menu browsing screen
        btnViewMenu.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", username);
            startActivity(i);
        });

        // View Bookings → customer's bookings list
        btnViewBookings.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", username);
            startActivity(i);
        });

        // Notifications icon → notifications screen (can be stub for now)
        imgNotification.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", username);
            startActivity(i);
        });

        // Logout → clear session and return to home screen
        txtLogout.setOnClickListener(v -> {
            // Later: clear SharedPreferences session here
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        // Sanity check (you can remove this later)
        Toast.makeText(this, "Customer dashboard loaded", Toast.LENGTH_SHORT).show();
    }
}
