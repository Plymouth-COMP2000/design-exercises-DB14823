package com.example.restaurantapp.ui.Staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;

import java.util.List;

public class StaffManageMenuActivity extends AppCompatActivity {

    private GridLayout menuGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!com.example.restaurantapp.data.session.SessionManager.isLoggedIn(this)
                || com.example.restaurantapp.data.session.SessionManager.getRole(this)
                != com.example.restaurantapp.data.session.SessionManager.Role.STAFF) {
            startActivity(new android.content.Intent(
                    this,
                    com.example.restaurantapp.ui.Staff.StaffLoginActivity.class
            ));
            finish();
            return;
        }

        setContentView(R.layout.activity_manage_menu);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnAddNewItem = findViewById(R.id.btnAddNewItem);
        menuGrid = findViewById(R.id.menuGrid);

        btnBack.setOnClickListener(v -> finish());

        btnAddNewItem.setOnClickListener(v -> {
            Intent i = new Intent(this, AddMenuItemActivity.class);
            startActivity(i);
        });

        loadMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }

    private void loadMenu() {
        if (menuGrid == null) return;

        DatabaseHelper db = new DatabaseHelper(this);
        List<MenuItem> items = db.getAllMenuItems();

        menuGrid.removeAllViews();
        menuGrid.setColumnCount(2);

        for (int i = 0; i < items.size(); i++) {
            int column = i % 2;
            LinearLayout card = buildMenuCard(items.get(i), column);
            menuGrid.addView(card);
        }
    }

    private LinearLayout buildMenuCard(MenuItem item, int column) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(6), dp(6), dp(6), dp(6));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.columnSpec = GridLayout.spec(column, 1f);
        params.setMargins(dp(8), dp(16), dp(8), dp(16));
        card.setLayoutParams(params);

        ImageView img = new ImageView(this);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(150)
        );
        img.setLayoutParams(imgParams);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundResource(R.drawable.rounded_corners);
        img.setClipToOutline(true);
        img.setImageResource(item.imageRes);

        TextView name = new TextView(this);
        name.setText(item.name);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        name.setPadding(0, dp(10), 0, 0);
        name.setTypeface(name.getTypeface(), android.graphics.Typeface.BOLD);

        TextView price = new TextView(this);
        price.setText("£" + String.format("%.2f", item.price));
        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        card.addView(img);
        card.addView(name);
        card.addView(price);

        card.setOnClickListener(v -> {
            Intent i = new Intent(this, EditMenuItemActivity.class);
            i.putExtra(EditMenuItemActivity.EXTRA_ITEM_ID, item.id);
            startActivity(i);
        });

        return card;
    }


    private int dp(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
