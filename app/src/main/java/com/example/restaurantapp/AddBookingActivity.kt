package com.example.restaurantapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale

class AddBookingActivity : AppCompatActivity() {

    private lateinit var txtSelectDate: TextView
    private lateinit var txtSelectTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_booking)

        txtSelectDate = findViewById(R.id.txtSelectDate)
        txtSelectTime = findViewById(R.id.txtSelectTime)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmBooking)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Open Date Picker
        txtSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                txtSelectDate.text = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y)
            }, year, month, day).show()
        }

        // Open Time Picker
        txtSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, h, m ->
                val amPm = if (h < 12) "AM" else "PM"
                val hourFormatted = if (h % 12 == 0) 12 else h % 12
                txtSelectTime.text = String.format(Locale.getDefault(), "%d:%02d %s", hourFormatted, m, amPm)
            }, hour, minute, false).show()
        }

        // Confirm booking
        btnConfirm.setOnClickListener {
            val date = txtSelectDate.text.toString()
            val time = txtSelectTime.text.toString()

            if (date.contains("Select") || time.contains("Select")) {
                Toast.makeText(this, "Please select date & time", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Booking set for $date at $time", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        btnBack.setOnClickListener { finish()}
        }
    }

