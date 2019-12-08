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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Assessment extends AppCompatActivity {

    private TextView mTextMessage;
    protected BottomNavigationView navView;
    Intent myIntent1, myIntent3, myIntent4;

    private Button btnSubmit;
    private List<RadioButton> rg1, rg2;
    private RadioButton rg1b1, rg1b2, rg1b3, rg1b4, rg1b5, rg2b1, rg2b2, rg2b3, rg2b4, rg2b5;
    private String date;
    private List<String> feeling;
    private List<AssessmentRecord> assessmentRecordList;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    SharedPreferences sharedPreferences;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(Assessment.this, MainActivity.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        Toast.makeText(Assessment.this, "You are on assessment page now", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_history:
                        myIntent3 = new Intent(Assessment.this, History.class);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(Assessment.this, Profile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };

        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_dashboard);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        assessmentRecordList = new ArrayList<>();

        sharedPreferences = sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key",null);

        //date,type
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(c);

        //database
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("weeklyAssessment");

//        mDatabase = mDatabase.child("" +User.getInstance().getUsername());
//        mDatabase = mDatabase.child(date);
//        mDatabase.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        //setupUI
        btnSubmit = (Button)findViewById(R.id.button_submit);
        rg1 = new ArrayList<>();
        rg2 = new ArrayList<>();
        feeling = new ArrayList<>();
        rg1.add(rg1b1 = (RadioButton)findViewById(R.id.rg1b1));
        rg1.add(rg1b2 = (RadioButton)findViewById(R.id.rg1b2));
        rg1.add(rg1b3 = (RadioButton)findViewById(R.id.rg1b3));
        rg1.add(rg1b4 = (RadioButton)findViewById(R.id.rg1b4));
        rg1.add(rg1b5 = (RadioButton)findViewById(R.id.rg1b5));
        rg2.add(rg2b1 = (RadioButton)findViewById(R.id.rg2b1));
        rg2.add(rg2b2 = (RadioButton)findViewById(R.id.rg2b2));
        rg2.add(rg2b3 = (RadioButton)findViewById(R.id.rg2b3));
        rg2.add(rg2b4 = (RadioButton)findViewById(R.id.rg2b4));
        rg2.add(rg2b5 = (RadioButton)findViewById(R.id.rg2b5));


        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void save(){
        for(int i = 0; i < rg1.size(); i++){
            if(rg1.get(i).isChecked())
                feeling.add(rg1.get(i).getText().toString().trim());
        }
        for(int i = 0; i < rg2.size(); i++){
            if(rg2.get(i).isChecked())
                feeling.add(rg2.get(i).getText().toString().trim());
        }
        AssessmentRecord ar = new AssessmentRecord(date,feeling);
        mDatabase.child(key).push().setValue(ar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Assessment.this, "Add assessment record successful!", Toast.LENGTH_LONG).show();
                        finish();
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Assessment.this, "Add assessment record fail! Try again later.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
