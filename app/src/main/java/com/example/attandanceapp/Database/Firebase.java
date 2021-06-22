package com.example.attandanceapp.Database;

import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static FirebaseDatabase instance;

    public static synchronized FirebaseDatabase getInstance() {
        if (instance == null) {
            instance = FirebaseDatabase.getInstance();
        }
        return instance;
    }

}

