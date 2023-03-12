package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.models.User;
import com.example.myeyehealth.ui.MainMenuActivity;

public class CreateAccountSuccessActivity extends AppCompatActivity {

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_success);

        // Get the current user from the database based on their email
        String email = getIntent().getStringExtra("email");
        Database db = Database.getInstance(this);
        currentUser = db.getUserByEmail(email);

        // Start the session by passing the current user to the MainMenuActivity
        Intent intent = new Intent(CreateAccountSuccessActivity.this, MainMenuActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
        finish();
    }
}
