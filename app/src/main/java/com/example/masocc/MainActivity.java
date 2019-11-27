package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnStartExercise, btnStartTest;
    protected BottomNavigationView navView;
    Intent myIntent1, myIntent2, myIntent3,myIntent4;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        btnStartExercise = findViewById(R.id.button_start_exercise);
        btnStartExercise.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("type", "exercise level one");
                editor.apply();
                startActivity(new Intent(MainActivity.this, ExerciseList.class));
            }
        });

        btnStartTest = findViewById(R.id.button_start_test);
        btnStartTest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("type", "exercise level two");
                editor.apply();
                startActivity(new Intent(MainActivity.this, ExerciseList.class));
            }
        });

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Toast.makeText(MainActivity.this, "You are on Home page now", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(MainActivity.this, Assessment.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_notifications:
                        myIntent3 = new Intent(MainActivity.this, Notification.class);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(MainActivity.this, Profile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_home);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
