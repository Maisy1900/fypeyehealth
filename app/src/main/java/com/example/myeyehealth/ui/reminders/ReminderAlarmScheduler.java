package com.example.myeyehealth.ui.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.myeyehealth.model.Reminder;

public class ReminderAlarmScheduler {

    private Context context;
    private AlarmManager alarmManager;

    public ReminderAlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminderAlarm(Reminder reminder) {
        Intent intent = new Intent(context, ReminderReciever.class);
        intent.putExtra("reminderId", reminder.getId());
        intent.putExtra("reminderReason", reminder.getReason());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long reminderTimeMillis = getReminderTimeInMillis(reminder);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTimeMillis, pendingIntent);
    }

    public void cancelReminderAlarm(Reminder reminder) {
        Intent intent = new Intent(context, ReminderReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    private long getReminderTimeInMillis(Reminder reminder) {
        long currentTimeMillis = System.currentTimeMillis();
        int currentDayOfWeek = getDayOfWeek(currentTimeMillis);

        long reminderTimeMillis = currentTimeMillis + getTimeDifferenceMillis(currentDayOfWeek, reminder.getDayOfWeek(), reminder.getHour(), reminder.getMinute());
        if (reminderTimeMillis <= currentTimeMillis) {
            reminderTimeMillis += AlarmManager.INTERVAL_DAY * 7;
        }
        return reminderTimeMillis;
    }

    private int getDayOfWeek(long timeMillis) {
        return ((int) ((timeMillis / (1000 * 60 * 60 * 24)) + 4) % 7) + 1;
    }

    private long getTimeDifferenceMillis(int currentDayOfWeek, int reminderDayOfWeek, int reminderHour, int reminderMinute) {
        long timeDifferenceMillis = 0;

        int daysUntilReminder = reminderDayOfWeek - currentDayOfWeek;
        if (daysUntilReminder < 0) {
            daysUntilReminder += 7;
        }

        timeDifferenceMillis += daysUntilReminder * AlarmManager.INTERVAL_DAY;

        long currentTimeMillis = System.currentTimeMillis();
        long reminderTimeMillis = currentTimeMillis + timeDifferenceMillis;

        int reminderTimeHour = (int) (reminderTimeMillis / (1000 * 60 * 60)) % 24;
        int reminderTimeMinute = (int) (reminderTimeMillis / (1000 * 60)) % 60;

        long hourDifferenceMillis = (reminderHour - reminderTimeHour) * (1000 * 60 * 60);
        long minuteDifferenceMillis = (reminderMinute - reminderTimeMinute) * (1000 * 60);

        timeDifferenceMillis += hourDifferenceMillis + minuteDifferenceMillis;

        return timeDifferenceMillis;
    }
}
