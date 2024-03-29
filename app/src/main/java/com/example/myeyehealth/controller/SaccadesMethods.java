package com.example.myeyehealth.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myeyehealth.model.Database;
import com.example.myeyehealth.model.SaccadesData;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.User;

import java.util.ArrayList;
import java.util.Arrays;

public class SaccadesMethods {
    private final Context context;

    public SaccadesMethods(Context context) {
        this.context = context;
    }


    public SaccadesData getPastNSaccadesTests(int userId, int numLogs) {
        ArrayList<Integer> exerciseNumbers = new ArrayList<>();
        ArrayList<Float> completionTimes = new ArrayList<>();

        String[] columns = {
                Database.COLUMN_SACCADES_TEST_NUMBER,
                Database.COLUMN_SACCADES_TIME_TAKEN,
        };

        String selection = Database.COLUMN_SACCADES_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String orderBy = Database.COLUMN_SACCADES_TEST_NUMBER + " DESC";
        String limit = String.valueOf(numLogs);
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

    public void saveSaccadesResultsToDatabase(Context context, long[] tapTimes, float[] tapDistances) {
        Database db = Database.getInstance(context);
        SessionManager sessionManager = SessionManager.getInstance(context);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();

        String countQuery = "SELECT COUNT(*) FROM " + Database.TABLE_SACCADES + " WHERE " + Database.COLUMN_SACCADES_USER_ID + " = ?";
        Cursor cursor = db.getReadableDatabase().rawQuery(countQuery, new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        int testNumber = cursor.getInt(0) + 1;
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_SACCADES_USER_ID, userId);
        values.put(Database.COLUMN_SACCADES_TEST_NUMBER, testNumber);
        values.put(Database.COLUMN_SACCADES_TEST_DATE, String.valueOf(System.currentTimeMillis()));
        values.put(Database.COLUMN_SACCADES_TIME_TAKEN, Arrays.toString(tapTimes));
        values.put(Database.COLUMN_SACCADES_DISTANCE, Arrays.toString(tapDistances));
        db.close();
    }



}
