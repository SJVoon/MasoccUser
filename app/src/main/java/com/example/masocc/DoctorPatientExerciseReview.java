package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DoctorPatientExerciseReview extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference exerciseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<ExerciseRecord> exerciseRecordList;
    private TextView tvType, tvDate, tvFeeling, tvData, tvImage;
    private ImageView ivImage;
    private EditText etComment;
    private Button btnSave;
    String userKey, exerciseKey;
    Intent intent;
    ExerciseRecord record;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_exercise_review);

        //exerciseRecordList = new ArrayList<>();
        context = this;
        intent = getIntent();
        userKey = intent.getStringExtra("userKey");
        exerciseKey = intent.getStringExtra("exerciseKey");
        record = new ExerciseRecord();
        setupUI();

        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("userExercise").child(userKey).child(exerciseKey);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("userExercise").child(userKey);

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                record = dataSnapshot.getValue(ExerciseRecord.class);
                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        setupUI();
//        setData(record);



        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        record.setComment(etComment.getText().toString().trim());
                        exerciseReference.setValue(record).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DoctorPatientExerciseReview.this, "Comment complete!", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        finish();
                                    }
                                }
                        );

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
        btnSave = findViewById(R.id.save);
        tvImage = findViewById(R.id.image_text);
    }

    private void setData() {
        tvType.setText(record.getType());
        tvDate.setText(record.getDate());
        tvFeeling.setText(record.getFeeling());
        tvData.setText(record.getData());
        etComment.setText(record.getComment());

        storageReference.child(record.getUri()).getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        tvImage.setText("Image captured after exercise");
                        tvImage.setVisibility(View.VISIBLE);
                        ivImage.setVisibility(View.VISIBLE);
                        Glide.with(context).load(uri.toString()).into(ivImage);
                    }
                }
        );
    }

}
