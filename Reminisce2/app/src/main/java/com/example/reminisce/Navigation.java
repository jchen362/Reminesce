package com.example.reminisce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class Navigation extends Fragment {
    View myview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.navigation, container, false);

        SharedPreferences prefs = requireActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String username_text = prefs.getString("username", "User");
        TextView username = myview.findViewById(R.id.username_nav);
        username.setText(username_text);

        View navHamburgerIcon = myview.findViewById(R.id.hambergButtonCloser);
        navHamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.close();
                }
            }
        });
        //setup claw button
        View clawScreen = myview.findViewById(R.id.claw_nav);
        clawScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                Activity thisActivity = requireActivity();
                if (thisActivity.getClass() != MainActivity.class) {
                    Intent main = new Intent(thisActivity, MainActivity.class);
                    startActivity(main);
                    thisActivity.finish();
                }
            }
        });

        //setup claw button
        View tutorialScreen = myview.findViewById(R.id.tutorial_nav);
        tutorialScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                Activity thisActivity = requireActivity();
                if (thisActivity.getClass() != Tutorial.class) {
                    Intent main = new Intent(thisActivity, Tutorial.class);
                    startActivity(main);
                    thisActivity.finish();
                }
            }
        });

        View guesserScreen = myview.findViewById(R.id.number_guesser_nav);
        guesserScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                Activity thisActivity = requireActivity();
                if (thisActivity.getClass() != NumberGuesser.class) {
                    Intent main = new Intent(thisActivity, NumberGuesser.class);
                    startActivity(main);
                    thisActivity.finish();
                }
            }
        });

        View collectionsScreen = myview.findViewById(R.id.collection_nav);
        collectionsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle drawer open/close
                Activity thisActivity = requireActivity();
                if (thisActivity.getClass() != Collections.class) {
                    Intent main = new Intent(thisActivity, Collections.class);
                    startActivity(main);
                    thisActivity.finish();
                }
            }
        });

        //setup logout button
        View logout = myview.findViewById(R.id.logout_nav);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finish();
            }
        });
        return myview;
    }
}