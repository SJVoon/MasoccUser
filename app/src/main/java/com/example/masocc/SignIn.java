package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity{

    Button btnSignIn;
    EditText etUsername, etPassword;
    TextView tvForgetPassword, tvRegister;
    DatabaseReference databaseReference;
    List<User> userList;
    String errorMessage;
    SharedPreferences sharedPreferences;
    int autoSave;
    Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        setupUI();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        System.out.println(databaseReference);
        userList = new ArrayList<>();
        currentUser.init_Instance();

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt("key", 0);

        myIntent = new Intent(this, MainActivity.class);

        if(j > 0){
            User u = new User(
                    sharedPreferences.getString("username",null),
                    sharedPreferences.getString("fullName",null),
                    sharedPreferences.getString("email",null),
                    sharedPreferences.getString("icNumber",null),
                    sharedPreferences.getString("handphoneNumber",null),
                    sharedPreferences.getString("password",null));
            currentUser.getInstance().setUser(u);
            startActivity(myIntent);
            finish();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
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

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String thisUsername = etUsername.getText().toString();
                String thisPassword = etPassword.getText().toString();
                if(validate(thisUsername,thisPassword)) {
                    myIntent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
                else{
                    Toast.makeText(SignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        final Intent myIntent2 = new Intent(this, Registration.class);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(myIntent2);
            }
        });

        final Intent myIntent3 = new Intent(this, ForgetPassword.class);
        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                startActivity(myIntent3);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean validate(String un, String pw){
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getUsername().matches(un)){
                if(userList.get(i).getPassword().matches(pw)){
                    //save current user
                    currentUser.getInstance().setUser(userList.get(i));

                    //sharedPreference authenticate
                    if(sharedPreferences.getInt("key", 0) == 0) {
                        autoSave = 1;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", autoSave);
                        editor.putString("fullName", currentUser.getInstance().getUser().getFullName());
                        editor.putString("username", currentUser.getInstance().getUser().getUsername());
                        editor.putString("password", currentUser.getInstance().getUser().getPassword());
                        editor.putString("email",currentUser.getInstance().getUser().getEmail());
                        editor.putString("icNumber",currentUser.getInstance().getUser().getIcNumber());
                        editor.putString("handphoneNumber",currentUser.getInstance().getUser().getHandphoneNumber());
                        editor.apply();
                    }
                    return true;
                }
                errorMessage = "Password incorrect";
            }
            System.out.println(userList.get(i).getUsername() + " xx " + un);
            errorMessage = "This user is not exist";
        }
        errorMessage = "Fail to login";
        return false;
    }

    private void setupUI(){
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.button_signin);
        tvForgetPassword = findViewById(R.id.forget_pw);
        tvRegister = findViewById(R.id.register);
    }

}
