package com.example.myeyehealth.ui.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myeyehealth.R;

public class ReminderReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the reminder information from the intent
        int reminderId = intent.getIntExtra("reminderId", -1);
        String reminderReason = intent.getStringExtra("reminderReason");

        // Show a notification with the reminder information
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Reminder")
                .setContentText(reminderReason)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(reminderId, builder.build());

        // Play a sound to alert the user
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notificationUri);
        r.play();

        // Show a toast to the user
        Toast.makeText(context, "Reminder: " + reminderReason, Toast.LENGTH_SHORT).show();
    }
}
