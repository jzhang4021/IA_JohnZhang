package com.example.ia_johnzhang;

public class FinishedWorkout {
    Time workoutTime;
    String RPE;
    String date;
    String title;

    public FinishedWorkout(Time workoutTime, String RPE, String date, String title) {
        this.workoutTime = workoutTime;
        this.RPE = RPE;
        this.date = date;
        this.title = title;
    }

    public Time getWorkoutTime() {
        return workoutTime;
    }

    public void setWorkoutTime(Time workoutTime) {
        this.workoutTime = workoutTime;
    }

    public String getRPE() {
        return RPE;
    }

    public void setRPE(String RPE) {
        this.RPE = RPE;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
