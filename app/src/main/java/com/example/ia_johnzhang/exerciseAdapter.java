package com.example.ia_johnzhang;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class exerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    Context mContext;
    ArrayList<Exercise> exerciseArrayList;
    Exercise currExercise;



    public exerciseAdapter(ArrayList<Exercise> exerciseArrayList){
        this.exerciseArrayList = exerciseArrayList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_edit_row, parent,false);
        ExerciseViewHolder holder = new ExerciseViewHolder(myView);
        mContext = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {

        //get each individual exercise and cast its information onto display
        currExercise = exerciseArrayList.get(position);
        holder.exerciseTitle.setText(exerciseArrayList.get(position).getName());
        holder.RPETextView.setText("RPE: " + exerciseArrayList.get(position).getRPE());

        //show video if button is pressed
        holder.showVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof CreateWorkoutActivity){
                    ((CreateWorkoutActivity) mContext).createNewVideoDialog(currExercise);
                }
            }
        });

        //exercise deletes if teacher presses button
        holder.deleteExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof CreateWorkoutActivity) {
                    ((CreateWorkoutActivity) mContext).currWorkout.removeItem(position);
                    ((CreateWorkoutActivity) mContext).regenRecyclerView();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return exerciseArrayList.size();
    }

}
