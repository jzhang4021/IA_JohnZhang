package com.example.ia_johnzhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.api.Distribution;

public class ExcerciseCentralActivity extends AppCompatActivity {

    RecyclerView workoutLog;

    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_central);

        workoutLog = findViewById(R.id.workoutLog);
        workoutLog.setLayoutManager(layoutManager);
    }
}