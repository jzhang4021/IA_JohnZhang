package com.example.ia_johnzhang;

public class User {

    String email;
    String record;
    int currentHydration;
    double currentScreenTime;
    double goalSleepTime;
    double currentSleepTime;

    public User(){

        currentHydration = 0;
        currentScreenTime = 0;
        goalSleepTime = 0;
        currentSleepTime = 0;

    }

    public User(String email, String record){
        this.email = email;
        currentHydration = 0;
        currentScreenTime = 0;
        goalSleepTime = 0;
        currentSleepTime = 0;
        this.record = record;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getCurrentHydration() {
        return currentHydration;
    }

    public void setCurrentHydration(int currentHydration) {
        this.currentHydration = currentHydration;
    }

    public double getCurrentScreenTime() {
        return currentScreenTime;
    }

    public void setCurrentScreenTime(double currentScreenTime) {
        this.currentScreenTime = currentScreenTime;
    }

    public double getGoalSleepTime() {
        return goalSleepTime;
    }

    public void setGoalSleepTime(double goalSleepTime) {
        this.goalSleepTime = goalSleepTime;
    }

    public double getCurrentSleepTime() {
        return currentSleepTime;
    }

    public void setCurrentSleepTime(double currentSleepTime) {
        this.currentSleepTime = currentSleepTime;
    }
}
