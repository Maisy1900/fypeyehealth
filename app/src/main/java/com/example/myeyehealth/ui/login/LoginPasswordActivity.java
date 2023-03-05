package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class LoginPasswordActivity extends AppCompatActivity {
    EditText passwordInput;
    Button loginButton;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);

        // Initialize views
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_button);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check password here against database
                String password = passwordInput.getText().toString().trim();
                if (password.equals("password123")) {
                    // Password is correct, go to LoginSuccessActivity
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
