package com.example.myeyehealth.ui.amslergrid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.view.InteractiveAmslerGridView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmslerGridRightEyeTestActivity extends AppCompatActivity {

    private HashMap<String, ArrayList<Float>> distortionCoordinates = new HashMap<>();
    private int numDistortions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_grid_right_eye_test);

        InteractiveAmslerGridView amslerGridView = findViewById(R.id.amsler_grid_view);

        // get the number of distortions from the InteractiveAmslerGridView
        numDistortions = amslerGridView.getNumDistortions();

        amslerGridView.setOnCompleteListener(new InteractiveAmslerGridView.OnCompleteListener() {
            @Override
            public void onComplete() {
                // save distortion coordinates and go to next activity
                for (int i = 0; i < numDistortions; i++) {
                    distortionCoordinates.put("rightEyeDistortion" + (i + 1), amslerGridView.getDistortionCoordinates(i));
                }
                Intent intent = new Intent(AmslerGridRightEyeTestActivity.this, AmslerGridTestResultsActivity.class);
                intent.putExtra("leftEyeDistortionCoordinates", getIntent().getSerializableExtra("leftEyeDistortionCoordinates"));
                intent.putExtra("rightEyeDistortionCoordinates", distortionCoordinates);
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
                Intent intent = new Intent(AmslerGridRightEyeTestActivity.this, AmslerGridTestResultsActivity.class);
                intent.putExtra("leftEyeDistortionCoordinates", getIntent().getSerializableExtra("leftEyeDistortionCoordinates"));
                intent.putExtra("rightEyeDistortionCoordinates", distortionCoordinates);
                startActivity(intent);
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
        }
    }
}
