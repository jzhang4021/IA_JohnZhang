package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class CreateWorkoutActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText exerciseName;
    private EditText RPE;
    private EditText aVideo;
    private Button saveButton;
    private Button cancel;
    WorkoutSet currWorkout;

    StorageReference storageReference;
    //DatabaseReference databaseReference;
    public static final int PICK_VID_REQUEST = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        storageReference = FirebaseStorage.getInstance().getReference();
        currWorkout()


    }

    //solution taken from https://www.youtube.com/watch?v=4GYKOzgQDWI
    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopup = getLayoutInflater().inflate(R.layout.popup, null);

        exerciseName = contactPopup.findViewById(R.id.exerciseTitle);
        RPE = contactPopup.findViewById(R.id.RPE);
        aVideo = contactPopup.findViewById(R.id.aVideo);
        saveButton = contactPopup.findViewById(R.id.submitButton);
        cancel = contactPopup.findViewById(R.id.cancelButton);

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
    }

    //https://www.youtube.com/watch?v=lY9zSr6cxko
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
            saveButton.setEnabled(true);
            aVideo.setText(data.getDataString());

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadVideoFirebase(data.getData());
                }
            });
        }
    }

    private void uploadVideoFirebase(Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("Video" + System.currentTimeMillis());
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreateWorkoutActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred())/ snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploaded..." + (int) progress + "%");

            }
        });
    }
}