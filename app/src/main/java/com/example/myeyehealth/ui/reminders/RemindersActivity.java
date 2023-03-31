package com.example.myeyehealth.ui.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.data.SessionManager;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.MainMenuActivity;
import com.example.myeyehealth.view.ReminderItemView;

import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private Database database;
    private ScrollView weekdaysScrollView;

    private LinearLayout weekdays_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        // Add the following lines
        weekdaysScrollView = findViewById(R.id.weekdays_scroll_view);
        weekdays_container = findViewById(R.id.weekdays_container);
        ImageButton scrollUpButton = findViewById(R.id.scroll_up_button);
        ImageButton scrollDownButton = findViewById(R.id.scroll_down_button);

        // Set padding


        // Scroll to the desired position
        weekdaysScrollView.post(new Runnable() {
            @Override
            public void run() {
                weekdaysScrollView.scrollTo(0, 500); // Adjust the initial scroll position
            }
        });

        scrollUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWeekdays(-50); // Change -50 to the desired scroll amount
            }
        });

        scrollDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollWeekdays(50); // Change 50 to the desired scroll amount
            }
        });

        populateReminders();
        Button newReminderButton = findViewById(R.id.new_reminder_button);
        newReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindersActivity.this, RemindersReasonActivity.class);
                startActivity(intent);
            }
        });
        weekdays_container.setPadding(0, 500, 0, 1500);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace MainMenuActivity.class with your actual main menu activity class
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

        // Get the reminders for the current user from the database
        ReminderMethods reminderMethods = new ReminderMethods(getApplicationContext());
        List<Reminder> reminders = reminderMethods.getAllReminders(user.getId());

        // Iterate through the reminders and create ReminderItemView instances for each
        for (Reminder reminder : reminders) {
            ReminderItemView reminderItemView = new ReminderItemView(this);
            reminderItemView.setReminder(reminder);

            // Determine the appropriate day list to add the reminder to
            LinearLayout dayList = getDayList(reminder.getDayOfWeek());

            // Add the ReminderItemView to the appropriate LinearLayout
            dayList.addView(reminderItemView);
        }
    }



    private LinearLayout getDayList(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return findViewById(R.id.sunday_list);
            case 1:
                return findViewById(R.id.monday_list);
            case 2:
                return findViewById(R.id.tuesday_list);
            case 3:
                return findViewById(R.id.wednesday_list);
            case 4:
                return findViewById(R.id.thursday_list);
            case 5:
                return findViewById(R.id.friday_list);
            case 6:
                return findViewById(R.id.saturday_list);
            default:
                return null;
        }
    }

}
