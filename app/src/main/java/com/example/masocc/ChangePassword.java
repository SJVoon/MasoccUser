package com.example.masocc;

import android.content.Intent;
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

public class ChangePassword extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference userReference;
    protected BottomNavigationView navView;
    private Intent myIntent1, myIntent2, myIntent3, myIntent4;
    private Button btnChangePassword;
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private String currentPassword, newPassword, confirmNewPassword;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        myIntent1 = new Intent(ChangePassword.this, MainActivity.class);
                        myIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent1);
                        return true;
                    case R.id.navigation_dashboard:
                        myIntent2 = new Intent(ChangePassword.this, Assessment.class);
                        myIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent2);
                        return true;
                    case R.id.navigation_history:
                        myIntent3 = new Intent(ChangePassword.this, History.class);
                        myIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent3);
                        return true;
                    case R.id.navigation_profile:
                        myIntent4 = new Intent(ChangePassword.this, Profile.class);
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
        userReference = database.getReference().child("users");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                    User u = userSnapShot.getValue(User.class);
                    if(u.getUsername().equals(User.getInstance().getUsername())){
                        key = userSnapShot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        setupUI();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                define();
                if(validate()) {
                    userReference = userReference.child(key);

                    userReference.child("password").setValue(MD5Hash.encrypt(newPassword))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangePassword.this, "Change password successful!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangePassword.this, "Change password fail! Try again later", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });

                }

            }
        });
    }

    private void setupUI(){
        btnChangePassword = findViewById(R.id.button_change_password);
        etCurrentPassword = findViewById(R.id.edit_text_current_password);
        etNewPassword = findViewById(R.id.edit_text_new_password);
        etConfirmNewPassword = findViewById(R.id.edit_text_confirm_new_password);
    }

    private void define(){
        currentPassword = etCurrentPassword.getText().toString().trim();
        newPassword = etNewPassword.getText().toString().trim();
        confirmNewPassword = etConfirmNewPassword.getText().toString().trim();
    }

    private boolean validate(){
        boolean check = true;

        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(getApplicationContext(), "Enter current password!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (!MD5Hash.encrypt(currentPassword).matches((User.getInstance().getPassword()))) {
            Toast.makeText(getApplicationContext(), "Enter corrent current password!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getApplicationContext(), "Enter new password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.matches(confirmNewPassword)) {
            Toast.makeText(getApplicationContext(), "Comfirm password is not same as password, reenter!", Toast.LENGTH_SHORT).show();
            return check = false;
        }

        return check;
    }
}
