package com.example.restaurantapp.ui.Staff;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class EditMenuItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "ITEM_ID";

    private ImageView btnBack;
    private ImageView imgFood;
    private MaterialAutoCompleteTextView spnItemImage;
    private EditText edtFoodName;
    private EditText edtFoodPrice;
    private EditText edtAllergyInfo;
    private Button btnSave;
    private Button btnDelete;

    private int itemId;
    private int selectedImageRes;

    private final String[] imageNames = {"Burger", "Chicken", "Chips"};
    private final int[] imageResIds = {R.drawable.burger, R.drawable.chicken, R.drawable.chips};

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

        spnItemImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_down, 0);
        spnItemImage.setKeyListener(null);
        spnItemImage.setFocusable(false);
        spnItemImage.setCursorVisible(false);
        spnItemImage.setDropDownBackgroundResource(R.drawable.dropdown_background);
        spnItemImage.setDropDownVerticalOffset(dp(2));

        int hintColor = edtFoodName.getHintTextColors().getDefaultColor();
        int textColor = edtFoodName.getTextColors().getDefaultColor();

        spnItemImage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, edtFoodName.getTextSize());
        spnItemImage.setTypeface(edtFoodName.getTypeface());
        spnItemImage.setPadding(edtFoodName.getPaddingLeft(), edtFoodName.getPaddingTop(), edtFoodName.getPaddingRight(), edtFoodName.getPaddingBottom());
        spnItemImage.setIncludeFontPadding(edtFoodName.getIncludeFontPadding());
        spnItemImage.setTextColor(textColor);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, imageNames) {
            @NonNull
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.widget.TextView tv = (android.widget.TextView) super.getView(position, convertView, parent);
                tv.setText(imageNames[position]);
                tv.setTextColor(hintColor);
                tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, edtFoodName.getTextSize());
                tv.setTypeface(edtFoodName.getTypeface());
                tv.setPadding(dp(16), dp(14), dp(16), dp(14));
                tv.setMinHeight(dp(52));
                return tv;
            }
        };
        spnItemImage.setAdapter(adapter);

        final boolean[] ignoreNextClick = {false};

        spnItemImage.setOnDismissListener(() -> {
            spnItemImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_down, 0);
            ignoreNextClick[0] = true;
        });

        spnItemImage.setOnClickListener(v -> {
            if (ignoreNextClick[0]) {
                ignoreNextClick[0] = false;
                return;
            }
            if (spnItemImage.isPopupShowing()) {
                spnItemImage.dismissDropDown();
            } else {
                spnItemImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_up, 0);
                spnItemImage.showDropDown();
            }
        });

        spnItemImage.setOnItemClickListener((parent, view, position, id) -> {
            selectedImageRes = imageResIds[Math.max(0, Math.min(position, imageResIds.length - 1))];
            spnItemImage.setTextColor(textColor);
            imgFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgFood.setImageResource(selectedImageRes);
        });

        loadItem();

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

        int idx = 0;
        for (int i = 0; i < imageResIds.length; i++) {
            if (imageResIds[i] == item.imageRes) {
                idx = i;
                break;
            }
        }

        selectedImageRes = imageResIds[idx];
        spnItemImage.setText(imageNames[idx], false);
        imgFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgFood.setImageResource(selectedImageRes);
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

        DatabaseHelper db = new DatabaseHelper(this);
        boolean ok = db.updateMenuItem(itemId, name, price, selectedImageRes, allergy);

        if (!ok) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addNotification("customer_all", "MENU_EDITED", "Menu updated: " + name + " has been changed.");

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

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }
}
