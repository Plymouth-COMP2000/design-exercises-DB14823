package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val backButton: ImageView = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            startActivity(Intent(this, CustomerDashboardActivity::class.java))
        }
    }
}
