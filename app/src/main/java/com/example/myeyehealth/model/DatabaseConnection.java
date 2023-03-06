package com.example.myeyehealth.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "http://localhost/fypdb/dbconnect.php";

    public Connection connect() {
        Connection connection = null;
        try {
            // Create a request to the PHP file
            URL url = new URL(DB_URL);
            HttpURLConnection connectionRequest = (HttpURLConnection) url.openConnection();
            connectionRequest.setRequestMethod("GET");
            connectionRequest.connect();

            // Check if the connection was successful
            if (connectionRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get the connection string from the PHP file's response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connectionRequest.getInputStream()));
                String connectionString = reader.readLine();

                // Connect to the database
                connection = DriverManager.getConnection(connectionString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
