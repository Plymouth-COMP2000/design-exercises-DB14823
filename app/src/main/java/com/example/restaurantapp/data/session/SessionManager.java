package com.example.restaurantapp.data.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public enum Role { CUSTOMER, STAFF }

    private static final String PREF = "session";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";

    public static void login(Context context, Role role, String userId) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_ROLE, role.name())
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_LOGGED_IN, false);
    }

    public static Role getRole(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String r = sp.getString(KEY_ROLE, null);
        if (r == null) return null;
        try { return Role.valueOf(r); } catch (IllegalArgumentException e) { return null; }
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY_USER_ID, null);
    }
}
