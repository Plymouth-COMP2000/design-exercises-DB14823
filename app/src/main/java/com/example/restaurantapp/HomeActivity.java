package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnCustomer = findViewById(R.id.btnCustomerLogin);
        Button btnStaff = findViewById(R.id.btnStaffLogin);
        Button btnGuest = findViewById(R.id.btnViewMenu);

        btnCustomer.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerLoginActivity.class)));

        /*btnStaff.setOnClickListener(v ->
                startActivity(new Intent(this, StaffLoginActivity.class))); // you can stub this for now

        btnGuest.setOnClickListener(v ->
                startActivity(new Intent(this, MenuActivity.class))); */// goes straight to menu, no backend
    }
}
