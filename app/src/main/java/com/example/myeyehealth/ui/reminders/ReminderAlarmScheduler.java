package com.example.myeyehealth.ui.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.myeyehealth.model.Reminder;

import java.util.Calendar;

public class ReminderAlarmScheduler {

    private Context context;
    private AlarmManager alarmManager;
    private static final String TAG = "ReminderAlarmScheduler";

    public ReminderAlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminderAlarm(Reminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("reminderId", reminder.getId());
        intent.putExtra("reminderReason", reminder.getReason());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long reminderTimeMillis = getReminderTimeInMillis(reminder);

        // Set repeating alarm
        long interval = AlarmManager.INTERVAL_DAY * 7;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTimeMillis, interval, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderTimeMillis, interval, pendingIntent);
        }

        // Debug log
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminderTimeMillis);
        Log.d(TAG, "Alarm set: " + reminder.toString() + ", time: " + calendar.getTime().toString());
    }



    public void cancelReminderAlarm(Reminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

        Log.d(TAG, "Alarm canceled for reminder: " + reminder.toString());
    }
//issues
    private long getReminderTimeInMillis(Reminder reminder) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calendar.set(Calendar.MINUTE, reminder.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysUntilReminder = (reminder.getDayOfWeek() + 1 - currentDayOfWeek + 7) % 7;

        calendar.add(Calendar.DAY_OF_YEAR, daysUntilReminder);

        // If the reminder time is in the past, schedule it for the next week
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 7);
        }

        Log.d(TAG, "Reminder time calculated: " + calendar.getTime().toString());

        return calendar.getTimeInMillis();
    }


}
