package com.example.ia_johnzhang;

public class Time {
    int hour;
    int minute;

    public Time(){
        hour = 0;
        minute = 0;
    }

    public Time(int hour,int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void addHour(int add){
        hour = hour + add;
    }

    public void addMinute(int add){
        minute = minute + add;
    }

    //change total time into minutes, for progress bar usage
    public int getNumConversion(){
        int changedHour = hour * 60;
        int giveBack = changedHour + minute;
        return giveBack;
    }
}
