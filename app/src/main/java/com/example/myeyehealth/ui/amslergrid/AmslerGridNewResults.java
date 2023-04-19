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
    private ImageButton backButton, speakerButton, scrollUpButton, scrollDownButton;
    private TextView titleText, resultsSubtitle, resultsText;
    private CircularProgressBar leftEyeProgress, rightEyeProgress;
    private Button saveButton;
    private TextView leftEyePercentageText, rightEyePercentageText;
    private HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_new_results);
        getWindow().getDecorView().invalidate();
        getWindow().getDecorView().requestLayout();

        backButton = findViewById(R.id.back_button);
        speakerButton = findViewById(R.id.speaker_button);
        scrollUpButton = findViewById(R.id.scroll_up_button);
        scrollDownButton = findViewById(R.id.scroll_down_button);
        titleText = findViewById(R.id.title_text);
        resultsSubtitle = findViewById(R.id.results_subtitle);
        resultsText = findViewById(R.id.results_text);
        leftEyeProgress = findViewById(R.id.left_eye_progress);
        rightEyeProgress = findViewById(R.id.right_eye_progress);
        saveButton = findViewById(R.id.save_button);
        leftEyePercentageText = findViewById(R.id.left_eye_percentage);
        rightEyePercentageText = findViewById(R.id.right_eye_percentage);

        // Assume you have the distortion coordinates for the current Amsler Grid
        Intent intent = getIntent();
        leftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("leftEyeDistortionCoordinates");
        rightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("rightEyeDistortionCoordinates");

