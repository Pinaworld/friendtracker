package com.example.pinbe.friendtracker.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pinbe.friendtracker.Interfaces.GroupCustomClickListener;
import com.example.pinbe.friendtracker.Fragments.GroupCreationFragment;
import com.example.pinbe.friendtracker.Fragments.GroupFragment;
import com.example.pinbe.friendtracker.GroupAdapter;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class GroupsActivity extends AppCompatActivity {

    private Button createGroupButton;
    private RecyclerView groupRecyclerView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private GroupCustomClickListener clickListener;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupRecyclerView = findViewById(R.id.groupRecyclerView);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        createGroupButton = findViewById(R.id.goToCreateGroupButton);

        mFirebaseInstance = getDatabase(getApplicationContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference().child("Group");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = mFirebaseDatabase.orderByChild("members");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Group> groups = new ArrayList<>();

                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Group group = ds.getValue(Group.class);
                        if(group.getMembersId().contains(userId)){
                            groups.add(group);
                        }
                    }
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

                groupRecyclerView.setAdapter(new GroupAdapter(getApplicationContext(), groups, new GroupCustomClickListener() {
                    @Override
                    public void onGroupItemClick(View v, Group group) {
                        setGroupFragment(group);
                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = new GroupCreationFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.groupFrameLayout, currentFragment)
                        .commit();
            }
        });
    }

    private void setGroupFragment(Group group) {
        currentFragment = new GroupFragment();
        ((GroupFragment) currentFragment).getGroup(group);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.groupFrameLayout, currentFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
