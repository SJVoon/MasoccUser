package com.example.masocc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Notification extends AppCompatActivity {
    private TextView mTextMessage;
    protected BottomNavigationView navView;
    Intent myIntent1, myIntent2, myIntent4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(Notification.this,MainActivity.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(Notification.this, Assessment.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_notifications:
                        Toast.makeText(Notification.this, "You are on Notification page now", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(Notification.this, Profile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_notifications);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
