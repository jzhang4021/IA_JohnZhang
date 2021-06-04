package com.example.ia_johnzhang;

import java.util.ArrayList;

public class User {

    String email;
    String record;
    ArrayList<PriorData> priorRecords;
    PriorData currRecord;

    public User(){

        currRecord = new PriorData();

    }

    public User(String email, String record){
        this.email = email;
        currRecord = new PriorData();
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
}
