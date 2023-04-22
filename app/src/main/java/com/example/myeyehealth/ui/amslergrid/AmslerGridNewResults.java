package com.example.myeyehealth.ui.amslergrid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.view.InteractiveAmslerGridView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.HashMap;


public class AmslerGridNewResults extends AppCompatActivity {
    private static final String RED_COLOR = "#FF0000";
    private static final String ORANGE_COLOR = "#FFA500";
    private static final String GREEN_COLOR = "#008000";
    private ImageButton backButton, speakerButton, scrollUpButton, scrollDownButton;
    private TextView titleText, resultsSubtitle, resultsText;
    private CircularProgressBar leftEyeProgress, rightEyeProgress;
    private Button saveButton;
    private TextView leftEyePercentageText, rightEyePercentageText,leftEyeBaseline,rightEyeBaseline ;
    private HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_new_results);
        getWindow().getDecorView().invalidate();
        getWindow().getDecorView().requestLayout();

        backButton = findViewById(R.id.back_button);
        speakerButton = findViewById(R.id.speaker_button);
        titleText = findViewById(R.id.title_text);
        resultsSubtitle = findViewById(R.id.results_subtitle);
        resultsText = findViewById(R.id.results_text);
        leftEyeProgress = findViewById(R.id.left_eye_progress);
        rightEyeProgress = findViewById(R.id.right_eye_progress);
        saveButton = findViewById(R.id.save_button);
        leftEyePercentageText = findViewById(R.id.left_eye_percentage);
        rightEyePercentageText = findViewById(R.id.right_eye_percentage);
        leftEyeBaseline = findViewById(R.id.left_eye_baseline);
        rightEyeBaseline = findViewById(R.id.right_eye_baseline);

        // Assume you have the distortion coordinates for the current Amsler Grid
        Intent intent = getIntent();
        HashMap<String, ArrayList<Float>> currentLeftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("leftEyeDistortionCoordinates");
        HashMap<String, ArrayList<Float>> currentRightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("rightEyeDistortionCoordinates");
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);
        // Calculate the percentage of distortion for each eye


        // Add log statements here
        Log.d("AmslerGridNewResults", "Received Current Left Eye Distortion Coordinates: " + currentLeftEyeDistortionCoordinates.toString());
        Log.d("AmslerGridNewResults", "Received Current Right Eye Distortion Coordinates: " + currentRightEyeDistortionCoordinates.toString());

        AmslerGridMethods amslerResultMethods = new AmslerGridMethods(this);
        InteractiveAmslerGridView interactive = new InteractiveAmslerGridView(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userId = user.getId();
        long currentDate = System.currentTimeMillis();

        // Get the baseline coordinates for the left and right eyes
        HashMap<String, ArrayList<Float>>[] baselineResults = amslerResultMethods.getBaselineAmslerResults(userId);
        HashMap<String, ArrayList<Float>> baselineLeftEyeCoordinates = baselineResults[0];
        HashMap<String, ArrayList<Float>> baselineRightEyeCoordinates = baselineResults[1];
        ArrayList<ArrayList<Float>> baselineLeftEyeCoordinatesList = convertHashMapToArrayList(baselineResults[0]);
        ArrayList<ArrayList<Float>> baselineRightEyeCoordinatesList = convertHashMapToArrayList(baselineResults[1]);
        HashMap<String, Float> baselineleftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineLeftEyeCoordinatesList);
        HashMap<String, Float> baselinerightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineRightEyeCoordinatesList);
        double baselineleftEyeDistortionPercentage = baselineleftEyeDistortionPercentages.get("total");
        double baselinerightEyeDistortionPercentage = baselinerightEyeDistortionPercentages.get("total");
        leftEyeProgress.setProgressMax(1f);
        rightEyeProgress.setProgressMax(1f);

        // Convert HashMap to ArrayList for current coordinates
        ArrayList<ArrayList<Float>> currentLeftEyeDistortionCoordinatesList = convertHashMapToArrayList(currentLeftEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> currentRightEyeDistortionCoordinatesList = convertHashMapToArrayList(currentRightEyeDistortionCoordinates);

        // Calculate the percentage of distortion for each eye based on the current coordinates
        HashMap<String, Float> currentLeftEyeDistortionPercentages = interactive.calculateDistortionPercentages(currentLeftEyeDistortionCoordinatesList);
        HashMap<String, Float> currentRightEyeDistortionPercentages = interactive.calculateDistortionPercentages(currentRightEyeDistortionCoordinatesList);

        // Get the total distortion percentages for left and right eyes based on the current coordinates
        double currentLeftEyeTotalDistortionPercentage = currentLeftEyeDistortionPercentages.get("total");
        double currentRightEyeTotalDistortionPercentage = currentRightEyeDistortionPercentages.get("total");
        compareDistortions(currentLeftEyeTotalDistortionPercentage, currentRightEyeTotalDistortionPercentage, baselineleftEyeDistortionPercentage, baselinerightEyeDistortionPercentage);
        displayDistortionPercentages(currentLeftEyeTotalDistortionPercentage, currentRightEyeTotalDistortionPercentage,baselineleftEyeDistortionPercentage,baselinerightEyeDistortionPercentage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass both left and right eye HashMaps to the updated saveAmslerGridData() method
                amslerResultMethods.saveAmslerGridData(userId, currentDate, leftEyeDistortionCoordinates, rightEyeDistortionCoordinates);

                Log.d("AmslerGridGraph", "Results saved successfully by the save button.foruser" + userId);
                Toast.makeText(AmslerGridNewResults.this, "Results saved successfully", Toast.LENGTH_SHORT).show();

                // Return to the main menu activity
                Intent mainMenuIntent = new Intent(AmslerGridNewResults.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void displayDistortionPercentages(double leftEyeDistortionPercentage, double rightEyeDistortionPercentage,double baselineleftEyeDistortionPercentage,double baselinerightEyeDistortionPercentage) {
        Log.d("AmslerGridNewResults", "Left Eye Distortion Percentage: " + leftEyeDistortionPercentage);
        Log.d("AmslerGridNewResults", "Right Eye Distortion Percentage: " + rightEyeDistortionPercentage);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Calculate the remaining percentage
                double leftEyeRemainingPercentage = 100 - leftEyeDistortionPercentage;
                double rightEyeRemainingPercentage = 100 - rightEyeDistortionPercentage;

                // Set the progress for each eye's CircularProgressBar
                leftEyeProgress.setProgress((float) leftEyeRemainingPercentage / 100);
                rightEyeProgress.setProgress((float) rightEyeRemainingPercentage / 100);

                // Update the TextView in the center of CircularProgressBars with the calculated distortion percentages
                leftEyePercentageText.setText(String.format("%.1f%%", leftEyeDistortionPercentage));
                rightEyePercentageText.setText(String.format("%.1f%%", rightEyeDistortionPercentage));

                // Update the TextViews with the baseline distortion percentages
                leftEyeBaseline.setText(String.format("Baseline: %.1f%%", baselineleftEyeDistortionPercentage));
                rightEyeBaseline.setText(String.format("Baseline: %.1f%%", baselinerightEyeDistortionPercentage));
            }
        });

        // Add log statements for the progress text
        Log.d("AmslerGridNewResults", "Left Eye Progress Text: " + leftEyePercentageText.getText());
        Log.d("AmslerGridNewResults", "Right Eye Progress Text: " + rightEyePercentageText.getText());
    }

    private void compareDistortions(double leftEyeDistortionPercentage, double rightEyeDistortionPercentage, double baselineLeftEyeDistortionPercentage, double baselineRightEyeDistortionPercentage) {
        Log.d("AmslerGridNewResults", "compareDistortions() called");

        // Calculate percentage difference in distortion compared to baseline for left eye
        double leftEyeDifference = leftEyeDistortionPercentage - baselineLeftEyeDistortionPercentage;

        // Calculate percentage difference in distortion compared to baseline for right eye
        double rightEyeDifference = rightEyeDistortionPercentage - baselineRightEyeDistortionPercentage;

        // Define color thresholds for left eye
        String leftEyeColor;
        if (leftEyeDifference >= 10) {
            leftEyeColor = RED_COLOR;
        } else if (leftEyeDifference >= 5) {
            leftEyeColor = ORANGE_COLOR;
        } else {
            leftEyeColor = GREEN_COLOR;
        }

        // Define color thresholds for right eye
        String rightEyeColor;
        if (rightEyeDifference >= 10) {
            rightEyeColor = RED_COLOR;
        } else if (rightEyeDifference >= 5) {
            rightEyeColor = ORANGE_COLOR;
        } else {
            rightEyeColor = GREEN_COLOR;
        }

        // Set the progress colors for left and right eye indicators
        leftEyeProgress.setProgressBarColor(Color.parseColor(leftEyeColor));
        rightEyeProgress.setProgressBarColor(Color.parseColor(rightEyeColor));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (leftEyeDifference < 5 && rightEyeDifference < 5) {
                    resultsText.setText("Your Amsler grid test results are very close to the baseline, indicating no significant changes in your vision. Continue testing regularly to monitor your eye health and maintain a healthy lifestyle.");
                } else if ((leftEyeDifference >= 5 && leftEyeDifference < 10) || (rightEyeDifference >= 5 && rightEyeDifference < 10)) {
                    resultsText.setText("Your Amsler grid test results show a slight increase in distortion in one or both eyes. This could be a sign of visual degradation. We recommend monitoring your vision closely and scheduling an appointment with an eye care professional if the distortion persists or worsens.");
                } else if ((leftEyeDifference >= 10 && leftEyeDifference < 15) || (rightEyeDifference >= 10 && rightEyeDifference < 15)) {
                    resultsText.setText("Your Amsler grid test results show a moderate increase in distortion in one or both eyes. This could be a sign of an eye condition. We recommend seeing an ophthalmologist to get a thorough evaluation of your eye health.");
                } else if (leftEyeDifference >= 15 || rightEyeDifference >= 15) {
                    resultsText.setText("Based on your Amsler grid test results, there has been a significant increase in distortion in one or both eyes. This could be a sign of a serious eye condition that requires immediate attention. We recommend scheduling an appointment with an eye care professional as soon as possible to receive a thorough evaluation and treatment.");
                }
                resultsText.invalidate();
            }
        });


        // Add log statement for the resultsText
        Log.d("AmslerGridNewResults", "Results Text: " + resultsText.getText());

    }



    private ArrayList<ArrayList<Float>> convertHashMapToArrayList(HashMap<String, ArrayList<Float>> hashMap) {
        ArrayList<ArrayList<Float>> arrayList = new ArrayList<>();
        for (ArrayList<Float> value : hashMap.values()) {
            arrayList.add(value);
        }
        return arrayList;
    }
    private HashMap<String, ArrayList<Float>> convertToHashMap(ArrayList<ArrayList<Float>> coordinates) {
        HashMap<String, ArrayList<Float>> result = new HashMap<>();
        for (int i = 0; i < coordinates.size(); i++) {
            result.put("Quadrant" + (i + 1), coordinates.get(i));
        }
        return result;
    }
    public HashMap<String, ArrayList<Float>> getLeftEyeDistortionCoordinates() {
        return leftEyeDistortionCoordinates;
    }

    public HashMap<String, ArrayList<Float>> getRightEyeDistortionCoordinates() {
        return rightEyeDistortionCoordinates;
    }
}
