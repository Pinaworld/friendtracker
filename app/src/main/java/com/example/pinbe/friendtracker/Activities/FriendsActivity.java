package com.example.pinbe.friendtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pinbe.friendtracker.Fragments.FriendFragment;
import com.example.pinbe.friendtracker.Adapters.FriendsAdapter;
import com.example.pinbe.friendtracker.Interfaces.UserCustomClickListener;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.Models.User;
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

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private TextView typeTxtView;
    private EditText searchingView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private UserCustomClickListener clickListener;
    private Fragment currentFragment;
    private ArrayList<User> friends;
    private User currentUser;
    private Group group;
    private String viewType;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        typeTxtView = findViewById(R.id.type);
        searchingView = findViewById(R.id.searchingView);
        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseInstance = getDatabase(getApplicationContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference().child("User");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friends = new ArrayList<>();
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("Group");
        type = intent.getStringExtra("Type");
        viewType = intent.getStringExtra("ViewType");

        typeTxtView.setText(type);

        setSearchingViewListener();

        if(viewType.equals("View")) {
            searchingView.setVisibility(View.GONE);
        }
        getAllFriends();
    }

    private void getAllFriends(){
        if(viewType.equals("View")){
            if(group != null){
                getGroupFriends();
            }
            else{
                getUser();
            }
        }
        else{
            getUser();
        }
    }

    private void setSearchingViewListener() {
        searchingView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchFriends();
            }
        });
    }

    private void searchFriends() {
        final String text = searchingView.getText().toString();
        Query query = mFirebaseDatabase;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    friends = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if(!user.getId().equals(currentUser.getId()) && (user.getFirstname().contains(text) || user.getLastname().contains(text))){
                            friends.add(user);
                        }
                    }

                    setAdapter();
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUser() {
        Query query = mFirebaseDatabase.child(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    currentUser = dataSnapshot.getValue(User.class);
                    if(group == null){
                        getUserFriends();
                    }
                } catch (Exception e) {
                    Log.i("ERROR", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ERROR", databaseError.getMessage());
            }
        });

    }

    private void getUserFriends() {
        for (String id : currentUser.getFriendsId()) {
            getFriends(id);
        }
    }

    private void getGroupFriends() {
        for (String id : group.getMembersId()) {
            getFriends(id);
        }
    }

    private void getFriends(String id){
        Query query = mFirebaseDatabase.child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    User user = dataSnapshot.getValue(User.class);
                    if(viewType.equals("View")){
                        friends.add(user);
                    }
                    setAdapter();
                } catch (Exception e) {
                    Log.i("ERROR", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        userRecyclerView.setAdapter(new FriendsAdapter(getApplicationContext(), friends, new UserCustomClickListener() {
            @Override
            public void onUserItemClick(View v, User user) {
                setUserFragment(user);
            }
        }));
    }

    private void setUserFragment(User user) {
        currentFragment = new FriendFragment();
        ((FriendFragment) currentFragment).setData(user, group, currentUser, viewType);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.userFrameLayout, currentFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void clearFriendsList(){
        if(friends != null){
            friends.clear();
        }
    }

    public void updateGroup(Group group){
        this.group = group;
        if(group.getMembersId().size() == 0){
            clearFriendsList();
            setAdapter();
        }
        if(viewType.equals("View")){
            clearFriendsList();
            getGroupFriends();
        }
    }

    public void updateUser(User user){
        currentUser = user;
        if(currentUser.getFriendsId().size() == 0){
            clearFriendsList();
            setAdapter();
        }
        if(viewType.equals("View")) {
            clearFriendsList();
            getUserFriends();
        }
    }

}
