package com.example.pinbe.friendtracker.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Models.User;
import com.example.pinbe.friendtracker.R;
import com.example.pinbe.friendtracker.Database.UserFirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    UserFirebaseHelper userFirebaseHelper;

    private EditText inputEmail, inputPassword, inputFirstname, inputLastname, inputPhoneNumber;
    private Button btnSignup, btnLogin;
    private ProgressBar progressBar ;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("THEME", android.content.Context.MODE_PRIVATE);
        String theme = pref.getString("my_theme", "");
        switch (theme) {
            case "theme1":
                setTheme(R.style.theme1);
                break;
            case "theme2":
                setTheme(R.style.theme2);
                break;
            case "default":
                setTheme(R.style.AppTheme);
                break;
            default:
                break;
        }
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

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( getApplicationContext(), RegisterActivity.class));
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void register() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        final String lastName = inputLastname.getText().toString();
        final String firstName = inputFirstname.getText().toString();
        final String phoneNumber = inputPhoneNumber.getText().toString();

        boolean inputsAreValid = validateInputs(email, password, lastName, firstName, phoneNumber);

        progressBar.setVisibility(View.VISIBLE);
        if(inputsAreValid) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                userId = auth.getCurrentUser().getUid();
                                createUser(email, password, lastName, firstName, phoneNumber);
                                Toast.makeText(RegisterActivity.this, "Registered.",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

    public void createUser(String email, String password, String lastname, String firstname, String phoneNumber){
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(firstname, lastname, email, password, phoneNumber);
        user.setId(userId);
        userFirebaseHelper.save(user, userId);
    }

}
