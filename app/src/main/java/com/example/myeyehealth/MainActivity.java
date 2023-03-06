package com.example.myeyehealth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myeyehealth.model.DatabaseConnection;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    private Button testConnectionButton;
    private TextView connectionResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to UI elements
        testConnectionButton = findViewById(R.id.test_connection_button);
        connectionResultTextView = findViewById(R.id.connection_result_text_view);

        // Set click listener for testConnectionButton
        testConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Test database connection
                DatabaseConnection databaseConnection = new DatabaseConnection();
                Connection connection = databaseConnection.connect();
                if (connection != null) {
                    connectionResultTextView.setText("Connection Successful");
                    databaseConnection.disconnect(connection);
                } else {
                    connectionResultTextView.setText("Connection Failed");
                }
            }
        });
    }
}
