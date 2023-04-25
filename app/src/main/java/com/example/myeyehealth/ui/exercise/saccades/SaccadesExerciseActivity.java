package com.example.myeyehealth.ui.exercise.saccades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;
import com.example.myeyehealth.view.SaccadesExerciseView;

import java.util.Arrays;
import java.util.List;

public class SaccadesExerciseActivity extends BaseActivity {

    private SaccadesExerciseView saccadesExerciseView;
    private TextView promptText;
    private Button finishButton;
    private ImageButton backButton;
    private boolean exerciseComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_exercise);

        saccadesExerciseView = findViewById(R.id.saccades_exercise_view);
        finishButton = findViewById(R.id.finish_button);
        backButton = findViewById(R.id.back_button);
        promptText = findViewById(R.id.prompt_text);

        finishButton.setEnabled(false);

        saccadesExerciseView.setOnExerciseCompleteListener(() -> {
            promptText.setText("Exercise complete");
            finishButton.setEnabled(true);
            exerciseComplete = true;
        });

        finishButton.setOnClickListener(v -> {
            if (exerciseComplete) {
                List<Long> tapTimes = saccadesExerciseView.getTapTimes();
                List<Float> tapDistances = saccadesExerciseView.getTapDistances();


                int n = tapTimes.size();
                long[] timeDifferences = new long[n - 1];
                for (int i = 0; i < n - 1; i++) {
                    timeDifferences[i] = tapTimes.get(i + 1) - tapTimes.get(i);
                }



                float[] tapDistancesArray = new float[tapDistances.size()];
                for (int i = 0; i < tapDistances.size(); i++) {
                    tapDistancesArray[i] = tapDistances.get(i);
                }
                Intent intent = new Intent(SaccadesExerciseActivity.this, SaccadesExerciseNewActivity.class);
                intent.putExtra("EXTRA_TAP_TIMES", tapTimes.stream().mapToLong(Long::longValue).toArray());
                intent.putExtra("EXTRA_TIME_DIFFERENCES", timeDifferences);
                intent.putExtra("EXTRA_TAP_DISTANCES", tapDistancesArray);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
