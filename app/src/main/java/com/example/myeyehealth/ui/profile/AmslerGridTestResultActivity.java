package com.example.myeyehealth.ui.profile;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.view.AmslerGridPlotView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AmslerGridTestResultActivity extends SessionActivity {
    public static final String TEST_ID_KEY = "test_id_key";
    private static final int GRID_LINES = 40;
    private HashMap<String, ArrayList<Float>> leftEyeCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeCoordinates;
    private int leftEyeGridSize;
    private int rightEyeGridSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_test_result);
        AmslerGridPlotView leftEyeAmslerGridPlotView = findViewById(R.id.leftEyeAmslerGridPlotView);
        AmslerGridPlotView rightEyeAmslerGridPlotView = findViewById(R.id.rightEyeAmslerGridPlotView);

        String testId = getIntent().getStringExtra(TEST_ID_KEY);
        Log.d("AmslerTestResult", "Test ID: " + testId);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int currentUserId = user.getId();
        leftEyeGridSize = getIntent().getIntExtra("leftEyeGridSize", 0);
        rightEyeGridSize = getIntent().getIntExtra("rightEyeGridSize", 0);

        AmslerGridMethods amslerGridMethods = new AmslerGridMethods(this);
        HashMap<String, ArrayList<Float>>[] coordinates = amslerGridMethods.getAmslerGridCoordinatesForTest(currentUserId, Integer.parseInt(testId));
        int[] gridSizes = amslerGridMethods.getGridSizesForTest(currentUserId, Integer.parseInt(testId));
        leftEyeGridSize = gridSizes[0];
        rightEyeGridSize = gridSizes[1];

        leftEyeCoordinates = normalizeCoordinates(coordinates[0], leftEyeGridSize);
        rightEyeCoordinates = normalizeCoordinates(coordinates[1], rightEyeGridSize);

        leftEyeAmslerGridPlotView.setOriginalGridSize(leftEyeGridSize);
        rightEyeAmslerGridPlotView.setOriginalGridSize(rightEyeGridSize);

        leftEyeAmslerGridPlotView.setCoordinates(leftEyeCoordinates);
        rightEyeAmslerGridPlotView.setCoordinates(rightEyeCoordinates);

        Log.d("AmslerTestResult", "Left Eye Coordinates: " + leftEyeCoordinates);
        Log.d("AmslerTestResult", "Right Eye Coordinates: " + rightEyeCoordinates);
    }

    private HashMap<String, ArrayList<Float>> normalizeCoordinates(HashMap<String, ArrayList<Float>> originalCoordinates, int gridSize) {
        HashMap<String, ArrayList<Float>> normalizedCoordinates = new HashMap<>();

        ArrayList<Float> xCoordinates = originalCoordinates.get("x");
        ArrayList<Float> yCoordinates = originalCoordinates.get("y");
        Log.d("AmslerTestResult", "Original X Coordinates: " + xCoordinates);
        Log.d("AmslerTestResult", "Original Y Coordinates: " + yCoordinates);

        ArrayList<Float> normalizedX = new ArrayList<>();
        ArrayList<Float> normalizedY = new ArrayList<>();

        for (int i = 0; i < xCoordinates.size(); i++) {
            float normalizedXValue = xCoordinates.get(i) / (float) gridSize;
            float normalizedYValue = yCoordinates.get(i) / (float) gridSize;
            normalizedX.add(normalizedXValue);
            normalizedY.add(normalizedYValue);
        }
        Log.d("AmslerTestResult", "Normalized X Coordinates: " + normalizedX);
        Log.d("AmslerTestResult", "Normalized Y Coordinates: " + normalizedY);

        normalizedCoordinates.put("x", normalizedX);
        normalizedCoordinates.put("y", normalizedY);
        return normalizedCoordinates;
    }

    @Override
    protected void onLoggedIn(User user) {
    }
}
