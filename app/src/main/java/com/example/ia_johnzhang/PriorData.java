package com.example.ia_johnzhang;

public class PriorData {
    int hydration;
    Time screenTime;
    Time sleepDuration;

    public PriorData(int hydration, Time screenTime, Time sleepDuration) {
        this.hydration = hydration;
        this.screenTime = screenTime;
        this.sleepDuration = sleepDuration;
    }

    public PriorData(){
        hydration = 0;
        screenTime = new Time();
        sleepDuration = new Time();
    }

    public int getHydration() {
        return hydration;
    }

    public void setHydration(int hydration) {
        this.hydration = hydration;
    }

    public Time getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(Time screenTime) {
        this.screenTime = screenTime;
    }

    public Time getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(Time sleepDuration) {
        this.sleepDuration = sleepDuration;
    }
}
