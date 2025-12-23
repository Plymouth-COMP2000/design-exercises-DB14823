package com.example.restaurantapp.data.db;

import android.content.Context;
import android.database.Cursor;
import androidx.annotation.Nullable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import com.example.restaurantapp.model.Booking;
import com.example.restaurantapp.R;
import com.example.restaurantapp.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurant.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_MENU = "menu";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image_res";
    public static final String COL_ALLERGY = "allergy_info";
    public static final String TABLE_BOOKINGS = "bookings";

    public static final String COL_BOOKING_ID = "id";
    public static final String COL_BOOKING_USERNAME = "username";
    public static final String COL_BOOKING_DATE = "date";
    public static final String COL_BOOKING_TIME = "time";
    public static final String COL_BOOKING_CREATED_AT = "created_at";



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
        seedMenu(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);

        onCreate(db);
    }

    private void seedMenu(SQLiteDatabase db) {
        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (name, price, image_res, allergy_info) VALUES " +
                        "('Burger', 10.00, " + R.drawable.burger + ", 'Contains gluten');"
        );

        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (name, price, image_res, allergy_info) VALUES " +
                        "('Chicken', 5.00, " + R.drawable.chicken + ", 'Contains poultry');"
        );

        db.execSQL(
                "INSERT INTO " + TABLE_MENU +
                        " (name, price, image_res, allergy_info) VALUES " +
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
    public long createTableBooking(String username, String date, String time) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_USERNAME, username);
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
                        c.getLong(c.getColumnIndexOrThrow(COL_BOOKING_CREATED_AT))
                );
                bookings.add(b);
            } while (c.moveToNext());
        }

        c.close();
        return bookings;
    }

    public boolean deleteBooking(int bookingId) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_BOOKINGS, COL_BOOKING_ID + "=?", new String[]{String.valueOf(bookingId)});
        return rows > 0;
    }





}
