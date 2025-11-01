package com.example.restaurantapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_item_details)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val txtFoodName = findViewById<TextView>(R.id.txtFoodName)
        val txtFoodPrice = findViewById<TextView>(R.id.txtFoodPrice)
        val txtAllergyInfo = findViewById<TextView>(R.id.txtAllergyInfo)
        val imgFood = findViewById<ImageView>(R.id.imgFood)

        val name = intent.getStringExtra("foodName") ?: "Food name"
        val price = intent.getStringExtra("foodPrice") ?: "£0.00"
        val allergy = intent.getStringExtra("foodAllergy") ?: "No allergy info"
        val imageRes = intent.getIntExtra("foodImage", 0)

        txtFoodName.text = name
        txtFoodPrice.text = "Price: $price"
        txtAllergyInfo.text = "Allergy info: $allergy"

        if (imageRes != 0) {
            imgFood.setImageResource(imageRes)
        }

        btnBack.setOnClickListener { finish() }
    }
}
