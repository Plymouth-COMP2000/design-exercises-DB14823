package com.example.restaurantapp.model;

public class Booking {
    public int id;
    public String username;
    public String date;
    public String time;
    public long createdAt;
    public String displayName;

    public Booking(int id, String username, String date, String time, long createdAt, String displayName) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.date = date;
        this.time = time;
        this.createdAt = createdAt;
    }
}
