package com.example.myeyehealth.ui.reminders.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.myeyehealth.R;
import com.example.myeyehealth.ui.MainMenuActivity;

public class NotificationHelper {

    private static final String CHANNEL_ID = "eye_health_reminder_channel";
    private static final String CHANNEL_NAME = "Eye Health Reminder";
    private static final String CHANNEL_DESCRIPTION = "Notifications for eye health reminders";
    private static final String TAG = "NotificationHelper";

    public static void showNotification(Context context, String reminderReason, String reminderTime) {
        Log.d(TAG, "showNotification called with reason: " + reminderReason + " and time: " + reminderTime);

        createNotificationChannel(context);

        String contentText = reminderReason + " at " + reminderTime;
        PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainMenuActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(openAppPendingIntent)
                .setContentTitle("Eye Health Reminder")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle()) // Use the custom view style
                .setCustomContentView(getRemoteViews(context, contentText)); // Set the custom content view

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());

        Log.d(TAG, "Notification shown with ID: " + notificationId);
    }

    private static RemoteViews getRemoteViews(Context context, String contentText) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.notification_content, contentText);
        return remoteViews;
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
