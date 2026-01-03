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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class AddMenuItemActivity extends AppCompatActivity {

    private ImageView imgFood;
    private EditText edtName;
    private EditText edtPrice;
    private EditText edtAllergy;
    private MaterialAutoCompleteTextView spnImage;
    private Button btnSave;

    private final String[] spinnerItems = {"Select image...", "Burger", "Chicken", "Chips"};
    private final int[] imageResIds = {R.drawable.burger, R.drawable.chicken, R.drawable.chips};
    private final int placeholderRes = R.drawable.ic_image_placeholder;



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

        setContentView(R.layout.activity_add_menu_item);

        ImageView btnBack = findViewById(R.id.btnBack);
        imgFood = findViewById(R.id.imgFood);
        edtName = findViewById(R.id.edtItemName);
        edtPrice = findViewById(R.id.edtItemPrice);
        edtAllergy = findViewById(R.id.edtAllergyInfo);
        spnImage = findViewById(R.id.spnItemImage);
        btnSave = findViewById(R.id.btnSaveItem);
        spnImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_down, 0);
        spnImage.setOnDismissListener(() -> spnImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_down, 0));

        spnImage.setKeyListener(null);

        int hintColor = edtName.getHintTextColors().getDefaultColor();
        int textColor = edtName.getTextColors().getDefaultColor();
        spnImage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, edtName.getTextSize());
        spnImage.setTypeface(edtName.getTypeface());
        spnImage.setPadding(edtName.getPaddingLeft(), edtName.getPaddingTop(), edtName.getPaddingRight(), edtName.getPaddingBottom());
        spnImage.setIncludeFontPadding(edtName.getIncludeFontPadding());


        btnBack.setOnClickListener(v -> finish());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, spinnerItems) {
            @NonNull
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.widget.TextView tv = (android.widget.TextView) super.getView(position, convertView, parent);
                tv.setText(spinnerItems[position]);
                tv.setTextColor(hintColor);
                tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, edtName.getTextSize());
                tv.setTypeface(edtName.getTypeface());
                tv.setPadding(dp(16), dp(14), dp(16), dp(14));
                tv.setMinHeight(dp(52));
                return tv;
            }
        };
        spnImage.setAdapter(adapter);
        final boolean[] ignoreNextClick = {false};

        spnImage.setOnDismissListener(() -> {
            spnImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_down, 0);
            ignoreNextClick[0] = true;
        });

        spnImage.setOnClickListener(v -> {
            if (ignoreNextClick[0]) {
                ignoreNextClick[0] = false;
                return;
            }
            if (spnImage.isPopupShowing()) {
                spnImage.dismissDropDown();
            } else {
                spnImage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown_arrow_up, 0);
                spnImage.showDropDown();
            }
        });


        spnImage.setFocusable(false);
        spnImage.setCursorVisible(false);




        spnImage.setDropDownBackgroundResource(R.drawable.dropdown_background);
        spnImage.setDropDownVerticalOffset(dp(2));


        spnImage.setText(spinnerItems[0], false);
        imgFood.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgFood.setImageResource(placeholderRes);
        spnImage.setTextColor(hintColor);


        spnImage.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                spnImage.setTextColor(hintColor);
                imgFood.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imgFood.setImageResource(placeholderRes);
            } else {
                spnImage.setTextColor(textColor);
                imgFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgFood.setImageResource(imageResIds[position - 1]);
            }
        });


        btnSave.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String allergy = edtAllergy.getText().toString().trim();
        String selected = spnImage.getText() == null ? "" : spnImage.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Enter item price", Toast.LENGTH_SHORT).show();
            return;
        }

        int imageRes = resolveImageRes(selected);
        if (imageRes == 0) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
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

        DatabaseHelper db = new DatabaseHelper(this);
        long id = db.createMenuItem(name, price, imageRes, allergy);

        if (id == -1) {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addNotification("customer_all", "MENU_EDITED", "Menu updated: " + name + " has been added.");

        Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int resolveImageRes(String selected) {
        if (TextUtils.isEmpty(selected) || selected.equals(spinnerItems[0])) return 0;
        for (int i = 1; i < spinnerItems.length; i++) {
            if (spinnerItems[i].equals(selected)) return imageResIds[i - 1];
        }
        return 0;
    }

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

}
