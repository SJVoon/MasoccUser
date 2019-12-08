package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    private List<AssessmentRecord> exerciseRecordList;
    private TextView tvDate, tvQ1, tvQ2;
    private EditText etComment;
    private Button btnSave;
    String userKey, exerciseKey;
    Intent intent;
    AssessmentRecord record;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_assessment_review);

        exerciseRecordList = new ArrayList<>();
        context = this;
        intent = getIntent();
        userKey = intent.getStringExtra("userKey");
        exerciseKey = intent.getStringExtra("exerciseKey");

        database = FirebaseDatabase.getInstance();
        exerciseReference = database.getReference().child("weeklyAssessment").child(userKey).child(exerciseKey);

        exerciseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                record = dataSnapshot.getValue(AssessmentRecord.class);
                setData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        record.setComment(etComment.getText().toString().trim());
                        exerciseReference.setValue(record).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DoctorPatientAssessmentReview.this, "Comment complete!", Toast.LENGTH_LONG).show();
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
        tvDate = findViewById(R.id.date);
        tvQ1 = findViewById(R.id.q1);
        etComment = findViewById(R.id.comment);
        tvQ2 = findViewById(R.id.q2);
        btnSave = findViewById(R.id.save);
    }

    private void setData() {
        tvDate.setText(record.getDate());
        tvQ1.setText(record.getFeeling().get(0));
        tvQ2.setText(record.getFeeling().get(1));
        etComment.setText(record.getComment());
    }

}
