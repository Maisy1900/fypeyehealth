package com.example.myeyehealth.ui.exercise.saccades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.view.SaccadesExerciseView;

import java.util.List;

public class SaccadesExerciseActivity extends AppCompatActivity {

    private SaccadesExerciseView saccadesExerciseView;
    private TextView promptText;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saccades_exercise);

        saccadesExerciseView = findViewById(R.id.saccades_exercise_view);
        promptText = findViewById(R.id.prompt_text);
        finishButton = findViewById(R.id.finish_button);

        // Set the exercise completion listener to update the UI and launch the results activity
        saccadesExerciseView.setOnExerciseCompleteListener(new SaccadesExerciseView.OnExerciseCompleteListener() {
            @Override
            public void onExerciseComplete() {
                promptText.setText("Exercise complete");
                finishButton.setEnabled(true);

                // Get the tap times and distances from the exercise view
                List<Long> tapTimes = saccadesExerciseView.getTapTimes();
                List<Float> tapDistances = saccadesExerciseView.getTapDistances();

                // Launch the results activity
                Intent intent = new Intent(SaccadesExerciseActivity.this, SaccadesExerciseResultsActivity.class);
                intent.putExtra("EXTRA_TAP_TIMES", tapTimes.toArray(new Long[0]));
                intent.putExtra("EXTRA_TAP_DISTANCES", tapDistances.toArray(new Float[0]));
                startActivity(intent);
            }
        });

        // Set the click listener for the Finish button
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
