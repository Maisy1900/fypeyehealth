package com.example.myeyehealth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.data.ReminderMethods;
import com.example.myeyehealth.model.Reminder;
import com.example.myeyehealth.ui.reminders.ReminderAlarmScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderItemView extends LinearLayout {

    private TextView reminderReason;
    private TextView reminderTime;
    private ImageButton deleteButton;
    private Reminder reminder;

    public ReminderItemView(Context context) {
        super(context);
        init(context);
    }

    public ReminderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReminderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.reminder_item_view, this, true);

        reminderReason = findViewById(R.id.tv_reminder_reason);
        reminderTime = findViewById(R.id.tv_reminder_time);
        deleteButton = findViewById(R.id.btn_delete);

        int buttonSize = Math.max(reminderReason.getLineHeight(), reminderTime.getLineHeight());
        ViewGroup.LayoutParams layoutParams = deleteButton.getLayoutParams();
        layoutParams.width = buttonSize;
        layoutParams.height = buttonSize;
        deleteButton.setLayoutParams(layoutParams);

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderMethods reminderMethods = new ReminderMethods(getContext());
                reminderMethods.deleteReminder(reminder.getId());

                ReminderAlarmScheduler reminderAlarmScheduler = new ReminderAlarmScheduler(getContext());
                reminderAlarmScheduler.cancelReminderAlarm(reminder);

                ((ViewGroup) getParent()).removeView(ReminderItemView.this);
            }
        });
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
        reminderTime.setText(getTimeAsString(reminder.getHour(), reminder.getMinute()));
        reminderReason.setText(reminder.getReason());
    }

    private String getWeekdayFromDayOfWeek(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        dayOfWeek = dayOfWeek % 7 + 1;
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String getTimeAsString(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }
}
