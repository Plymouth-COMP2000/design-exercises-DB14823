package com.example.restaurantapp.model;

public class AppNotification {
    public int id;
    public String recipient;
    public String category;
    public String message;
    public long createdAt;
    public int isRead;

    public AppNotification(int id, String recipient, String category, String message, long createdAt, int isRead) {
        this.id = id;
        this.recipient = recipient;
        this.category = category;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }
}
