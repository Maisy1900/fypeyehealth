package com.example.myeyehealth.data;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.LoginActivity;

public abstract class SessionActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get session manager instance
        sessionManager = new SessionManager(this);

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
}
