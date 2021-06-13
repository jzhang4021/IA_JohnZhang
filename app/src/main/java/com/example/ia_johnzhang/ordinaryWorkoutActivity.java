package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class ordinaryWorkoutActivity extends AppCompatActivity {

    TextView ordinaryTitleView;
    RecyclerView ordinaryExerciseRecycler;
    WorkoutSet currWorkout;

    private AlertDialog.Builder dialogBuilder1;
    private AlertDialog dialog1;
    VideoView vidView;
    Button closeVidButton;

    StorageReference storageReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    User currUser;

    int seconds;
    private boolean running;
    String totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordinary_workout);

        ordinaryExerciseRecycler = findViewById(R.id.ordinaryRecycler);
        ordinaryTitleView = findViewById(R.id.ordinaryTitleView);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

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

        Intent i = getIntent();
        currWorkout =(WorkoutSet) i.getSerializableExtra("currWorkout");
        System.out.println(currWorkout.getExercises().get(0).getName());
        System.out.println(currWorkout.getExercises().get(0).getRPE());

        ordinaryExerciseRecycler = findViewById(R.id.ordinaryRecycler);
        regenRecyclerView();

        running = true;
        keepTrack();

    }

    public void createNewVideoDialog(Exercise thisExercise){
        dialogBuilder1 = new AlertDialog.Builder(this);

        final View vidPopup = getLayoutInflater().inflate(R.layout.watch_video_popup, null);

        vidView = vidPopup.findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        vidView.setMediaController(mediaController);
        mediaController.setAnchorView(vidView);

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
                Toast.makeText(ordinaryWorkoutActivity.this, "Video upload failed", Toast.LENGTH_SHORT).show();
            }
        });

        closeVidButton = vidPopup.findViewById(R.id.closeVidButton);

        dialogBuilder1.setView(vidPopup);
        dialog1 = dialogBuilder1.create();
        dialog1.show();

        closeVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

    }

    public void regenRecyclerView(){

        ordinaryWorkoutAdapter adapter = new ordinaryWorkoutAdapter(currWorkout.getExercises());
        LinearLayoutManager hi = new LinearLayoutManager(this);
        ordinaryExerciseRecycler.setAdapter(adapter);
        ordinaryExerciseRecycler.setLayoutManager(hi);

    }

    public void keepTrack(){
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                 totalTime = String.format(Locale.getDefault(), "%d:%02d%02d", hours, minutes, secs);

                if(running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void finishWorkout(View v){
        running = false;


        //calculate date, done with guide from https://stackoverflow.com/questions/8654990/how-can-i-get-current-date-in-android/15698784
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String RPEavg = currWorkout.getAverageRPE();



        FinishedWorkout newFin = new FinishedWorkout(totalTime, RPEavg ,formattedDate, currWorkout.getTitle());
        currUser.getFinishedWorkouts().add(newFin);
        firestore.collection("Users").document(currUser.getEmail()).set(currUser);
        finish();
    }
}