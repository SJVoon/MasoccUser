package com.example.masocc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.List;

public class SignIn extends AppCompatActivity{

    private Button btnSignIn;
    private EditText etUsername, etPassword;
    private TextView tvForgetPassword, tvRegister;
    private CheckBox checkBox;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private List<User> userList;
    private List<String> userKeyList;
    private String errorMessage, tempKey;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference().child("users");

        setupUI();
        userKeyList = new ArrayList<>();
        userList = new ArrayList<>();
        final Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt("login", 0);

        if(j > 0){
                User.getInstance().setUser(
                        sharedPreferences.getString("username", null),
                        sharedPreferences.getString("fullName", null),
                        sharedPreferences.getString("email", null),
                        sharedPreferences.getString("icNumber", null),
                        sharedPreferences.getString("handphoneNumber", null),
                        sharedPreferences.getString("password", null));
                startActivity(myIntent);
                finish();
        }

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                userKeyList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    User u = userSnapShot.getValue(User.class);
                    userList.add(u);
                    userKeyList.add(userSnapShot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String thisUsername = etUsername.getText().toString();
                String thisPassword = etPassword.getText().toString();
                    if(validateUser(thisUsername,thisPassword)) {
                        //sharedPreference authenticate
                        if(sharedPreferences.getInt("login", 0) == 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("login", 1);
                            editor.putString("key",tempKey);
                            editor.putString("fullName", User.getInstance().getFullName());
                            editor.putString("username", User.getInstance().getUsername());
                            editor.putString("password", User.getInstance().getPassword());
                            editor.putString("email",User.getInstance().getEmail());
                            editor.putString("icNumber",User.getInstance().getIcNumber());
                            editor.putString("handphoneNumber",User.getInstance().getHandphoneNumber());
                            editor.apply();
                        }
                        startActivity(myIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(SignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
            }
        });

        final Intent myIntent2 = new Intent(this, Registration.class);
        myIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(myIntent2);
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //to be implemented
            }
        });
    }

    @Override
    protected void onStart() { super.onStart(); }

    private boolean validateUser(String un, String pw){
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().matches(un)){
                if(userList.get(i).getPassword().matches(MD5Hash.encrypt(pw))){
                    User.getInstance().setUser(userList.get(i));
                    tempKey = userKeyList.get(i);
                    return true;
                }
                errorMessage = "Password incorrect";
            }
            errorMessage = "This user is not exist";
        }
        errorMessage = "Fail to login";
        return false;
    }

    private void setupUI(){
        checkBox = findViewById(R.id.checkbox);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.button_signin);
        tvForgetPassword = findViewById(R.id.forget_pw);
        tvRegister = findViewById(R.id.register);
    }

}
