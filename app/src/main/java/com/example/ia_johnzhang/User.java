package com.example.ia_johnzhang;

import java.util.ArrayList;

public class User {

    String email;
    String record;
    ArrayList<PriorData> priorRecords;
    PriorData currRecord;
    ArrayList<FinishedWorkout> finishedWorkouts;
    ArrayList<WorkoutSet> ownedSets;

    public User(){

        currRecord = new PriorData();
        ownedSets = new ArrayList<>();
        finishedWorkouts = new ArrayList<>();

    }

    public User(String email, String record){
        this.email = email;
        currRecord = new PriorData();
        this.record = record;
        finishedWorkouts = new ArrayList<>();
        ownedSets = new ArrayList<>();
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


    public ArrayList<PriorData> getPriorRecords() {
        return priorRecords;
    }

    public void setPriorRecords(ArrayList<PriorData> priorRecords) {
        this.priorRecords = priorRecords;
    }

    public PriorData getCurrRecord() {
        return currRecord;
    }

    public void setCurrRecord(PriorData currRecord) {
        this.currRecord = currRecord;
    }

    public ArrayList<FinishedWorkout> getFinishedWorkouts() {
        return finishedWorkouts;
    }

    public void setFinishedWorkouts(ArrayList<FinishedWorkout> finishedWorkouts) {
        this.finishedWorkouts = finishedWorkouts;
    }

    public ArrayList<WorkoutSet> getOwnedSets() {
        return ownedSets;
    }

    public void setOwnedSets(ArrayList<WorkoutSet> ownedSets) {
        this.ownedSets = ownedSets;
    }
}
