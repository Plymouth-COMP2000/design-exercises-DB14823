package com.example.restaurantapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewBookingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bookings)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val tableBookings = findViewById<TableLayout>(R.id.tableBookings)
        val txtNoBookings = findViewById<TextView>(R.id.txtNoBookings)

        btnBack.setOnClickListener { finish() }

        val bookings = listOf(
            Pair("12/11/2024", "6:30 PM"),
            Pair("15/11/2024", "7:00 PM")
        )

        if (bookings.isEmpty()) {
            txtNoBookings.visibility = TextView.VISIBLE
        } else {
            txtNoBookings.visibility = TextView.GONE

            for (booking in bookings) {
                val row = TableRow(this)

                row.setBackgroundResource(R.drawable.table_row_background)
                row.setPadding(16, 16, 16, 16)

                val dateView = TextView(this).apply {
                    text = booking.first
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black))
                }

                val timeView = TextView(this).apply {
                    text = booking.second
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black))
                }

                row.addView(dateView)
                row.addView(timeView)
                tableBookings.addView(row)
            }

        }
    }
}
