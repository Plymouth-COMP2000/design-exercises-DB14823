package com.example.restaurantapp.ui.Customer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurantapp.R;
import com.example.restaurantapp.data.db.DatabaseHelper;
import com.example.restaurantapp.model.MenuItem;


public class MenuItemDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_FOOD_NAME = "FOOD_NAME";
    public static final String EXTRA_FOOD_PRICE = "FOOD_PRICE"; // can be "£5.00" or "5.00"
    public static final String EXTRA_FOOD_IMAGE_RES = "FOOD_IMAGE_RES"; // int drawable id
    public static final String EXTRA_ALLERGY_INFO = "ALLERGY_INFO";
    public static final String EXTRA_ITEM_ID = "ITEM_ID";

    private TextView txtFoodName;
    private TextView txtFoodPrice;
    private TextView txtAllergyInfo;
    private ImageView imgFood;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        btnBack = findViewById(R.id.btnBack);
        txtFoodName = findViewById(R.id.txtFoodName);
        txtFoodPrice = findViewById(R.id.txtFoodPrice);
        txtAllergyInfo = findViewById(R.id.txtAllergyInfo);
        imgFood = findViewById(R.id.imgFood);

        btnBack.setOnClickListener(v -> finish());


        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId == -1) {
            finish();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        MenuItem item = db.getMenuItemById(itemId);

        if (item == null) {
            finish();
            return;
        }

// Populate UI using DB values
        txtFoodName.setText(item.name);
        txtFoodPrice.setText("£" + String.format("%.2f", item.price));
        imgFood.setImageResource(item.imageRes);

        if (item.allergyInfo != null && !item.allergyInfo.isEmpty()) {
            txtAllergyInfo.setText(item.allergyInfo);
        }

    }
}
