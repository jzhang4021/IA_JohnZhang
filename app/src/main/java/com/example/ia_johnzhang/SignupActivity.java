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


    public void signUp(View v){
        final String emailString = username.getText().toString();
        String passwordString = password.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (emailString.contains("@cis.edu.hk")) {
                        Teacher newTeach = new Teacher(emailString, "Teacher");
                        firestore.collection("Users").document(emailString).set(newTeach);
                        updateUI(mAuth.getCurrentUser());
                    } else {
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
