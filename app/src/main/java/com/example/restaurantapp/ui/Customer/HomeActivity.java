package com.example.restaurantapp.ui.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;

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

        /* btnStaff.setOnClickListener(v ->
                startActivity(new Intent(this, StaffLoginActivity.class))); */

        btnGuest.setOnClickListener(v ->
                startActivity(new Intent(this, MenuActivity.class)));
    }
}
