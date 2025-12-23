package com.example.restaurantapp.ui.Customer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;

public class NotificationsActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notifications);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        Toast.makeText(this, "Notifications opened", Toast.LENGTH_SHORT).show();

    }
}
