package com.example.pinbe.friendtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Database.UserFirebaseHelper;
import com.example.pinbe.friendtracker.GroupAdapter;
import com.example.pinbe.friendtracker.Interfaces.GroupCustomClickListener;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.Models.User;
import com.example.pinbe.friendtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    UserFirebaseHelper userFirebaseHelper;

    private EditText inputEmail, inputPassword, inputFirstname, inputLastname, inputPhoneNumber;
    private Button btnSignup, btnLogin;
    private ProgressBar progressBar ;

    private String userId;
    private User currentUser;
    private AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        mFirebaseInstance = getDatabase(getApplicationContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference();

        userFirebaseHelper = new UserFirebaseHelper(mFirebaseDatabase);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputFirstname = findViewById(R.id.firstname);
        inputLastname = findViewById(R.id.lastname);
        inputPhoneNumber = findViewById(R.id.phoneNumber);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);

        btnSignup.setText("Sauvegarder les modifications");
        btnLogin.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        getCurrentUserInfos();
    }



    private void getCurrentUserInfos() {
        Query query = mFirebaseDatabase.child("User").child(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                        currentUser = dataSnapshot.getValue(User.class);

                } catch (Exception e) {
                    Log.i("ERROR", e.getMessage());
                }

                credential =EmailAuthProvider.getCredential(currentUser.getEmail(), currentUser.getPassword());

                setTextViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTextViews(){
        inputEmail.setText(currentUser.getEmail());
        inputPassword.setText(currentUser.getPassword());
        inputFirstname.setText(currentUser.getFirstname());
        inputLastname.setText(currentUser.getLastname());
        inputPhoneNumber.setText(currentUser.getPhoneNumber());

    }

    private void updateUser() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        final String lastName = inputLastname.getText().toString();
        final String firstName = inputFirstname.getText().toString();
        final String phoneNumber = inputPhoneNumber.getText().toString();

        Boolean inputsAreValid = validateInputs(email, password, lastName, firstName, phoneNumber);

        if(inputsAreValid) {
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setLastname(lastName);
            currentUser.setFirstname(firstName);
            currentUser.setPhoneNumber(phoneNumber);

            mFirebaseDatabase.child("User").child(userId).setValue(currentUser);


            auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("AUTHENTICATION", "User email address updated.");
                        auth = FirebaseAuth.getInstance();
                        auth.getCurrentUser().updateEmail(email);
                        auth.getCurrentUser().updatePassword(password);

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("AUTHENTICATION", e.getMessage());
                }
            });

            finish();
        }
    }

    private boolean validateInputs(String email, String password, String lastname, String firstname, String phoneNumber){

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer votre adresse mail!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer votre mçot de passe!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer votre prénom!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer votre nom!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer votre numéro de téléphone!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
