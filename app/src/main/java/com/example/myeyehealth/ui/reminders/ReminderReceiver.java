package com.example.myeyehealth.ui.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myeyehealth.controller.ReminderAlarmScheduler;
import com.example.myeyehealth.controller.ReminderMethods;
import com.example.myeyehealth.utils.SessionManager;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.utils.NotificationHelper;

import java.util.List;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String TAG = "ReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            SessionManager sessionManager = SessionManager.getInstance(context);
            int userId = sessionManager.getUser().getId();
            ReminderMethods reminderMethods = new ReminderMethods(context);
            List<Reminder> reminders = reminderMethods.getAllReminders(userId);
            ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(context);

            for (Reminder reminder : reminders) {
                reminderAlarmScheduler.setReminderAlarm(reminder);
            }
        } else {



            int reminderId = intent.getIntExtra("reminderId", -1);




            if (reminderId != -1) {
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
}
