package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

import java.util.ArrayList;
import java.util.List;

public class RemindersWeeklyActivity extends AppCompatActivity {

    private TextView mAmslerTestButton;
    private TextView mSaccadesExerciseButton;
    private Button mNextButton;
    private Button[] weekdayButtons;
    private LinearLayout mAmslerTestLayout;
    private LinearLayout mSaccadesExerciseLayout;
    private List<String> mReasons;
    private String selectedWeekday = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_weekly);

        mAmslerTestLayout = findViewById(R.id.amsler_test_button);
        mSaccadesExerciseLayout = findViewById(R.id.saccades_exercise_button);
        mAmslerTestButton = mAmslerTestLayout.findViewById(R.id.amsler_test_text);
        mSaccadesExerciseButton = mSaccadesExerciseLayout.findViewById(R.id.saccades_exercise_text);
        mNextButton = findViewById(R.id.next_button);
        weekdayButtons = new Button[]{
                findViewById(R.id.monday_button),
                findViewById(R.id.tuesday_button),
                findViewById(R.id.wednesday_button),
                findViewById(R.id.thursday_button),
                findViewById(R.id.friday_button),
                findViewById(R.id.saturday_button),
                findViewById(R.id.sunday_button)
        };

        mReasons = new ArrayList<>();

        List<View> exerciseButtons = new ArrayList<>();
        exerciseButtons.add(mAmslerTestLayout);
        exerciseButtons.add(mSaccadesExerciseLayout);

        for (View view : exerciseButtons) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout selectedLayout = (LinearLayout) v;
                    TextView selectedTextView = (TextView) selectedLayout.getChildAt(1);
                    String reason = selectedTextView.getText().toString().trim().toLowerCase();

                    if (mReasons.contains(reason)) {
                        mReasons.remove(reason);
                    } else {
                        mReasons.add(reason);
                    }

                    for (View otherView : exerciseButtons) {
                        LinearLayout otherLayout = (LinearLayout) otherView;
                        TextView otherTextView = (TextView) otherLayout.getChildAt(1);

                        if (otherView == v) {
                            otherTextView.setTextColor(Color.WHITE);
                            otherView.setClickable(false);
                        } else {
                            otherTextView.setTextColor(Color.GRAY);
                            otherView.setClickable(true);
                        }
                    }
                }
            });
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWeekday != null && !mReasons.isEmpty()) {
                    Intent nextActivityIntent = new Intent(RemindersWeeklyActivity.this, RemindersSaveActivity.class);
                    nextActivityIntent.putStringArrayListExtra("reasons", new ArrayList<>(mReasons));
                    nextActivityIntent.putExtra("weekday", selectedWeekday);
                    startActivity(nextActivityIntent);
                } else {
                    Toast.makeText(RemindersWeeklyActivity.this, "Select a day of the week and at least one reason", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        for (Button button : weekdayButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedWeekday = ((Button) v).getText().toString().trim().toLowerCase();
                    for (Button otherButton : weekdayButtons) {
                        if (otherButton == v) {
                            otherButton.setTextColor(Color.WHITE);
                            otherButton.setClickable(false);
                        } else {
                            otherButton.setTextColor(Color.GRAY);
                            otherButton.setClickable(true);
                        }
                    }
                }
            });
        }
    }
}
