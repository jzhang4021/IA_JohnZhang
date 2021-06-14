package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateWorkoutActivity extends AppCompatActivity {

    //popup and components for creating new exercise
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText exerciseName;
    private EditText RPE;
    private EditText aVideo;
    private Button saveButton;
    private Button cancel;

    //main UI components
    private RecyclerView exerciseView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    User currUser;
    WorkoutSet currWorkout;
    Exercise tempExercise;
    Intent tempStuff;
    StorageReference storageReference;

    TextView WorkoutTitle;


    //dialog and components for watching vdieo
    private AlertDialog.Builder dialogBuilder1;
    private AlertDialog dialog1;
    VideoView vidView;
    Button closeVidButton;

    //DatabaseReference databaseReference;
    public static final int PICK_VID_REQUEST = 12;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        //get current user from firebase
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

        //get current workout set from exercisecentralactivity
        Intent i = getIntent();
        currWorkout =(WorkoutSet) i.getSerializableExtra("currWorkout");

        WorkoutTitle = findViewById(R.id.textView12);
        WorkoutTitle.setText(currWorkout.getTitle());
        exerciseView = findViewById(R.id.ordinaryRecycler);
        //generate recycler view layout - made into a function so it can be updated
        regenRecyclerView();


    }

    /**solution taken from https://www.youtube.com/watch?v=4GYKOzgQDWI
     * creates new popup menu for adding an exercise
     * @param v called by the create exercise button
     */
    public void createNewExerciseDialog(View v){
        //set up popup menu components
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopup = getLayoutInflater().inflate(R.layout.popup, null);
        exerciseName = contactPopup.findViewById(R.id.exerciseName);
        RPE = contactPopup.findViewById(R.id.RPE);
        aVideo = contactPopup.findViewById(R.id.aVideo);
        saveButton = contactPopup.findViewById(R.id.submitButton);
        cancel = contactPopup.findViewById(R.id.cancelButton);

        //cannot save until fields are entered
        saveButton.setEnabled(false);

        aVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        dialogBuilder.setView(contactPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempExercise = null;
                dialog.dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new exercise and add it to the workout
                tempExercise = new Exercise(exerciseName.getText().toString(),RPE.getText().toString());
                uploadVideoFirebase(tempStuff.getData());
                currWorkout.addExercise(tempExercise);
                tempExercise = null;
                dialog.dismiss();
                regenRecyclerView();
            }
        });
    }

    /**solution from https://www.youtube.com/watch?v=lY9zSr6cxko
     * choosing an intent and saving an image
     */
    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "VIDEO FILE SELECT"),PICK_VID_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_VID_REQUEST && resultCode == RESULT_OK && data.getData() != null){
            //allow exercise to be saved
            saveButton.setEnabled(true);
            aVideo.setText(data.getDataString());
            tempStuff = data;
        }
    }

    /**
     * when saved, gathers the user's tutorial video and sends it to firebase storage
     * @param data takes in the video data that was received by the intent
     */
    private void uploadVideoFirebase(Uri data){
        //create a temporary progress dialog to monitor outcome
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        //set reference for video
        tempExercise.setResourcePath("video/" + System.currentTimeMillis());
        StorageReference reference = storageReference.child(tempExercise.getResourcePath());
        //put video in firebase storage
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreateWorkoutActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //monitor progress in the meantime
                double progress = (100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded..." + (int) progress + "%");

            }
        });
    }

    /**
     * With every exercise possessing a video, this locates the video in firebase and displays it
     * in a popup
     * @param thisExercise should contain the resource path to receive video
     */
    public void createNewVideoDialog(Exercise thisExercise){
        //create popup components
        dialogBuilder1 = new AlertDialog.Builder(this);
        final View vidPopup = getLayoutInflater().inflate(R.layout.watch_video_popup, null);
        vidView = vidPopup.findViewById(R.id.videoView);

        //media controller for vidview
        MediaController mediaController = new MediaController(this);
        vidView.setMediaController(mediaController);
        mediaController.setAnchorView(vidView);

        //getting vid from firebase
        StorageReference newRef = storageReference.child(thisExercise.getResourcePath());
        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                vidView.setVideoURI(uri);
                vidView.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateWorkoutActivity.this, "Video upload failed", Toast.LENGTH_SHORT).show();
            }
        });

        closeVidButton = vidPopup.findViewById(R.id.closeVidButton);

        //open up dialog
        dialogBuilder1.setView(vidPopup);
        dialog1 = dialogBuilder1.create();
        dialog1.show();

        //monitor user input
        closeVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

    }

    /**
     * Saves the new edited workout. Since this is both the edit workout and create workout page,
     * we first check if the user already has an existing workout similar to this one. If so,
     * we remove the similar workout first (since this one will always be added)
     * @param v takes in a button
     */
    public void finishActivity(View v){
        firestore.collection("WorkoutSets").document(currWorkout.getTitle()).set(currWorkout).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //for loop through user's workouts incase this is already owned
                for(int i = 0; i < currUser.getOwnedSets().size(); i++) {
                    if (currUser.getOwnedSets().get(i).getTitle().equals(currWorkout.getTitle())){
                        currUser.getOwnedSets().remove(i);
                    }
                }

                currUser.getOwnedSets().add(currWorkout);

                //update user profile
                firestore.collection("Users").document(currUser.getEmail()).set(currUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });

            }
        });

    }


    //code to regenerate adapters after update
    public void regenRecyclerView(){
        exerciseAdapter adapter = new exerciseAdapter(currWorkout.getExercises());
        exerciseView.setAdapter(adapter);
        exerciseView.setLayoutManager(new LinearLayoutManager(this));
    }

}