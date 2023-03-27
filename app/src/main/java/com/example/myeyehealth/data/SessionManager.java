package com.example.myeyehealth.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myeyehealth.model.User;

public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences prefs;

    private static final String LINE_THICKNESS_KEY = "line_thickness";

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    // Change the access modifier from private to public
    private SessionManager(Context context) {
        prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public void startSession(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userId", user.getId());
        editor.putString("userName", user.getName());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userPassword", user.getPassword());
        editor.putString("userDoctorName", user.getDoctorName());
        editor.putString("userDoctorEmail", user.getDoctorEmail());
        editor.putString("userCarerName", user.getCarerName());
        editor.putString("userCarerEmail", user.getCarerEmail());
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("isLoggedIn", false);
    }

    public User getUser() {
        int id = prefs.getInt("userId", -1);
        String name = prefs.getString("userName", "");
        String email = prefs.getString("userEmail", "");
        String password = prefs.getString("userPassword", "");
        String doctorName = prefs.getString("userDoctorName", "");
        String doctorEmail = prefs.getString("userDoctorEmail", "");
        String carerName = prefs.getString("userCarerName", "");
        String carerEmail = prefs.getString("userCarerEmail", "");
        return new User(id, name, email, password, doctorName, doctorEmail, carerName, carerEmail);
    }

    public void endSession() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setLineThickness(float thickness) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(LINE_THICKNESS_KEY, thickness);
        editor.apply();
    }

    public float getLineThickness() {
        return prefs.getFloat(LINE_THICKNESS_KEY, 2); // 2 is the default thickness
    }
}
