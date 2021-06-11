package com.example.ia_johnzhang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ordinaryWorkoutAdapter extends RecyclerView.Adapter<ordinaryWorkoutViewHolder> {

    ArrayList<Exercise> mData;
    Context mContext;

    public ordinaryWorkoutAdapter(ArrayList<Exercise> mData){
        this.mData = mData;
    }

    @NonNull
    @Override
    public ordinaryWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordinary_row_view, parent, false);

        ordinaryWorkoutViewHolder holder = new ordinaryWorkoutViewHolder(myView);

        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ordinaryWorkoutViewHolder holder, int position) {
        holder.ordinaryTitle.setText(mData.get(position).getName());

        holder.ordinaryRPE.setText("RPE: " + mData.get(position).getRPE());

        Exercise currExercise = mData.get(position);

        holder.watchVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof ordinaryWorkoutActivity){
                    ((ordinaryWorkoutActivity) mContext).createNewVideoDialog(currExercise);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
