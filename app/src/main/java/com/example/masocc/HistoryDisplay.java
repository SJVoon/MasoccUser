package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryDisplay extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference exerciseReference;
    private List<ExerciseRecord> exerciseRecordList;
    private TextView tvType, tvDate, tvFeeling, tvComment, tvData;
    SharedPreferences sharedPreferences;
    int tempPosition;
    String key, tempDate;
    Intent intent;
    ExerciseRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_display);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key", null);
        exerciseRecordList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("userExercise").child(key);
        intent = getIntent();
        tempPosition = intent.getIntExtra("id",0);
        tempDate = intent.getStringExtra("date");

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseRecordList.clear();
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    ExerciseRecord u = userSnapShot.getValue(ExerciseRecord.class);
                    exerciseRecordList.add(u);
                }
                if(exerciseRecordList.get(tempPosition).getDate().equals(tempDate)){
                    record = exerciseRecordList.get(tempPosition);
                    setData(record);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();


    }

    private void setupUI() {
        tvType = findViewById(R.id.type);
        tvDate = findViewById(R.id.date);
        tvFeeling = findViewById(R.id.feeling);
        tvComment = findViewById(R.id.comment);
        tvData = findViewById(R.id.data);
    }

    private void setData(ExerciseRecord er) {
        tvType.setText(er.getType());
        tvDate.setText(er.getDate());
        tvFeeling.setText(er.getFeeling());
        tvComment.setText(er.getComment());
        tvData.setText(er.getData());
    }

}
