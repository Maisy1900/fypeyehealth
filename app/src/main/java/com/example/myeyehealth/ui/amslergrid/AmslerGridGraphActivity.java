package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.view.InteractiveAmslerGridView;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AmslerGridGraphActivity extends AppCompatActivity {
    private InteractiveAmslerGridView interactiveAmslerGridView;
    private TextView comparisonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_graph);

        interactiveAmslerGridView = new InteractiveAmslerGridView(this);
        ArrayList<ArrayList<Float>> leftEyeDistortionCoordinates = (ArrayList<ArrayList<Float>>) getIntent().getSerializableExtra("leftEyeDistortionCoordinates");
        ArrayList<ArrayList<Float>> rightEyeDistortionCoordinates = (ArrayList<ArrayList<Float>>) getIntent().getSerializableExtra("rightEyeDistortionCoordinates");
        HashMap<String, Float> leftEyeDistortionPercentages = (HashMap<String, Float>) getIntent().getSerializableExtra("leftEyeDistortionPercentages");
        HashMap<String, Float> rightEyeDistortionPercentages = (HashMap<String, Float>) getIntent().getSerializableExtra("rightEyeDistortionPercentages");

        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userID= user.getId();
        long currentDate = System.currentTimeMillis();

        AmslerGridMethods amslerResultMethods = new AmslerGridMethods(this);
        HashMap<Integer, List<HashMap<String, String>>> pastFiveTests = amslerResultMethods.getPastFiveTests(userID);
        HashMap<String, Float> averageDistortionPercentages = interactiveAmslerGridView.getAverageDistortionPercentages(pastFiveTests);

        initGraph(leftEyeDistortionPercentages, rightEyeDistortionPercentages, averageDistortionPercentages);
        comparisonText = findViewById(R.id.comparison_text);

        // Call the compareDistortions method
        compareDistortions(userID, leftEyeDistortionCoordinates, rightEyeDistortionCoordinates);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass both left and right eye HashMaps to the updated saveAmslerGridData() method
                amslerResultMethods.saveAmslerGridData(userID, currentDate, convertToHashMap(leftEyeDistortionCoordinates), convertToHashMap(rightEyeDistortionCoordinates));

                Log.d("AmslerGridGraph", "Results saved successfully by the save button.foruser" + userID);
                Toast.makeText(AmslerGridGraphActivity.this, "Results saved successfully", Toast.LENGTH_SHORT).show();

                // Return to the main menu activity
                Intent mainMenuIntent = new Intent(AmslerGridGraphActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);

            }
        });
    }


    private void initGraph(HashMap<String, Float> leftEyeDistortionPercentages, HashMap<String, Float> rightEyeDistortionPercentages, HashMap<String, Float> averageDistortionPercentages) {

        List<BarEntry> leftEyeResultEntries = new ArrayList<>();
        List<BarEntry> rightEyeResultEntries = new ArrayList<>();
        int index = 0;
        for (String quadrant : leftEyeDistortionPercentages.keySet()) {
            leftEyeResultEntries.add(new BarEntry(index * 2, leftEyeDistortionPercentages.get(quadrant)));
            rightEyeResultEntries.add(new BarEntry(index * 2 + 1, rightEyeDistortionPercentages.get(quadrant)));
            index++;
        }

        BarDataSet leftEyeDataSet = new BarDataSet(leftEyeResultEntries, "Left Eye Test");
        leftEyeDataSet.setColor(Color.RED);
        leftEyeDataSet.setDrawValues(false);

        BarDataSet rightEyeDataSet = new BarDataSet(rightEyeResultEntries, "Right Eye Test");
        rightEyeDataSet.setColor(Color.BLUE);
        rightEyeDataSet.setDrawValues(false);

        BarData barData = new BarData(leftEyeDataSet, rightEyeDataSet);

        CombinedChart combinedChart = findViewById(R.id.combined_chart);
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new QuadrantFormatter());
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);

        combinedChart.getDescription().setEnabled(false);

        ScatterData scatterData = generateScatterData(averageDistortionPercentages);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(scatterData);

        combinedChart.setData(combinedData);
        combinedChart.invalidate();
    }

    private ScatterData generateScatterData(HashMap<String, Float> averageDistortionPercentages) {
        List<Entry> leftEyeEntries = new ArrayList<>();
        List<Entry> rightEyeEntries = new ArrayList<>();
        int index = 0;
        for (String quadrant : averageDistortionPercentages.keySet()) {
            leftEyeEntries.add(new Entry(index * 2, averageDistortionPercentages.get(quadrant)));
            rightEyeEntries.add(new Entry(index * 2 + 1, averageDistortionPercentages.get(quadrant)));
            index++;
        }
        ScatterDataSet leftEyeScatterDataSet = new ScatterDataSet(leftEyeEntries, "Left Eye Average");
        leftEyeScatterDataSet.setColor(Color.GREEN);
        leftEyeScatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        leftEyeScatterDataSet.setDrawValues(false);

        ScatterDataSet rightEyeScatterDataSet = new ScatterDataSet(rightEyeEntries, "Right Eye Average");
        rightEyeScatterDataSet.setColor(Color.MAGENTA);
        rightEyeScatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        rightEyeScatterDataSet.setDrawValues(false);
        ScatterData scatterData = new ScatterData(leftEyeScatterDataSet, rightEyeScatterDataSet);
        return scatterData;
    }

    private class QuadrantFormatter extends ValueFormatter {
        private final String[] quadrantNames = {"Upper Left", "Upper Right", "Lower Left", "Lower Right"};

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            if (value >= 0 && value < quadrantNames.length * 2) {
                return quadrantNames[(int) (value / 2)];
            }
            return "";
        }
    }
    private void compareDistortions(int userId, ArrayList<ArrayList<Float>> leftEyeDistortionCoordinates, ArrayList<ArrayList<Float>> rightEyeDistortionCoordinates) {
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = interactiveAmslerGridView.calculateQuadrantDistortions(leftEyeDistortionCoordinates);
        HashMap<String, Float> rightEyeDistortionPercentages = interactiveAmslerGridView.calculateQuadrantDistortions(rightEyeDistortionCoordinates);

        // Fetch the Amsler Grid results from the database
        AmslerGridMethods database = new AmslerGridMethods(this);
        HashMap<Integer, List<HashMap<String, String>>> pastFiveTests = database.getPastFiveTests(userId);

        // Calculate the average distortions from the past 5 tests
        HashMap<String, Float> averageDistortions = interactiveAmslerGridView.getAverageDistortionPercentages(pastFiveTests);

        // Compare the current distortion percentages with the average of the 5 previous tests
        if (averageDistortions != null) {
            boolean significantChangeDetected = false;
            for (String quadrant : averageDistortions.keySet()) {
                float leftEyeDifference = Math.abs(leftEyeDistortionPercentages.get(quadrant) - averageDistortions.get(quadrant));
                float rightEyeDifference = Math.abs(rightEyeDistortionPercentages.get(quadrant) - averageDistortions.get(quadrant));
                if (leftEyeDifference > 10 || rightEyeDifference > 10) {
                    significantChangeDetected = true;
                    break;
                }
            }

            if (significantChangeDetected) {
                comparisonText.setText("Significant change in vision detected. Seek medical assistance.");
            } else {
                comparisonText.setText("No significant change in vision detected. Continue monitoring.");
            }
        } else {
            comparisonText.setText("Not enough previous results to compare with. Continue monitoring.");
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


