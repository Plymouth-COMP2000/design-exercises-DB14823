package com.example.restaurantapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt

class CustomerLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        val backButton = findViewById<ImageView>(R.id.btnBack)
        val emailInput = findViewById<EditText>(R.id.edtCustomerEmail)
        val passwordInput = findViewById<EditText>(R.id.edtCustomerPassword)
        val continueButton = findViewById<Button>(R.id.btnCustomerContinue)
        val createAccountText = findViewById<TextView>(R.id.txtCreateAccount)

        // ✅ Make only "here" clickable & blue
        val fullText = "Not a user? Create account here"
        val spannable = SpannableString(fullText)

        val clickablePart = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@CustomerLoginActivity, CreateAccountActivity::class.java))
            }
        }

        val startIndex = fullText.indexOf("here")
        val endIndex = startIndex + "here".length

        spannable.setSpan(clickablePart, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
            ForegroundColorSpan("#007AFF".toColorInt()),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        createAccountText.text = spannable
        createAccountText.movementMethod = LinkMovementMethod.getInstance()
        createAccountText.highlightColor = Color.TRANSPARENT // No background on tap

        // ✅ Login action
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

        // ✅ Back button
        backButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
