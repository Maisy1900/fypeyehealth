package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.controller.UserMethods;
import com.example.myeyehealth.model.User;

public class LoginPasswordActivity extends BaseActivity {
    EditText passwordInput;
    Button loginButton;
    ImageButton backButton;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        // Initialize views
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_button);

        // Get the email from the previous activity
        email = getIntent().getStringExtra("email");

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check email and password here against database
                String password = passwordInput.getText().toString().trim();

                // Lookup user in the database
                UserMethods userMethods = new UserMethods(LoginPasswordActivity.this);
                User user = userMethods.getUserByEmail(email);

                // Check if user exists and if password matches
                if (user != null && password.equals(user.getPassword())) {
                    // Password is correct, create a session for the user
                    SessionManager sessionManager = SessionManager.getInstance(LoginPasswordActivity.this);
                    sessionManager.createLoginSession(user);

                    // Go to LoginSuccessActivity
                    Intent intent = new Intent(LoginPasswordActivity.this, LoginSuccessActivity.class);
                    startActivity(intent);
                } else {
                    // Password is incorrect, go to LoginErrorActivity
                    Intent intent = new Intent(LoginPasswordActivity.this, LoginErrorActivity.class);
                    startActivity(intent);
                }

            }
        });

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
