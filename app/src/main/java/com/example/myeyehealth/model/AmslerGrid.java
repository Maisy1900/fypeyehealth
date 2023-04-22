package com.example.myeyehealth.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AmslerGrid implements Parcelable {
    private int size;
    private int[][] data;

    public AmslerGrid(int size) {
        this.size = size;
        data = new int[size][size];
    }

    protected AmslerGrid(Parcel in) {
        size = in.readInt();
        data = new int[size][size];
        for (int i = 0; i < size; i++) {
            in.readIntArray(data[i]);
        }
    }

    public static final Creator<AmslerGrid> CREATOR = new Creator<AmslerGrid>() {
        @Override
        public AmslerGrid createFromParcel(Parcel in) {
            return new AmslerGrid(in);
        }

        @Override
        public AmslerGrid[] newArray(int size) {
            return new AmslerGrid[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size);
        for (int[] row : data) {
            dest.writeIntArray(row);
        }
    }

    public int getSize() {
        return size;
    }

    public int[][] getData() {
        return data;
    }

    public void setData(int[][] data) {
        this.data = data;
    }

    public int getValueAt(int row, int col) {
        return data[row][col];
    }

    public void setValueAt(int row, int col, int value) {
        data[row][col] = value;
    }
}
