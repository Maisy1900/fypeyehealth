package com.example.myeyehealth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myeyehealth.model.User;
//singleton class that helps manage the user session. It is responsible for starting and ending user sessions, checking if a user is logged in, and getting user information from the SharedPreferences.
public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences prefs;
    private static final String FONT_SIZE_KEY = "font_size";
    private static final String GRID_THICKNESS_KEY = "grid_thickness";
    private static final String SACCADES_COLOR_KEY = "saccades_color";

    private static final String LINE_THICKNESS_KEY = "line_thickness";
    private static final String THEME_KEY = "theme";
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

    // Add this method to create a login session
    public void createLoginSession(User user) {
        startSession(user);
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
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userId");
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
    public void setFontSize(String fontSize) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FONT_SIZE_KEY, fontSize);
        editor.apply();
    }

    public String getFontSize() {
        return prefs.getString(FONT_SIZE_KEY, "M"); // M is the default font size
    }

    public void setGridThickness(String thickness) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(GRID_THICKNESS_KEY, thickness);
        editor.apply();
    }

    public String getGridThickness() {
        return prefs.getString(GRID_THICKNESS_KEY, "M"); // M is the default grid thickness
    }

    public void setSaccadesColor(String color) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SACCADES_COLOR_KEY, color);
        editor.apply();
    }

    public String getSaccadesColor() {
        return prefs.getString(SACCADES_COLOR_KEY, "red"); // red is the default saccades color
    }
    public void updateTheme(String theme) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(THEME_KEY, theme);
        editor.apply();
    }

    public String getThemeName() {
        return prefs.getString(THEME_KEY, "Theme.MyEyeHealth.Light");
    }

    public void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("userId");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

}
