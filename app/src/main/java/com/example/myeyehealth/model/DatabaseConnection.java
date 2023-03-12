package com.example.myeyehealth.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

import java.sql.*;

public class DatabaseConnection {
    private Connection conn;

    public DatabaseConnection(String url, String username, String password) throws SQLException {
        this.conn = DriverManager.getConnection(url, username, password);
    }

    public void close() throws SQLException {
        if (this.conn != null) {
            this.conn.close();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }
}

