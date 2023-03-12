package com.example.myeyehealth.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myeyehealth.model.User;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public void startSession(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userId", user.getId());
        editor.putString("userName", user.getName());
        editor.putString("userEmail", user.getEmail());
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
        return new User(name, email, password, doctorName, doctorEmail, carerName, carerEmail);
    }



    public void endSession() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
