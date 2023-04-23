package com.example.myeyehealth.ui.reminders;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.BaseActivity;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.Reminder;

import java.util.ArrayList;
import java.util.Locale;

public class RemindersSaveActivity extends BaseActivity {

    private ImageButton backButton;
    private TextView timeInput;
    private Button saveButton;
    private ArrayList<String> reasons;
    private String weekday;
    private int hour = -1;
    private int minute = -1;
    private SessionManager sessionManager;
    private ReminderMethods reminderMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(this);
        reminderMethods = new ReminderMethods(this);

        setContentView(R.layout.activity_reminders_save);

        backButton = findViewById(R.id.back_button);
        timeInput = findViewById(R.id.time_input);
        saveButton = findViewById(R.id.save_button);

        // Get the reasons and weekday passed from previous activities
        Intent intent = getIntent();
        reasons = intent.getStringArrayListExtra("reasons");
        weekday = intent.getStringExtra("weekday");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeInput.setText("");
                finish();
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int initialHour = 0;
                int initialMinute = 0;

                if (hour != -1) {
                    initialHour = hour;
                }

                if (minute != -1) {
                    initialMinute = minute;
                }

                TimePickerDialog timePickerDialog = new TimePickerDialog(RemindersSaveActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        hour = hourOfDay;
                        minute = minuteOfDay;
                        timeInput.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                }, initialHour, initialMinute, true);
                timePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hour != -1 && minute != -1) {
                    int dayOfWeek = -1;
                    if (weekday != null) {
                        switch (weekday) {
                            case "sunday":
                                dayOfWeek = 7;
                                break;
                            case "monday":
                                dayOfWeek = 1;
                                break;
                            case "tuesday":
                                dayOfWeek = 2;
                                break;
                            case "wednesday":
                                dayOfWeek = 3;
                                break;
                            case "thursday":
                                dayOfWeek = 4;
                                break;
                            case "friday":
                                dayOfWeek = 5;
                                break;
                            case "saturday":
                                dayOfWeek = 6;
                                break;
                        }

                    }

                    // Iterate through reasons and save a reminder for each activity
                    for (String reason : reasons) {
                        int userId = sessionManager.getUser().getId();
                        Reminder reminder = new Reminder(-1, userId, dayOfWeek, hour, minute, reason, false);
                        int reminderId = reminderMethods.addReminder(reminder);
                        reminder.setId(reminderId);

                        // Debug log
                        Log.d("ReminderSave", "Reminder saved: " + reminder.toString());

                        ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(RemindersSaveActivity.this);
                        reminderAlarmScheduler.setReminderAlarm(reminder);
                    }

                    Intent nextActivityIntent = new Intent(RemindersSaveActivity.this, RemindersActivity.class);
                    startActivity(nextActivityIntent);
                } else {
                    Toast.makeText(RemindersSaveActivity.this, "Enter a valid time", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
