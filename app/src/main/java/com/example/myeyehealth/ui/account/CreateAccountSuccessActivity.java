package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.controller.UserMethods;
import com.example.myeyehealth.model.User;

public class CreateAccountSuccessActivity extends BaseActivity {

    private TextView userDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CreateAccountSuccess", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_success);

        userDetailsTextView = findViewById(R.id.user_details_text_view);
        ImageButton backButton = findViewById(R.id.back_button);
        Button completeButton = findViewById(R.id.complete_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountSuccessActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        int userId = getIntent().getIntExtra("user_id", -1);

        if (userId != -1) {
            UserMethods userMethods = new UserMethods(this);
            User user = userMethods.getUserById(userId);

            if (user != null) {
                userDetailsTextView.setText(user.toString());
                SessionManager sessionManager = SessionManager.getInstance(this);
                sessionManager.startSession(user);
            } else {
                userDetailsTextView.setText("Error: User not found in database");
            }
        } else {
            userDetailsTextView.setText("Error: Invalid user ID");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
