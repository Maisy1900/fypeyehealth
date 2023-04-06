package com.example.myeyehealth.ui.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myeyehealth.data.Database;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.ui.reminders.notification.NotificationHelper;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int reminderId = intent.getIntExtra("reminderId", -1);

        if (reminderId != -1) {
            Database database = Database.getInstance(context);
            ReminderMethods reminderMethods = new ReminderMethods(context);
            Reminder reminder = reminderMethods.getReminderById(reminderId);

            if (reminder != null) {
                String reminderReason = reminder.getReason();
                String reminderTime = String.format("%02d:%02d", reminder.getHour(), reminder.getMinute());

                NotificationHelper.showNotification(context, reminderReason, reminderTime);
                ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(context);
                reminderAlarmScheduler.setReminderAlarm(reminder);
            }
        }
    }
}
