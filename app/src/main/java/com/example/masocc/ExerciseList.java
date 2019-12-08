package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ExerciseList extends AppCompatActivity {

    private Button btnExercise, btnRandom;
    private TextView tvTitle;
    private int boundary;
    private String type;
    private ArrayList<String> exerciseList = new ArrayList<>();
    private ArrayList<String> exerciseTimeList = new ArrayList<>();
    //exercise level one
    private ArrayList<String> exerciseList1 = new ArrayList<>(Arrays.asList("Shoulder Shrug", "Seated Ladder Climb", "Seated Russian Twist", "Sit to Stand",
            "Seated Bent over Row", "Toe Lift", "Wall Push Up", "Oblique Squeeze"));
    private ArrayList<String> exerciseTimeList1 = new ArrayList<>(Arrays.asList("20s","20s","20s","20s","20s","20s","20s","20s"));
    //exercise level two
    private ArrayList<String> exerciseList2 = new ArrayList<>(Arrays.asList("Seated Bicycle Crunch","Seated Butterfly","Lateral Leg Raise", "Squat with Rotational Press",
            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"));
    private ArrayList<String> exerciseTimeList2 = new ArrayList<>(Arrays.asList("30s","30s","30s","30s","30s","30s","30s"));
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_list);

        tvTitle = findViewById(R.id.textview_title);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        type = sharedPreferences.getString("type",null);
        if(type.contentEquals("exercise level one")){
            exerciseList = exerciseList1;
            exerciseTimeList = exerciseTimeList1;
            boundary = 8;
        }
        else{
            exerciseList = exerciseList2;
            exerciseTimeList = exerciseTimeList2;
            boundary = 7;
            tvTitle.setText("Exercise Level 2");
        }

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new ExerciseListAdapter(this, exerciseList, exerciseTimeList);
        recyclerView.setAdapter(mAdapter);

        btnExercise = (Button)findViewById(R.id.button_start);
        btnExercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("list",exerciseList.toString());
                editor.putString("timelist",exerciseTimeList.toString());
                editor.putString("videoType", type);
                editor.apply();
                startActivity(new Intent(ExerciseList.this, Exercise.class));
            }
        });

        btnRandom = (Button)findViewById(R.id.button_random);
        btnRandom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Random random = new Random();
                ArrayList<String> temp1 = new ArrayList<>();
                ArrayList<String> temp2 = new ArrayList<>();
                ArrayList<Integer> temp = new ArrayList<>();
                int i;

                while(true){
                    i = random.nextInt(boundary);
                    if(temp.size() == boundary){
                        break;
                    }
                    if(!temp.contains(i)) {
                        temp1.add(exerciseList.get(i));
                        temp2.add(exerciseTimeList.get(i));
                        temp.add(i);
                    }
                }
                exerciseList.clear();
                exerciseList.addAll(temp1);
                exerciseTimeList.clear();
                exerciseTimeList.addAll(temp2);

                mAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
