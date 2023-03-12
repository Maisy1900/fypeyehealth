package com.example.myeyehealth.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String email;
    private String password;
    private String doctorName;
    private String doctorEmail;
    private String carerName;
    private String carerEmail;

    public User(String name, String email, String password, String doctorName, String doctorEmail, String carerName, String carerEmail) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.carerName = carerName;
        this.carerEmail = carerEmail;
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        doctorName = in.readString();
        doctorEmail = in.readString();
        carerName = in.readString();
        carerEmail = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(doctorName);
        dest.writeString(doctorEmail);
        dest.writeString(carerName);
        dest.writeString(carerEmail);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getCarerName() {
        return carerName;
    }

    public String getCarerEmail() {
        return carerEmail;
    }
}
