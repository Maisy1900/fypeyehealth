package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;

public class LoginSuccessActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize views
        ImageButton backButton = findViewById(R.id.back_button);
        Button completeButton = findViewById(R.id.complete_button);

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log user out here
                sessionManager.endSession();
                finish();
            }
        });

        // Set click listener for complete button
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start session with the logged in user
                User user = sessionManager.getUser();
                // Navigate to MainMenuActivity
                Intent intent = new Intent(LoginSuccessActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
