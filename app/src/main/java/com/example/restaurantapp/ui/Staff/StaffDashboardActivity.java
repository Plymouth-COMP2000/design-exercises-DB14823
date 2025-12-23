package com.example.restaurantapp.ui.Staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.ui.Customer.HomeActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    private String staffNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        staffNumber = getIntent().getStringExtra("USERNAME");
        if (staffNumber == null) staffNumber = "";

        ImageView imgNotification = findViewById(R.id.imgNotification);
        Button btnManageMenu = findViewById(R.id.btnManageMenu);
        Button btnManageBookings = findViewById(R.id.btnManageBookings);
        TextView txtLogout = findViewById(R.id.txtLogout);


        btnManageMenu.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", staffNumber);
            startActivity(i);
        });

        btnManageBookings.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", staffNumber);
            startActivity(i);
        });

        imgNotification.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.putExtra("USERNAME", staffNumber);
            startActivity(i);
        });

        txtLogout.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        Toast.makeText(this, "Staff dashboard loaded", Toast.LENGTH_SHORT).show();
    }
}
