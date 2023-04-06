package com.example.myeyehealth.model;

public class Reminder {
    private int id;
    private int userId;
    private int dayOfWeek;
    private int hour;
    private int minute;
    private String reason;


    private boolean completed;

    public Reminder(int id, int userId, int dayOfWeek, int hour, int minute, String reason, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
        this.reason = reason;
        this.completed = completed;
    }
    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getDayOfWeekString() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        return days[dayOfWeek - 1];
    }

    public String getTimeString() {
        String hourFormatted = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minuteFormatted = minute < 10 ? "0" + minute : String.valueOf(minute);
        return hourFormatted + ":" + minuteFormatted;
    }
}
