package com.example.restaurantapp.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restaurantapp.R;
import com.example.restaurantapp.model.Booking;
import com.example.restaurantapp.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurant.db";

    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_MENU = "menu";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image_res";
    public static final String COL_ALLERGY = "allergy_info";

    public static final String TABLE_BOOKINGS = "bookings";

    public static final String COL_BOOKING_ID = "id";
    public static final String COL_BOOKING_USERNAME = "username";
    public static final String COL_BOOKING_DISPLAY_NAME = "display_name";
    public static final String COL_BOOKING_DATE = "date";
    public static final String COL_BOOKING_TIME = "time";
    public static final String COL_BOOKING_CREATED_AT = "created_at";
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COL_NOTIF_ID = "id";
    public static final String COL_NOTIF_RECIPIENT = "recipient";
    public static final String COL_NOTIF_CATEGORY = "category";
    public static final String COL_NOTIF_MESSAGE = "message";
    public static final String COL_NOTIF_CREATED_AT = "created_at";
    public static final String COL_NOTIF_IS_READ = "is_read";

    private static final String CREATE_NOTIFICATIONS_TABLE =
            "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                    COL_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NOTIF_RECIPIENT + " TEXT NOT NULL, " +
                    COL_NOTIF_CATEGORY + " TEXT NOT NULL, " +
                    COL_NOTIF_MESSAGE + " TEXT NOT NULL, " +
                    COL_NOTIF_CREATED_AT + " INTEGER NOT NULL, " +
                    COL_NOTIF_IS_READ + " INTEGER NOT NULL DEFAULT 0" +
                    ");";

    private static final String CREATE_MENU_TABLE =
            "CREATE TABLE " + TABLE_MENU + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT NOT NULL, " +
                    COL_PRICE + " REAL NOT NULL, " +
                    COL_IMAGE + " INTEGER NOT NULL, " +
                    COL_ALLERGY + " TEXT" +
                    ");";

    private static final String CREATE_BOOKINGS_TABLE =
            "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                    COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_BOOKING_USERNAME + " TEXT NOT NULL, " +
                    COL_BOOKING_DISPLAY_NAME + " TEXT, " +
                    COL_BOOKING_DATE + " TEXT NOT NULL, " +
                    COL_BOOKING_TIME + " TEXT NOT NULL, " +
                    COL_BOOKING_CREATED_AT + " INTEGER NOT NULL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);
        seedMenu(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(db);
    }

    private void seedMenu(SQLiteDatabase db) {
        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (" + COL_NAME + ", " + COL_PRICE + ", " + COL_IMAGE + ", " + COL_ALLERGY + ") VALUES " +
                        "('Burger', 10.00, " + R.drawable.burger + ", 'Contains gluten');"
        );

        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (" + COL_NAME + ", " + COL_PRICE + ", " + COL_IMAGE + ", " + COL_ALLERGY + ") VALUES " +
                        "('Chicken', 5.00, " + R.drawable.chicken + ", 'Contains poultry');"
        );

        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (" + COL_NAME + ", " + COL_PRICE + ", " + COL_IMAGE + ", " + COL_ALLERGY + ") VALUES " +
                        "('Chips', 1.00, " + R.drawable.chips + ", 'Vegan');"
        );
    }



    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null);

        if (cursor.moveToFirst()) {
            do {
                MenuItem item = new MenuItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ALLERGY))
                );
                items.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }

    @Nullable
    public MenuItem getMenuItemById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_MENU + " WHERE " + COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        MenuItem item = null;

        if (cursor.moveToFirst()) {
            item = new MenuItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ALLERGY))
            );
        }

        cursor.close();
        return item;
    }

    public long createMenuItem(String name, double price, int imageRes, String allergyInfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, imageRes);
        values.put(COL_ALLERGY, allergyInfo);

        return db.insert(TABLE_MENU, null, values);
    }

    public boolean updateMenuItem(int id, String name, double price, int imageRes, String allergyInfo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, imageRes);
        values.put(COL_ALLERGY, allergyInfo);

        int rows = db.update(TABLE_MENU, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }


    public long createTableBooking(String username, String displayName, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_USERNAME, username);
        values.put(COL_BOOKING_DISPLAY_NAME, displayName);
        values.put(COL_BOOKING_DATE, date);
        values.put(COL_BOOKING_TIME, time);
        values.put(COL_BOOKING_CREATED_AT, System.currentTimeMillis());

        return db.insert(TABLE_BOOKINGS, null, values);
    }


    public List<Booking> getBookingsForUser(String username) {
        List<Booking> bookings = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_BOOKINGS +
                        " WHERE " + COL_BOOKING_USERNAME + "=? " +
                        " ORDER BY " + COL_BOOKING_CREATED_AT + " DESC",
                new String[]{username}
        );

        if (c.moveToFirst()) {
            do {
                Booking b = new Booking(
                        c.getInt(c.getColumnIndexOrThrow(COL_BOOKING_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_USERNAME)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_DATE)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_TIME)),
                        c.getLong(c.getColumnIndexOrThrow(COL_BOOKING_CREATED_AT)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_DISPLAY_NAME))
                );


                bookings.add(b);
            } while (c.moveToNext());
        }

        c.close();
        return bookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_BOOKINGS +
                        " ORDER BY " + COL_BOOKING_DATE + " ASC, " + COL_BOOKING_TIME + " ASC",
                null
        );

        if (c.moveToFirst()) {
            do {
                Booking b = new Booking(
                        c.getInt(c.getColumnIndexOrThrow(COL_BOOKING_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_USERNAME)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_DATE)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_TIME)),
                        c.getLong(c.getColumnIndexOrThrow(COL_BOOKING_CREATED_AT)),
                        c.getString(c.getColumnIndexOrThrow(COL_BOOKING_DISPLAY_NAME))
                );

                bookings.add(b);
            } while (c.moveToNext());
        }

        c.close();
        return bookings;
    }
    public long addNotification(String recipient, String category, String message) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_NOTIF_RECIPIENT, recipient);
        v.put(COL_NOTIF_CATEGORY, category);
        v.put(COL_NOTIF_MESSAGE, message);
        v.put(COL_NOTIF_CREATED_AT, System.currentTimeMillis());
        v.put(COL_NOTIF_IS_READ, 0);
        return db.insert(TABLE_NOTIFICATIONS, null, v);
    }

    public List<com.example.restaurantapp.model.AppNotification> getNotificationsForStaff() {
        return getNotificationsForRecipients(new String[]{"staff"});
    }

    public List<com.example.restaurantapp.model.AppNotification> getNotificationsForCustomer(String usernameEmail) {
        return getNotificationsForRecipients(new String[]{"customer:" + usernameEmail, "customer_all"});
    }

    private List<com.example.restaurantapp.model.AppNotification> getNotificationsForRecipients(String[] recipients) {
        List<com.example.restaurantapp.model.AppNotification> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        StringBuilder where = new StringBuilder();
        for (int i = 0; i < recipients.length; i++) {
            if (i > 0) where.append(" OR ");
            where.append(COL_NOTIF_RECIPIENT).append("=?");
        }

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTIFICATIONS +
                        " WHERE " + where +
                        " ORDER BY " + COL_NOTIF_CREATED_AT + " DESC",
                recipients
        );

        if (c.moveToFirst()) {
            do {
                out.add(new com.example.restaurantapp.model.AppNotification(
                        c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_NOTIF_RECIPIENT)),
                        c.getString(c.getColumnIndexOrThrow(COL_NOTIF_CATEGORY)),
                        c.getString(c.getColumnIndexOrThrow(COL_NOTIF_MESSAGE)),
                        c.getLong(c.getColumnIndexOrThrow(COL_NOTIF_CREATED_AT)),
                        c.getInt(c.getColumnIndexOrThrow(COL_NOTIF_IS_READ))
                ));
            } while (c.moveToNext());
        }

        c.close();
        return out;
    }


    public boolean deleteBooking(int bookingId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_BOOKINGS, COL_BOOKING_ID + "=?", new String[]{String.valueOf(bookingId)});
        return rows > 0;
    }
    public boolean deleteNotification(int notifId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_NOTIFICATIONS, COL_NOTIF_ID + "=?", new String[]{String.valueOf(notifId)});
        return rows > 0;
    }

    public int clearNotificationsForStaff() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NOTIFICATIONS, COL_NOTIF_RECIPIENT + "=?", new String[]{"staff"});
    }

    public int clearNotificationsForCustomer(String usernameEmail, boolean includeGlobal) {
        SQLiteDatabase db = getWritableDatabase();

        if (includeGlobal) {
            return db.delete(
                    TABLE_NOTIFICATIONS,
                    COL_NOTIF_RECIPIENT + "=? OR " + COL_NOTIF_RECIPIENT + "=?",
                    new String[]{"customer:" + usernameEmail, "customer_all"}
            );
        } else {
            return db.delete(
                    TABLE_NOTIFICATIONS,
                    COL_NOTIF_RECIPIENT + "=?",
                    new String[]{"customer:" + usernameEmail}
            );
        }
    }

}
