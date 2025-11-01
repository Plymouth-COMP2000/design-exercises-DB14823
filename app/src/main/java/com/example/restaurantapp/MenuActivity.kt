package com.example.restaurantapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val backButton: ImageView = findViewById(R.id.btnBack)
        backButton.setOnClickListener { finish() }

        val burgerItem: LinearLayout = findViewById(R.id.menuItemBurger)
        burgerItem.setOnClickListener {
            val intent = Intent(this, MenuItemDetailsActivity::class.java)
            intent.putExtra("foodName", "Burger")
            intent.putExtra("foodPrice", "£10.00")
            intent.putExtra("foodAllergy", "Contains gluten and dairy")
            intent.putExtra("foodImage", R.drawable.burger)
            startActivity(intent)
        }
        val chickenItem: LinearLayout = findViewById(R.id.menuItemChicken)
        chickenItem.setOnClickListener {
            val intent = Intent(this, MenuItemDetailsActivity::class.java)
            intent.putExtra("foodName", "Chicken")
            intent.putExtra("foodPrice", "£5.00")
            intent.putExtra("foodAllergy", "Contains chicken")
            intent.putExtra("foodImage", R.drawable.chicken)
            startActivity(intent)
        }
        val chipsItem: LinearLayout = findViewById(R.id.menuItemChips)
        chipsItem.setOnClickListener {
            val intent = Intent(this, MenuItemDetailsActivity::class.java)
            intent.putExtra("foodName", "Chips")
            intent.putExtra("foodPrice", "£1.00")
            intent.putExtra("foodAllergy", "Contains potato")
            intent.putExtra("foodImage", R.drawable.chips)
            startActivity(intent)
        }
    }
}
