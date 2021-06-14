package com.example.ia_johnzhang;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.os.StrictMode.setThreadPolicy;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button signIn;
    ImageView logo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        username = this.findViewById(R.id.enterUsername);
        password = this.findViewById(R.id.enterPassword);
        signIn = this.findViewById(R.id.signInButton);
        logo = this.findViewById(R.id.logoView);
        logo.setImageResource(R.drawable.hanji);

    }


    /**
     * utilises firebase auth method to login, update UI after
     * @param v takes in login button
     */
    public void login(View v){
        String emailString = username.getText().toString();
        String passwordString = password.getText().toString();

        //firebase auth method
        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("SIGN IN", "Successfully signed in the user");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this,"yay", Toast.LENGTH_SHORT).show();
                    updateUI(user);

                }
                else{
                    Log.w("SIGN IN", "SignInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this,"Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    //create new intent with info
    public void updateUI(FirebaseUser currentUser){
        if(currentUser != null) {
            Intent intent = new Intent(this, HabitActivity.class);
            startActivity(intent);
        }
    }

    public void goToSignup(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}

