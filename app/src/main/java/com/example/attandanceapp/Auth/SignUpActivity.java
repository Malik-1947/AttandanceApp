package com.example.attandanceapp.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attandanceapp.Database.Database;
import com.example.attandanceapp.MainActivity;
import com.example.attandanceapp.R;
import com.example.attandanceapp.User;
import com.example.attandanceapp.Utils.Preferences;
import com.example.attandanceapp.Utils.Respone;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "TAg";
    TextInputEditText name_edt, password_edt, email_edt;
    TextView signup;
    TextView createAcc;


    FusedLocationProviderClient fusedLocationProviderClient;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_edt = findViewById(R.id.name_signup);
        password_edt = findViewById(R.id.password_signup);
        email_edt = findViewById(R.id.email_signup);
        signup = findViewById(R.id.signup_btn);
        createAcc = findViewById(R.id.create_account);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    signup();
                }
            }
        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private boolean validate() {
        if ("".equals(name_edt.getText().toString()) && name_edt.getText().toString().isEmpty()) {
            name_edt.setError("Full name_edt Required");
            name_edt.requestFocus();
            return false;
        } else if ("".equals(email_edt.getText().toString()) && email_edt.getText().toString().isEmpty()) {
            email_edt.setError("email_edt Required");
            email_edt.requestFocus();
            return false;
        } else if ("".equals(password_edt.getText().toString()) && password_edt.getText().toString().isEmpty()) {
            password_edt.setError("password_edt Required");
            password_edt.requestFocus();
            return false;
        }
        return true;
    }

    private void signup() {
        String email = email_edt.getText().toString();
        String name = name_edt.getText().toString().trim();
        String password = password_edt.getText().toString();


        Database.signUp(SignUpActivity.this, email, password, new Respone() {
            @Override
            public void onResponse(Object... params) {
                user.setName(name);
                user.setEmail(email);
                Preferences.setName(SignUpActivity.this, name);
                Preferences.setEmail(SignUpActivity.this, email);
                Database.setUser(SignUpActivity.this, user);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                Toast.makeText(SignUpActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String error) {

            }
        });

    }


}