package com.example.myeyehealth.ui.reminders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeyehealth.R;
import com.example.myeyehealth.model.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private List<Reminder> reminders;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        private TextView reminderDayOfWeek;
        private TextView reminderTime;
        private TextView reminderReason;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
;
        }

        public void bind(Reminder reminder) {
            reminderDayOfWeek.setText(reminder.getDayOfWeekString());
            reminderTime.setText(reminder.getTimeString());
            reminderReason.setText(reminder.getReason());
        }
    }
}
