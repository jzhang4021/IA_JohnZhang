package com.example.ia_johnzhang;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ordinaryWorkoutViewHolder extends RecyclerView.ViewHolder {

    TextView ordinaryTitle;
    TextView ordinaryRPE;
    ImageButton watchVidButton;

    public ordinaryWorkoutViewHolder(@NonNull View itemView) {
        super(itemView);

        ordinaryTitle = itemView.findViewById(R.id.ordinaryTitleView);
        ordinaryRPE = itemView.findViewById(R.id.ordinaryRPEText);
        watchVidButton = itemView.findViewById(R.id.watchVidButton);

    }
}
