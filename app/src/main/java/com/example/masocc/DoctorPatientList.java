package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientList extends AppCompatActivity {

    protected BottomNavigationView navView;
    protected Intent myIntent1, myIntent2,myIntent4;
    private FirebaseDatabase database;
    private DatabaseReference userReference, userKeyReference, exerciseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<ExerciseRecord> exerciseList;
    private List<String> userKeyList;
    private List<User> userList;
    String key;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_list);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_exercise:
                        myIntent1 = new Intent(DoctorPatientList.this, DoctorPatientList.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_assessment:
                        myIntent2 = new Intent(DoctorPatientList.this, DoctorPatientAssList.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(DoctorPatientList.this, DoctorProfile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_exercise);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        key = sharedPreferences.getString("key", null);
        database = FirebaseDatabase.getInstance();
        userKeyReference = database.getReference().child("usercollection").child(key);
        userReference = database.getReference().child("users");
        exerciseReference = database.getReference().child("userExercise");
        userKeyList = new ArrayList<>();
        userList = new ArrayList<>();
        exerciseList = new ArrayList<>();

        userKeyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userKeyList.clear();
                for(DataSnapshot dss : dataSnapshot.getChildren()){
                    userKeyList.add(dss.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot dss : dataSnapshot.getChildren()){
                    for(String str : userKeyList){
                        if(str.matches(dss.getKey())){
                            userList.add(dss.getValue(User.class));
                        }
                    }
                }
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
//        mAdapter = new DoctorPatientListAdapter(this, viewList);
        mAdapter = new DoctorPatientListAdapter(this, userList, userKeyList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
//    }
    }
}
