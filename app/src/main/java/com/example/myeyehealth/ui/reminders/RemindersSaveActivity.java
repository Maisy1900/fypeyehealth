package com.example.myeyehealth.ui.reminders;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.Reminder;

import java.util.Locale;

public class RemindersSaveActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView timeInput;
    private Button saveButton;
    private String reason;
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

        // Get the reason and weekday passed from previous activities
        Intent intent = getIntent();
        reason = intent.getStringExtra("reason");
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
                    // Convert the weekday to an integer (assuming Sunday is 1, Monday is 2, and so on)
                    int dayOfWeek = -1;
                    if (weekday != null) {
                        switch (weekday.toLowerCase()) {
                            case "sunday":
                                dayOfWeek = 1;
                                break;
                            case "monday":
                                dayOfWeek = 2;
                                break;
                            case "tuesday":
                                dayOfWeek = 3;
                                break;
                            case "wednesday":
                                dayOfWeek = 4;
                                break;
                            case "thursday":
                                dayOfWeek = 5;
                                break;
                            case "friday":
                                dayOfWeek = 6;
                                break;
                            case "saturday":
                                dayOfWeek = 7;
                                break;
                        }
                    }

                    // Save the reminder to the local database
                    // Get the user ID from the SessionManager and include it in the Reminder constructor
                    int userId = sessionManager.getUser().getId();
                    Reminder reminder = new Reminder(-1, userId, dayOfWeek, hour, minute, reason); // Set the id to -1 initially
                    int reminderId = reminderMethods.addReminder(reminder, RemindersSaveActivity.this);
                    reminder.setId(reminderId); // Set the ID for the reminder object

                    // Set the alarm for the reminder
                    ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(RemindersSaveActivity.this);
                    reminderAlarmScheduler.setReminderAlarm(reminder);

                    // Navigate to the ReminderListActivity or any other activity
                    Intent nextActivityIntent = new Intent(RemindersSaveActivity.this, RemindersActivity.class);
                    startActivity(nextActivityIntent);
                } else {
                    Toast.makeText(RemindersSaveActivity.this, "Enter a valid time", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}