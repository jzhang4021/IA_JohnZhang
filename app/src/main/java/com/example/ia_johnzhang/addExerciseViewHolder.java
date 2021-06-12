package com.example.ia_johnzhang;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class addExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageView addExerciseImage;
    ConstraintLayout coolLayout;

    public addExerciseViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.textView18);
        addExerciseImage = itemView.findViewById(R.id.imageView2);
        coolLayout = itemView.findViewById(R.id.coolLayout);

    }
}
