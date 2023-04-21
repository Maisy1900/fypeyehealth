package com.example.myeyehealth.model;

import java.util.Date;

public class AmslerGridTestData {
    private String testId;
    private String userId;
    private Date date;
    private AmslerGrid amslerGrid;

    public AmslerGridTestData(String testId, String userId, Date date, AmslerGrid amslerGrid) {
        this.testId = testId;
        this.userId = userId;
        this.date = date;
        this.amslerGrid = amslerGrid;
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

    public AmslerGrid getAmslerGrid() {
        return amslerGrid;
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

    public void setAmslerGrid(AmslerGrid amslerGrid) {
        this.amslerGrid = amslerGrid;
    }

    // Add other methods as needed
}
