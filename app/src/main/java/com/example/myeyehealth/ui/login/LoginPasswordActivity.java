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


        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_button);

        email = getIntent().getStringExtra("email");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = passwordInput.getText().toString().trim();


                UserMethods userMethods = new UserMethods(LoginPasswordActivity.this);
                User user = userMethods.getUserByEmail(email);


                if (user != null && password.equals(user.getPassword())) {

                    SessionManager sessionManager = SessionManager.getInstance(LoginPasswordActivity.this);
                    sessionManager.createLoginSession(user);


                    Intent intent = new Intent(LoginPasswordActivity.this, LoginSuccessActivity.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(LoginPasswordActivity.this, LoginErrorActivity.class);
                    startActivity(intent);
                }

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
