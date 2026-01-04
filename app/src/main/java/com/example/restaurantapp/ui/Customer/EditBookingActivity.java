package com.example.restaurantapp.ui.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditBookingActivity extends AppCompatActivity {

    public static final String EXTRA_BOOKING_ID = "BOOKING_ID";
    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_DISPLAY_NAME = "DISPLAY_NAME";
    public static final String EXTRA_DATE = "DATE";
    public static final String EXTRA_TIME = "TIME";
    public static final String EXTRA_PARTY_SIZE = "PARTY_SIZE";

    private int bookingId;
    private String username;
    private String displayName;

    private TextView txtSelectDate;
    private TextView txtSelectTime;
    private EditText edtPartySize;

    private String selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!com.example.restaurantapp.data.session.SessionManager.isLoggedIn(this)
                || com.example.restaurantapp.data.session.SessionManager.getRole(this)
                != com.example.restaurantapp.data.session.SessionManager.Role.CUSTOMER) {
            startActivity(new android.content.Intent(this, com.example.restaurantapp.ui.Customer.CustomerLoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_customer_edit_booking);

        bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
        username = getIntent().getStringExtra(EXTRA_USERNAME);
        displayName = getIntent().getStringExtra(EXTRA_DISPLAY_NAME);

        if (bookingId == -1 || username == null || username.trim().isEmpty()) {
            Toast.makeText(this, "Missing booking info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (displayName == null) displayName = "";

        selectedDate = getIntent().getStringExtra(EXTRA_DATE);
        selectedTime = getIntent().getStringExtra(EXTRA_TIME);
        int partySize = getIntent().getIntExtra(EXTRA_PARTY_SIZE, 2);

        ImageView btnBack = findViewById(R.id.btnBack);
        txtSelectDate = findViewById(R.id.txtSelectDate);
        txtSelectTime = findViewById(R.id.txtSelectTime);
        edtPartySize = findViewById(R.id.edtPartySize);
        Button btnSave = findViewById(R.id.btnConfirmBooking);

        btnBack.setOnClickListener(v -> finish());

        if (selectedDate != null) {
            txtSelectDate.setText(formatDateForDisplay(selectedDate));
            txtSelectDate.setAlpha(1f);
            txtSelectDate.setSelected(true);
        } else {
            txtSelectDate.setAlpha(0.7f);
        }

        if (selectedTime != null) {
            txtSelectTime.setText(selectedTime);
            txtSelectTime.setAlpha(1f);
            txtSelectTime.setSelected(true);
        } else {
            txtSelectTime.setAlpha(0.7f);
        }

        edtPartySize.setText(String.valueOf(partySize));

        txtSelectDate.setOnClickListener(v -> showDatePicker());
        txtSelectTime.setOnClickListener(v -> showTimePicker());
        btnSave.setOnClickListener(v -> saveChanges());
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

    private void saveChanges() {
        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String partyStr = edtPartySize.getText().toString().trim();
        if (TextUtils.isEmpty(partyStr)) {
            Toast.makeText(this, "Please enter party size", Toast.LENGTH_SHORT).show();
            return;
        }

        int partySize;
        try {
            partySize = Integer.parseInt(partyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Party size must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (partySize < 1 || partySize > 20) {
            Toast.makeText(this, "Party size must be between 1 and 20", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        boolean ok = db.updateBooking(bookingId, selectedDate, selectedTime, partySize);

        if (!ok) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            return;
        }

        String who = displayNameOrEmail(displayName, username);
        String prettyDate = formatDateDdMmYyyy(selectedDate);

        db.addNotification(
                "staff",
                "BOOKING_UPDATED",
                who + " updated a booking to " + selectedTime + " on " + prettyDate + " for " + partySize
        );

        Toast.makeText(this, "Booking updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String formatDateForDisplay(int year, int monthZeroBased, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthZeroBased, day);
        SimpleDateFormat fmt = new SimpleDateFormat("EEE d MMM yyyy", Locale.UK);
        return fmt.format(cal.getTime());
    }

    private String formatDateForDisplay(String yyyyMmDd) {
        try {
            java.text.SimpleDateFormat inFmt = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.UK);
            java.text.SimpleDateFormat outFmt = new java.text.SimpleDateFormat("EEE d MMM yyyy", java.util.Locale.UK);
            java.util.Date d = inFmt.parse(yyyyMmDd);
            return outFmt.format(d);
        } catch (Exception e) {
            return yyyyMmDd;
        }
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