package com.example.ia_johnzhang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class addExerciseAdapter extends RecyclerView.Adapter<addExerciseViewHolder> {

    ArrayList<WorkoutSet> availableWorkoutsList;

    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageRef;
    User currUser;
    Context context;

    public addExerciseAdapter (ArrayList<WorkoutSet> availableWorkoutList, User currUser){
        this.availableWorkoutsList = availableWorkoutList;
        this.currUser = currUser;
    }

    @NonNull
    @Override
    public addExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firestore = FirebaseFirestore.getInstance();

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_exercise_row, parent, false);

        addExerciseViewHolder holder = new addExerciseViewHolder(myView);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull addExerciseViewHolder holder, int position) {
        holder.title.setText(availableWorkoutsList.get(position).getTitle());
        String imageDirectory = availableWorkoutsList.get(position).getImageResource();

        StorageReference imageRef = storageRef.child(imageDirectory);

        //changed the maximum value the image can take up to be 20 megabytes
        final long TWENTY_MEGABYTES = 1024 * 1024 * 20;
        imageRef.getBytes(TWENTY_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // onSuccess code derived from https://stackoverflow.com/questions/3545493/display-byte-to-imageview-in-android/10712218#:~:text=%2F%2F%20Convert%20bytes%20data%20into,data%20to%20the%20ImageView%20imageView.
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.addExerciseImage.setImageBitmap(bitmap);
            }
        });

        holder.coolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currUser.getOwnedSets().add(availableWorkoutsList.get(position));
                firestore.collection("Users").document(currUser.getEmail()).set(currUser);
                ((ExcerciseCentralActivity) context).dialog2.dismiss();
                ((ExcerciseCentralActivity) context).regenRecyclerView();
            }
        });
    }

    @Override
    public int getItemCount() {
        return availableWorkoutsList.size();
    }
}
