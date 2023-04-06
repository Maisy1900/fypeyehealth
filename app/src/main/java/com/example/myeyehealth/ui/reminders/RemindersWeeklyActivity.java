package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class RemindersWeeklyActivity extends AppCompatActivity {

    private ImageButton mBackButton;
    private Button mContinueButton;
    private Button[] weekdayButtons;
    private String mReason;
    private String selectedWeekday = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_weekly);

        mBackButton = findViewById(R.id.back_button);
        mContinueButton = findViewById(R.id.continue_button);
        weekdayButtons = new Button[]{
                findViewById(R.id.monday_button),
                findViewById(R.id.tuesday_button),
                findViewById(R.id.wednesday_button),
                findViewById(R.id.thursday_button),
                findViewById(R.id.friday_button),
                findViewById(R.id.saturday_button),
                findViewById(R.id.sunday_button)
        };

        // Get the reason passed from ReminderReasonActivity
        Intent intent = getIntent();
        mReason = intent.getStringExtra("reason");

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWeekday != null) {
                    Intent nextActivityIntent = new Intent(RemindersWeeklyActivity.this, RemindersSaveActivity.class);
                    nextActivityIntent.putExtra("reason", mReason);
                    nextActivityIntent.putExtra("weekday", selectedWeekday);
                    startActivity(nextActivityIntent);
                } else {
                    Toast.makeText(RemindersWeeklyActivity.this, "Select a day of the week", Toast.LENGTH_SHORT).show();
                }
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
