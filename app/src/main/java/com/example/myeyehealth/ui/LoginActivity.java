package com.example.myeyehealth.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myeyehealth.R;
import com.example.myeyehealth.ui.account.CreateAccountNameActivity;
import com.example.myeyehealth.ui.login.LoginEmailActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginButton;
    private Button mCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = findViewById(R.id.login_button);
        mCreateAccountButton = findViewById(R.id.create_account_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginEmailActivity.class);
                startActivity(intent);
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountNameActivity.class);
                startActivity(intent);
            }
        });
    }
}
