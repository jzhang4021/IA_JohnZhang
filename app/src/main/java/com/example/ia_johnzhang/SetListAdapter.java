package com.example.ia_johnzhang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

public class SetListAdapter extends RecyclerView.Adapter<SetListViewHolder> implements PopupMenu.OnMenuItemClickListener {

    ArrayList<WorkoutSet> sets;
    Context context;
    LayoutInflater inflater;
    FirebaseStorage storage;
    StorageReference storageRef;
    User currUser;
    int currPos;

    public SetListAdapter(Context context,ArrayList<WorkoutSet> sets, User currUser){
        this.sets = sets;
        this.inflater = LayoutInflater.from(context);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        this.currUser = currUser;
    }

    @NonNull
    @Override
    public SetListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_row, parent,false);

        SetListViewHolder holder = new SetListViewHolder(view);
        context = parent.getContext();
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

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currUser.getRecord().equals("Teacher")){
                    currPos = position;

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.inflate(R.menu.menu_popup);
                    popupMenu.setOnMenuItemClickListener(SetListAdapter.this);

                    popupMenu.show();
                }
                else{
                    Intent intent = new Intent(context, ordinaryWorkoutActivity.class);
                    intent.putExtra("currWorkout", sets.get(position));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch(item.getItemId()){
            case R.id.edit_workout_popup:
                Intent intent = new Intent(context, CreateWorkoutActivity.class);
                intent.putExtra("currWorkout", sets.get(currPos));
                context.startActivity(intent);
                return true;
            case R.id.just_workout_popup:
                Intent intent1 = new Intent(context, ordinaryWorkoutActivity.class);
                intent1.putExtra("currWorkout", sets.get(currPos));
                context.startActivity(intent1);
                return true;
            default:
                return false;
        }

    }
}
