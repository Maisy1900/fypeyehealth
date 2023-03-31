package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public SaccadesData getPastFiveSaccadesTests(int userId) {
        ArrayList<Integer> exerciseNumbers = new ArrayList<>();
        ArrayList<Float> completionTimes = new ArrayList<>();

        String[] columns = {
                Database.COLUMN_SACCADES_TEST_NUMBER,
                Database.COLUMN_SACCADES_TIME_TAKEN,
        };

        String selection = Database.COLUMN_SACCADES_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String orderBy = Database.COLUMN_SACCADES_TEST_NUMBER + " DESC";
        String limit = "5";
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.query(Database.TABLE_SACCADES, columns, selection, selectionArgs, null, null, orderBy, limit);

        int testNumberIndex = cursor.getColumnIndex(Database.COLUMN_SACCADES_TEST_NUMBER);
        int timeTakenIndex = cursor.getColumnIndex(Database.COLUMN_SACCADES_TIME_TAKEN);

        while (cursor.moveToNext()) {
            int testNumber = cursor.getInt(testNumberIndex);
            String timeTakenString = cursor.getString(timeTakenIndex);
            long[] timeTakenArray = stringToLongArray(timeTakenString);
            float timeTaken = 0;
            for (long tapTime : timeTakenArray) {
                timeTaken += tapTime;
            }
            timeTaken /= timeTakenArray.length;

            exerciseNumbers.add(testNumber);
            completionTimes.add(timeTaken);
        }
        cursor.close();

        return new SaccadesData(exerciseNumbers, completionTimes);
    }
    private long[] stringToLongArray(String str) {
        str = str.replaceAll("\\[|\\]|\\s", "");
        String[] strArray = str.split(",");
        long[] longArray = new long[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            longArray[i] = Long.parseLong(strArray[i]);
        }
        return longArray;
    }



}