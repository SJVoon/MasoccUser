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
    private String username,icNumber,email,handphoneNumber, fullName;

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
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(EditProfile.this, Assessment.class);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_notifications:
                        myIntent3 = new Intent(EditProfile.this, Notification.class);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(EditProfile.this, Profile.class);
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
                if(validate()) {
                    editProfile();
                }

            }
        });
    }

    private void setupUI(){
        btnEditProfile = findViewById(R.id.button_edit_profile);
        etUsername = findViewById(R.id.edit_text_username);
        etFullName = findViewById(R.id.edit_text_fullname);
        etICNumber = findViewById(R.id.edit_text_ic_number);
        etEmail = findViewById(R.id.edit_text_email);
        etHandphoneNumber = findViewById(R.id.edit_text_handphone_number);
    }

    private void define(){
        username = etUsername.getText().toString().trim();
        icNumber = etICNumber.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        handphoneNumber = etHandphoneNumber.getText().toString().trim();
        fullName = etFullName.getText().toString().trim();
    }

    private void setData(){
        etUsername.setText(currentUser.getInstance().getUser().getUsername());
        etFullName.setText(currentUser.getInstance().getUser().getFullName());
        etEmail.setText(currentUser.getInstance().getUser().getEmail());
        etICNumber.setText(currentUser.getInstance().getUser().getIcNumber());
        etHandphoneNumber.setText(currentUser.getInstance().getUser().getHandphoneNumber());
    }

    private boolean validate(){
        boolean check = true;

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (TextUtils.isEmpty(icNumber) || icNumber.length() != 12) {
            Toast.makeText(getApplicationContext(), "Enter valid IC number!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (TextUtils.isEmpty(handphoneNumber) && !handphoneNumber.matches("^[0-9]*$")) {
            Toast.makeText(getApplicationContext(), "Enter valid handphone number!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        return check;
    }

    private void editProfile(){
        if(currentUser.getInstance().getUser().getUsername() != username){
            mDatabase.child(""+currentUser.getInstance().getUser().getUsername()).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mDatabase = mDatabase.child(""+username);
                            Map<String, String> userData = new HashMap<>();

                            User user = new User(fullName, email, icNumber, handphoneNumber, currentUser.getInstance().getUser().getPassword());
                            user.setUsername(username);

                            userData.put("username",user.getUsername());
                            userData.put("fullname",user.getFullName());
                            userData.put("email",user.getEmail());
                            userData.put("icNumber", user.getIcNumber());
                            userData.put("handphoneNumber",user.getHandphoneNumber());
                            userData.put("password",user.getPassword());

                            mDatabase.setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditProfile.this, "Edit profile successful!", Toast.LENGTH_LONG).show();
                                            currentUser.getInstance().setUser(user);
                                            SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("fullName", currentUser.getInstance().getUser().getFullName());
                                            editor.putString("username", currentUser.getInstance().getUser().getUsername());
                                            editor.putString("password", currentUser.getInstance().getUser().getPassword());
                                            editor.putString("email",currentUser.getInstance().getUser().getEmail());
                                            editor.putString("icNumber",currentUser.getInstance().getUser().getIcNumber());
                                            editor.putString("handphoneNumber",currentUser.getInstance().getUser().getHandphoneNumber());
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
                    )
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Edit profile fail! Try again later", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }

        else{
            mDatabase = mDatabase.child(""+username);
            Map<String, String> userData = new HashMap<>();

            User user = new User(fullName, email, icNumber, handphoneNumber, currentUser.getInstance().getUser().getPassword());
            user.setUsername(username);

            userData.put("username",user.getUsername());
            userData.put("fullname",user.getFullName());
            userData.put("email",user.getEmail());
            userData.put("icNumber", user.getIcNumber());
            userData.put("handphoneNumber",user.getHandphoneNumber());
            userData.put("password",user.getPassword());
            mDatabase.setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfile.this, "Edit profile successful!", Toast.LENGTH_LONG).show();
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
}
