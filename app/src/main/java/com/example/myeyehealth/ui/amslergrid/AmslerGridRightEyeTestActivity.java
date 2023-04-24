package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.view.InteractiveAmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridRightEyeTestActivity extends BaseActivity {

    private HashMap<String, ArrayList<Float>> distortionCoordinates = new HashMap<>();
    private int numDistortions;
    private int leftEyeGridSize;
    private int rightEyeGridSize; // Add a new variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_right_eye_test);

        final InteractiveAmslerGridView amslerGridView = findViewById(R.id.amsler_grid_view);

        // Receive the left eye grid size from the previous activity
        leftEyeGridSize = getIntent().getIntExtra("leftEyeGridSize", 0);

        // Get the right eye grid size from the current InteractiveAmslerGridView
        rightEyeGridSize = amslerGridView.getGridSize(); // Replace with the appropriate method to get the grid size

        // get the number of distortions from the InteractiveAmslerGridView
        numDistortions = amslerGridView.getNumDistortions();

        amslerGridView.setOnCompleteListener(new InteractiveAmslerGridView.OnCompleteListener() {
            @Override
            public void onComplete(ArrayList<ArrayList<Float>> distortionCoordinates) {
                System.out.println("Right Eye onComplete called");
                // save distortion coordinates and go to next activity
                for (int i = 0; i < distortionCoordinates.size(); i++) {
                    ArrayList<Float> coordinates = distortionCoordinates.get(i);
                    System.out.println("Right Eye Distortion " + (i + 1) + ": " + coordinates);
                    AmslerGridRightEyeTestActivity.this.distortionCoordinates.put("distortion" + (i + 1), coordinates);
                }
                System.out.println("Right Eye Distortion Coordinates: " + AmslerGridRightEyeTestActivity.this.distortionCoordinates);

                // Add these log statements
                Log.d("AmslerGridNewResults", "Left Eye Distortion Coordinates: " + getIntent().getSerializableExtra("leftEyeDistortionCoordinates").toString());
                Log.d("AmslerGridNewResults", "Right Eye Distortion Coordinates: " + AmslerGridRightEyeTestActivity.this.distortionCoordinates.toString());

                Intent intent = new Intent(AmslerGridRightEyeTestActivity.this, AmslerGridNewResults.class);
                intent.putExtra("leftEyeDistortionCoordinates", getIntent().getSerializableExtra("leftEyeDistortionCoordinates"));
                intent.putExtra("rightEyeDistortionCoordinates", AmslerGridRightEyeTestActivity.this.distortionCoordinates);
                intent.putExtra("leftEyeGridSize", leftEyeGridSize); // Pass the left eye grid size to the next activity
                intent.putExtra("rightEyeGridSize", rightEyeGridSize); // Pass the right eye grid size to the next activity
                startActivity(intent);

            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button finishButton = findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amslerGridView.callOnComplete();
            }
        });

    }
    @Override
    public void onBackPressed() {
        removeExtraDataFromIntent();
        super.onBackPressed();
    }

    private void removeExtraDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            intent.removeExtra("leftEyeDistortionCoordinates");
            intent.removeExtra("rightEyeDistortionCoordinates");
            intent.removeExtra("gridSize"); // Add this line to remove the grid size extra
        }
    }

}
