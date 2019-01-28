package com.example.pinbe.friendtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AuthenticationActivity extends AppCompatActivity{


    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener  authStateListener;

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnSignup, btnLogin, btnReset;
    private ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthenticationActivity.this, AuthenticationActivity.class));
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        if(auth.getCurrentUser()!= null){

            Toast.makeText(AuthenticationActivity.this, "Connection Successfull." ,
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void register() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        boolean inputsAreValid = validateInputs(email, password);

        progressBar.setVisibility(View.VISIBLE);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                Toast.makeText(AuthenticationActivity.this, "Registered.",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(AuthenticationActivity.this, "Register failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    private void attemptLogin() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        boolean inputsAreValid = validateInputs(email, password);

        //authenticate user
        if(inputsAreValid) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(AuthenticationActivity.this, "Authenticated",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AuthenticationActivity.this, MapsActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(AuthenticationActivity.this, "Impossible to login." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateInputs(String email, String password){

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

