package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;

public class RemindersWeeklyActivity extends AppCompatActivity {

    private ImageButton mBackButton;
    private EditText mWeekdayInput;
    private Button mContinueButton;
    private String mReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders_weekly);

        mBackButton = findViewById(R.id.back_button);
        mWeekdayInput = findViewById(R.id.email_input);
        mContinueButton = findViewById(R.id.continue_button);

        // Get the reason passed from ReminderReasonActivity
        Intent intent = getIntent();
        mReason = intent.getStringExtra("reason");

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekdayInput.setText("");
                finish();
            }
        });

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weekday = mWeekdayInput.getText().toString().trim().toLowerCase();

                if (isValidWeekday(weekday)) {
                    Intent nextActivityIntent = new Intent(RemindersWeeklyActivity.this, RemindersSaveActivity.class);
                    nextActivityIntent.putExtra("reason", mReason);
                    nextActivityIntent.putExtra("weekday", weekday);
                    startActivity(nextActivityIntent);
                } else {
                    Toast.makeText(RemindersWeeklyActivity.this, "Enter a valid day of the week", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidWeekday(String weekday) {
        String[] validWeekdays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        for (String validWeekday : validWeekdays) {
            if (validWeekday.equals(weekday)) {
                return true;
            }
        }
        return false;
    }
}
