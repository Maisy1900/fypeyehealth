package com.example.myeyehealth.model;

import android.os.Parcel;
import android.os.Parcelable;
//Represents an amsler grid
//square grid
//that holds the size of the grid in the 2d array
public class AmslerGrid implements Parcelable {
    private int size;//size of the amsler grid
    private int[][] data;//size of the amsler grid
//constructs amsler grid from parcel- more optimised way of passing large variables
    //parcel contains the data to construct the amsler grid
    protected AmslerGrid(Parcel in) {
        size= in.readInt();
        data =new int[size] [size];
        for (int i = 0; i < size; i++) {
            in.readIntArray(data[i]);
        }
    }
//creator onject in caharge of creating instances of the amsler grid
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
//part of interface
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Writes the contents of the AmslerGrid object to a parcel to efficiently pass the amsler grid to the activity ect.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( size);
        for (int[]  row : data) {
            dest.writeIntArray(row);
        }
    }

}
