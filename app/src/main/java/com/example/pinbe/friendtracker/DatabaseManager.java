package com.example.pinbe.friendtracker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private FirebaseDatabase firebaseInstance;

    public DatabaseManager(){

        firebaseInstance = FirebaseDatabase.getInstance();
    }



}
