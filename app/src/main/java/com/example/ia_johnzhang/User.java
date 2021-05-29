package com.example.ia_johnzhang;

public class User {

    String email;
    String record;
    int currentHydration;
    double currentScreenTime;
    double goalSleepTime;
    double currentSleepTime;

    public User(String email, String record){
        this.email = email;
        currentHydration = 0;
        currentScreenTime = 0;
        goalSleepTime = 0;
        currentSleepTime = 0;
        this.record = record;
    }

}
