package com.example.pinbe.friendtracker.Database;

import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private FirebaseDatabase firebaseInstance;

    public DatabaseManager(){

        firebaseInstance = FirebaseDatabase.getInstance();
    }



}
