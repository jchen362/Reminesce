package com.example.reminisce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Entry extends AppCompatActivity {

    private DatabaseReference usersRef;
    private int hasLoggedIn;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasLoggedIn = 0;
        setContentView(R.layout.entry);
        FirebaseApp.initializeApp(this);
        getSupportActionBar().hide(); // Hide the ActionBar

        usersRef = FirebaseDatabase.getInstance().getReference("users");


        // Initially, display the login fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Login())
                .commit();
    }

    protected void onResume() {
        super.onResume();
        if (hasLoggedIn == 1) {
            // Always start Fragment A when returning to this activity
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Login())
                    .commit();
            hasLoggedIn = 0;
        }
    }

    // Method to switch to login fragment
    public void switchToLogin(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Login())
                .commit();
    }

    public void pressLogin(View view) {
        Context context = this;
        EditText username = findViewById(R.id.username_box);
        EditText passcode = findViewById(R.id.password_box);

        String username_text = username.getText().toString();
        String password_text = passcode.getText().toString();
        if (username_text.length() < 1 || username_text.contains(" ")) {
            Toast myToast = Toast.makeText(context, "ERROR: INVALID USERNAME", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        } else if (password_text.length() < 1 || password_text.contains(" ")) {
            Toast myToast = Toast.makeText(context, "ERROR: INVALID PASSWORD", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }


        usersRef.child(username_text).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (password_text.equals(dataSnapshot.child("pwd").getValue())) {
                        Login(username_text);
                    } else {
                        Toast myToast = Toast.makeText(context, "ERROR: PASSWORDS DO NOT MATCH", Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                } else {
                    // Username not found or no matching password found
                    Toast myToast = Toast.makeText(context, "ERROR: USERNAME COULD NOT BE FOUND", Toast.LENGTH_SHORT);
                    myToast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
                Toast myToast = Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }

    private void Login(String usr) {
        hasLoggedIn = 1;
        Intent main = new Intent(this, MainActivity.class);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.storage), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", usr);
        editor.apply();
        startActivity(main);
    }

    private void Signup(String usr) {
        hasLoggedIn = 1;
        Intent main = new Intent(this, Tutorial.class);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.storage), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", usr);
        editor.apply();
        startActivity(main);
    }

    // Method to switch to sign up fragment
    public void switchToSignUp(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Signup())
                .commit();
    }

    public void pressSignup(View view) {
        Context context = this;
        EditText username = findViewById(R.id.username_textbox);
        EditText passcode = findViewById(R.id.password_textbox);
        EditText confirmation = findViewById(R.id.confirm_password_box);

        String username_text = username.getText().toString();
        String password_text = passcode.getText().toString();
        String confirmation_text = confirmation.getText().toString();
        if (username_text.length() < 1 || username_text.contains(" ")) { // if username is invalid
            Toast myToast = Toast.makeText(context, "ERROR: INVALID USERNAME", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        } else if (password_text.length() < 1 || password_text.contains(" ")) { // if password is invalid
            Toast myToast = Toast.makeText(context, "ERROR: INVALID PASSWORD", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        } else if (!(password_text.equals(confirmation_text))) {
            Toast myToast = Toast.makeText(context, "ERROR: PASSWORDS DO NOT MATCH", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }
        usersRef.child(username_text).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast myToast = Toast.makeText(context, "ERROR: USER ALREADY EXISTS", Toast.LENGTH_SHORT);
                    myToast.show();
                } else {
                    DatabaseReference user = usersRef.child(username_text);
                    user.child("pwd").setValue(password_text);
                    user.child("money").setValue(0);
                    user.child("bonus").setValue(0);
                    user.child("collected").setValue("");
                    Signup(username_text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Toast myToast = Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }
}