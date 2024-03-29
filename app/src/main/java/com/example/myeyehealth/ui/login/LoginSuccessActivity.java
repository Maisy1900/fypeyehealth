package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;

public class LoginSuccessActivity extends BaseActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);


        sessionManager = SessionManager.getInstance(this);


        ImageButton backButton = findViewById(R.id.back_button);
        Button completeButton = findViewById(R.id.complete_button);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log user out here
                sessionManager.logout();
                finish();
            }
        });



        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginSuccessActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
