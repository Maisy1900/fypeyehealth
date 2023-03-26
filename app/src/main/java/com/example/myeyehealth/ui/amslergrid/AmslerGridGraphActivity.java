package com.example.myeyehealth.ui.amslergrid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
        HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("leftEyeDistortionCoordinates");
        HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("rightEyeDistortionCoordinates");
        HashMap<String, Float> distortionPercentages = (HashMap<String, Float>) getIntent().getSerializableExtra("distortionPercentages");

        // Initialize the database
        Database database = Database.getInstance(this);
        SessionManager sessionManager = new SessionManager(this);
        User user = sessionManager.getUser();
        int userId = user.getId();

        // Save the Amsler Grid results to the database
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        database.saveAmslerGridData(userId, currentDate, leftEyeDistortionCoordinates);
        database.saveAmslerGridData(userId, currentDate, rightEyeDistortionCoordinates);

        // Fetch the Amsler Grid results from the database
        List<Entry> previousResults = database.getAmslerGridResults(userId);

        // Initialize the graph with the fetched data
        initGraph(previousResults, distortionPercentages);

        // Set up the save button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current Amsler Grid results to the database
                database.saveAmslerGridData(userId, currentDate, leftEyeDistortionCoordinates);
                database.saveAmslerGridData(userId, currentDate, rightEyeDistortionCoordinates);

                Toast.makeText(AmslerGridGraphActivity.this, "Results saved successfully", Toast.LENGTH_SHORT).show();

                // Return to the main menu activity
                finish();
            }
        });
    }

    private void initGraph(List<Entry> previousResults, HashMap<String, Float> currentResults) {
        LineDataSet previousDataSet = new LineDataSet(previousResults, "Previous Results");
        // Customize the previousDataSet appearance (e.g., colors, line width, etc.)

        List<Entry> currentResultEntries = new ArrayList<>();
        int index = 0;
        for (String section : currentResults.keySet()) {
            currentResultEntries.add(new Entry(index, currentResults.get(section)));
            index++;
        }
        LineDataSet currentDataSet = new LineDataSet(currentResultEntries, "Current Test");
        // Customize the currentDataSet appearance (e.g., colors, line


        LineData lineData = new LineData(previousDataSet, currentDataSet);

        LineChart chart = findViewById(R.id.graph);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateFormatter());
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

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
}
