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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import io.grpc.Context;

public class ExcerciseCentralActivity extends AppCompatActivity {

    RecyclerView workoutLog;
    RecyclerView setsList;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User currUser;
    SetListAdapter adapter;
    private Button makeNewExerciseButton;

    //for pop up screen
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText setTitle;
    ImageView relatedImage;
    private Button save;
    private Button cancel;
    private Uri imageURI;
    private String tempLink;
    FirebaseStorage storage;
    StorageReference storageReference;
    private WorkoutSet aSet;

    //for finding an available exercise
    private AlertDialog.Builder dialogBuilder2;
    AlertDialog dialog2;
    private RecyclerView availableExerciseRecycler;
    private Button button5;


    //temp storage
    View tempPopup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_central);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        makeNewExerciseButton = findViewById(R.id.button3);

        workoutLog = findViewById(R.id.workoutLog);


        regenRecyclerView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        regenRecyclerView();
    }

    public void createNewContactDialog(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.make_exercise, null);

        relatedImage = contactPopupView.findViewById(R.id.workoutIcon);
        relatedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 choosePicture();
            }
        });

        setTitle = contactPopupView.findViewById(R.id.setTitle);
        save = contactPopupView.findViewById(R.id.save);
        cancel = contactPopupView.findViewById(R.id.cancel);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setTitle.getText() == null || imageURI == null){
                    Toast.makeText(ExcerciseCentralActivity.this, "Please input all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    aSet = new WorkoutSet(setTitle.getText().toString(),tempLink);
                    tempLink = null;
                    imageURI = null;

                    firestore.collection("WorkoutSets").document(aSet.getTitle()).set(aSet);
                    dialog.dismiss();

                    Intent intent = new Intent(ExcerciseCentralActivity.this, CreateWorkoutActivity.class);
                    intent.putExtra("currWorkout", aSet);
                    startActivity(intent);

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageURI = null;
                dialog.dismiss();
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


        tempLink = "images/" + System.currentTimeMillis();
        StorageReference newRef = storageReference.child(tempLink);

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

    public void regenRecyclerView(){
        firestore.collection("Users").document(mUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //convert data from firebase into user
                    DocumentSnapshot ds = task.getResult();
                    currUser = ds.toObject(User.class);

                    //set button invisible for students
                    if(!currUser.getRecord().equals("Teacher")){
                        makeNewExerciseButton.setVisibility(View.INVISIBLE);
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(ExcerciseCentralActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    workoutLog.setLayoutManager(layoutManager);

                    WorkoutLogAdapter myAdapter = new WorkoutLogAdapter(currUser.getFinishedWorkouts());
                    workoutLog.setAdapter(myAdapter);

                    adapter = new SetListAdapter(ExcerciseCentralActivity.this, currUser.getOwnedSets(), currUser);

                    setsList = findViewById(R.id.setsList);
                    setsList.setAdapter(adapter);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ExcerciseCentralActivity.this, 2,GridLayoutManager.VERTICAL, false);
                    setsList.setLayoutManager(gridLayoutManager);
                }
            }
        });
    }

    public void addExercise(View v){
        dialogBuilder2 = new AlertDialog.Builder(this);

        tempPopup = getLayoutInflater().inflate(R.layout.choose_workout_popup, null);

        availableExerciseRecycler = tempPopup.findViewById(R.id.availableExerciseRecycler);
        button5 = tempPopup.findViewById(R.id.button5);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        availableExerciseRecycler.setLayoutManager(layoutManager);


        ArrayList<WorkoutSet> passIn = new ArrayList<>();

        firestore.collection("WorkoutSets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        WorkoutSet temp = document.toObject(WorkoutSet.class);
                        passIn.add(temp);
                    }

                    addExerciseAdapter adapter = new addExerciseAdapter(passIn, currUser);
                    availableExerciseRecycler.setAdapter(adapter);

                    dialogBuilder2.setView(tempPopup);
                    dialog2 = dialogBuilder2.create();
                    dialog2.show();

                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

    }

    public void goToHabits(View v){
        Intent intent = new Intent(ExcerciseCentralActivity.this, HabitActivity.class);
        startActivity(intent);
    }

}