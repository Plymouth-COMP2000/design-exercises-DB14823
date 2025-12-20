package com.example.restaurantapp.model;

public class MenuItem {

    public int id;
    public String name;
    public double price;
    public int imageRes;
    public String allergyInfo;

    public MenuItem(int id, String name, double price, int imageRes, String allergyInfo) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.allergyInfo = allergyInfo;
    }
}
