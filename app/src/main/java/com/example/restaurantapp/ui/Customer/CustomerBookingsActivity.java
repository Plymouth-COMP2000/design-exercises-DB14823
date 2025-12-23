package com.example.restaurantapp.ui.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.Booking;

import java.util.List;

public class CustomerBookingsActivity extends AppCompatActivity {

    private String username;

    private RecyclerView rvBookings;
    private BookingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        TextView txtMakeBooking = findViewById(R.id.txtMakeBooking);
        txtMakeBooking.setOnClickListener(v -> {
            Intent i = new Intent(this, AddBookingActivity.class);
            i.putExtra(AddBookingActivity.EXTRA_USERNAME, username);
            startActivity(i);
        });

        rvBookings = findViewById(R.id.rvBookings);

        adapter = new BookingsAdapter(booking -> {
            DatabaseHelper db = new DatabaseHelper(this);
            boolean ok = db.deleteBooking(booking.id);
            if (!ok) {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
            loadBookings();
        });

        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setAdapter(adapter);

        rvBookings.addItemDecoration(new BookingItemSpacing(dp(8)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Booking> bookings = db.getBookingsForUser(username);
        adapter.setData(bookings);
    }

    private int dp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
