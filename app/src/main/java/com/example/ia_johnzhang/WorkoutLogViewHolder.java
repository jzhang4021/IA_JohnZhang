package com.example.ia_johnzhang;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutLogViewHolder extends RecyclerView.ViewHolder {

    protected TextView workoutTitle;
    protected TextView workoutDate;
    protected TextView workoutDuration;
    protected TextView RPE;

    public WorkoutLogViewHolder(@NonNull View itemView) {
        super(itemView);

        workoutTitle = itemView.findViewById(R.id.workoutTitle);
        workoutDate = itemView.findViewById(R.id.workoutDate);
        workoutDuration = itemView.findViewById(R.id.workoutTime);
        RPE = itemView.findViewById(R.id.RPE);
    }
}
