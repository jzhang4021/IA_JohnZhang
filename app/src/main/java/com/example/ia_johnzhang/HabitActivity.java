package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    EditText addHydration;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User currUser;
    TextView border1;
    TextView wakeUpTime;
    TextView hydrationMessage;
    TextView screenTimeMessage;
    TextView hourTextView;
    TextView minuteTextView;
    TextView sleepHourText;
    TextView sleepMinuteText;
    TextView sleepIndicator;
    int t1hour,t1minute;
    ProgressBar screenTimeBar;
    ProgressBar sleepProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        //initialising different views
        waterBar = this.findViewById(R.id.waterBar);
        border1 = this.findViewById(R.id.screenTimeMessage);
        wakeUpTime = this.findViewById(R.id.wakeUpTime);
        addHydration = this.findViewById(R.id.enterHydration);
        hydrationMessage = this.findViewById(R.id.hydrationMessage);
        screenTimeMessage = this.findViewById(R.id.screenTimeMessage);
        screenTimeBar = this.findViewById(R.id.screenTimeBar);
        hourTextView = this.findViewById(R.id.hourTextView);
        minuteTextView = this.findViewById(R.id.minuteTextView);
        sleepHourText = this.findViewById(R.id.sleepHourText);
        sleepMinuteText = this.findViewById(R.id.sleepMinuteText);
        sleepProgress = this.findViewById(R.id.sleepProgress);
        sleepIndicator = this.findViewById(R.id.sleepIndicator);

        //set visibility
        hydrationMessage.setVisibility(View.INVISIBLE);
        screenTimeMessage.setVisibility(View.INVISIBLE);


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

                    waterCounter = currUser.getCurrRecord().getHydration();

                    //if conditions for the water progress bar: Check if the maximum has been reached and if a message needs to be displayed
                    waterBar.setMax(3000);
                    if(waterCounter <  3000) {
                        waterBar.setProgress(waterCounter);
                        ObjectAnimator.ofInt(waterBar, "Progress", waterCounter).setDuration(2000).start();
                    }
                    else{
                        waterBar.setProgress(3000);
                        ObjectAnimator.ofInt(waterBar, "Progress", 3000).setDuration(2000).start();
                        hydrationMessage.setVisibility(View.VISIBLE);
                    }

                    //if conditions for the screen time: Check if the maximum has been reached and if a message needs to be displayed
                    screenTimeBar.setMax(240);
                    int temp = currUser.getCurrRecord().getScreenTime().getNumConversion();
                    if(temp < 240){
                        screenTimeBar.setProgress(temp);
                        ObjectAnimator.ofInt(screenTimeBar, "Progress", temp).setDuration(2000).start();
                    }
                    else{
                        screenTimeBar.setProgress(240);
                        ObjectAnimator.ofInt(screenTimeBar, "Progress",240).setDuration(2000).start();
                        screenTimeMessage.setVisibility(View.VISIBLE);
                    }

                    //set maximum time for sleep in minutes
                    sleepProgress.setMax(480);
                    //gather current sleep info from user;
                    int temp1 = currUser.getCurrRecord().getSleepDuration().getNumConversion();
                    sleepIndicator.setText(currUser.getCurrRecord().getSleepDuration().getHour() + " Hrs " +
                            currUser.getCurrRecord().getSleepDuration().getMinute() + " min");
                    //show progress differently depending on user's data
                    if(temp1 < 480){
                        sleepProgress.setProgress(temp1);
                        ObjectAnimator.ofInt(sleepProgress, "Progress", temp1).setDuration(2000).start();
                    }else{
                        sleepProgress.setProgress(temp1);
                        ObjectAnimator.ofInt(sleepProgress, "Progress", 480).setDuration(2000).start();
                    }
                }
            }
        });



    }

    /**
     * updates the hydration progress bar and firebase information as the user inputs more water.
     * @param v takes in the input water button
     */
    public void saveHydration(View v){

       int addAmount = Integer.parseInt(addHydration.getText().toString());

       //increment to max amount if user has drank more water than the limit
        if (currUser.getCurrRecord().getHydration() + addAmount <  3000){
            waterBar.incrementProgressBy(addAmount);
        }
        else{
            waterBar.incrementProgressBy(3000 - currUser.getCurrRecord().getHydration());
            hydrationMessage.setVisibility(View.VISIBLE);
        }

       //update user's hydration record
        currUser.getCurrRecord().setHydration(currUser.getCurrRecord().getHydration() + addAmount);
        firestore.collection("Users").document(currUser.getEmail()).set(currUser);

    }

    /**
     * updates the screen time progress bar and firebase information as the user inputs more time.
     * @param v takes in the screen time button
     */
    public void saveScreenTime(View v){
        //casts info to time
        int addHour = Integer.parseInt(hourTextView.getText().toString());
        int addMinute = Integer.parseInt(minuteTextView.getText().toString());
        Time tempTime = new Time(addHour,addMinute);

        //error handling of too many minutes
        if(addMinute > 60){
            Toast.makeText(this, "Less than 60 minutes please", Toast.LENGTH_SHORT).show();
        }
        else {
            //check if user has crossed the screen time limit, if so increment to max
            if(currUser.getCurrRecord().getScreenTime().getNumConversion() + tempTime.getNumConversion() < 240){
                screenTimeBar.incrementProgressBy(tempTime.getNumConversion());
            }
            else{
                screenTimeBar.incrementProgressBy(240 - currUser.getCurrRecord().getScreenTime().getNumConversion());
                screenTimeMessage.setVisibility(View.VISIBLE);
            }

            //update firebase info
            currUser.getCurrRecord().getScreenTime().addHour(addHour);
            currUser.getCurrRecord().getScreenTime().addMinute(addMinute);
            firestore.collection("Users").document(currUser.getEmail()).set(currUser);

        }
    }

    /**
     * resets user's current sleep time depending on how much they received last night
     * @param v takes in the sleep time button
     */
    public void SaveSleepTime(View v){
        //casts to time
        int hour = Integer.parseInt(sleepHourText.getText().toString());
        int minute = Integer.parseInt(sleepMinuteText.getText().toString());
        Time temp = new Time(hour, minute);

        currUser.getCurrRecord().setSleepDuration(temp);
        firestore.collection("Users").document(currUser.getEmail()).set(currUser);

        if(minute > 60){
            Toast.makeText(this, "Less than 60 minutes please", Toast.LENGTH_SHORT).show();
        }
        else {
            //check if user has slept past the limit, set value to max if so
            if (temp.getNumConversion() < 480) {
                sleepProgress.setProgress(temp.getNumConversion());
                ObjectAnimator.ofInt(sleepProgress, "Progress", temp.getNumConversion()).setDuration(2000).start();
            } else {
                sleepProgress.setProgress(480);
                ObjectAnimator.ofInt(sleepProgress, "Progress", 480).setDuration(2000).start();
            }
            sleepIndicator.setText(currUser.getCurrRecord().getSleepDuration().getHour() + " Hrs " +
                    currUser.getCurrRecord().getSleepDuration().getMinute() + " min");
        }
    }


    public void goToExerciseCentral(View v){
        System.out.println("Please respond x ");
        Intent intent = new Intent(HabitActivity.this, ExcerciseCentralActivity.class);
        startActivity(intent);
    }

    public void LogOut(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HabitActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}