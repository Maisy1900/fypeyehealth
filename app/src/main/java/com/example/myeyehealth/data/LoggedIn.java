package com.example.myeyehealth.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;

public abstract class LoggedIn extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get session manager instance
        sessionManager = new SessionManager(this);

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
    }

    // Abstract method to be implemented by subclasses
    protected abstract void onLoggedIn(User user);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close session manager instance to release resources
        sessionManager = null;
    }
}
