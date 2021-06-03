package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HabitActivity extends AppCompatActivity {

    ProgressBar waterBar;
    int waterCounter;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User currUser;
    TextView border1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        waterBar = this.findViewById(R.id.waterBar);
        border1 = this.findViewById(R.id.screenTimeMessage);


        //add firebase functionality
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        firestore.collection("Users").document(mUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //convert data from firebase into user
                    DocumentSnapshot ds = task.getResult();
                    currUser = ds.toObject(User.class);
                }
            }
        });

        waterBar.setMax(3000);
        waterBar.setProgress(waterCounter);
        ObjectAnimator.ofInt(waterBar, "Progress",waterCounter ).setDuration(2000).start();





    }


}