package com.example.restaurantapp.ui.Staff;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.AppNotification;
import com.example.restaurantapp.ui.NotificationsAdapter;

import java.util.List;

public class StaffNotificationsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private TextView txtNoNotifications;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

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
            db.clearNotificationsForStaff();
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
        List<AppNotification> list = db.getNotificationsForStaff();

        adapter.setData(list);

        boolean empty = (list == null || list.isEmpty());
        txtNoNotifications.setVisibility(empty ? android.view.View.VISIBLE : android.view.View.GONE);
    }


}
