package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridTestResultsActivity extends AppCompatActivity {

    private HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_test_results);

        // Get distortion coordinates for both eyes from the intent
        leftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("leftEyeDistortionCoordinates");
        rightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("rightEyeDistortionCoordinates");

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = calculateDistortionPercentages(leftEyeDistortionCoordinates);
        HashMap<String, Float> rightEyeDistortionPercentages = calculateDistortionPercentages(rightEyeDistortionCoordinates);

        // Display the results
        TextView resultsText = findViewById(R.id.results_text);
        resultsText.setText(formatResults(leftEyeDistortionPercentages, rightEyeDistortionPercentages));

        // Set up back button and continue button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AmslerGridTestResultsActivity.this, AmslerGridGraphActivity.class);
                intent.putExtra("leftEyeDistortionPercentages", leftEyeDistortionPercentages);
                intent.putExtra("rightEyeDistortionPercentages", rightEyeDistortionPercentages);
                startActivity(intent);
            }
        });
    }

    private HashMap<String, Float> calculateDistortionPercentages(HashMap<String, ArrayList<Float>> distortionCoordinates) {
        // Implement the logic to calculate distortion percentages for each grid section using distortionCoordinates
        HashMap<String, Float> distortionPercentages = new HashMap<>();

        // Calculate the distortion percentages based on the received coordinates
        // Example: distortionPercentages.put("Upper Left", calculatedPercentage);

        return distortionPercentages;
    }

    private String formatResults(HashMap<String, Float> leftEyeDistortionPercentages, HashMap<String, Float> rightEyeDistortionPercentages) {
        StringBuilder resultsBuilder = new StringBuilder();
        resultsBuilder.append("Your results show that:\n\n");
        resultsBuilder.append("Some distortion or missing areas were found in the following parts of the grid:\n\n");

        // Format results for the left eye
        resultsBuilder.append("Left Eye:\n");
        for (String section : leftEyeDistortionPercentages.keySet()) {
            resultsBuilder.append(String.format("      [%s]    %.0f%%\n", section, leftEyeDistortionPercentages.get(section)));
        }

        // Format results for the right eye
        resultsBuilder.append("\nRight Eye:\n");
        for (String section : rightEyeDistortionPercentages.keySet()) {
            resultsBuilder.append(String.format("      [%s]    %.0f%%\n", section, rightEyeDistortionPercentages.get(section)));
        }
        return resultsBuilder.toString();
    }
}