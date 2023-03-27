package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AmslerGridGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_graph);

// Get distortion coordinates and percentages from the intent
        ArrayList<ArrayList<Float>> leftEyeDistortionCoordinates = (ArrayList<ArrayList<Float>>) getIntent().getSerializableExtra("leftEyeDistortionCoordinates");
        ArrayList<ArrayList<Float>> rightEyeDistortionCoordinates = (ArrayList<ArrayList<Float>>) getIntent().getSerializableExtra("rightEyeDistortionCoordinates");
        HashMap<String, Float> leftEyeDistortionPercentages = (HashMap<String, Float>) getIntent().getSerializableExtra("leftEyeDistortionPercentages");
        HashMap<String, Float> rightEyeDistortionPercentages = (HashMap<String, Float>) getIntent().getSerializableExtra("rightEyeDistortionPercentages");

        // Initialize the database
        Database database = Database.getInstance(this);
        SessionManager sessionManager = new SessionManager(this);
        User user = sessionManager.getUser();
        int userId = user.getId();

        // Save the Amsler Grid results to the database
// Save the Amsler Grid results to the database
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        if (leftEyeDistortionCoordinates != null) {
            database.saveAmslerGridData(userId, currentDate, convertToHashMap(leftEyeDistortionCoordinates));
        }

        if (rightEyeDistortionCoordinates != null) {
            database.saveAmslerGridData(userId, currentDate, convertToHashMap(rightEyeDistortionCoordinates));
        }

        // Fetch the Amsler Grid results from the database
        List<Entry> previousResults = database.getAmslerGridResults(userId);

        // Initialize the graph with the fetched data
// Initialize the graph with the fetched data
        initGraph(previousResults, leftEyeDistortionPercentages, rightEyeDistortionPercentages);


        // Set up the save button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current Amsler Grid results to the database
                database.saveAmslerGridData(userId, currentDate, convertToHashMap(leftEyeDistortionCoordinates));
                database.saveAmslerGridData(userId, currentDate, convertToHashMap(rightEyeDistortionCoordinates));

                Toast.makeText(AmslerGridGraphActivity.this, "Results saved successfully", Toast.LENGTH_SHORT).show();

                // Return to the main menu activity
                Intent mainMenuIntent = new Intent(AmslerGridGraphActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

    }

    private void initGraph(List<Entry> previousResults, HashMap<String, Float> leftEyeDistortionPercentages, HashMap<String, Float> rightEyeDistortionPercentages){
        LineDataSet previousDataSet = null;

        if (previousResults != null && !previousResults.isEmpty()) {
            previousDataSet = new LineDataSet(previousResults, "Previous Results");
            // Customize the previousDataSet appearance (e.g., colors, line width, etc.)
            previousDataSet.setColor(Color.BLUE);
            previousDataSet.setLineWidth(2f);
            previousDataSet.setCircleColor(Color.BLUE);
            previousDataSet.setCircleRadius(4f);
            previousDataSet.setDrawCircleHole(false);
        }

        List<Entry> leftEyeResultEntries = new ArrayList<>();
        int leftEyeIndex = 0;
        for (String quadrant : leftEyeDistortionPercentages.keySet()) {
            leftEyeResultEntries.add(new Entry(leftEyeIndex, leftEyeDistortionPercentages.get(quadrant)));
            leftEyeIndex++;
        }

        List<Entry> rightEyeResultEntries = new ArrayList<>();
        int rightEyeIndex = 0;
        for (String quadrant : rightEyeDistortionPercentages.keySet()) {
            rightEyeResultEntries.add(new Entry(rightEyeIndex, rightEyeDistortionPercentages.get(quadrant)));
            rightEyeIndex++;
        }

        LineDataSet leftEyeDataSet = new LineDataSet(leftEyeResultEntries, "Left Eye Test");
        leftEyeDataSet.setColor(Color.RED);
        leftEyeDataSet.setLineWidth(2f);
        leftEyeDataSet.setCircleColor(Color.RED);
        leftEyeDataSet.setCircleRadius(4f);
        leftEyeDataSet.setDrawCircleHole(false);

        LineDataSet rightEyeDataSet = new LineDataSet(rightEyeResultEntries, "Right Eye Test");
        rightEyeDataSet.setColor(Color.GREEN);
        rightEyeDataSet.setLineWidth(2f);
        rightEyeDataSet.setCircleColor(Color.GREEN);
        rightEyeDataSet.setCircleRadius(4f);
        rightEyeDataSet.setDrawCircleHole(false);


        LineData lineData;
        if (previousDataSet != null) {
            lineData = new LineData(previousDataSet, leftEyeDataSet, rightEyeDataSet);
        } else {
            lineData = new LineData(leftEyeDataSet, rightEyeDataSet);
        }


        LineChart chart = findViewById(R.id.graph);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateFormatter());
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.getDescription().setEnabled(false);

        chart.invalidate();
    }


    private static class DateFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat;

        DateFormatter() {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        }

        @Override
        public String getFormattedValue(float value) {
            return dateFormat.format(new Date((long) value));
        }
    }
    private HashMap<String, ArrayList<Float>> convertToHashMap(ArrayList<ArrayList<Float>> coordinates) {
        HashMap<String, ArrayList<Float>> result = new HashMap<>();
        for (int i = 0; i < coordinates.size(); i++) {
            result.put("Quadrant" + (i + 1), coordinates.get(i));
        }
        return result;
    }

}
