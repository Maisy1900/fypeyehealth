package com.example.myeyehealth.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseSingleton {

    private static DatabaseSingleton instance = null;
    private SQLiteDatabase db;
    private Database dbHelper;

    private DatabaseSingleton(Context context) {
        dbHelper = new Database(context);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DatabaseSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseSingleton(context);
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

    public void closeDatabase() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
