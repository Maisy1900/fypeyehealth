package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.model.User;

public class LoginPasswordActivity extends AppCompatActivity {
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
                Database db = Database.getInstance(LoginPasswordActivity.this);
                User user = db.getUserByEmail(email);

                // Check if user exists and if password matches
                if (user != null && password.equals(user.getPassword())) {
                    // Password is correct, go to LoginSuccessActivity
                    Intent intent = new Intent(LoginPasswordActivity.this, LoginSuccessActivity.class);
                    startActivity(intent);
                } else {
                    // Password is incorrect, go to LoginErrorActivity
                    Intent intent = new Intent(LoginPasswordActivity.this, LoginErrorActivity.class);
                    startActivity(intent);
                }
                db.close();
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
