package com.example.restaurantapp.ui.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBookingActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_DISPLAY_NAME = "DISPLAY_NAME";


    private TextView txtSelectDate;
    private TextView txtSelectTime;
    private Button btnConfirm;

    private String selectedDate = null;
    private String selectedTime = null;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);

        ImageView btnBack = findViewById(R.id.btnBack);
        txtSelectDate = findViewById(R.id.txtSelectDate);
        txtSelectTime = findViewById(R.id.txtSelectTime);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        btnBack.setOnClickListener(v -> finish());

        username = getIntent().getStringExtra(EXTRA_USERNAME);
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Missing user info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtSelectDate.setAlpha(0.7f);
        txtSelectTime.setAlpha(0.7f);

        txtSelectDate.setOnClickListener(v -> showDatePicker());
        txtSelectTime.setOnClickListener(v -> showTimePicker());
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();

        DatePickerDialog dlg = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.UK, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            txtSelectDate.setText(formatDateForDisplay(year, month, dayOfMonth));
            markSelected(txtSelectDate);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dlg.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        dlg.show();
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();

        TimePickerDialog dlg = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            int roundedMinute = (int) (Math.round(minute / 5.0) * 5);
            if (roundedMinute == 60) roundedMinute = 55;

            selectedTime = String.format(Locale.UK, "%02d:%02d", hourOfDay, roundedMinute);
            txtSelectTime.setText(selectedTime);
            markSelected(txtSelectTime);
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

        dlg.show();
    }

    private void confirmBooking() {
        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String displayName = getIntent().getStringExtra(EXTRA_DISPLAY_NAME);
        if (displayName == null) displayName = "";

        DatabaseHelper db = new DatabaseHelper(this);
        long newId = db.createTableBooking(username, displayName, selectedDate, selectedTime);

        String prettyDate = formatDateDdMmYyyy(selectedDate);
        db.addNotification(
                "staff",
                "BOOKING_CREATED",
                (displayName.isEmpty() ? username : displayName) +
                        " booked a table at " + selectedTime + " on " + prettyDate
        );

        if (newId == -1) {
            Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Booking created!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String formatDateForDisplay(int year, int monthZeroBased, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthZeroBased, day);
        SimpleDateFormat fmt = new SimpleDateFormat("EEE d MMM yyyy", Locale.UK);
        return fmt.format(cal.getTime());
    }

    private String formatDateDdMmYyyy(String yyyyMmDd) {
        try {
            java.text.SimpleDateFormat inFmt = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.UK);
            java.text.SimpleDateFormat outFmt = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.UK);
            java.util.Date d = inFmt.parse(yyyyMmDd);
            return outFmt.format(d);
        } catch (Exception e) {
            return yyyyMmDd;
        }
    }

    private String displayNameOrEmail(String displayName, String email) {
        if (displayName == null) displayName = "";
        displayName = displayName.trim();
        return displayName.isEmpty() ? email : displayName;
    }


    private void markSelected(TextView tv) {
        tv.setAlpha(1f);
        tv.setSelected(true);
    }
}
