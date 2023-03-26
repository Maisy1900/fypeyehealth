package com.example.myeyehealth.ui.reminders;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.view.ReminderItemView;

import java.util.List;

public class RemindersActivity extends AppCompatActivity {

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        database = Database.getInstance(getApplicationContext());

        populateReminders();
    }

    private void populateReminders() {
        // Get the reminders from the database
        List<Reminder> reminders = database.getAllReminders(getApplicationContext());

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
