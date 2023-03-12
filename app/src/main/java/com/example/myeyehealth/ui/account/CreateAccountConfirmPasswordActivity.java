package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class CreateAccountConfirmPasswordActivity extends AppCompatActivity {
    private EditText confirmedPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_confirm_password);

        confirmedPasswordInput = findViewById(R.id.confirmed_password_input);
        Button confirmButton = findViewById(R.id.confirm_button);
        ImageButton backButton = findViewById(R.id.back_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = getIntent().getStringExtra("password");
                String confirmedPassword = confirmedPasswordInput.getText().toString();

                if (confirmedPassword.equals(password)) {
                    Intent intent = new Intent(CreateAccountConfirmPasswordActivity.this, CreateAccountMedicalInfoActivity.class);
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(CreateAccountConfirmPasswordActivity.this, CreateAccountPassowrdsDontMatchActivity.class);
                    startActivity(intent);
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeExtraDataFromIntent();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        removeExtraDataFromIntent();
        super.onBackPressed();
    }

    private void removeExtraDataFromIntent() {
        Intent intent = getIntent();
        intent.removeExtra("name");
        intent.removeExtra("email");
        intent.removeExtra("password");
        setIntent(intent);
    }
}

