package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnCustomerLogin = findViewById<Button>(R.id.btnCustomerLogin)
        val btnViewMenu = findViewById<Button>(R.id.btnViewMenu)

        btnCustomerLogin.setOnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))
        }
        btnViewMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}
