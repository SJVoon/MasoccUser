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
    private DatabaseReference userReference, doctorReference, tempReference;
    private List<User> userList;
    private List<Doctor> doctorList;
    private List<String> userKeyList, doctorKeyList, tempKeyList;
    private String errorMessage, tempKey;
    private SharedPreferences sharedPreferences;
    private boolean changePw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference().child("users");
        doctorReference = database.getReference().child("doctors");
        tempReference = database.getReference().child("firstLogin");

        setupUI();
        userKeyList = new ArrayList<>();
        doctorKeyList = new ArrayList<>();
        userList = new ArrayList<>();
        doctorList = new ArrayList<>();
        tempKeyList = new ArrayList<>();
        final Intent myIntent = new Intent(this, MainActivity.class);
        final Intent myIntentDoc = new Intent(this, DoctorPatientList.class);
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt("login", 0);
        int k = sharedPreferences.getInt("user", 0);

        if(j > 0){
            if(k == 0) {
                User.getInstance().setUser(
                        sharedPreferences.getString("username", null),
                        sharedPreferences.getString("fullName", null),
                        sharedPreferences.getString("email", null),
                        sharedPreferences.getString("icNumber", null),
                        sharedPreferences.getString("handphoneNumber", null),
                        sharedPreferences.getString("password", null),
                        sharedPreferences.getString("doctor", null));
                startActivity(myIntent);
                finish();
            }
            else{
                Doctor.getInstance().setDoctor(
                        sharedPreferences.getString("username", null),
                        sharedPreferences.getString("fullName", null),
                        sharedPreferences.getString("email", null),
                        sharedPreferences.getString("icNumber", null),
                        sharedPreferences.getString("handphoneNumber", null),
                        sharedPreferences.getString("password", null));
                startActivity(myIntentDoc);
                finish();
            }
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

        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                doctorKeyList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    Doctor u = userSnapShot.getValue(Doctor.class);
                    doctorList.add(u);
                    doctorKeyList.add(userSnapShot.getKey());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        tempReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempKeyList.clear();
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    String u = userSnapShot.getValue(String.class);
                    tempKeyList.add(u);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final Intent tempIntent = new Intent(this, DoctorChangePassword.class);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String thisUsername = etUsername.getText().toString();
                String thisPassword = etPassword.getText().toString();
                if(checkBox.isChecked()){
                    if(validateDoctor(thisUsername,thisPassword)) {
                        //sharedPreference authenticate
                        if(sharedPreferences.getInt("login", 0) == 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("login", 1);
                            editor.putInt("user",1);
                            editor.putString("key",tempKey);
                            editor.putString("fullName", Doctor.getInstance().getFullName());
                            editor.putString("username", Doctor.getInstance().getUsername());
                            editor.putString("password", Doctor.getInstance().getPassword());
                            editor.putString("email",Doctor.getInstance().getEmail());
                            editor.putString("icNumber",Doctor.getInstance().getIcNumber());
                            editor.putString("handphoneNumber",Doctor.getInstance().getHandphoneNumber());
                            editor.apply();
                        }
                        if(changePw)
                            startActivity(tempIntent);
                        else
                            startActivity(myIntentDoc);
                        finish();
                    }
                    else{
                        Toast.makeText(SignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if(validateUser(thisUsername,thisPassword)) {
                        //sharedPreference authenticate
                        if(sharedPreferences.getInt("login", 0) == 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("login", 1);
                            editor.putInt("user",0);
                            editor.putString("key",tempKey);
                            editor.putString("fullName", User.getInstance().getFullName());
                            editor.putString("username", User.getInstance().getUsername());
                            editor.putString("password", User.getInstance().getPassword());
                            editor.putString("email",User.getInstance().getEmail());
                            editor.putString("icNumber",User.getInstance().getIcNumber());
                            editor.putString("handphoneNumber",User.getInstance().getHandphoneNumber());
                            editor.putString("doctor",User.getInstance().getDoctor());
                            editor.apply();
                        }
                        startActivity(myIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(SignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
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

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(checkBox.isChecked()){
                    doctorForgetPasswordDialog();
                }
                else{
                    userForgetPasswordDialog();
                }
            }
        });
    }

    @Override
    protected void onStart() { super.onStart(); }

    public void userForgetPasswordDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("What is your registered e-mail address? We will send reset credentials for you.");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Save</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String email = input.getText().toString().trim();
                        String usernameTemp = "";
                        String keyTemp = "";
                        for(int i = 0; i < userList.size(); i++){
                            if(userList.get(i).getEmail().equals(email)){
                                keyTemp = userKeyList.get(i);
                                usernameTemp = userList.get(i).getUsername();
                                break;
                            }
                        }
                        final String username = usernameTemp;
                        final String random = "A83Yzz62";
                        final String key = keyTemp;
                        userReference.child(key).child("password").setValue(MD5Hash.encrypt(random))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                        emailIntent.setData(Uri.parse("mailto:" + email));
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MASOCC user Forget Password");
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your username : " + username +
                                                "\nYour new password : " + random +
                                                "\nPlease change your password on your next login! " +
                                                "\n\nSent from MASOCC, \nadmin");
                                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(emailIntent);
                                        }
                                        Toast.makeText(SignIn.this, "Retrieve password successful!", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignIn.this, "Fail to retrieve password!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>Cancel</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void doctorForgetPasswordDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("What is your registered e-mail address? We will send reset credentials for you.");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Save</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String email = input.getText().toString().trim();
                        String usernameTemp = "";
                        String keyTemp = "";
                        for(int i = 0; i < doctorList.size(); i++){
                            if(doctorList.get(i).getEmail().equals(email)){
                                keyTemp = doctorKeyList.get(i);
                                usernameTemp = doctorList.get(i).getUsername();
                                break;
                            }
                        }
                        final String username = usernameTemp;
                        final String random = "A83Yzz62";
                        final String key = keyTemp;
                        doctorReference.child(key).child("password").setValue(MD5Hash.encrypt(random))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                        emailIntent.setData(Uri.parse("mailto:" + email));
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "MASOCC physician Forget Password");
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your username : " + username +
                                                "\nYour new password : " + random +
                                                "\nPlease change your password on your next login! " +
                                                "\n\nSent from MASOCC, \nadmin");
                                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivity(emailIntent);
                                        }
                                        Toast.makeText(SignIn.this, "Retrieve password successful!", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignIn.this, "Fail to retrieve password!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>Cancel</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

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

    private boolean validateDoctor(String un, String pw){
        for(int i = 0; i < doctorList.size(); i++){
            if(doctorList.get(i).getUsername().matches(un)){
                if(doctorList.get(i).getPassword().matches(MD5Hash.encrypt(pw))){
                    //save current user
                    Doctor.getInstance().setDoctor(doctorList.get(i));
                    tempKey = doctorKeyList.get(i);
                    if(tempKeyList.contains(doctorKeyList.get(i))){
                        tempReference.child(doctorKeyList.get(i)).removeValue();
                        changePw = true;
                    }
                    return true;
                }
                errorMessage = "Password incorrect";
            }
            errorMessage = "This physician is not exist";
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
