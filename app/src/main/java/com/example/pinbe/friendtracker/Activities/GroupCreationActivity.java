package com.example.pinbe.friendtracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Services.Database.getDatabase;

public class GroupCreationActivity extends AppCompatActivity {

    private Button createGroupButton;
    private EditText groupName;
    private EditText groupeDescription;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;
    private Boolean saved=null;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createGroupButton = findViewById(R.id.createGroupButton);
        groupName = findViewById(R.id.groupName);
        groupeDescription = findViewById(R.id.groupDescription);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = getDatabase();
        mFirebaseDatabase =  mFirebaseInstance.getReference().child("Group");

        userId = auth.getCurrentUser().getUid();

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

    }

    private void createGroup(){
        String name = groupName.getText().toString();
        String description = groupeDescription.getText().toString();
        ArrayList<String> appointmentList = new ArrayList<String>();
        ArrayList<String> membersList = new ArrayList<String>();

        appointmentList.add("");
        membersList.add("");

        if(validateInputs(name)){

            Group group = new Group(name, description, appointmentList, membersList, userId);

            Boolean result =  save(group);
            if(result){
                finish();
            }
            else{
                Toast.makeText(GroupCreationActivity.this, "Error during group creation.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validateInputs(String name){

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Veuillez indiquer le nom du groupe", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Boolean save(Group group)
    {
        if(group==null)
        {
            saved=false;
        }else {

            try
            {
                mFirebaseDatabase.push().setValue(group);
                addGroupChangeListener(group);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }

        }

        return saved;
    }

    private void addGroupChangeListener(Group group) {
        // User data change listener
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);

                // Check for null
                if (group == null) {
                    Log.e("GROUP_CREATION", "Group data is null!");
                    return;
                }

                Log.e("GROUP_CREATION", "Group data is changed: " + group.getName() + ", " + group.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("GROUP_CREATION", "Failed to read user", error.toException());
            }
        });
    }


}
