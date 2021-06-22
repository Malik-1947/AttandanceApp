package com.example.attandanceapp.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attandanceapp.Database.Database;
import com.example.attandanceapp.MainActivity;
import com.example.attandanceapp.R;
import com.example.attandanceapp.Utils.Respone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText password_edt, email_edt;
    TextView signup;
    TextView signin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password_edt = findViewById(R.id.password_signin);
        email_edt = findViewById(R.id.email_signin);
        signin = findViewById(R.id.signin_btn);
        signup = findViewById(R.id.already_account);
        auth = FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                Toast.makeText(LoginActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validate() {
        if ("".equals(email_edt.getText().toString()) && email_edt.getText().toString().isEmpty()) {
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

    private void signIn() {
        String email = email_edt.getText().toString();
        String password = password_edt.getText().toString();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + email);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + password);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            }
        });
       /* Database.signIn(LoginActivity.this, email, password, new Respone() {
            @Override
            public void onResponse(Object... params) {
                //if ((boolean) params[0]) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                //}
            }

            @Override
            public void onError(String error) {

            }
        });*/

    }
}