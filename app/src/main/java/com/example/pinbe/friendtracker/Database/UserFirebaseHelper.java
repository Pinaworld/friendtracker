package com.example.pinbe.friendtracker.Database;

import android.util.Log;

import com.example.pinbe.friendtracker.Models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<User> users=new ArrayList<>();

    public UserFirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //SAVE
    public Boolean save(User user, String userId)
    {
        if(user==null)
        {
            saved=false;
        }else {

            try
            {
                db.child("User").child(userId).setValue(user);
                addUserChangeListener(user);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }

        }

        return saved;
    }

    //READ
    public ArrayList<User> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return users;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        users.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            User user = ds.getValue(User.class);
            users.add(user);
        }
    }

    private void addUserChangeListener(User user) {
        // User data change listener
        db.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e("USER_CREATION", "User data is null!");
                    return;
                }

                Log.e("USER_CREATION", "User data is changed: " + user.getFirstname() + ", " + user.getLastname() + ", " + user.getEmail() + ", " + user.getPhoneNumber() + ", PASSWORD = *******");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("USER_CREATION", "Failed to read user", error.toException());
            }
        });
    }

}
