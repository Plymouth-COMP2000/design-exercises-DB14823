package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewBookingsActivity : AppCompatActivity() {

    private val bookings = mutableListOf(
        Pair("12/11/2024", "6:30 PM"),
        Pair("15/11/2024", "7:00 PM")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bookings)

        val layoutBookings = findViewById<LinearLayout>(R.id.layoutBookings)
        val makeBooking = findViewById<TextView>(R.id.txtMakeBooking)
        val backButton = findViewById<ImageView>(R.id.btnBack)

        loadBookings(layoutBookings)

        makeBooking.setOnClickListener {
            val intent = Intent(this, AddBookingActivity::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener { finish() }
    }

    private fun loadBookings(layoutBookings: LinearLayout) {
        layoutBookings.removeAllViews()

        bookings.forEach { (date, time) ->
            val row = layoutInflater.inflate(R.layout.booking_row, null)

            row.findViewById<TextView>(R.id.txtDate).text = date
            row.findViewById<TextView>(R.id.txtTime).text = time

            row.findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
                bookings.remove(Pair(date, time))
                loadBookings(layoutBookings)
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 12, 0, 12)
            row.layoutParams = params

            layoutBookings.addView(row)
        }
    }
}
