package com.example.myeyehealth.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myeyehealth.R;

public class CreateAccountPasswordActivity extends AppCompatActivity {

    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_password);

        passwordInput = findViewById(R.id.password_input);
        Button confirmButton = findViewById(R.id.confirm_button);
        ImageButton backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordInput.getText().toString();
                // TODO: Validate the password input and proceed to the next activity
                if (!password.isEmpty()) {
                    Intent intent = new Intent(CreateAccountPasswordActivity.this, CreateAccountConfirmPasswordActivity.class);
                    intent.putExtra("password", password); // pass the password to the next activity
                    startActivity(intent);
                } else {
                    // Show error message to user
                    Toast.makeText(CreateAccountPasswordActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
