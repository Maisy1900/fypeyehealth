package com.example.myeyehealth.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.AmslerGridMethods;
import com.example.myeyehealth.data.SessionActivity;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.AmslerGridTestData;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.view.AmslerGridPlotView;
import com.example.myeyehealth.view.AmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridTestResultActivity extends SessionActivity {
    public static final String TEST_ID_KEY = "test_id_key";

    private HashMap<String, ArrayList<Float>> leftEyeCoordinates;
    private HashMap<String, ArrayList<Float>> rightEyeCoordinates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_test_result);

        String testId = getIntent().getStringExtra(TEST_ID_KEY);
        Log.d("AmslerTestResult", "Test ID: " + testId);
        SessionManager sessionManager = SessionManager.getInstance(this);
        User user = sessionManager.getUser();
        int currentUserId = user.getId();

        AmslerGridMethods amslerGridMethods = new AmslerGridMethods(this);
        HashMap<String, ArrayList<Float>>[] coordinates = amslerGridMethods.getAmslerGridCoordinatesForTest(currentUserId, Integer.parseInt(testId));

        leftEyeCoordinates = coordinates[0];
        rightEyeCoordinates = coordinates[1];

        Log.d("AmslerTestResult", "Left Eye Coordinates: " + leftEyeCoordinates);
        Log.d("AmslerTestResult", "Right Eye Coordinates: " + rightEyeCoordinates);

        addCoordinatesToAmslerGridView();
    }

    private void addCoordinatesToAmslerGridView() {
        AmslerGridPlotView leftEyeGrid = findViewById(R.id.left_eye_grid);
        AmslerGridPlotView rightEyeGrid = findViewById(R.id.right_eye_grid);

        leftEyeGrid.setCoordinates(leftEyeCoordinates);
        rightEyeGrid.setCoordinates(rightEyeCoordinates);
    }


    @Override
    protected void onLoggedIn(User user) {
    }
}
