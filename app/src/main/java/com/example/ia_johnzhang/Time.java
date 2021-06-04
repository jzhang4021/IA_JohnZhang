package com.example.ia_johnzhang;

public class Time {
    int hour;
    int minute;
    //conversion into a double for the progress bar
    double numConversion;

    public Time(){
        hour = 0;
        minute = 0;
        numConversion = 0.0;
    }

    public Time(int hour,int minute){
        this.hour = hour;
        this.minute = minute;
        numConversion = 0.0;
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

    public int getNumConversion(){
        int changedHour = hour * 60;
        int giveBack = changedHour + minute;
        return giveBack;
    }
}
