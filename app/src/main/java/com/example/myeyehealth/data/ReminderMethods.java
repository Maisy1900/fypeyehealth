package com.example.myeyehealth.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.model.User;
import com.example.myeyehealth.ui.reminders.ReminderAlarmScheduler;

import java.util.ArrayList;
import java.util.List;

public class ReminderMethods {
    private final Context context;

    public ReminderMethods(Context context) {
        this.context = context;
    }

    public int addReminder(Reminder reminder) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        User user = sessionManager.getUser();

        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(Database.COLUMN_REMINDER_USER_ID, user.getId());
        values.put(Database.COLUMN_REMINDER_DAY_OF_WEEK, reminder.getDayOfWeek());
        values.put(Database.COLUMN_REMINDER_HOUR, reminder.getHour());
        values.put(Database.COLUMN_REMINDER_MINUTE, reminder.getMinute());
        values.put(Database.COLUMN_REMINDER_REASON, reminder.getReason());
        values.put(Database.COLUMN_REMINDER_REASON, reminder.getReason());
        values.put(Database.COLUMN_REMINDER_COMPLETED, reminder.getCompleted() ? 1 : 0);


        long id = db.insert(Database.TABLE_REMINDER, null, values);
        db.close();

        // Schedule the reminder using the AlarmManager
        ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(context);
        reminder.setId((int) id); // Make sure to set the ID for the reminder object
        reminderAlarmScheduler.setReminderAlarm(reminder);

        return (int) id;
    }

    public List<Reminder> getAllReminders(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                Database.COLUMN_REMINDER_ID,
                Database.COLUMN_REMINDER_DAY_OF_WEEK,
                Database.COLUMN_REMINDER_HOUR,
                Database.COLUMN_REMINDER_MINUTE,
                Database.COLUMN_REMINDER_REASON,
                Database.COLUMN_REMINDER_COMPLETED // Add this line
        };


        String selection = Database.COLUMN_REMINDER_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                Database.TABLE_REMINDER,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_ID));
                int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_DAY_OF_WEEK));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_MINUTE));
                String reason = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_REASON));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_COMPLETED)) == 1;

                Reminder reminder = new Reminder(id, userId, dayOfWeek, hour, minute, reason, completed);
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reminders;
    }

    public void clearRemindersTable() {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        db.delete(Database.TABLE_REMINDER, null, null);
        db.close();
    }

    public Reminder getReminderById(int reminderId) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();

        Cursor cursor = db.query(Database.TABLE_REMINDER, null, Database.COLUMN_REMINDER_ID + " = ?", new String[]{String.valueOf(reminderId)}, null, null, null);

        Reminder reminder = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_USER_ID));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_DAY_OF_WEEK));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_MINUTE));
            String reason = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_REASON));
            boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_REMINDER_COMPLETED)) == 1;

            reminder = new Reminder(id, userId, dayOfWeek, hour, minute, reason, completed);
        }

        cursor.close();
        db.close();
        return reminder;
    }

    public void deleteReminder(int reminderId) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getReadableDatabase();
        db.delete(Database.TABLE_REMINDER, Database.COLUMN_REMINDER_ID + " = ?", new String[]{String.valueOf(reminderId)});
        db.close();
    }


}