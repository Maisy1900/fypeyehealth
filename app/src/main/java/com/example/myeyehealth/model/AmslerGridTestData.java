package com.example.myeyehealth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AmslerGridTestData implements Parcelable {
    private String testId;
    private String userId;
    private Date date;
    private AmslerGrid grid;
    private int testNumber;

    public AmslerGridTestData(String testId, String userId, Date date, AmslerGrid grid, int testNumber) {
        this.testId = testId;
        this.userId = userId;
        this.date = date;
        this.grid = grid;
        this.testNumber = testNumber;
    }

    protected AmslerGridTestData(Parcel in) {
        testId = in.readString();
        userId = in.readString();
        date = new Date(in.readLong());
        grid = in.readParcelable(AmslerGrid.class.getClassLoader());
        testNumber = in.readInt();
    }

    public static final Creator<AmslerGridTestData> CREATOR = new Creator<AmslerGridTestData>() {
        @Override
        public AmslerGridTestData createFromParcel(Parcel in) {
            return new AmslerGridTestData(in);
        }

        @Override
        public AmslerGridTestData[] newArray(int size) {
            return new AmslerGridTestData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(testId);
        dest.writeString(userId);
        dest.writeLong(date.getTime());
        dest.writeParcelable(grid, flags);
        dest.writeInt(testNumber);
    }

    // Getters
    public String getTestId() {
        return testId;
    }

    public String getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public int getTestNumber() {
        return testNumber;
    }

    // Setters
    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
