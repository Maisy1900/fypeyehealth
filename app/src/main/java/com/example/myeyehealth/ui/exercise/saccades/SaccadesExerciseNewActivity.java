package com.example.myeyehealth.ui.exercise.saccades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.SaccadesData;
import com.example.myeyehealth.data.SaccadesMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaccadesExerciseNewActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView titleText, resultsSubtitle, bestTime, timeTaken, resultsText;
    private ProgressBar personalRecordProgress;
    private Button saveButton;
    ArrayList<Long> totalTimePerTest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_exercise_new);

        // Initialize views
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        resultsSubtitle = findViewById(R.id.results_subtitle);
        bestTime = findViewById(R.id.best_time);
        personalRecordProgress = findViewById(R.id.personal_record_progress);
        timeTaken = findViewById(R.id.time_taken);
        resultsText = findViewById(R.id.results_text);
        saveButton = findViewById(R.id.save_button);

        SessionManager sessionManager = SessionManager.getInstance(this);
        User currentUser = sessionManager.getUser();
        int userId = currentUser.getId();
        System.out.println("this is user" +userId);

        SaccadesMethods saccadesMethods = new SaccadesMethods(SaccadesExerciseNewActivity.this);
        SaccadesData pastTestResultsData = saccadesMethods.getPastNSaccadesTests(userId, 20);
        ArrayList<Integer> exerciseNumbers = pastTestResultsData.getExerciseNumbers();
        ArrayList<Float> pastTestResults = pastTestResultsData.getCompletionTimes();

        ArrayList<Float> completionTimes = new ArrayList<>();
        for (Float testResult : pastTestResults) {
            completionTimes.add(testResult / 1000);
        }

        // Print each completion time
        Log.d("CompletionTimes", "Completion times: ");
        for (int i = 0; i < completionTimes.size(); i++) {
            Log.d("CompletionTimes", "Test " + (i + 1) + ": " + completionTimes.get(i) + "s");
        }



        Intent intent = getIntent();
        long[] tapTimes = getIntent().getLongArrayExtra("EXTRA_TAP_TIMES");
        long[] timeDifferences = getIntent().getLongArrayExtra("EXTRA_TIME_DIFFERENCES");
        float[] tapDistances = getIntent().getFloatArrayExtra("EXTRA_TAP_DISTANCES");
        // Check if tapTimes and tapDistances are not null
        if (tapTimes == null || tapDistances == null) {
            Toast.makeText(SaccadesExerciseNewActivity.this, "Error: Could not retrieve tap times and distances.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Calculate the total time taken for the last test
        float lastTestTotalTime = 0;
        for (long tapTime : tapTimes) {
            lastTestTotalTime += tapTime;
        }
        lastTestTotalTime /= 1000; // Convert to seconds

// Add the last test's total time to the completionTimes list
// Add the last test's total time to the completionTimes list
        completionTimes.add(lastTestTotalTime);
        System.out.println("Current time" + lastTestTotalTime);
        List<Integer> saccadesTestNumbers = new ArrayList<>();

        for (int i = 0; i < pastTestResultsData.getExerciseNumbers().size(); i++) {
            saccadesTestNumbers.add(pastTestResultsData.getExerciseNumbers().get(i));
        }

// Add the current test number to the saccadesTestNumbers list
        int currentTestNumber = saccadesTestNumbers.size() > 0 ? saccadesTestNumbers.get(saccadesTestNumbers.size() - 1) + 1 : 1;
        saccadesTestNumbers.add(currentTestNumber);


// Calculate the minimum time (best time) among completionTimes
        float bestTimeValue = getBestTotalCompletionTime(completionTimes);
        System.out.println("best full time"+bestTimeValue);
// Update the bestTime TextView
        bestTime.setText("Best Time: " + String.format("%.2f", bestTimeValue) + "s");

// Update the timeTaken TextView
        timeTaken.setText("Time Taken: " + String.format("%.2f", lastTestTotalTime) + "s");

// Classify performance and update resultsText and personalRecordProgress
        // Classify performance and update resultsText and personalRecordProgress
        String performance = classifyPerformance(completionTimes, lastTestTotalTime);

        int progressValue;
        int progressColor;

        switch (performance) {
            case "Green (best or close to best time)":
                resultsText.setText("Great job! Your timings are above average. Keep up the good work!");
                progressValue = 100;
                progressColor = getResources().getColor(R.color.green);
                break;
            case "Orange":
                resultsText.setText("Your timings are average. Keep practicing to improve your performance.");
                progressValue = 50;
                progressColor = getResources().getColor(R.color.amber);
                break;
            default:
                resultsText.setText("Your timings are below average. You might need to do another Amsler test to ensure your vision has not deteriorated.");
                progressValue = 0;
                progressColor = getResources().getColor(R.color.red);
        }

        personalRecordProgress.setProgress(progressValue);
        personalRecordProgress.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);

        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saccadesMethods.saveSaccadesResultsToDatabase(SaccadesExerciseNewActivity.this, tapTimes, tapDistances);
                Toast.makeText(SaccadesExerciseNewActivity.this, "Results saved to database", Toast.LENGTH_SHORT).show();
                Intent mainMenuIntent = new Intent(SaccadesExerciseNewActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

// Handle back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(SaccadesExerciseNewActivity.this, MainMenuActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });


    }

    private String classifyPerformance(ArrayList<Float> completionTimes, float lastTestCompletionTime) {
        int numResults = completionTimes.size();

        // Handle the case when there's only one result
        if (numResults == 1) {
            return "This is your first result. Please keep testing.";
        }

        Collections.sort(completionTimes);

        // Calculate the interquartile range (IQR)
        float q1 = completionTimes.get(numResults / 4);
        float q3 = completionTimes.get(3 * numResults / 4);
        float iqr = q3 - q1;
        float median = completionTimes.get(numResults / 2);
        float bestCompletionTime = completionTimes.get(0);

        // Determine the threshold value based on the median and best completion time
        float threshold = 0.1f * (median - bestCompletionTime);

        // Classify performance
        if (lastTestCompletionTime <= (bestCompletionTime + threshold)) {
            return "Green (best or close to best time)";
        } else if (lastTestCompletionTime > q1 && lastTestCompletionTime <= q3) {
            return "Orange";
        } else {
            return "Red (worst time)";
        }
    }






    public float getBestTotalCompletionTime(List<Float> completionTimes) {
        float bestTime = Float.MAX_VALUE;
        for (float time : completionTimes) {
            if (time < bestTime) {
                bestTime = time;
            }
        }
        return bestTime;
    }


}