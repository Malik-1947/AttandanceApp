package com.example.attandanceapp.Database;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.attandanceapp.User;
import com.example.attandanceapp.Utils.Preferences;
import com.example.attandanceapp.Utils.Respone;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;


public class Database {
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static String TAG = "Database";
    private static final String Users = "User";
    private static final String Attendance = "Attendance";

    static DatabaseReference reference = Firebase.getInstance().getReference(Users);
    static DatabaseReference attendanceRef = Firebase.getInstance().getReference();


    public static void signIn(Context context, String email, String password, Respone respone) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            respone.onResponse(true);


        });
    }

    public static void signUp(Context context, String email, String password,
                              Respone response) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    response.onResponse(task.isSuccessful());
                }).addOnFailureListener(e -> {
            Log.e(TAG, "signIn: ", e);
            response.onError(e.getMessage());
        });
    }

    public static void setUser(Activity context, User user) {
        String key = reference.push().getKey();
        Preferences.setKey(context, key);
            String currentUser = auth.getCurrentUser().getUid();
            reference.child(currentUser).setValue(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "SignedIn", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    public static void setInAttendance(Activity context, User user) {

        attendanceRef.child(Attendance).child(Preferences.getKey(context)).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "SignedIn", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void setOutAttendance(Activity context, User user) {
        attendanceRef.child(Attendance).child(Preferences.getKey(context)).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "SignedIn", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void signOut(Activity activity) {
        auth.signOut();
    }
}


