package com.example.restaurantapp.model;

public class Booking {
    public int id;
    public String username;     // the logged-in user's identifier (email-based username)
    public String date;         // store as ISO-like text e.g. "2025-12-20"
    public String time;         // store as "18:30"
    public long createdAt;      // epoch millis, useful for sorting

    public Booking(int id, String username, String date, String time, long createdAt) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.time = time;
        this.createdAt = createdAt;
    }
}
