package com.example.myeyehealth.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.BaseActivity;

public class LoginErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_error);

        // Initialize views
        ImageButton backButton = findViewById(R.id.back_button);
        Button retryButton = findViewById(R.id.try_again_button);

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to previous activity
                finish();
            }
        });

        // Set click listener for retry button
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to LoginEmailActivity
                Intent intent = new Intent(LoginErrorActivity.this, LoginEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
