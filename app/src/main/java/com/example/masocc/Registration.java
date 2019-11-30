package com.example.masocc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private TextView text;
    private EditText etUsername, etICNumber, etEmail, etHandphoneNumber, etPassword, etConfirmPassword, etFullName;
    private Button btnRegister;
    private String username,icNumber,email,handphoneNumber, password,confirmPassword, fullName;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("users");
        userList = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    User u = userSnapShot.getValue(User.class);
                    userList.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();

        final Intent myIntent = new Intent(this, SignIn.class);
        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(myIntent);
            }
        });

        final Intent myIntent2 = new Intent(this, SignIn.class);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                define();
                if(validate()) {

                    mDatabase = mDatabase.child(""+username);
                    User.getInstance().setUser(username, fullName, email, icNumber, handphoneNumber, password, "");

                    mDatabase.setValue(User.getInstance())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Registration.this, "Registration successful! Sign in now.", Toast.LENGTH_LONG).show();
                                        startActivity(myIntent2);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Registration.this, "Registration Fail! Sign in now.", Toast.LENGTH_LONG).show();
                                        startActivity(myIntent2);
                                        finish();
                                    }
                                });

                    }

            }
        });
    }

    private void setupUI(){
        etUsername = findViewById(R.id.username);
        etICNumber = findViewById(R.id.ic_number);
        etEmail = findViewById(R.id.email);
        etHandphoneNumber = findViewById(R.id.handphone_number);
        etPassword = findViewById(R.id.password);
        etConfirmPassword = findViewById(R.id.confirm_password);
        etFullName = findViewById(R.id.full_name);
        btnRegister = findViewById(R.id.button_register);
        text = findViewById(R.id.sign_in);
    }

    private void define(){
        username = etUsername.getText().toString().trim();
        icNumber = etICNumber.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        handphoneNumber = etHandphoneNumber.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        fullName = etFullName.getText().toString().trim();
    }

    private boolean validate(){
        boolean check = true;

        for(int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().matches(username)) {
                Toast.makeText(getApplicationContext(), "Username is taken!", Toast.LENGTH_SHORT).show();
                return check = false;
            }
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getApplicationContext(), "Enter full name!", Toast.LENGTH_SHORT).show();
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

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (!password.matches(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Comfirm password is not same as password, reenter!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        return check;
    }
}
