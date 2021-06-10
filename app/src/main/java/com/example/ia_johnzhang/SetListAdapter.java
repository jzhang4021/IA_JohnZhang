package com.example.ia_johnzhang;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<WorkoutSet> sets;
    Context context;
    LayoutInflater inflater;

    public SetListAdapter(Context context,ArrayList<WorkoutSet> sets){
        this.sets = sets;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_row, parent,false);
        return new SetListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SetListViewHolder holder, int position) {
        holder.title.setText(sets.get(position).getTitle());
       // holder.background.setBackgroundResource(sets.get(position).getImageResource());
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

}
