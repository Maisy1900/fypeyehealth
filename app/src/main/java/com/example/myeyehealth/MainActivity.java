package com.example.myeyehealth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.data.Database;

public class MainActivity extends AppCompatActivity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper object
        database = new Database(this);

        Button testButton = findViewById(R.id.test_database_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Test the database by adding a user and checking if it exists
                database.addUser("John Smith", "john.smith@example.com", "password123",
                        "Dr. Brown", "dr.brown@example.com", "Jane Doe", "jane.doe@example.com");
                boolean userExists = database.checkUserEmailExists("john.smith@example.com");
                if (userExists) {
                    // User exists, display a toast message
                    Toast.makeText(MainActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // User does not exist, display a toast message
                    Toast.makeText(MainActivity.this, "Failed to add user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
