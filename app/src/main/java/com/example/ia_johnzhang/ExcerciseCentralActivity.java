package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class ExcerciseCentralActivity extends AppCompatActivity {

    RecyclerView workoutLog;
    RecyclerView setsList;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User currUser;
    SetListAdapter adapter;

    //for pop up screen
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText workoutTitle;
    ImageView relatedImage;
    private Button save;
    private Button cancel;
    private Uri imageURI;
    FirebaseStorage storage;
    StorageReference storageReference;




    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_central);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firestore.collection("Users").document(mUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                 @Override
                                                                                                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                     if (task.isSuccessful()) {
                                                                                                         //convert data from firebase into user
                                                                                                         DocumentSnapshot ds = task.getResult();
                                                                                                         currUser = ds.toObject(User.class);
                                                                                                     }
                                                                                                 }
                                                                                             });


        workoutLog = findViewById(R.id.workoutLog);
        workoutLog.setLayoutManager(layoutManager);
        WorkoutLogAdapter myAdapter = new WorkoutLogAdapter(currUser.getFinishedWorkouts());
        workoutLog.setAdapter(myAdapter);

        adapter = new SetListAdapter(this, currUser.getOwnedSets());

        setsList = findViewById(R.id.setsList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false);
        setsList.setLayoutManager(gridLayoutManager);

    }

    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.make_exercise, null);

        relatedImage = contactPopupView.findViewById(R.id.workoutIcon);
        relatedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 choosePicture();
            }
        });


    }

    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageURI = data.getData();
            relatedImage.setImageURI(imageURI);
            uploadPicture();
        }
    }

    //https://www.youtube.com/watch?v=CQ5qcJetYAI inspiration for code
    public void uploadPicture(){


        StorageReference newRef = storageReference.child("images/" + System.currentTimeMillis());

        newRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Image Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}