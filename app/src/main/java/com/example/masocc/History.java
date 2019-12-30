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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class History extends AppCompatActivity {
    private TextView mTextMessage, tvBpm;
    protected BottomNavigationView navView;
    Intent myIntent1, myIntent2, myIntent4;
    private FirebaseDatabase database;
    private DatabaseReference exerciseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;
    private List<ExerciseRecord> exerciseList;
    private ImageView day1, day2, day3;
    String key;
    SharedPreferences sharedPreferences;
    ValueEventListener vel;

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
                        myIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        exerciseReference.removeEventListener(vel);
                        startActivity(myIntent1);
                        finish();
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(History.this, Assessment.class);
                        myIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        exerciseReference.removeEventListener(vel);
                        startActivity(myIntent2);
                        finish();
                        return true;
                    case R.id.navigation_history:
                        Toast.makeText(History.this, "You are on History page now", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(History.this, Profile.class);
                        myIntent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        exerciseReference.removeEventListener(vel);
                        startActivity(myIntent4);
                        finish();
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

        vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    ExerciseRecord u = userSnapShot.getValue(ExerciseRecord.class);
                    exerciseList.add(u);
                }
                //update();
                load();
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        exerciseReference.addValueEventListener(vel);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new HistoryAdapter(this, exerciseList);
        recyclerView.setAdapter(mAdapter);

        tvBpm = findViewById(R.id.bpm);
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);


    }

    @Override
    public void onPause() {
        super.onPause();
        exerciseReference.removeEventListener(vel);
        overridePendingTransition(0, 0);
    }

    public long calDif(Date firstDate){
        Date secondDate = Calendar.getInstance().getTime();
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void load(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date firstDate = null;

        ArrayList<ExerciseRecord> temp = new ArrayList<>();
        for(ExerciseRecord er : exerciseList){
            Log.d("1",Calendar.getInstance().getTime().toString());
            try {
                firstDate = sdf.parse(er.getDate());
                Log.d("2",firstDate.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("3",Long.toString(calDif(firstDate)));
            if(calDif(firstDate) < 7){
                temp.add(er);
            }
        }

        if(temp.size()>=3){
            day1.setImageResource(R.drawable.check);
            day2.setImageResource(R.drawable.check);
            day3.setImageResource(R.drawable.check);
            Toast.makeText(History.this, "Well done! You have completed the exercises! Come Back 2 days later!", Toast.LENGTH_SHORT).show();
        }else if(temp.size() == 2){
            day1.setImageResource(R.drawable.check);
            day2.setImageResource(R.drawable.check);
            Toast.makeText(History.this, "Well done! You are one exercise away to complete! Start exercise now!", Toast.LENGTH_SHORT).show();
        }else if(temp.size() == 1){
            day1.setImageResource(R.drawable.check);
            Toast.makeText(History.this, "You have completed one exercise, keep it up! Start exercise now!", Toast.LENGTH_SHORT).show();
        }

        if(temp.size() > 0) {
            double bpm = 0, tempBpm = 0;
            for (ExerciseRecord er : temp) {
                List<String> arr = er.getPulseData();
                tempBpm = 0;
                for(String s : arr){
//                    s = s.substring(0,s.length()-2);
                    tempBpm += Double.parseDouble(s);
                }
                bpm += (tempBpm/arr.size());
            }
            bpm = bpm / temp.size();
            tvBpm.setText(Integer.toString((int)bpm));
        }else{
            tvBpm.setText("BPM");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(myIntent1);
    }
}
