package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myeyehealth.R;
import com.example.myeyehealth.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RemindersWeeklyActivity extends BaseActivity {

    private TextView amslerTestButton;
    private TextView saccadesExerciseButton;
    private Button nextButton;
    private Button[] weekdayButtons;
    private LinearLayout amslerTestLayout;
    private LinearLayout saccadesExerciseLayout;
    private List<String> reasons;
    private String selectedWeekday = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_weekly);

        amslerTestLayout = findViewById(R.id.amsler_test_button);
        saccadesExerciseLayout = findViewById(R.id.saccades_exercise_button);
        nextButton = findViewById(R.id.next_button);
        weekdayButtons = new Button[]{
                findViewById(R.id.monday_button),
                findViewById(R.id.tuesday_button),
                findViewById(R.id.wednesday_button),
                findViewById(R.id.thursday_button),
                findViewById(R.id.friday_button),
                findViewById(R.id.saturday_button),
                findViewById(R.id.sunday_button)
        };

        reasons = new ArrayList<>();

        List<View> exerciseButtons = new ArrayList<>();
        exerciseButtons.add(amslerTestLayout);
        exerciseButtons.add(saccadesExerciseLayout);

        for (View view : exerciseButtons) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout selectedLayout = (LinearLayout) v;
                    TextView selectedTextView = (TextView) selectedLayout.getChildAt(1);
                    String reason = selectedTextView.getText().toString().trim().toLowerCase();

                    if (reasons.contains(reason)) {
                        reasons.remove(reason);
                        v.getBackground().clearColorFilter();
                    } else {
                        reasons.add(reason);
                        v.getBackground().setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            });
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWeekday != null && !reasons.isEmpty()) {
                    Intent nextActivityIntent = new Intent(RemindersWeeklyActivity.this, RemindersSaveActivity.class);
                    nextActivityIntent.putStringArrayListExtra("reasons", new ArrayList<>(reasons));
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
                    Button selectedButton = (Button) v;
                    selectedWeekday = selectedButton.getText().toString().trim().toLowerCase();

                    if (!selectedButton.isClickable()) {
                        selectedButton.getBackground().clearColorFilter();
                        selectedButton.setClickable(true);
                    } else {
                        selectedButton.getBackground().setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
                        selectedButton.setClickable(false);
                    }
                    for (Button otherButton : weekdayButtons) {
                        if (otherButton != v) {
                            otherButton.getBackground().clearColorFilter();
                            otherButton.setClickable(true);
                        }
                    }
                }
            });
        }
    }
}