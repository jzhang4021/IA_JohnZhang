package com.example.ia_johnzhang;

public class FinishedWorkout {
    Time workoutTime;
    String timeStringVers;
    String RPE;
    String date;
    String title;

    public FinishedWorkout(){
        workoutTime = new Time(0,0);
        timeStringVers = "0";
        RPE = "0";
        date = "0";
        title = "asdlkf";
    }

    public FinishedWorkout(Time workoutTime, String RPE, String date, String title) {
        this.workoutTime = workoutTime;
        this.RPE = RPE;
        this.date = date;
        this.title = title;
    }

    public FinishedWorkout(String timeStringVers, String RPE, String date, String title){
        this.timeStringVers = timeStringVers;
        this.RPE = RPE;
        this.date = date;
        this.title = title;
    }

    public String getTimeStringVers() {
        return timeStringVers;
    }

    public void setTimeStringVers(String timeStringVers) {
        this.timeStringVers = timeStringVers;
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
