package com.example.attandanceapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.attandanceapp.Auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


public class Preferences {

    private static final String CURRENT_USER = "CurrentUser";
    private static final String Email = "Email";
    private static final String UserData = "UserData";

    private static final String IsManager = "Manager";
    private static final String UserName = "Name";
    private static final String SignInTime = "SignInTime";
    private static final String Date = "Date";
    private static final String Day = "Day";
    private static final String SignoutTime = "SignoutTime";
    private static final String NoOfResident = "Residents";
    private static final String Lat = "Latitude";
    private static final String lng = "Longitude";
    private static final String key = "key";


    private static final String IN = "IN";
    private static final String Address = "Address";
    private static final String BuildingCode = "Code";
    private static Preferences myPrefs;

    public static String getUID() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static void signOut(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        endSession(activity);
        activity.finish();

    }

    public static void setEmail(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(Email,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Email,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }

    public static void setSignInTime(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(SignInTime,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getSignInTime(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SignInTime,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }

    public static void setSignoutTime(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(SignInTime,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getSignoutTime(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SignInTime,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }


    public static void setDate(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(Date,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getDate(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Date,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }

    public static void setDay(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(Day,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getDay(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Day,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }


    public static void setName(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(CURRENT_USER,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(CURRENT_USER,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }

    public static void setKey(Context context, String name) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(key,
                Context.MODE_PRIVATE).edit();
        sharedPref.putString(UserName, name);
        sharedPref.apply();
    }

    public static String getKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(key,
                Context.MODE_PRIVATE);
        return sharedPref.getString(UserName, "");
    }

    public static void setLatitude(Context context, String no) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE)
                .edit();
        preferences.putString(Lat, String.valueOf(no));
        preferences.apply();
    }

    public static String getLatitude(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE);
        return preferences.getString(Lat, "");
    }

    public static void setLongitude(Context context, String no) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE)
                .edit();
        preferences.putString(String.valueOf(lng), String.valueOf(no));
        preferences.apply();
    }

    public static String getLongitude(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE);
        return preferences.getString(lng, "");
    }


    public static int IsIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE);
        return preferences.getInt(IN, 0);
    }

    public static void setIsIn(Context context, int in) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE)
                .edit();
        preferences.putString(IN, String.valueOf(in));
        preferences.apply();
    }

    public static void setSignInAddress(Context context, String no) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(Address, Context.MODE_PRIVATE)
                .edit();
        preferences.putString(Address, no);
        preferences.apply();
    }

    public static String getSignInAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Address, Context.MODE_PRIVATE);
        return preferences.getString(Address, "");
    }

    public static void setSignOutAddress(Context context, String no) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(Address, Context.MODE_PRIVATE)
                .edit();
        preferences.putString(String.valueOf(lng), no);
        preferences.apply();
    }

    public static int getSignOutAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Address, Context.MODE_PRIVATE);
        return preferences.getInt(String.valueOf(lng), 0);
    }

    public static void endSession(Activity activity) {
        Preferences.setLongitude(activity, "");
        Preferences.setLongitude(activity, "");
        Preferences.setName(activity, "");


    }
}
