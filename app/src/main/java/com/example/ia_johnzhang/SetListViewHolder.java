package com.example.ia_johnzhang;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SetListViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;
    protected CardView background;
    protected ImageView imgView;

    public SetListViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.setTitle);
        background = itemView.findViewById(R.id.background);
        imgView = itemView.findViewById(R.id.imgView);
    }


}
