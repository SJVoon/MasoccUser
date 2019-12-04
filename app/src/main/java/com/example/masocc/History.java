package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private TextView mTextMessage;
    protected BottomNavigationView navView;
    Intent myIntent1, myIntent2, myIntent4;
    private FirebaseDatabase database;
    private DatabaseReference exerciseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<ExerciseRecord> exerciseList;
    String key;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(History.this,MainActivity.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(History.this, Assessment.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_history:
                        Toast.makeText(History.this, "You are on History page now", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(History.this, Profile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_history);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key", null);
        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("userExercise").child(key);
        exerciseList = new ArrayList<>();

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    ExerciseRecord u = userSnapShot.getValue(ExerciseRecord.class);
                    exerciseList.add(u);
                }
                //update();
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new HistoryAdapter(this, exerciseList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
