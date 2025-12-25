package com.example.restaurantapp.ui.Staff;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;

public class AddMenuItemActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPrice;
    private EditText edtAllergy;
    private Spinner spnImage;
    private Button btnSave;

    private final String[] imageNames = {"Burger", "Chicken", "Chips"};
    private final int[] imageResIds = {R.drawable.burger, R.drawable.chicken, R.drawable.chips};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        ImageView btnBack = findViewById(R.id.btnBack);
        edtName = findViewById(R.id.edtItemName);
        edtPrice = findViewById(R.id.edtItemPrice);
        edtAllergy = findViewById(R.id.edtAllergyInfo);
        spnImage = findViewById(R.id.spnItemImage);
        btnSave = findViewById(R.id.btnSaveItem);

        btnBack.setOnClickListener(v -> finish());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                imageNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnImage.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String allergy = edtAllergy.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Enter item price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price must be a number (e.g. 5.00)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price < 0) {
            Toast.makeText(this, "Price cannot be negative", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = spnImage.getSelectedItemPosition();
        int imageRes = imageResIds[Math.max(0, Math.min(selectedIndex, imageResIds.length - 1))];

        DatabaseHelper db = new DatabaseHelper(this);
        long id = db.createMenuItem(name, price, imageRes, allergy);

        if (id == -1) {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addNotification(
                "customer_all",
                "MENU_EDITED",
                "Menu updated: " + name + " has been added."
        );

        Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
