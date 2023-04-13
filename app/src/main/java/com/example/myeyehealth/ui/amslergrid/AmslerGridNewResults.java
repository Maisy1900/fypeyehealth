package com.example.myeyehealth.ui.amslergrid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import com.example.myeyehealth.R;

public class AmslerGridNewResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_new_results);

        // Sample values for left and right eye scores
        double leftEyeScore = 75;
        double rightEyeScore = 50;

        // Calculate progress and color for left and right eyes
        String leftEyeColor = leftEyeScore >= 50 ? "#4CAF50" : "#F44336";
        double leftEyeIncrease = (leftEyeScore / 100) * 100;

        String rightEyeColor = rightEyeScore >= 50 ? "#4CAF50" : "#F44336";
        double rightEyeIncrease = (rightEyeScore / 100) * 100;

        // Update the circular progress bars
        CircularProgressBar leftEyeProgress = findViewById(R.id.left_eye_progress);
        leftEyeProgress.setProgress((int) leftEyeIncrease);
        leftEyeProgress.setCircleColor(Color.parseColor(leftEyeColor));

        CircularProgressBar rightEyeProgress = findViewById(R.id.right_eye_progress);
        rightEyeProgress.setProgress((int) rightEyeIncrease);
        rightEyeProgress.setCircleColor(Color.parseColor(rightEyeColor));

        // Update the results TextView
        TextView resultsText = findViewById(R.id.results_text);
        resultsText.setText(String.format("Left Eye Score: %.1f%%\nRight Eye Score: %.1f%%", leftEyeScore, rightEyeScore));
    }
}
