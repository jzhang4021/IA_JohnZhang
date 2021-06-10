package com.example.ia_johnzhang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutLogAdapter extends RecyclerView.Adapter<WorkoutLogViewHolder> {

    ArrayList<FinishedWorkout> mData;

    public WorkoutLogAdapter(ArrayList<FinishedWorkout> mData){
        this.mData = mData;
    }

    @NonNull
    @Override
    public WorkoutLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_row_view, parent, false);

        WorkoutLogViewHolder holder = new WorkoutLogViewHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutLogViewHolder holder, int position) {
        holder.workoutTitle.setText(mData.get(position).getTitle());
        holder.workoutDuration.setText(mData.get(position).getWorkoutTime().toString());
        holder.workoutDate.setText(mData.get(position).getDate());
        holder.RPE.setText(mData.get(position).getRPE());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
