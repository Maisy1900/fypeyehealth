package com.example.myeyehealth.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myeyehealth.R;

public class CreateAccountEmailActivity extends AppCompatActivity {
    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_email);

        emailInput = findViewById(R.id.email_input);
        Button confirmButton = findViewById(R.id.confirm_button);
        ImageButton backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailInput != null) {
                    String email = emailInput.getText().toString();
                    // TODO: Validate the email input and proceed to the next activity
                    Intent intent = new Intent(CreateAccountEmailActivity.this, CreateAccountPasswordActivity.class);
                    startActivity(intent);
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
