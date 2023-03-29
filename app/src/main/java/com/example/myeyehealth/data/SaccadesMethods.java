package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SaccadesMethods {
    private final Context context;

    public SaccadesMethods(Context context) {
        this.context = context;
    }

    public void addSaccadesData(int user_id, int test_number, float time_taken,
                                float distance, String testDate) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_SACCADES_USER_ID, user_id);
        values.put(Database.COLUMN_SACCADES_TEST_NUMBER, test_number);
        values.put(Database.COLUMN_SACCADES_TIME_TAKEN, time_taken);
        values.put(Database.COLUMN_SACCADES_DISTANCE, distance);
        values.put(Database.COLUMN_SACCADES_TEST_DATE, testDate); // add test date to values
        db.insert(Database.TABLE_SACCADES, null, values);
        db.close();
    }

    public void addSaccadesData(int user_id, int test_number, ArrayList<Float> distances, String test_date) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_SACCADES_USER_ID, user_id);
        values.put(Database.COLUMN_SACCADES_TEST_NUMBER, test_number);
        values.put(Database.COLUMN_SACCADES_TIME_TAKEN, 0); // You may need to modify this based on your requirements.
        values.put(Database.COLUMN_SACCADES_TEST_DATE, test_date);

        for (Float distance : distances) {
            values.put(Database.COLUMN_SACCADES_DISTANCE, distance);
            db.insert(Database.TABLE_SACCADES, null, values);
        }

        db.close();
    }
}
