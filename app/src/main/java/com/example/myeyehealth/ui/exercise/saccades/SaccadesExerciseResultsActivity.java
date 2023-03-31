package com.example.myeyehealth.ui.exercise.saccades;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SaccadesData;
import com.example.myeyehealth.data.SaccadesMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SaccadesExerciseResultsActivity extends AppCompatActivity {


    private TextView performanceText;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_exercise_results);

        // Get references to the UI elements
        ScatterChart chart = findViewById(R.id.graph);




        performanceText = findViewById(R.id.performance_text);
        saveButton = findViewById(R.id.save_button);

        // Get the tap times and distances from the intent
        Intent intent = getIntent();
        long[] tapTimes = getIntent().getLongArrayExtra("EXTRA_TAP_TIMES");
        long[] timeDifferences = getIntent().getLongArrayExtra("EXTRA_TIME_DIFFERENCES");
        float[] tapDistances = getIntent().getFloatArrayExtra("EXTRA_TAP_DISTANCES");
        Log.d("SaccadesResults", "tapTimes: " + Arrays.toString(tapTimes));
        Log.d("SaccadesResults", "timeDifferences: " + Arrays.toString(timeDifferences));
        Log.d("SaccadesResults", "tapDistances: " + Arrays.toString(tapDistances));



        // Check if tapTimes and tapDistances are not null
        if (tapTimes == null || tapDistances == null) {
            Toast.makeText(SaccadesExerciseResultsActivity.this, "Error: Could not retrieve tap times and distances.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Calculate the average time for the current test
        float currentTestAverageTime = 0;
        for (long tapTime : tapTimes) {
            currentTestAverageTime += tapTime;
        }
        currentTestAverageTime /= tapTimes.length;

        // Get the current user ID
        SessionManager sessionManager = SessionManager.getInstance(this);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();

        // Call the getPastFiveSaccadesTests method to get the past 5 test results
        SaccadesMethods saccadesMethods = new SaccadesMethods(SaccadesExerciseResultsActivity.this);
        SaccadesData pastTestResultsData = saccadesMethods.getPastFiveSaccadesTests(userId);
        ArrayList<Integer> exerciseNumbers = pastTestResultsData.getExerciseNumbers();
        ArrayList<Float> pastTestResults = pastTestResultsData.getCompletionTimes();

        String TAG = "SaccadesResults"; // Add this line if the TAG constant is not defined in your class

        // Calculate the total time taken for each test
        float[] testTimes = new float[pastTestResults.size()];
        for (int i = 0; i < pastTestResults.size(); i++) {
            testTimes[i] = pastTestResults.get(i) / 1000; // Convert to seconds
        }


        // Add the test times to the entries

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < testTimes.length; i++) {
            Log.d(TAG, "Adding entry: x=" + (i + 1) + ", y=" + testTimes[i]);
            entries.add(new Entry(i + 1, testTimes[i]));
        }

// Add the current result to the entries
        int lastTestNumber = exerciseNumbers.isEmpty() ? 0 : Collections.max(exerciseNumbers);
        entries.add(new Entry(lastTestNumber + 1, currentTestAverageTime / 1000)); // Convert to seconds




        Log.d("SaccadesResults", "entries after loop: " + entries);

    // Add the current result to the entries

        Log.d("SaccadesResults", "Data values: " + entries.toString());

        ScatterDataSet dataSet = new ScatterDataSet(entries, "Time Taken");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);
        dataSet.setValueTextSize(12f);
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        dataSet.setScatterShapeSize(10f);
        dataSet.setScatterShapeHoleColor(Color.WHITE);
        dataSet.setScatterShapeHoleRadius(3f);

        ScatterData scatterData = new ScatterData(dataSet);

        chart.setData(scatterData);


        chart.getDescription().setText("Saccades Performance");
        chart.getDescription().setTextColor(Color.BLUE);
        chart.getDescription().setTextSize(14f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.GREEN);
        xAxis.setAxisLineColor(Color.YELLOW);
        xAxis.setAxisLineWidth(2f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Current Test"}));
        xAxis.setLabelCount(entries.size(), true);



        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setTextColor(Color.CYAN);
        yAxisLeft.setAxisLineColor(Color.MAGENTA);
        yAxisLeft.setAxisLineWidth(2f);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        chart.animateX(1000);


        // Calculate the improvement percentage based on the past test results
        float improvementPercentage = calculateImprovementPercentage(pastTestResults);

// Update the UI with the performance text
        if (improvementPercentage >= 0) {
            performanceText.setText(String.format("Based on your performance in the most recent test, we have observed a %.1f%% improvement in accuracy compared to your last 5 tests.", improvementPercentage));
        } else {
            performanceText.setText(String.format("Based on your performance in the most recent test, we have observed a %.1f%% decline in accuracy compared to your last 5 tests.", -improvementPercentage));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveSaccadesResultsToDatabase(SaccadesExerciseResultsActivity.this, tapTimes, tapDistances);

                Toast.makeText(SaccadesExerciseResultsActivity.this, "Results saved to database", Toast.LENGTH_SHORT).show();

                Intent mainMenuIntent = new Intent(SaccadesExerciseResultsActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });


    }

    private float calculateImprovementPercentage(ArrayList<Float> pastTestResults) {
        // Calculate the improvement percentage based on the past test results
        if (pastTestResults.size() < 2) {
            return 0.0f;
        }

        float improvementSum = 0;

        for (int i = 1; i < pastTestResults.size(); i++) {
            float prevAvgTime = pastTestResults.get(i - 1);
            float currentAvgTime = pastTestResults.get(i);
            float improvement = (prevAvgTime - currentAvgTime) / prevAvgTime * 100;
            improvementSum += improvement;
        }

        return improvementSum / (pastTestResults.size() - 1);
    }

    private void saveSaccadesResultsToDatabase(Context context, long[] tapTimes, float[] tapDistances) {
        // Get a reference to your database
        Database db = Database.getInstance(context);

        // Get the current user ID
        SessionManager sessionManager = SessionManager.getInstance(context);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();

        // Get the number of existing tests for the user
        String countQuery = "SELECT COUNT(*) FROM " + Database.TABLE_SACCADES + " WHERE " + Database.COLUMN_SACCADES_USER_ID + " = ?";
        Cursor cursor = db.getReadableDatabase().rawQuery(countQuery, new String[]{String.valueOf(userId)});
        cursor.moveToFirst();
        int testNumber = cursor.getInt(0) + 1;
        cursor.close();

        // Create a ContentValues object to store the values you want to insert into the database
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_SACCADES_USER_ID, userId);
        values.put(Database.COLUMN_SACCADES_TEST_NUMBER, testNumber);
        values.put(Database.COLUMN_SACCADES_TEST_DATE, String.valueOf(System.currentTimeMillis()));
        values.put(Database.COLUMN_SACCADES_TIME_TAKEN, Arrays.toString(tapTimes));
        values.put(Database.COLUMN_SACCADES_DISTANCE, Arrays.toString(tapDistances));

        // Insert the values into the database
        long saccadesId = db.getWritableDatabase().insert(Database.TABLE_SACCADES, null, values);

        // Close the database
        db.close();

        // Display a message to the user indicating that the results have been saved
        Toast.makeText(context, "Saccades results saved to database", Toast.LENGTH_SHORT).show();
    }
}