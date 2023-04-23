package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.BaseActivity;
import com.example.myeyehealth.view.InteractiveAmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridTestResultsActivity extends BaseActivity {

    private ArrayList<ArrayList<Float>> leftEyeDistortionCoordinates;
    private ArrayList<ArrayList<Float>> rightEyeDistortionCoordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_test_results);

        // Get distortion coordinates for both eyes from the intent
        HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinatesMap = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("leftEyeDistortionCoordinates");
        HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinatesMap = (HashMap<String, ArrayList<Float>>) getIntent().getSerializableExtra("rightEyeDistortionCoordinates");

        leftEyeDistortionCoordinates = new ArrayList<>();
        rightEyeDistortionCoordinates = new ArrayList<>();

        for (ArrayList<Float> value : leftEyeDistortionCoordinatesMap.values()) {
            leftEyeDistortionCoordinates.add(value);
        }

        for (ArrayList<Float> value : rightEyeDistortionCoordinatesMap.values()) {
            rightEyeDistortionCoordinates.add(value);
        }

        // Calculate the percentage of distortion for each eye
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);
        HashMap<String, Float> leftEyeDistortionPercentages = interactiveAmslerGridView.calculateQuadrantDistortions(leftEyeDistortionCoordinates);
        HashMap<String, Float> rightEyeDistortionPercentages = interactiveAmslerGridView.calculateQuadrantDistortions(rightEyeDistortionCoordinates);


        // Debug: Print the contents of the leftEyeDistortionPercentages and rightEyeDistortionPercentages maps
        System.out.println("Left Eye Distortion Percentages: " + leftEyeDistortionPercentages);
        System.out.println("Right Eye Distortion Percentages: " + rightEyeDistortionPercentages);

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
                intent.putExtra("leftEyeDistortionCoordinates", leftEyeDistortionCoordinates);
                intent.putExtra("rightEyeDistortionCoordinates", rightEyeDistortionCoordinates);
                intent.putExtra("leftEyeDistortionPercentages", leftEyeDistortionPercentages); // Pass left eye distortion percentages
                intent.putExtra("rightEyeDistortionPercentages", rightEyeDistortionPercentages); // Pass right eye distortion percentages
                startActivity(intent);
            }
        });



    }


    private HashMap<String, Float> calculateDistortionPercentages(ArrayList<ArrayList<Float>> distortionCoordinates) {
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);
        return interactiveAmslerGridView.calculateQuadrantDistortions(distortionCoordinates);
    }



    private String formatResults(HashMap<String, Float> leftEyeDistortionPercentages, HashMap<String, Float> rightEyeDistortionPercentages) {
        StringBuilder resultsBuilder = new StringBuilder();
        resultsBuilder.append("Your results show that:\n\n");
        resultsBuilder.append("Some distortion or missing areas were found in the following quadrants of the grid:\n\n");
// Format results for the left eye
        resultsBuilder.append("Left Eye:\n");
        for (String quadrant : leftEyeDistortionPercentages.keySet()) {
            resultsBuilder.append(String.format("      [%s]    %.2f%%\n", quadrant, leftEyeDistortionPercentages.get(quadrant)));

        }

// Format results for the right eye
        resultsBuilder.append("\nRight Eye:\n");
        for (String quadrant : rightEyeDistortionPercentages.keySet()) {
            resultsBuilder.append(String.format("      [%s]    %.2f%%\n", quadrant, rightEyeDistortionPercentages.get(quadrant)));

        }
        return resultsBuilder.toString();
    }
}