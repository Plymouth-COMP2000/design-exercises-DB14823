package com.example.restaurantapp.ui.Staff;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.Booking;

import java.util.List;

public class StaffManageBookingsActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private StaffBookingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage_bookings);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        rvBookings = findViewById(R.id.rvBookings);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StaffBookingsAdapter(booking -> {
            DatabaseHelper db = new DatabaseHelper(this);
            boolean ok = db.deleteBooking(booking.id);

            String prettyDate = formatDateDdMmYyyy(booking.date);

            db.addNotification(
                    "customer:" + booking.username,
                    "BOOKING_CANCELLED_BY_STAFF",
                    "Your booking on " + prettyDate + " at " + booking.time + " was cancelled by staff."
            );

            if (!ok) Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            loadBookings();
        });

        rvBookings.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Booking> all = db.getAllBookings();
        adapter.setBookings(all);
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
}
