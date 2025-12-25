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
import com.example.restaurantapp.model.MenuItem;

public class EditMenuItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "ITEM_ID";

    private ImageView btnBack;
    private ImageView imgFood;
    private Spinner spnItemImage;
    private EditText edtFoodName;
    private EditText edtFoodPrice;
    private EditText edtAllergyInfo;
    private Button btnSave;
    private Button btnDelete;

    private int itemId;

    private final String[] imageNames = {"Burger", "Chicken", "Chips"};
    private final int[] imageResIds = {R.drawable.burger, R.drawable.chicken, R.drawable.chips};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId == -1) {
            Toast.makeText(this, "Missing item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack = findViewById(R.id.btnBack);
        imgFood = findViewById(R.id.imgFood);
        spnItemImage = findViewById(R.id.spnItemImage);
        edtFoodName = findViewById(R.id.edtFoodName);
        edtFoodPrice = findViewById(R.id.edtFoodPrice);
        edtAllergyInfo = findViewById(R.id.edtAllergyInfo);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v -> finish());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                imageNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnItemImage.setAdapter(adapter);

        loadItem();

        spnItemImage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                int res = imageResIds[Math.max(0, Math.min(position, imageResIds.length - 1))];
                imgFood.setImageResource(res);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        btnSave.setOnClickListener(v -> saveChanges());
        btnDelete.setOnClickListener(v -> deleteItem());
    }

    private void loadItem() {
        DatabaseHelper db = new DatabaseHelper(this);
        MenuItem item = db.getMenuItemById(itemId);

        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtFoodName.setText(item.name);
        edtFoodPrice.setText(String.format("%.2f", item.price));
        edtAllergyInfo.setText(item.allergyInfo == null ? "" : item.allergyInfo);

        imgFood.setImageResource(item.imageRes);

        int idx = 0;
        for (int i = 0; i < imageResIds.length; i++) {
            if (imageResIds[i] == item.imageRes) {
                idx = i;
                break;
            }
        }
        spnItemImage.setSelection(idx);
    }

    private void saveChanges() {
        String name = edtFoodName.getText().toString().trim();
        String priceStr = edtFoodPrice.getText().toString().trim();
        String allergy = edtAllergyInfo.getText().toString().trim();

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

        int pos = spnItemImage.getSelectedItemPosition();
        int imageRes = imageResIds[Math.max(0, Math.min(pos, imageResIds.length - 1))];

        DatabaseHelper db = new DatabaseHelper(this);
        boolean ok = db.updateMenuItem(itemId, name, price, imageRes, allergy);

        if (!ok) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            return;
        }
        db.addNotification(
                "customer_all",
                "MENU_EDITED",
                "Menu updated: " + name + " has been changed."
        );

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteItem() {
        DatabaseHelper db = new DatabaseHelper(this);
        boolean ok = db.deleteMenuItem(itemId);

        if (!ok) {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
