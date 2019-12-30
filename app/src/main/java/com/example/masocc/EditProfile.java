package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    protected BottomNavigationView navView;
    private Intent myIntent1, myIntent2, myIntent3, myIntent4;
    private Button btnEditProfile;
    private EditText etUsername, etICNumber, etEmail, etHandphoneNumber, etFullName;
    private String username, icNumber, email, handphoneNumber, fullName, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(EditProfile.this, MainActivity.class);
                        myIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_assessment:
                        myIntent2 = new Intent(EditProfile.this, Assessment.class);
                        myIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent3 = new Intent(EditProfile.this, Dashboard.class);
                        myIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(EditProfile.this, Profile.class);
                        myIntent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    User u = userSnapShot.getValue(User.class);
                    if (u.getUsername().equals(User.getInstance().getUsername())) {
                        key = userSnapShot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();
        setData();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                define();
                if (validate()) {
                    editProfile();
                }

            }
        });
    }

    private void setupUI() {
        btnEditProfile = findViewById(R.id.button_edit_profile);
        etUsername = findViewById(R.id.edit_text_username);
        etFullName = findViewById(R.id.edit_text_fullname);
        etICNumber = findViewById(R.id.edit_text_ic_number);
        etEmail = findViewById(R.id.edit_text_email);
        etHandphoneNumber = findViewById(R.id.edit_text_handphone_number);
    }

    private void define() {
        username = etUsername.getText().toString().trim();
        icNumber = etICNumber.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        handphoneNumber = etHandphoneNumber.getText().toString().trim();
        fullName = etFullName.getText().toString().trim();
    }

    private void setData() {
        etUsername.setText(User.getInstance().getUsername());
        etFullName.setText(User.getInstance().getFullName());
        etEmail.setText(User.getInstance().getEmail());
        etICNumber.setText(User.getInstance().getIcNumber());
        etHandphoneNumber.setText(User.getInstance().getHandphoneNumber());
    }

    private boolean validate() {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(icNumber) || icNumber.length() != 12) {
            Toast.makeText(getApplicationContext(), "Enter valid IC number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(handphoneNumber) && !handphoneNumber.matches("^[0-9]*$")) {
            Toast.makeText(getApplicationContext(), "Enter valid handphone number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void editProfile() {

        User.getInstance().setUser(username, fullName, email, icNumber, handphoneNumber, User.getInstance().getPassword());

        mDatabase.child(key).setValue(User.getInstance())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Edit profile successful!", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fullName", User.getInstance().getFullName());
                        editor.putString("username", User.getInstance().getUsername());
                        editor.putString("password", User.getInstance().getPassword());
                        editor.putString("email", User.getInstance().getEmail());
                        editor.putString("icNumber", User.getInstance().getIcNumber());
                        editor.putString("handphoneNumber", User.getInstance().getHandphoneNumber());
                        editor.apply();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Edit profile Fail! Try again later.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

    }
}
