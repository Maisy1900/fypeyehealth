package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myeyehealth.R;

public class RemindersReasonActivity extends AppCompatActivity {
    private Button mAmslerTestButton;
    private Button mSaccadesExerciseButton;
    private Button mNextButton;
    private String mReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_reason);

        mAmslerTestButton = findViewById(R.id.amsler_test_button);
        mSaccadesExerciseButton = findViewById(R.id.saccades_exercise_button);
        mNextButton = findViewById(R.id.next_button);

        mAmslerTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReason = mAmslerTestButton.getText().toString();
                mAmslerTestButton.setBackgroundColor(ContextCompat.getColor(RemindersReasonActivity.this, R.color.colorAccent));
                mSaccadesExerciseButton.setBackgroundColor(ContextCompat.getColor(RemindersReasonActivity.this, R.color.grey));
                mSaccadesExerciseButton.setEnabled(false);
            }
        });

        mSaccadesExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReason = mSaccadesExerciseButton.getText().toString();
                mSaccadesExerciseButton.setBackgroundColor(ContextCompat.getColor(RemindersReasonActivity.this, R.color.colorAccent));
                mAmslerTestButton.setBackgroundColor(ContextCompat.getColor(RemindersReasonActivity.this, R.color.grey));
                mAmslerTestButton.setEnabled(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindersReasonActivity.this, RemindersWeeklyActivity.class);
                intent.putExtra("reason", mReason);
                startActivity(intent);
            }
        });
    }
}
