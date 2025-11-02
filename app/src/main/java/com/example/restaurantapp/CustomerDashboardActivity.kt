package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CustomerDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)

        val txtLogout = findViewById<TextView>(R.id.txtLogout)
        val txtWelcome = findViewById<TextView>(R.id.txtWelcome)
        val btnViewMenu = findViewById<Button>(R.id.btnViewMenu)
        val btnViewBookings = findViewById<Button>(R.id.btnViewBookings)
        val imgNotification = findViewById<ImageView>(R.id.imgNotification)

        val username = "Oh great esteemed customer"
        txtWelcome.text = getString(R.string.welcome_message, username)

        txtLogout.setOnClickListener {
            startActivity(Intent(this, CustomerLoginActivity::class.java))

        }
        btnViewMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
        btnViewBookings.setOnClickListener {
            startActivity(Intent(this, ViewBookingsActivity::class.java))
        }
        imgNotification.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))

        }


    }
}
