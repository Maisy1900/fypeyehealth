package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.BaseActivity;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.view.ReminderItemView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RemindersActivity extends BaseActivity {

    private ScrollView weekdaysScrollView;
    private LinearLayout weekdays_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        weekdaysScrollView = findViewById(R.id.weekdays_scroll_view);
        weekdays_container = findViewById(R.id.weekdays_container);
        ImageButton scrollUpButton = findViewById(R.id.scroll_up_button);
        ImageButton scrollDownButton = findViewById(R.id.scroll_down_button);

        weekdaysScrollView.post(new Runnable() {
            @Override
            public void run() {
                weekdaysScrollView.scrollTo(0, 500);
            }
        });

        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWeekdays(-50);
            }
        });

        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWeekdays(50);
            }
        });

        populateReminders();
        Button newReminderButton = findViewById(R.id.new_reminder_button);
        newReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindersActivity.this, RemindersWeeklyActivity.class);
                startActivity(intent);
            }
        });
        weekdays_container.setPadding(0, 500, 0, 1500);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(RemindersActivity.this, MainMenuActivity.class);
                mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainMenuIntent);
                finish();
            }
        });
    }

    private void scrollWeekdays(int dy) {
        int maxScrollY = weekdaysScrollView.getChildAt(0).getMeasuredHeight() - weekdaysScrollView.getHeight();
        int targetY = weekdaysScrollView.getScrollY() + dy;
        if (targetY < 0) {
            targetY = 0;
        } else if (targetY > maxScrollY) {
            targetY = maxScrollY;
        }
        weekdaysScrollView.scrollTo(0, targetY);
    }

    private void populateReminders() {
        SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
        User user = sessionManager.getUser();

        ReminderMethods reminderMethods = new ReminderMethods(getApplicationContext());
        List<Reminder> reminders = reminderMethods.getAllReminders(user.getId());

        // Sort the reminders by day of the week and time
        Collections.sort(reminders, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder r1, Reminder r2) {
                int dayComparison = Integer.compare(r1.getDayOfWeek(), r2.getDayOfWeek());
                if (dayComparison == 0) {
                    return (r1.getHour() * 60 + r1.getMinute()) - (r2.getHour() * 60 + r2.getMinute());
                } else {
                    return dayComparison;
                }
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout weekdaysContainer = findViewById(R.id.weekdays_container);
        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < 7; i++) {
            // Create container for reminders on this day
            LinearLayout dayContainer = new LinearLayout(this);
            dayContainer.setOrientation(LinearLayout.VERTICAL);
            weekdaysContainer.addView(dayContainer);

            boolean hasReminderForThisDay = false;

            // Add reminders for this day
            for (Reminder reminder : reminders) {
                if (reminder.getDayOfWeek() == i) {
                    if (!hasReminderForThisDay) {
                        // Create day title
                        TextView dayTitle = new TextView(this);
                        dayTitle.setText(dayNames[i]);
                        dayTitle.setTextSize(18);
                        dayTitle.setTextColor(Color.BLACK);
                        dayContainer.addView(dayTitle);
                        hasReminderForThisDay = true;
                    }

                    ReminderItemView reminderItemView = new ReminderItemView(this);
                    reminderItemView.setReminder(reminder);


                    TextView reminderReason = reminderItemView.findViewById(R.id.tv_reminder_reason);
                    TextView reminderTime = reminderItemView.findViewById(R.id.tv_reminder_time);

                    reminderReason.setText(reminder.getReason());
                    reminderTime.setText(reminder.getTimeString());

                    dayContainer.addView(reminderItemView);
                }
            }
        }
    }



    private String getDayLabel(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "";
        }
    }


}


