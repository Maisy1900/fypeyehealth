package com.example.myeyehealth.model;

import java.util.Date;

public class AmslerGridTestData {
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

    // Add a getter method for testNumber


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