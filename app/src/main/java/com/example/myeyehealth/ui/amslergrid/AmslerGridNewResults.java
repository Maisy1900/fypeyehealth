package com.example.myeyehealth.ui.amslergrid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.view.InteractiveAmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class AmslerGridNewResults extends AppCompatActivity {
    private ImageButton backButton, speakerButton, scrollUpButton, scrollDownButton;
    private TextView titleText, resultsSubtitle, resultsText;
    private CircularProgressBar leftEyeProgress, rightEyeProgress;
    private Button startTestButton;
    private TextView leftEyePercentageText, rightEyePercentageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_new_results);

        backButton = findViewById(R.id.back_button);
        speakerButton = findViewById(R.id.speaker_button);
        scrollUpButton = findViewById(R.id.scroll_up_button);
        scrollDownButton = findViewById(R.id.scroll_down_button);
        titleText = findViewById(R.id.title_text);
        resultsSubtitle = findViewById(R.id.results_subtitle);
        resultsText = findViewById(R.id.results_text);
        leftEyeProgress = findViewById(R.id.left_eye_progress);
        rightEyeProgress = findViewById(R.id.right_eye_progress);
        startTestButton = findViewById(R.id.start_test_button);
        leftEyePercentageText = findViewById(R.id.left_eye_percentage);
        rightEyePercentageText = findViewById(R.id.right_eye_percentage);

        // Assume you have the distortion coordinates for the current Amsler Grid
        Intent intent = getIntent();
        HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("leftEyeDistortionCoordinates");
        HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("rightEyeDistortionCoordinates");

        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userId= user.getId();


        displayDistortionPercentages(userId, leftEyeDistortionCoordinates, rightEyeDistortionCoordinates);

    }
    private void displayDistortionPercentages(int userId, HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates, HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates) {
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);

        // Convert HashMap to ArrayList
        ArrayList<ArrayList<Float>> leftEyeDistortionCoordinatesList = convertHashMapToArrayList(leftEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> rightEyeDistortionCoordinatesList = convertHashMapToArrayList(rightEyeDistortionCoordinates);

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(leftEyeDistortionCoordinatesList);
        HashMap<String, Float> rightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(rightEyeDistortionCoordinatesList);

        // Update the CircularProgressBars and display the percentages
        leftEyeProgress.setProgress(leftEyeDistortionPercentages.get("total"));
        rightEyeProgress.setProgress(rightEyeDistortionPercentages.get("total"));
        leftEyePercentageText.setText(String.format("%.1f%%", leftEyeDistortionPercentages.get("total")));
        rightEyePercentageText.setText(String.format("%.1f%%", rightEyeDistortionPercentages.get("total")));
    }

    private void compareDistortions(int userId, ArrayList<ArrayList<Float>> leftEyeDistortionCoordinates, ArrayList<ArrayList<Float>> rightEyeDistortionCoordinates) {
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(leftEyeDistortionCoordinates);
        HashMap<String, Float> rightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(rightEyeDistortionCoordinates);

        // Fetch the Amsler Grid results from the database
        AmslerGridMethods database = new AmslerGridMethods(this);
        HashMap<String, ArrayList<ArrayList<Float>>> baselineDistortionCoordinates = database.getBaselineTest(userId);

        // Calculate the baseline distortion percentages
        HashMap<String, Float> baselineLeftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineDistortionCoordinates.get("leftEye"));
        HashMap<String, Float> baselineRightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineDistortionCoordinates.get("rightEye"));

        // Compare the current distortion percentages with the baseline test
        if (baselineLeftEyeDistortionPercentages != null && baselineRightEyeDistortionPercentages != null) {
            boolean significantChangeDetected = false;
            float leftEyeDifference = Math.abs(leftEyeDistortionPercentages.get("total") - baselineLeftEyeDistortionPercentages.get("total"));
            float rightEyeDifference = Math.abs(rightEyeDistortionPercentages.get("total") - baselineRightEyeDistortionPercentages.get("total"));
            if (leftEyeDifference > 10 || rightEyeDifference > 10) {
                significantChangeDetected = true;
            }

            if (significantChangeDetected) {
                comparisonText.setText("Significant change in vision detected. Seek medical assistance.");
            } else {
                comparisonText.setText("No significant change in vision detected. Continue monitoring.");
            }
        } else {
            comparisonText.setText("No baseline test found. This will be set as your baseline test.");
            // Save the current test as the baseline test
            database.saveBaselineTest(userId, leftEyeDistortionPercentages, rightEyeDistortionPercentages);
        }
    }
    private ArrayList<ArrayList<Float>> convertHashMapToArrayList(HashMap<String, ArrayList<Float>> hashMap) {
        ArrayList<ArrayList<Float>> arrayList = new ArrayList<>();
        for (ArrayList<Float> value : hashMap.values()) {
            arrayList.add(value);
        }
        return arrayList;
    }


}
