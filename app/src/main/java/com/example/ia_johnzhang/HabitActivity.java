package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HabitActivity extends AppCompatActivity {

    ProgressBar waterBar;
    int waterCounter;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User currUser;
    TextView border1;
    TextView wakeUpTime;
    int t1hour,t1minute,

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        waterBar = this.findViewById(R.id.waterBar);
        border1 = this.findViewById(R.id.screenTimeMessage);
        wakeUpTime = this.findViewById(R.id.wakeUpTime);


        //add firebase functionality
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getInstance().getCurrentUser();

        //set up analog time. Credits to https://www.youtube.com/watch?v=o-HVE_VxyjQ&t=43s
        wakeUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialise time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        HabitActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                //initialise hour and minute
                                t1hour = hourOfDay;
                                t1minute = minute;
                                //Store hour and minute in string
                                String time = t1hour + ":" + t1minute;
                                //Initialise 24 hours time format
                                SimpleDateFormat f24hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24hours.parse(time);
                                    //initialise 12 hours time format
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    //set selected time on text view
                                    wakeUpTime.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },12,0,false
                );
                //set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //display previous selected time
                timePickerDialog.updateTime(t1hour,t1minute);
                //show dialog
                timePickerDialog.show();
            }
        });

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