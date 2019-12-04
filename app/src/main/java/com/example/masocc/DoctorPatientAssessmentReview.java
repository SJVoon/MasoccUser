package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientAssessmentReview extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference exerciseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<ExerciseRecord> exerciseRecordList;
    private TextView tvType, tvDate, tvFeeling, tvData;
    private ImageView ivImage;
    private EditText etComment;
    String userKey, exerciseKey;
    Intent intent;
    ExerciseRecord record;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_exercise_review);

        exerciseRecordList = new ArrayList<>();
        context = this;
        intent = getIntent();
        userKey = intent.getStringExtra("userKey");
        exerciseKey = intent.getStringExtra("exerciseKey");

        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("userExercise").child(userKey);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("userExercise").child(userKey);

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseRecordList.clear();
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    ExerciseRecord u = userSnapShot.getValue(ExerciseRecord.class);
                    if(exerciseKey.equals(userSnapShot.getKey())){
                        record = u;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();
        setData(record);

        storageReference.getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri.toString()).into(ivImage);
                    }
                }
        );
    }

    private void setupUI() {
        tvType = findViewById(R.id.type);
        tvDate = findViewById(R.id.date);
        tvFeeling = findViewById(R.id.feeling);
        etComment = findViewById(R.id.comment);
        tvData = findViewById(R.id.data);
        ivImage = findViewById(R.id.image);
    }

    private void setData(ExerciseRecord er) {
        tvType.setText(er.getType());
        tvDate.setText(er.getDate());
        tvFeeling.setText(er.getFeeling());
        tvData.setText(er.getData());
    }

}
