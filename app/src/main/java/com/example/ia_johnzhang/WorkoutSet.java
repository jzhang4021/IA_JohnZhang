package com.example.ia_johnzhang;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WorkoutSet implements Serializable {
    String title;
    String imageResource;
    ArrayList<Exercise> exercises;

    public WorkoutSet(String title, String imageResource) {
        this.title = title;
        this.imageResource = imageResource;
        exercises = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public void removeItem(int index){
        exercises.remove(index);
    }

    public String getAverageRPE(){
        int giveBack = 0;

        if(getExercises().size() == 0){
            return "N/A";
        }

        for(int i = 0; i < getExercises().size(); i++){
            giveBack += Integer.parseInt(getExercises().get(i).getRPE());
        }

        giveBack = giveBack / getExercises().size();

        return String.valueOf(giveBack);
    }

    public void addExercise(Exercise a){
        exercises.add(a);
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }
}
