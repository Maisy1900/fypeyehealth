package com.example.myeyehealth.utils;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.LoginActivity;
import com.example.myeyehealth.utils.SessionManager;

public abstract class SessionActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme(); // Add this line to set the theme

        // Get session manager instance
        sessionManager = SessionManager.getInstance(this);

        if (shouldCheckLoginStatus()) {
            // Check if user is logged in
            if (!sessionManager.isLoggedIn()) {
                // User is not logged in, start LoginActivity and clear the activity stack
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                // User is logged in, get the user object from the session manager
                User user = sessionManager.getUser();
                // Call abstract method to perform activity-specific setup
                onLoggedIn(user);
            }
        } else {
            // Bypass the login check
            onLoggedIn(null);
        }
    }

    // Abstract method to be implemented by subclasses
    protected abstract void onLoggedIn(User user);

    // Method to determine if login status should be checked
    protected boolean shouldCheckLoginStatus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close session manager instance to release resources
        sessionManager = null;
    }
    // Add this method to set the theme
    protected void setAppTheme() {
        String theme = SessionManager.getInstance(this).getThemeName();
        switch (theme) {
            case "Theme.MyEyeHealth.Light":
                setTheme(R.style.Theme_MyEyeHealth_Light);
                break;
            case "Theme.MyEyeHealth.Dark":
                setTheme(R.style.Theme_MyEyeHealth_Dark);
                break;
            case "Theme.MyEyeHealth.HighContrast":
                setTheme(R.style.Theme_MyEyeHealth_HighContrast);
                break;
        }
    }
}
