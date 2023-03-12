package com.example.myeyehealth.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String doctorName;
    private String doctorEmail;
    private String carerName;
    private String carerEmail;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.carerName = carerName;
        this.carerEmail = carerEmail;
    }

    public int getId() {
        return id;
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
