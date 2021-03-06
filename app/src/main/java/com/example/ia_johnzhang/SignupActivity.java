package com.example.ia_johnzhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = this.findViewById(R.id.emailSignup);
        password = this.findViewById(R.id.passwordSignup);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }


    /**
     * signs up the user and creates a database reference if successful
     * Allocates whether a user is teacher or student based on email
     * @param v takes in signup button
     */
    public void signUp(View v){
        final String emailString = username.getText().toString();
        String passwordString = password.getText().toString();

        //firebase signup method
        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //characteristic of teacher emails are the addresses
                    if (emailString.contains("@cis.edu.hk")) {
                        Teacher newTeach = new Teacher(emailString, "Teacher");
                        //add new user to firebase firestore
                        firestore.collection("Users").document(emailString).set(newTeach);
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        //will make a student otherwise
                        Student newStud = new Student(emailString, "Student");
                        firestore.collection("Users").document(emailString).set(newStud);
                        updateUI(mAuth.getCurrentUser());
                    }
                } else {
                    Log.w("SIGN UP", "signUpWithEmail:failure", task.getException());
                    Toast.makeText(SignupActivity.this,"Sign Up Failed", Toast.LENGTH_SHORT).show();

                }


        }
        });

    }

    //update UI after sign up is successful.
    public void updateUI(FirebaseUser currentUser){
                if(currentUser != null) {
                    Intent intent = new Intent(SignupActivity.this, HabitActivity.class);
                    startActivity(intent);
                }
            }

    public void endStuff(View v){
        finish();
    }

}
