package com.example.myeyehealth.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;

public class CreateAccountSuccessActivity extends AppCompatActivity {

    private TextView userDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_success);

        userDetailsTextView = findViewById(R.id.user_details_text_view);

        // Get the ID of the newly created user
        int userId = getIntent().getIntExtra("user_id", -1);

        if (userId != -1) {
            // Fetch the user object from the database
            Database db = Database.getInstance(this);
            User user = db.getUserById(userId);
            db.close();

            if (user != null) {
                // Display the user details in the TextView
                userDetailsTextView.setText(user.toString());
            } else {
                // User not found in the database, display an error message
                userDetailsTextView.setText("Error: User not found in database");
            }
        } else {
            // Invalid user ID, display an error message
            userDetailsTextView.setText("Error: Invalid user ID");
        }
    }

    @Override
    public void onBackPressed() {
        // Start the MainMenuActivity and clear the activity stack
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
