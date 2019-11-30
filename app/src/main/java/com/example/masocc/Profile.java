package com.example.masocc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    protected BottomNavigationView navView;
    Intent myIntent1, myIntent2, myIntent3, myIntent4;
    Button btnChangePassword, btnEditProfile, btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(Profile.this, MainActivity.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(Profile.this, Assessment.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_notifications:
                        myIntent3 = new Intent(Profile.this, Notification.class);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        Toast.makeText(Profile.this, "You are on Profile page now", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_profile);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnChangePassword = findViewById(R.id.button_change_password);
        btnEditProfile = findViewById(R.id.button_edit_profile);
        btnLogout = findViewById(R.id.button_logout);

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent4 = new Intent(Profile.this, ChangePassword.class);
                startActivity(myIntent4);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Profile.this, EditProfile.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void logoutDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", 0);
                        editor.apply();
                        myIntent4 = new Intent(Profile.this, SignIn.class);
                        startActivity(myIntent4);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
