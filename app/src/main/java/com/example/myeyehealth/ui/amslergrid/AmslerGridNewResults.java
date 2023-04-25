package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeyehealth.R;
import com.example.myeyehealth.controller.AmslerGridMethods;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.view.InteractiveAmslerGridView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.HashMap;


public class AmslerGridNewResults extends BaseActivity {
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
    private int leftEyeGridSize;
    private int rightEyeGridSize;

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


        Intent intent = getIntent();
        HashMap<String, ArrayList<Float>> currentLeftEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("leftEyeDistortionCoordinates");
        HashMap<String, ArrayList<Float>> currentRightEyeDistortionCoordinates = (HashMap<String, ArrayList<Float>>) intent.getSerializableExtra("rightEyeDistortionCoordinates");
        InteractiveAmslerGridView interactiveAmslerGridView = new InteractiveAmslerGridView(this);
        leftEyeDistortionCoordinates = currentLeftEyeDistortionCoordinates;
        rightEyeDistortionCoordinates = currentRightEyeDistortionCoordinates;
        leftEyeGridSize = getIntent().getIntExtra("leftEyeGridSize", 0);
        rightEyeGridSize = getIntent().getIntExtra("rightEyeGridSize", 0);


        AmslerGridMethods amslerResultMethods = new AmslerGridMethods(this);
        InteractiveAmslerGridView interactive = new InteractiveAmslerGridView(this);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int userId = user.getId();
        long currentDate = System.currentTimeMillis();


        HashMap<String, ArrayList<Float>>[] baselineResults = amslerResultMethods.getBaselineAmslerResults(userId);

        leftEyeProgress.setProgressMax(1f);
        rightEyeProgress.setProgressMax(1f);


        ArrayList<ArrayList<Float>> baselineLeftEyeCoordinatesList = baseconvertHashMapToArrayList(baselineResults[0]);
        ArrayList<ArrayList<Float>> baselineRightEyeCoordinatesList = baseconvertHashMapToArrayList(baselineResults[1]);

        for (ArrayList<Float> point : baselineLeftEyeCoordinatesList) {
            System.out.println(point);
        }

        for (ArrayList<Float> point : baselineRightEyeCoordinatesList) {
            System.out.println(point);
        }
        HashMap<String, Float> baselineleftEyeDistortionPercentages = interactive.calculateDistortionPercentages(baselineLeftEyeCoordinatesList);
        HashMap<String, Float> baselinerightEyeDistortionPercentages = interactive.calculateDistortionPercentages(baselineRightEyeCoordinatesList);


        double baselineleftEyeDistortionPercentage = baselineleftEyeDistortionPercentages.get("total");
        double baselinerightEyeDistortionPercentage = baselinerightEyeDistortionPercentages.get("total");


        ArrayList<ArrayList<Float>> currentLeftEyeDistortionCoordinatesList = convertHashMapToArrayList(currentLeftEyeDistortionCoordinates);
        ArrayList<ArrayList<Float>> currentRightEyeDistortionCoordinatesList = convertHashMapToArrayList(currentRightEyeDistortionCoordinates);


        HashMap<String, Float> currentLeftEyeDistortionPercentages = interactive.calculateDistortionPercentages(currentLeftEyeDistortionCoordinatesList);
        HashMap<String, Float> currentRightEyeDistortionPercentages = interactive.calculateDistortionPercentages(currentRightEyeDistortionCoordinatesList);


        double currentLeftEyeTotalDistortionPercentage = currentLeftEyeDistortionPercentages.get("total");
        double currentRightEyeTotalDistortionPercentage = currentRightEyeDistortionPercentages.get("total");
        compareDistortions(currentLeftEyeTotalDistortionPercentage, currentRightEyeTotalDistortionPercentage, baselineleftEyeDistortionPercentage, baselinerightEyeDistortionPercentage);
        displayDistortionPercentages(currentLeftEyeTotalDistortionPercentage, currentRightEyeTotalDistortionPercentage,baselineleftEyeDistortionPercentage,baselinerightEyeDistortionPercentage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                amslerResultMethods.saveAmslerGridData(userId, currentDate, leftEyeDistortionCoordinates, rightEyeDistortionCoordinates,leftEyeGridSize,rightEyeGridSize );


                Toast.makeText(AmslerGridNewResults.this, "Results saved successfully", Toast.LENGTH_SHORT).show();


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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                double leftEyeRemainingPercentage = 100 - leftEyeDistortionPercentage;
                double rightEyeRemainingPercentage = 100 - rightEyeDistortionPercentage;

                leftEyeProgress.setProgress((float) leftEyeRemainingPercentage / 100);
                rightEyeProgress.setProgress((float) rightEyeRemainingPercentage / 100);

                leftEyePercentageText.setText(String.format("%.1f%%", leftEyeDistortionPercentage));
                rightEyePercentageText.setText(String.format("%.1f%%", rightEyeDistortionPercentage));

                leftEyeBaseline.setText(String.format("Baseline: %.1f%%", baselineleftEyeDistortionPercentage));
                rightEyeBaseline.setText(String.format("Baseline: %.1f%%", baselinerightEyeDistortionPercentage));
            }
        });


    }


    private void compareDistortions(double leftEyeDistortionPercentage, double rightEyeDistortionPercentage, double baselineLeftEyeDistortionPercentage, double baselineRightEyeDistortionPercentage) {

        // calculates percentage difference distortion compared to baseline
        double leftEyeDifference = leftEyeDistortionPercentage - baselineLeftEyeDistortionPercentage;

        // calculates percentage difference distortion compared to baseline
        double rightEyeDifference = rightEyeDistortionPercentage - baselineRightEyeDistortionPercentage;

        // color thresholds
        String leftEyeColor;
        if (leftEyeDifference >= 10) {
            leftEyeColor = RED_COLOR;
        } else if (leftEyeDifference >= 5) {
            leftEyeColor = ORANGE_COLOR;
        } else {
            leftEyeColor = GREEN_COLOR;
        }

        //  color thresholds
        String rightEyeColor;
        if (rightEyeDifference >= 10) {
            rightEyeColor = RED_COLOR;
        } else if (rightEyeDifference >= 5) {
            rightEyeColor = ORANGE_COLOR;
        } else {
            rightEyeColor = GREEN_COLOR;
        }

        // set progress colors
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
    }



    private ArrayList<ArrayList<Float>> convertHashMapToArrayList(HashMap<String, ArrayList<Float>> hashMap) {
        ArrayList<ArrayList<Float>> arrayList = new ArrayList<>();
        for (ArrayList<Float> value : hashMap.values()) {
            arrayList.add(value);
        }
        return arrayList;
    }
    private ArrayList<ArrayList<Float>> baseconvertHashMapToArrayList(HashMap<String, ArrayList<Float>> coordinates) {
        ArrayList<ArrayList<Float>> coordinatesList = new ArrayList<>();

        if (coordinates.containsKey("x") && coordinates.containsKey("y")) {
            ArrayList<Float> xCoords = coordinates.get("x");
            ArrayList<Float> yCoords = coordinates.get("y");

            if (xCoords != null && yCoords != null) {
                for (int i = 0; i < xCoords.size(); i++) {
                    ArrayList<Float> coordPair = new ArrayList<>();
                    coordPair.add(xCoords.get(i));
                    coordPair.add(yCoords.get(i));
                    coordinatesList.add(coordPair);
                }
            }
        }

        return coordinatesList;
    }


}
