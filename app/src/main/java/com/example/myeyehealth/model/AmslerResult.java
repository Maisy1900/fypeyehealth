package com.example.myeyehealth.model;

public class AmslerResult {
        private int id;
        private int userId;
        private int testId;
        private String testDate;
        private String grid;
        private int xCoord;
        private int yCoord;

        public AmslerResult(int id, int userId, int testId, String testDate, String grid, int xCoord, int yCoord) {
            this.id = id;
            this.userId = userId;
            this.testId = testId;
            this.testDate = testDate;
            this.grid = grid;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }

        // Getters and setters for the fields

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

        public int getTestId() {
            return testId;
        }

        public void setTestId(int testId) {
            this.testId = testId;
        }

        public String getTestDate() {
            return testDate;
        }

        public void setTestDate(String testDate) {
            this.testDate = testDate;
        }

        public String getGrid() {
            return grid;
        }

        public void setGrid(String grid) {
            this.grid = grid;
        }

        public int getXCoord() {
            return xCoord;
        }

        public void setXCoord(int xCoord) {
            this.xCoord = xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }

        public void setYCoord(int yCoord) {
            this.yCoord = yCoord;
        }
    }
