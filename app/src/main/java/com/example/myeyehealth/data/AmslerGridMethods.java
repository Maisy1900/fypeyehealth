package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myeyehealth.model.AmslerResult;
import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AmslerGridMethods {

    private final Context context;

    public AmslerGridMethods(Context context) {
        this.context = context;
    }

    public void addAmslerGridData(int user_id, int test_id, String test_date, String grid,
                                  int x_coord, int y_coord, String notes, String created_date) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_AG_USER_ID, user_id);
        values.put(Database.COLUMN_AG_TEST_ID, test_id);
        values.put(Database.COLUMN_AG_TEST_DATE, test_date);
        values.put(Database.COLUMN_AG_GRID, grid);
        values.put(Database.COLUMN_AG_X_COORD, x_coord);
        values.put(Database.COLUMN_AG_Y_COORD, y_coord);
        values.put(Database.COLUMN_AG_CREATED_DATE, created_date);
        db.insert(Database.TABLE_AMSLER_GRID, null, values);
        db.close();
    }
    public List<Entry> getAmslerGridResults(int userId) {
        List<Entry> results = new ArrayList<>();
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String[] columns = {
                Database.COLUMN_AG_TEST_DATE,
                Database.COLUMN_AG_X_COORD,
                Database.COLUMN_AG_Y_COORD
        };
        String selection = Database.COLUMN_AG_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(Database.TABLE_AMSLER_GRID, columns, selection, selectionArgs, null, null, Database.COLUMN_AG_TEST_DATE + " ASC");

        int testDateIndex = cursor.getColumnIndex(Database.COLUMN_AG_TEST_DATE);
        int xCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_X_COORD);
        int yCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_Y_COORD);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        while (cursor.moveToNext()) {
            String testDate = cursor.getString(testDateIndex);
            int xCoord = cursor.getInt(xCoordIndex);
            int yCoord = cursor.getInt(yCoordIndex);

            try {
                Date date = dateFormat.parse(testDate);
                if (date != null) {
                    long timeInMillis = date.getTime();
                    float xAxisValue = (float) timeInMillis;
                    float yAxisValue = (float) Math.sqrt(xCoord * xCoord + yCoord * yCoord); // Calculate the distance from the origin.
                    results.add(new Entry(xAxisValue, yAxisValue));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        db.close();
        return results;
    }

    public void saveAmslerGridData(int userId, String testDate, HashMap<String, ArrayList<Float>> distortionCoordinates) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        for (Map.Entry<String, ArrayList<Float>> entry : distortionCoordinates.entrySet()) {
            String grid = entry.getKey();
            ArrayList<Float> coordinates = entry.getValue();
            int xCoord = Math.round(coordinates.get(0));
            int yCoord = Math.round(coordinates.get(1));

            ContentValues values = new ContentValues();
            values.put(Database.COLUMN_AG_USER_ID, userId);
            values.put(Database.COLUMN_AG_TEST_DATE, testDate);
            values.put(Database.COLUMN_AG_GRID, grid);
            values.put(Database.COLUMN_AG_X_COORD, xCoord);
            values.put(Database.COLUMN_AG_Y_COORD, yCoord);

            db.insert(Database.TABLE_AMSLER_GRID, null, values);
        }

        db.close();
    }
    public long insertResult(AmslerResult result) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Database.COLUMN_AG_USER_ID, result.getUserId());
        contentValues.put(Database.COLUMN_AG_TEST_ID, result.getTestId());
        contentValues.put(Database.COLUMN_AG_TEST_DATE, result.getTestDate());
        contentValues.put(Database.COLUMN_AG_GRID, result.getGrid());
        contentValues.put(Database.COLUMN_AG_X_COORD, result.getXCoord());
        contentValues.put(Database.COLUMN_AG_Y_COORD, result.getYCoord());

        long newRowId = db.insert(Database.TABLE_AMSLER_GRID, null, contentValues);
        db.close();
        return newRowId;
    }
    public Cursor getLatestAmslerGridResultForUser(int userId) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT * FROM " + Database.TABLE_AMSLER_GRID + " WHERE " + Database.COLUMN_AG_USER_ID + " = ? ORDER BY " + Database.COLUMN_AG_TEST_DATE + " DESC LIMIT 1";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
    public void displayLatestAmslerGridResultForUser(int userId) {
        Cursor cursor = getLatestAmslerGridResultForUser(userId);

        if (cursor.moveToFirst()) {
            String leftEyeCoordinates = cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_X_COORD));
            String rightEyeCoordinates = cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_Y_COORD));
            String testDate = cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_TEST_DATE));
            // Print the data using System.out
            System.out.println("Previous Amsler Grid performance data for user ID " + userId + ":");
            System.out.println("Left Eye Coordinates: " + leftEyeCoordinates);
            System.out.println("Right Eye Coordinates: " + rightEyeCoordinates);
            System.out.println("Test Date: " + testDate);
        } else {
            System.out.println("No previous test results found for user ID " + userId + ".");
        }

        cursor.close();
    }



}
