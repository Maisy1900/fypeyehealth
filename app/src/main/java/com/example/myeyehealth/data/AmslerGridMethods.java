package com.example.myeyehealth.data;

import static com.example.myeyehealth.data.Database.COLUMN_AG_USER_ID;
import static com.example.myeyehealth.data.Database.TABLE_AMSLER_GRID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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


    public List<Entry> getAmslerGridResults(int userId) {
        Log.d("AmslerGridResults", "Getting Amsler grid results for user: " + userId);

        List<Entry> results = new ArrayList<>();
        String[] columns = {
                Database.COLUMN_AG_TEST_DATE,
                Database.COLUMN_AG_X_COORD,
                Database.COLUMN_AG_Y_COORD
        };
        String selection = COLUMN_AG_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_AMSLER_GRID, columns, selection, selectionArgs, null, null, Database.COLUMN_AG_TEST_DATE + " ASC");
        Log.d("AmslerGridResults", "Cursor rows: " + cursor.getCount());

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
        Log.d("AmslerGridResults", "Results found: " + results.size());

        return results;
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

    public void saveAmslerGridData(int userId, long date, HashMap<String, ArrayList<Float>> leftCoordinates, HashMap<String, ArrayList<Float>> rightCoordinates) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String testDate = dateFormat.format(new Date(date));

        // Add log message
        Log.d("AmslerGridMethods", "Saving Amsler grid data for user: " + userId + " and date: " + testDate);

        // Retrieve the maximum testId from the AmslerResult table for the current user
        int maxTestId = getMaxTestIdForUser(userId);

        // Increment the maximum testId by 1 to generate a new unique testId
        int testId = maxTestId + 1;

        saveCoordinates(userId, testDate, testId, "L", leftCoordinates);
        saveCoordinates(userId, testDate, testId, "R", rightCoordinates);
    }

    private void saveCoordinates(int userId, String testDate, int testId, String gridType, HashMap<String, ArrayList<Float>> coordinates) {
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

            db.insert(TABLE_AMSLER_GRID, null, values);
            System.out.println("Saved data with Test ID: " + testId); // Add this line for debugging
            Log.d("AmslerGridMethods", "Saved data with Test ID: " + testId);
        }
    }



    public HashMap<Integer, List<HashMap<String, String>>> getPastFiveTests(int userId) {
        HashMap<Integer, List<HashMap<String, String>>> groupedResults = new LinkedHashMap<>();
        List<HashMap<String, String>> results = new ArrayList<>();
        String[] columns = {
                Database.COLUMN_AG_ID,
                Database.COLUMN_AG_TEST_ID,
                Database.COLUMN_AG_TEST_DATE,
                Database.COLUMN_AG_GRID,
                Database.COLUMN_AG_X_COORD,
                Database.COLUMN_AG_Y_COORD,
                Database.COLUMN_AG_CREATED_DATE
        };

        String selection = COLUMN_AG_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String orderBy = Database.COLUMN_AG_TEST_ID + " DESC";
        Cursor cursor = db.query(TABLE_AMSLER_GRID, columns, selection, selectionArgs, null, null, orderBy);

        int idIndex = cursor.getColumnIndex(Database.COLUMN_AG_ID);
        int testIdIndex = cursor.getColumnIndex(Database.COLUMN_AG_TEST_ID);
        int testDateIndex = cursor.getColumnIndex(Database.COLUMN_AG_TEST_DATE);
        int gridIndex = cursor.getColumnIndex(Database.COLUMN_AG_GRID);
        int xCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_X_COORD);
        int yCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_Y_COORD);
        int createdDateIndex = cursor.getColumnIndex(Database.COLUMN_AG_CREATED_DATE);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idIndex);
            int testId = cursor.getInt(testIdIndex);
            String testDate = cursor.getString(testDateIndex);
            String grid = cursor.getString(gridIndex);
            int xCoord = cursor.getInt(xCoordIndex);
            int yCoord = cursor.getInt(yCoordIndex);
            String createdDate = cursor.getString(createdDateIndex);

            HashMap<String, String> result = new HashMap<>();
            result.put("id", String.valueOf(id));
            result.put("testId", String.valueOf(testId));
            result.put("testDate", testDate);
            result.put("grid", grid);
            result.put("xCoord", String.valueOf(xCoord));
            result.put("yCoord", String.valueOf(yCoord));
            result.put("createdDate", createdDate);

            results.add(result);
        }
        cursor.close();

        Set<Integer> uniqueTestIds = new LinkedHashSet<>();
        for (HashMap<String, String> result : results) {
            int testId = Integer.parseInt(result.get("testId"));
            if (uniqueTestIds.size() < 5) {
                uniqueTestIds.add(testId);
            } else if (!uniqueTestIds.contains(testId)) {
                continue;
            }

            if (!groupedResults.containsKey(testId)) {
                groupedResults.put(testId, new ArrayList<>());
            }
            groupedResults.get(testId).add(result);
        }

        return groupedResults;
    }
    public HashMap<String, Float> calculateAverageDistortions(int userId) {
        HashMap<String, Float> averageDistortions = new HashMap<>();
        HashMap<Integer, List<HashMap<String, String>>> pastFiveTests = getPastFiveTests(userId);

        int totalTests = pastFiveTests.size();
        if (totalTests == 0) {
            return averageDistortions;
        }
        float leftEyeQuadrant1Total = 0;
        float leftEyeQuadrant2Total = 0;
        float leftEyeQuadrant3Total = 0;
        float leftEyeQuadrant4Total = 0;
        float rightEyeQuadrant1Total = 0;
        float rightEyeQuadrant2Total = 0;
        float rightEyeQuadrant3Total = 0;
        float rightEyeQuadrant4Total = 0;

        for (List<HashMap<String, String>> test : pastFiveTests.values()) {
            for (HashMap<String, String> distortion : test) {
                String grid = distortion.get("grid");
                int xCoord = Integer.parseInt(distortion.get("xCoord"));
                int yCoord = Integer.parseInt(distortion.get("yCoord"));

                if ("Left".equals(grid)) {
                    if (xCoord <= 50 && yCoord <= 50) {
                        leftEyeQuadrant1Total++;
                    } else if (xCoord > 50 && yCoord <= 50) {
                        leftEyeQuadrant2Total++;
                    } else if (xCoord <= 50 && yCoord > 50) {
                        leftEyeQuadrant3Total++;
                    } else if (xCoord > 50 && yCoord > 50) {
                        leftEyeQuadrant4Total++;
                    }
                } else if ("Right".equals(grid)) {
                    if (xCoord <= 50 && yCoord <= 50) {
                        rightEyeQuadrant1Total++;
                    } else if (xCoord > 50 && yCoord <= 50) {
                        rightEyeQuadrant2Total++;
                    } else if (xCoord <= 50 && yCoord > 50) {
                        rightEyeQuadrant3Total++;
                    } else if (xCoord > 50 && yCoord > 50) {
                        rightEyeQuadrant4Total++;
                    }
                }
            }
        }

        averageDistortions.put("leftEyeQuadrant1", leftEyeQuadrant1Total / totalTests);
        averageDistortions.put("leftEyeQuadrant2", leftEyeQuadrant2Total / totalTests);
        averageDistortions.put("leftEyeQuadrant3", leftEyeQuadrant3Total / totalTests);
        averageDistortions.put("leftEyeQuadrant4", leftEyeQuadrant4Total / totalTests);
        averageDistortions.put("rightEyeQuadrant1", rightEyeQuadrant1Total / totalTests);
        averageDistortions.put("rightEyeQuadrant2", rightEyeQuadrant2Total / totalTests);
        averageDistortions.put("rightEyeQuadrant3", rightEyeQuadrant3Total / totalTests);
        averageDistortions.put("rightEyeQuadrant4", rightEyeQuadrant4Total / totalTests);
        return averageDistortions;
    }

    public void getAllAmslerGridResults(int userId) {
        // Define the SQL query to fetch all Amsler Grid results for the user with the specified ID
        String query = "SELECT * FROM " + TABLE_AMSLER_GRID + " WHERE " + COLUMN_AG_USER_ID + " = " + userId;

        // Execute the query and store the result in a Cursor object
        Cursor cursor = db.rawQuery(query, null);

        // Check if there are any entries in the database for the specified user ID
        if (cursor.getCount() == 0) {
            Log.d("MyApp", "No Amsler Grid results found for user ID: " + userId);
            return;
        }

        // Loop through the cursor and log the results
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Database.COLUMN_AG_ID);
            int testIdIndex = cursor.getColumnIndex(Database.COLUMN_AG_TEST_ID);
            int testDateIndex = cursor.getColumnIndex(Database.COLUMN_AG_TEST_DATE);
            int gridIndex = cursor.getColumnIndex(Database.COLUMN_AG_GRID);
            int xCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_X_COORD);
            int yCoordIndex = cursor.getColumnIndex(Database.COLUMN_AG_Y_COORD);

            do {
                int id = cursor.getInt(idIndex);
                int testId = cursor.getInt(testIdIndex);
                String testDate = cursor.getString(testDateIndex);
                String grid = cursor.getString(gridIndex);
                int xCoord = cursor.getInt(xCoordIndex);
                int yCoord = cursor.getInt(yCoordIndex);

                // Log the results for debugging purposes
                Log.d("MyApp", "ID: " + id + ", User ID: " + userId + ", Test ID: " + testId + ", Test Date: " + testDate + ", Grid: " + grid + ", X Coord: " + xCoord + ", Y Coord: " + yCoord);
            } while (cursor.moveToNext());
        }

        // Close the cursor
        cursor.close();
    }



    public void close() {
        if (db != null) {
            db.close();
        }
    }
}
