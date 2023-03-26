package com.example.myeyehealth.ui.exercise.saccades;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaccadesExerciseResultsActivity extends AppCompatActivity {

    private LineChart graph;
    private TextView performanceText;
    private Button exportResultsButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_exercise_results);

        // Get references to the UI elements
        graph = findViewById(R.id.graph);
        performanceText = findViewById(R.id.performance_text);
        exportResultsButton = findViewById(R.id.export_results_button);
        saveButton = findViewById(R.id.save_button);

        // Get the tap times and distances from the intent
        Intent intent = getIntent();
        Long[] tapTimes = (Long[]) intent.getSerializableExtra("EXTRA_TAP_TIMES");
        Float[] tapDistances = (Float[]) intent.getSerializableExtra("EXTRA_TAP_DISTANCES");

        // Generate the data entries for the graph
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < tapDistances.length; i++) {
            entries.add(new Entry(i, tapDistances[i]));
        }

        // Create a data set from the data entries
        LineDataSet dataSet = new LineDataSet(entries, "Tap Distances");

        // Customize the appearance of the data set
        dataSet.setLineWidth(2f);
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(Color.BLUE);

        // Create a data object from the data set
        LineData data = new LineData(dataSet);

        // Set the data object on the graph
        graph.setData(data);

        // Update the UI with the performance text
        float improvementPercentage = calculateImprovementPercentage(tapDistances);
        performanceText.setText(String.format("Based on your performance in the most recent test, we have observed a %.1f%% improvement in accuracy compared to your last 5 tests.", improvementPercentage));

        // Set click listeners for the buttons
        exportResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement export functionality
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the saveSaccadesResultsToDatabase method to save the results to the database
                saveSaccadesResultsToDatabase(SaccadesExerciseResultsActivity.this, tapTimes, tapDistances);
                // Display a message to the user indicating that the results have been saved
                Toast.makeText(SaccadesExerciseResultsActivity.this, "Results saved to database", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private float calculateImprovementPercentage(Float[] tapDistances) {
        // Calculate the improvement percentage based on the tap distances compared to the last 5 tests
        // This is just a placeholder implementation
        return 10.0f;
    }
    private void saveSaccadesResultsToDatabase(Context context, Long[] tapTimes, Float[] tapDistances) {
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
        db.getWritableDatabase().insert(Database.TABLE_SACCADES, null, values);

        // Close the database
        db.close();

        // Display a message to the user indicating that the results have been saved
        Toast.makeText(context, "Saccades results saved to database", Toast.LENGTH_SHORT).show();
    }
}
