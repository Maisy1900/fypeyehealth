package com.example.myeyehealth.controller;

import static com.example.myeyehealth.model.Database.COLUMN_AG_USER_ID;
import static com.example.myeyehealth.model.Database.TABLE_AMSLER_GRID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myeyehealth.model.Database;
import com.example.myeyehealth.model.AmslerGridTestData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AmslerGridMethods {
    private SQLiteDatabase db;

    public AmslerGridMethods(Context context) {
        Database database = Database.getInstance(context);
        db = database.getWritableDatabase();
    }

    public int getMaxTestIdForUser(int userId) {
        String query = "SELECT MAX(" + Database.COLUMN_AG_TEST_ID + ") FROM " + TABLE_AMSLER_GRID + " WHERE " + COLUMN_AG_USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(query, null);
        int maxTestId = 0;

        if (cursor.moveToFirst()) {
            maxTestId = cursor.getInt(0);
        }

        cursor.close();
        return maxTestId;
    }

    public int[] getGridSizesForTest(int userId, int testId) {
        int[] gridSizes = new int[2];

        Cursor cursor = db.query(
                Database.TABLE_AMSLER_GRID,
                new String[]{Database.COLUMN_AG_LEFT_GRID_SIZE, Database.COLUMN_AG_RIGHT_GRID_SIZE},
                Database.COLUMN_AG_USER_ID + "=? AND " + Database.COLUMN_AG_TEST_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(testId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            gridSizes[0] = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_AG_LEFT_GRID_SIZE));
            gridSizes[1] = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_AG_RIGHT_GRID_SIZE));
        }

        cursor.close();
        db.close();
        return gridSizes;
    }

    public void saveAmslerGridData(int userId, long date, HashMap<String, ArrayList<Float>> leftCoordinates, HashMap<String, ArrayList<Float>> rightCoordinates, int leftGridSize, int rightGridSize) {
        if (leftCoordinates == null && rightCoordinates == null) {
            Log.d("AmslerGridMethods", "No coordinates to save for user: " + userId + ". Skipping save operation.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String testDate = dateFormat.format(new Date(date));

        // Add log message
        Log.d("AmslerGridMethods", "Saving Amsler grid data for user: " + userId + " and date: " + testDate);

        // Retrieve the maximum testId from the AmslerResult table for the current user
        int maxTestId = getMaxTestIdForUser(userId);

        // Increment the maximum testId by 1 to generate a new unique testId
        int testId = maxTestId + 1;

        if (leftCoordinates != null) {
            saveCoordinates(userId, testDate, testId, "L", leftCoordinates, leftGridSize,rightGridSize);
        }
        if (rightCoordinates != null) {
            saveCoordinates(userId, testDate, testId, "R", rightCoordinates,leftGridSize, rightGridSize);
        }
    }


    private void saveCoordinates(int userId, String testDate, int testId, String gridType, HashMap<String, ArrayList<Float>> coordinates, int leftGridSize, int rightGridSize) {
        for (Map.Entry<String, ArrayList<Float>> entry : coordinates.entrySet()) {
            String grid = gridType;
            ArrayList<Float> coords = entry.getValue();
            int xCoord = Math.round(coords.get(0));
            int yCoord = Math.round(coords.get(1));

            ContentValues values = new ContentValues();
            values.put(COLUMN_AG_USER_ID, userId);
            values.put(Database.COLUMN_AG_TEST_ID, testId);
            values.put(Database.COLUMN_AG_TEST_DATE, testDate);
            values.put(Database.COLUMN_AG_GRID, grid);
            values.put(Database.COLUMN_AG_X_COORD, xCoord);
            values.put(Database.COLUMN_AG_Y_COORD, yCoord);
            values.put(Database.COLUMN_AG_LEFT_GRID_SIZE, leftGridSize); // Add this line to store left grid size
            values.put(Database.COLUMN_AG_RIGHT_GRID_SIZE, rightGridSize); // Add this line to store right grid size

            db.insert(TABLE_AMSLER_GRID, null, values);
            System.out.println("Saved data with Test ID: " + testId); // Add this line for debugging
            Log.d("AmslerGridMethods", "Saved data with Test ID: " + testId);
        }
    }



    public HashMap<String, ArrayList<Float>>[] getBaselineAmslerResults(int userId) {
        HashMap<String, ArrayList<Float>> leftCoordinates = new HashMap<>();
        HashMap<String, ArrayList<Float>> rightCoordinates = new HashMap<>();

        String query = "SELECT * FROM " + Database.TABLE_AMSLER_GRID +
                " WHERE " + Database.COLUMN_AG_USER_ID + " = ?" +
                " AND " + Database.COLUMN_AG_TEST_ID + " = 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String grid = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_GRID));
                float x_coord = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_X_COORD));
                float y_coord = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_Y_COORD));

                if (grid.equals("L")) {
                    addCoordinate(leftCoordinates, x_coord, y_coord);
                } else if (grid.equals("R")) {
                    addCoordinate(rightCoordinates, x_coord, y_coord);
                }
                    Log.d("getBaselineAmslerResults", "LeftCoordinates: " + leftCoordinates.toString() + ", RightCoordinates: " + rightCoordinates.toString());
                } while (cursor.moveToNext());
        }
        cursor.close();

        return new HashMap[]{leftCoordinates, rightCoordinates};
    }

    private void addCoordinate(HashMap<String, ArrayList<Float>> coordinates, float x, float y) {
        ArrayList<Float> x_coords = coordinates.get("x");
        if (x_coords == null) {
            x_coords = new ArrayList<>();
        }
        x_coords.add(x);
        coordinates.put("x", x_coords);

        ArrayList<Float> y_coords = coordinates.get("y");
        if (y_coords == null) {
            y_coords = new ArrayList<>();
        }
        y_coords.add(y);
        coordinates.put("y", y_coords);
    }

    public List<AmslerGridTestData> getSortedAmslerGridTests(String targetUserId) {
        List<AmslerGridTestData> amslerGridTests = new ArrayList<>();
        Set<String> seenTestIds = new HashSet<>(); // Create a set to keep track of seen test IDs

        String selectQuery = "SELECT " + Database.COLUMN_AG_TEST_ID + ", " + Database.COLUMN_AG_USER_ID + ", " + Database.COLUMN_AG_TEST_DATE
                + " FROM " + Database.TABLE_AMSLER_GRID
                + " WHERE " + Database.COLUMN_AG_USER_ID + " = ?"
                + " ORDER BY " + Database.COLUMN_AG_TEST_ID + " DESC";
        SQLiteDatabase db = this.db;

        Cursor cursor = db.rawQuery(selectQuery, new String[]{targetUserId});

        if (cursor.moveToFirst()) {
            do {
                String testId = cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_TEST_ID));
                String userId = cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_USER_ID));

                if (seenTestIds.contains(testId)) {
                    continue; // If the test ID has already been seen, skip this entry
                }

                seenTestIds.add(testId); // Add the test ID to the set of seen test IDs

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date testDate;
                try {
                    testDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(Database.COLUMN_AG_TEST_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                    testDate = new Date(); // Default to the current date if there's an error
                }

                int testNumber = Integer.parseInt(testId); // Use the actual test ID as the test number

                AmslerGridTestData amslerGridTestData = new AmslerGridTestData(testId, userId, testDate, null, testNumber);

                amslerGridTests.add(amslerGridTestData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return amslerGridTests;
    }


    public void close() {
        if (db != null) {
            db.close();
        }
    }
    public HashMap<String, ArrayList<Float>>[] getAmslerGridCoordinatesForTest(int userId, int testId) {
        HashMap<String, ArrayList<Float>> leftCoordinates = new HashMap<>();
        HashMap<String, ArrayList<Float>> rightCoordinates = new HashMap<>();

        String query = "SELECT * FROM " + Database.TABLE_AMSLER_GRID +
                " WHERE " + Database.COLUMN_AG_USER_ID + " = ?" +
                " AND " + Database.COLUMN_AG_TEST_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(testId)});

        if (cursor.moveToFirst()) {
            do {
                String grid = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_GRID));
                float x_coord = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_X_COORD));
                float y_coord = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_AG_Y_COORD));

                Log.d("AmslerTestResult", "Grid: " + grid + ", X: " + x_coord + ", Y: " + y_coord);

                if (grid.equals("L")) {
                    addCoordinate(leftCoordinates, x_coord, y_coord);
                } else if (grid.equals("R")) {
                    addCoordinate(rightCoordinates, x_coord, y_coord);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("AmslerTestResult", "LeftCoordinates: " + leftCoordinates);
        Log.d("AmslerTestResult", "RightCoordinates: " + rightCoordinates);

        return new HashMap[]{leftCoordinates, rightCoordinates};
    }

}
