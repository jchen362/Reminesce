package com.example.reminisce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NumberGuesser extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_guesser);
        FirebaseApp.initializeApp(this);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        View hamburgerIcon = findViewById(R.id.hamburgerIcon);
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Navigation_drawer, new Navigation())
                            .commit();

                }
            }
        });

        TextView guess_button = findViewById(R.id.confirm_guess);
        guess_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guess();
            }
        });

        TextView money = findViewById(R.id.money_text);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String username_text = prefs.getString("username", "User");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(username_text);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Object data = dataSnapshot.child("money").getValue();
                    if (data != null) {
                        money.setText(data.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
            }
        });
        NumberPicker picker = findViewById(R.id.number_picker);
        picker.setMinValue(1);
        picker.setMaxValue(20);
    }

    private void Guess() {
        //get random number
        Random rand = new Random();
        int randomNumber = rand.nextInt(21);

        //get user guess
        NumberPicker guessTextBox = findViewById(R.id.number_picker);
        int guess = guessTextBox.getValue();
        int cmp = Math.abs(guess - randomNumber);

        //build alert
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.guess, null);
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        String wonMsg;
        String awayMsg = "You were " + cmp + " away!";
        String guessMsg = "Your Guess: " + guess;
        String numberMsg = "Number: " + randomNumber;


        if (cmp == 20) {
            userRef.child("bonus").setValue(0);
            wonMsg = "You won no coins!";
            awayMsg = "Tough luck! " + awayMsg;

        } else if (cmp > 10) {
            //addHistory(guess, randomNumber);
            wonMsg = "You won no coins!";
        } else if (cmp > 5) {
            //pretty close
            userGain(10);
            wonMsg = "You won 10 coins!";
        } else if (cmp >= 1) {
            userGain(25);
            wonMsg = "You won 25 coins!";
            awayMsg = "So close! " + awayMsg;
        } else {
            userGain(50);
            wonMsg = "You won 50 coins!";
            awayMsg = "Amazing! You were dead on!";
            if (guess <= 5 || guess >= 15) {
                userRef.child("bonus").setValue(5);
                Toast.makeText(getApplicationContext(), "You picked a bold number! You are feeling lucky on your next few spins", Toast.LENGTH_SHORT).show();
            }
        }
        alertDialog.show();
        ((TextView)alertDialog.findViewById(R.id.num_coins_text)).setText(wonMsg);
        ((TextView)alertDialog.findViewById(R.id.num_away)).setText(awayMsg);
        ((TextView)alertDialog.findViewById(R.id.guess_display)).setText(guessMsg);
        ((TextView)alertDialog.findViewById(R.id.number_display)).setText(numberMsg);

        //built the x button to exit out
        View x_button = alertDialog.findViewById(R.id.x_button);
        if (x_button != null) {
            x_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }

    }
    void userGain(int amt) {
        //find old amount of money
        TextView money = findViewById(R.id.money_text);
        String money_text = money.getText().toString();
        //find new money number
        int money_number = Math.max(Integer.parseInt(money_text) + amt, 0);

        userRef.child("money").setValue(money_number);
        money.setText(String.format(Integer.toString(money_number)));
    }
}