// Add log statements here
        Log.d("AmslerGridNewResults", "Received Left Eye Distortion Coordinates: " + leftEyeDistortionCoordinates.toString());
        Log.d("AmslerGridNewResults", "Received Right Eye Distortion Coordinates: " + rightEyeDistortionCoordinates.toString());

        AmslerGridMethods amslerResultMethods = new AmslerGridMethods(this);
        InteractiveAmslerGridView interactive = new InteractiveAmslerGridView(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userId= user.getId();
        long currentDate = System.currentTimeMillis();
        // Get the baseline coordinates for the left and right eyes
        HashMap<String, ArrayList<Float>>[] baselineResults = amslerResultMethods.getBaselineAmslerResults(userId);
        HashMap<String, ArrayList<Float>> leftCoordinates = baselineResults[0];
        HashMap<String, ArrayList<Float>> rightCoordinates = baselineResults[1];

        compareDistortions(leftEyeDistortionCoordinates, rightEyeDistortionCoordinates, leftCoordinates, rightCoordinates);
        leftEyeProgress.setProgressMax(1f);
        rightEyeProgress.setProgressMax(1f);
        // Convert HashMap to ArrayList
        ArrayList<ArrayList<Float>> leftEyeDistortionCoordinatesList = convertHashMapToArrayList(leftEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> rightEyeDistortionCoordinatesList = convertHashMapToArrayList(rightEyeDistortionCoordinates);

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = interactive.calculateDistortionPercentages(leftEyeDistortionCoordinatesList);
        HashMap<String, Float> rightEyeDistortionPercentages = interactive.calculateDistortionPercentages(rightEyeDistortionCoordinatesList);

        // Get the total distortion percentages for left and right eyes
        double leftEyeTotalDistortionPercentage = leftEyeDistortionPercentages.get("total");
        double rightEyeTotalDistortionPercentage = rightEyeDistortionPercentages.get("total");

        displayDistortionPercentages(leftEyeTotalDistortionPercentage, rightEyeTotalDistortionPercentage);
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
    }

    private void displayDistortionPercentages(double leftEyeDistortionPercentage, double rightEyeDistortionPercentage) {
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
            }
        });

        // Add log statements for the progress text
        Log.d("AmslerGridNewResults", "Left Eye Progress Text: " + leftEyePercentageText.getText());
        Log.d("AmslerGridNewResults", "Right Eye Progress Text: " + rightEyePercentageText.getText());
    }




    private void compareDistortions(HashMap<String, ArrayList<Float>> leftEyeDistortionCoordinates, HashMap<String, ArrayList<Float>> rightEyeDistortionCoordinates, HashMap<String, ArrayList<Float>> baselineLeftEyeCoordinates, HashMap<String, ArrayList<Float>> baselineRightEyeCoordinates) {
        Log.d("AmslerGridNewResults", "compareDistortions() called");
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);

        // Convert HashMap to ArrayList
        ArrayList<ArrayList<Float>> leftEyeDistortionCoordinatesList = convertHashMapToArrayList(leftEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> rightEyeDistortionCoordinatesList = convertHashMapToArrayList(rightEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> baselineLeftEyeCoordinatesList = convertHashMapToArrayList(baselineLeftEyeCoordinates);
        ArrayList<ArrayList<Float>> baselineRightEyeCoordinatesList = convertHashMapToArrayList(baselineRightEyeCoordinates);

        // Calculate the percentage of distortion for each eye
        HashMap<String, Float> leftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(leftEyeDistortionCoordinatesList);
        HashMap<String, Float> rightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(rightEyeDistortionCoordinatesList);

        // Calculate the baseline distortion percentages
        HashMap<String, Float> baselineLeftEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineLeftEyeCoordinatesList);
        HashMap<String, Float> baselineRightEyeDistortionPercentages = interactiveAmslerGridView.calculateDistortionPercentages(baselineRightEyeCoordinatesList);

        // Calculate percentage increase in distortion compared to baseline for left eye
        double leftEyeIncrease = ((leftEyeDistortionPercentages.get("total") - baselineLeftEyeDistortionPercentages.get("total")) / baselineLeftEyeDistortionPercentages.get("total")) * 100;

        // Calculate percentage increase in distortion compared to baseline for right eye
        double rightEyeIncrease = ((rightEyeDistortionPercentages.get("total") - baselineRightEyeDistortionPercentages.get("total")) / baselineRightEyeDistortionPercentages.get("total")) * 100;

        // Define color thresholds for left eye
        String leftEyeColor;
        if (leftEyeIncrease >= 20) {
            leftEyeColor = "red";
        } else if (leftEyeIncrease >= 10) {
            leftEyeColor = "orange";
        } else {
            leftEyeColor = "green";
        }

        // Define color thresholds for right eye
        String rightEyeColor;
        if (rightEyeIncrease >= 20) {
            rightEyeColor = "red";
        } else if (rightEyeIncrease >= 10) {
            rightEyeColor = "orange";
        } else {
            rightEyeColor = "green";
        }
    // Set the progress colors for left and right eye indicators
        leftEyeProgress.setProgressBarColor(Color.parseColor(leftEyeColor));
        rightEyeProgress.setProgressBarColor(Color.parseColor(rightEyeColor));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (leftEyeIncrease < 10 && rightEyeIncrease < 10) {
                    resultsText.setText("Congratulations! Your Amsler grid test results show no distortion or abnormalities in your vision. Keep up the good work by scheduling regular eye exams and following a healthy lifestyle to maintain your eye health.");
                } else if ((leftEyeIncrease >= 10 && leftEyeIncrease < 20) || (rightEyeIncrease >= 10 && rightEyeIncrease < 20)) {
                    resultsText.setText("Your Amsler grid test results show a slight increase in distortion in one or both eyes. This could be a sign of visual degradation or the early stages of an eye condition. We recommend that you monitor your vision closely and schedule an appointment with an eye care professional if the distortion persists or worsens.");
                } else if (leftEyeIncrease >= 20 || rightEyeIncrease >= 20) {
                    resultsText.setText("Based on your Amsler grid test results, it appears that there has been a significant increase in distortion in one or both eyes. This could be a sign of a serious eye condition that requires immediate attention. We recommend that you schedule an appointment with an eye care professional as soon as possible to receive a thorough evaluation and treatment.");
                } else {
                    resultsText.setText("If your Amsler grid test results show no improvement or a worsening of your vision over time, this may be a sign of an underlying eye condition that requires further evaluation and treatment. We recommend that you see an ophthalmologist or optometrist for a complete eye exam and any necessary medical intervention.");
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
