package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CustomerLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        val emailInput = findViewById<EditText>(R.id.edtCustomerEmail)
        val passwordInput = findViewById<EditText>(R.id.edtCustomerPassword)
        val continueButton = findViewById<Button>(R.id.btnCustomerContinue)
        val createAccount = findViewById<TextView>(R.id.txtCreateAccount)

        continueButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CustomerDashboardActivity::class.java))
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        createAccount.setOnClickListener {
            Toast.makeText(this, "Redirect to Create Account screen", Toast.LENGTH_SHORT).show()
        }
    }
}
