package com.example.myeyehealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myeyehealth.model.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private Button testConnectionButton;
    private TextView connectionResultTextView;
    private DatabaseConnection dbConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testConnectionButton = findViewById(R.id.test_connection_button);
        connectionResultTextView = findViewById(R.id.connection_result_text_view);
        dbConnection = new DatabaseConnection();

        testConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = testConnection();
                if (result != null) {
                    connectionResultTextView.setText(result);
                } else {
                    connectionResultTextView.setText("Connection failed");
                }
            }
        });
    }

    private String testConnection() {
        String url = "http://localhost/fypdb/dbconnect.php";
        HttpURLConnection connection = null;
        try {
            // Open connection
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Return response
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
