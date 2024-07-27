package com.example.reminisce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Tutorial extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        Activity thisActivity = this;

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide(); // Hide the ActionBar
        }
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
        Button btn = findViewById(R.id.return_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(thisActivity, MainActivity.class);
                startActivity(main);
                finish();
            }
        });
    }

    public void goToClawScreen(View view) {

    }
}