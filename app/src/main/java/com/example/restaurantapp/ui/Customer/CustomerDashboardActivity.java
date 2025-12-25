package com.example.restaurantapp.ui.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;

public class CustomerDashboardActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_dashboard);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        ImageView imgNotification = findViewById(R.id.imgNotification);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        Button btnViewMenu = findViewById(R.id.btnViewMenu);
        Button btnViewBookings = findViewById(R.id.btnViewBookings);
        TextView txtLogout = findViewById(R.id.txtLogout);

        if (!username.isEmpty()) {
            String displayName = getIntent().getStringExtra("DISPLAY_NAME");
            if (displayName != null) {
                String firstName = displayName.split(" ")[0];
                txtWelcome.setText("Welcome back, " + firstName);
            }

        }

        btnViewMenu.setOnClickListener(v -> {
            Intent i = new Intent(this, MenuActivity.class);
            i.putExtra("USERNAME", username);
            startActivity(i);
        });

        btnViewBookings.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerBookingsActivity.class);
            i.putExtra("USERNAME", username);

            String displayName = getIntent().getStringExtra("DISPLAY_NAME");
            i.putExtra("DISPLAY_NAME", displayName);

            startActivity(i);
        });


        imgNotification.setOnClickListener(v -> {
            Intent i = new Intent(this, CustomerNotificationsActivity.class);
            i.putExtra("USERNAME", username);
            startActivity(i);
        });

        txtLogout.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        Toast.makeText(this, "Customer dashboard loaded", Toast.LENGTH_SHORT).show();
    }
}
