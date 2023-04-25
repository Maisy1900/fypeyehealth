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
    private int rightEyeGridSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_right_eye_test);

        final InteractiveAmslerGridView amslerGridView = findViewById(R.id.amsler_grid_view);

        leftEyeGridSize = getIntent().getIntExtra("leftEyeGridSize", 0);


        rightEyeGridSize = amslerGridView.getGridSize();


        numDistortions = amslerGridView.getNumDistortions();

        amslerGridView.setOnCompleteListener(new InteractiveAmslerGridView.OnCompleteListener() {
            @Override
            public void onComplete(ArrayList<ArrayList<Float>> distortionCoordinates) {

                for (int i = 0; i < distortionCoordinates.size(); i++) {
                    ArrayList<Float> coordinates = distortionCoordinates.get(i);

                    AmslerGridRightEyeTestActivity.this.distortionCoordinates.put("distortion" + (i + 1), coordinates);
                }

                Intent intent = new Intent(AmslerGridRightEyeTestActivity.this, AmslerGridNewResults.class);
                intent.putExtra("leftEyeDistortionCoordinates", getIntent().getSerializableExtra("leftEyeDistortionCoordinates"));
                intent.putExtra("rightEyeDistortionCoordinates", AmslerGridRightEyeTestActivity.this.distortionCoordinates);
                intent.putExtra("leftEyeGridSize", leftEyeGridSize);
                intent.putExtra("rightEyeGridSize", rightEyeGridSize);
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
            intent.removeExtra("gridSize");
        }
    }

}
