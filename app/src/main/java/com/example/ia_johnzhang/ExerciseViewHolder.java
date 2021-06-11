package com.example.ia_johnzhang;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView exerciseTitle;
    TextView RPETextView;
    Button showVidButton;
    Button deleteExerciseButton;


    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);

        exerciseTitle = itemView.findViewById(R.id.exerciseTitle);
        RPETextView = itemView.findViewById(R.id.RPETextView);
        showVidButton = itemView.findViewById(R.id.openVidButton);
        deleteExerciseButton = itemView.findViewById(R.id.deleteButton);
    }
}
