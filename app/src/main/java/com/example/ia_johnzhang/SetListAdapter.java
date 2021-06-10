package com.example.ia_johnzhang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SetListAdapter extends RecyclerView.Adapter<SetListViewHolder> {

    ArrayList<WorkoutSet> sets;
    Context context;
    LayoutInflater inflater;
    FirebaseStorage storage;
    StorageReference storageRef;

    public SetListAdapter(Context context,ArrayList<WorkoutSet> sets){
        this.sets = sets;
        this.inflater = LayoutInflater.from(context);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @NonNull
    @Override
    public SetListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_row, parent,false);

        SetListViewHolder holder = new SetListViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull SetListViewHolder holder, int position) {
        holder.title.setText(sets.get(position).getTitle());
        String imageDirectory = sets.get(position).getImageResource();
       // holder.background.setBackgroundResource(sets.get(position).getImageResource());

        StorageReference imageRef = storageRef.child(imageDirectory);

        //changed the maximum value the image can take up to be 20 megabytes
        final long TWENTY_MEGABYTES = 1024 * 1024 * 20;
        imageRef.getBytes(TWENTY_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // onSuccess code derived from https://stackoverflow.com/questions/3545493/display-byte-to-imageview-in-android/10712218#:~:text=%2F%2F%20Convert%20bytes%20data%20into,data%20to%20the%20ImageView%20imageView.
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

}
