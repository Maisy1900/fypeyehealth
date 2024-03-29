package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.view.InteractiveAmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridLeftEyeTestActivity extends BaseActivity {

    private HashMap<String, ArrayList<Float>> distortionCoordinates = new HashMap<>();
    private int numDistortions;
    private int leftEyeGridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_left_eye_test);

        final InteractiveAmslerGridView amslerGridView = findViewById(R.id.amsler_grid_view);


        numDistortions = amslerGridView.getNumDistortions();
        leftEyeGridSize = amslerGridView.getGridSize();

        amslerGridView.setOnCompleteListener(new InteractiveAmslerGridView.OnCompleteListener() {
            @Override
            public void onComplete(ArrayList<ArrayList<Float>> distortionCoordinates) {

                for (int i = 0; i < distortionCoordinates.size(); i++) {
                    ArrayList<Float> coordinates = distortionCoordinates.get(i);
                    System.out.println("Left Eye Distortion " + (i + 1) + ": " + coordinates);
                    AmslerGridLeftEyeTestActivity.this.distortionCoordinates.put("distortion" + (i + 1), coordinates);
                }

                Intent intent = new Intent(AmslerGridLeftEyeTestActivity.this, AmslerGridRightEyeTestActivity.class);
                intent.putExtra("leftEyeDistortionCoordinates", AmslerGridLeftEyeTestActivity.this.distortionCoordinates);
                intent.putExtra("leftEyeGridSize", leftEyeGridSize);
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
            intent.removeExtra("distortionCoordinates");
        }
    }
}
