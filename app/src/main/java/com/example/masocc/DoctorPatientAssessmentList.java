package com.example.masocc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientAssessmentList extends AppCompatActivity {

    protected BottomNavigationView navView;
    protected Intent myIntent1, myIntent2,myIntent4;
    private FirebaseDatabase database;
    private DatabaseReference userReference, userKeyReference, exerciseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    //private List<DoctorView> viewList;
    private List<ExerciseRecord> exerciseList;
    private List<String> exerciseKeyList;
    //private List<ExerciseRecord> exerciseList;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_exercise_list);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_exercise:
                        myIntent1 = new Intent(DoctorPatientAssessmentList.this, DoctorPatientList.class);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_assessment:
                        myIntent2 = new Intent(DoctorPatientAssessmentList.this, DoctorPatientList.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(DoctorPatientAssessmentList.this, DoctorProfile.class);
                        startActivity(myIntent4);
                        return true;
                }
                return false;
            }
        };
        navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().findItem(R.id.navigation_profile);
        item.setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        key = intent.getStringExtra("userKey");
        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("userExercise").child(key);
        exerciseList = new ArrayList<>();
        exerciseKeyList = new ArrayList<>();

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseList.clear();
                exerciseKeyList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    ExerciseRecord u = userSnapShot.getValue(ExerciseRecord.class);
                    exerciseList.add(u);
                    exerciseKeyList.add(userSnapShot.getKey());
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
        mAdapter = new DoctorPatientExerciseListAdapter(this, exerciseList, exerciseKeyList, key);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
//    }
    }
}
