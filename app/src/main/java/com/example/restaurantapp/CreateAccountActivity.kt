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

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val backButton = findViewById<ImageView>(R.id.btnBack)
        val nameInput = findViewById<EditText>(R.id.editTextName)
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val createButton = findViewById<Button>(R.id.btnCreateAccount)
        val loginRedirect = findViewById<TextView>(R.id.txtLoginRedirect)

        val fullText = "Already a user? Log in here"
        val spannable = SpannableString(fullText)

        val clickablePart = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@CreateAccountActivity, CustomerLoginActivity::class.java))
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

        loginRedirect.text = spannable
        loginRedirect.movementMethod = LinkMovementMethod.getInstance()
        loginRedirect.highlightColor = Color.TRANSPARENT

        backButton.setOnClickListener { finish() }

        createButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CustomerLoginActivity::class.java))
            }
        }
    }
}
