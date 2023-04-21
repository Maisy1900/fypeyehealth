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

    public User(int id, String name, String email, String password, String doctorName, String doctorEmail, String carerName, String carerEmail) {
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", docName='" + doctorName + '\'' +
                ", docEmail='" + doctorEmail + '\'' +
                ", carerName='" + carerName + '\'' +
                ", carerEmail='" + carerEmail + '\'' +
                '}';
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public void setCarerName(String carerName) {
        this.carerName = carerName;
    }

    public void setCarerEmail(String carerEmail) {
        this.carerEmail = carerEmail;
    }

}