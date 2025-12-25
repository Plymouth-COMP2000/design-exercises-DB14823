package com.example.restaurantapp.ui.Customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.AppNotification;
import com.example.restaurantapp.ui.NotificationsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomerNotificationsActivity extends AppCompatActivity {

    private static final String PREFS = "notif_prefs";
    private static final String KEY_MENU_EDITED = "notif_MENU_EDITED";
    private static final String KEY_CANCELLED_BY_STAFF = "notif_BOOKING_CANCELLED_BY_STAFF";

    private String username;

    private RecyclerView recycler;
    private TextView txtNoNotifications;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_customer);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "";

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        ImageView btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) btnSettings.setOnClickListener(v -> showSettingsDialog());

        txtNoNotifications = findViewById(R.id.txtNoNotifications);
        recycler = findViewById(R.id.recyclerNotifications);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsAdapter(n -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteNotification(n.id);
            loadNotifications();
        });
        recycler.setAdapter(adapter);

        TextView txtClearAll = findViewById(R.id.txtClearAll);
        txtClearAll.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);

            db.clearNotificationsForCustomer(username, true);

            loadNotifications();
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<AppNotification> raw = db.getNotificationsForCustomer(username);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean allowMenuEdited = prefs.getBoolean(KEY_MENU_EDITED, true);
        boolean allowCancelled = prefs.getBoolean(KEY_CANCELLED_BY_STAFF, true);

        List<AppNotification> filtered = new ArrayList<>();
        if (raw != null) {
            for (AppNotification n : raw) {
                if ("MENU_EDITED".equals(n.category) && !allowMenuEdited) continue;
                if ("BOOKING_CANCELLED_BY_STAFF".equals(n.category) && !allowCancelled) continue;
                filtered.add(n);
            }
        }

        adapter.setData(filtered);

        boolean empty = filtered.isEmpty();
        txtNoNotifications.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void showSettingsDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        boolean[] checked = new boolean[]{
                prefs.getBoolean(KEY_CANCELLED_BY_STAFF, true),
                prefs.getBoolean(KEY_MENU_EDITED, true)
        };

        String[] items = new String[]{
                "Booking cancellations by staff",
                "Menu updates"
        };

        new AlertDialog.Builder(this)
                .setTitle("Notification settings")
                .setMultiChoiceItems(items, checked, (dialog, which, isChecked) -> checked[which] = isChecked)
                .setPositiveButton("Save", (dialog, which) -> {
                    prefs.edit()
                            .putBoolean(KEY_CANCELLED_BY_STAFF, checked[0])
                            .putBoolean(KEY_MENU_EDITED, checked[1])
                            .apply();
                    loadNotifications();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
