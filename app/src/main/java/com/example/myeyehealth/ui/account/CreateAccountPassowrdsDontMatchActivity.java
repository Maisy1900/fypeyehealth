package com.example.myeyehealth.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;

public class CreateAccountPassowrdsDontMatchActivity extends AppCompatActivity {

    private Button tryAgainButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_passowrds_dont_match);

        tryAgainButton = findViewById(R.id.button2);
        backButton = findViewById(R.id.back_button);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountPassowrdsDontMatchActivity.this, CreateAccountPasswordActivity.class);
                startActivity(intent);
                finish();
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
