package com.example.masocc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Exercise extends YouTubeBaseActivity {

    protected static final int REQUEST_TAKE_PHOTO = 1;

    private Button btnStart, btnEnd, btnReset;
    private TextView tvStopwatchName, tvVideoName;
    private Handler handler;
    private Uri imgUri;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private int Seconds, Minutes, MilliSeconds ;
    private List<String> timeRecord = new ArrayList<>();
    private String date, type, time, feeling, currentPhotoPath;;
    private String[] currentExerciseList;
    private ArrayList<Integer> currentExerciseIdList = new ArrayList<>();
    private String[] oriExerciseList1 = {"Shoulder Shrug","Seated Ladder Climb","Seated Russian Twist", "Sit to Stand",
    "Seated Bent over Row", "Toe Lift", "Wall Push Up", "Oblique Squeeze"};
    private int[] oriExerciseIdList1 = {R.raw.shoulder_shrug,R.raw.seated_ladder_climb, R.raw.seated_russian_twist, R.raw.sit_to_stand,
    R.raw.seated_bend_over_row, R.raw.toe_lift, R.raw.wall_push_up, R.raw.oblique_squeeze};
    private String[] oriExerciseList2 = {"Seated Bicycle Crunch","Seated Butterfly","Lateral Leg Raise", "Squat with Rotational Press",
            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"};
    private int[] oriExerciseIdList2 = {R.raw.seated_bicycle_crunch,R.raw.seated_butterfly, R.raw.lateral_leg_raise, R.raw.squat_with_rotational_press,
            R.raw.wood_cutter, R.raw.empty_the_can, R.raw.standing_bicycle_crunch};
    private int exerciseCounter = 0;
    private boolean pause_check;
    private List<ExerciseRecord> exerciseRecordList;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private SharedPreferences sharedPreferences;
    String key, recordKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        type = sharedPreferences.getString("videoType", null);
        String s = sharedPreferences.getString("list",null);
        String str = s.substring(1,s.length()-1);
        currentExerciseList = str.split(", ");

        //define current list with id

        if(type.contentEquals("exercise level one")) {
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList1.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList1[j])) {
                        currentExerciseIdList.add(oriExerciseIdList1[j]);
                    }
                }
            }
            //storage
            storage = FirebaseStorage.getInstance();
            mStorage = storage.getReference().child("userExerciseLevelOne");

            //database
            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference().child("userExerciseLevelOne");
        }
        else{
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList2.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList2[j])) {
                        currentExerciseIdList.add(oriExerciseIdList2[j]);
                    }
                }
            }
            //storage
            storage = FirebaseStorage.getInstance();
            mStorage = storage.getReference().child("userExerciseLevelTwo");

            //database
            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference().child("userExerciseLevelTwo");
        }

        VideoView view = (VideoView)findViewById(R.id.video_view);
        MediaController mc= new MediaController(this);
        view.setMediaController(mc);
        playVideo(view);

        //date,type
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(c);


            mDatabase = mDatabase.child(date);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
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



        //setupUI
        handler = new Handler();
        btnStart = (Button)findViewById(R.id.button_start);
        btnEnd = (Button)findViewById(R.id.button_end);
        btnReset = (Button)findViewById(R.id.button_reset);
        tvStopwatchName = (TextView) findViewById(R.id.stopwatch_name);
        tvVideoName = (TextView) findViewById(R.id.video_name);

        tvVideoName.setText(currentExerciseList[exerciseCounter]);
        btnEnd.setEnabled(false);
        pause_check = false;

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause_check) {
                    btnStart.setText("Start");
                    TimeBuff += MillisecondTime;
                    pause_check = false;
                    btnReset.setEnabled(true);
                    handler.removeCallbacks(runnable);
                } else {
                    StartTime = SystemClock.uptimeMillis();
                    btnStart.setText("Pause");
                    pause_check = true;
                    btnEnd.setEnabled(true);
                    btnReset.setEnabled(false);
                    handler.postDelayed(runnable, 0);
                }
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(exerciseCounter == 7) {
                    timeRecord.add(time);
                    handler.removeCallbacks(runnable);
                    btnReset.setEnabled(true);
                    btnEnd.setEnabled(false);
                    btnStart.setEnabled(false);
                    saveDialog();
                }

                else {
                    exerciseCounter++;
                    if(exerciseCounter == 7)
                        btnEnd.setText("END");
                    tvVideoName.setText(currentExerciseList[exerciseCounter]);
                    playVideo(view);
                    timeRecord.add(time);

                    handler.removeCallbacks(runnable);
                    btnReset.setEnabled(true);
                    btnEnd.setEnabled(false);
                    btnStart.setEnabled(false);

                    MillisecondTime = 0L ;
                    StartTime = 0L ;
                    TimeBuff = 0L ;
                    UpdateTime = 0L ;
                    Seconds = 0 ;
                    Minutes = 0 ;
                    MilliSeconds = 0 ;
                    tvStopwatchName.setText("00:00:00");
                    //
                    StartTime = SystemClock.uptimeMillis();
                    btnStart.setText("Pause");
                    pause_check = true;
                    btnEnd.setEnabled(true);
                    btnReset.setEnabled(false);
                    handler.postDelayed(runnable, 0);
                    btnStart.setEnabled(true);
                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                tvStopwatchName.setText("00:00:00");
                //
                btnStart.setEnabled(true);
            }
        });
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000)/10;

            time = ("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds));

            tvStopwatchName.setText(time);

            handler.postDelayed(this, 0);
        }

    };

    public void playVideo(VideoView view){
        System.out.println(exerciseCounter);
        System.out.println(currentExerciseIdList.size());
        String path = "android.resource://" + getPackageName() + "/" + currentExerciseIdList.get(exerciseCounter);
        view.setVideoURI(Uri.parse(path));
        view.start();
    }

    //save record
    public void saveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to save this exercise?");
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        feelingDialog();
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>No</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void feelingDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("What is your feeling after doing this exercise?");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Save</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        feeling = input.getText().toString().trim();

                        saveExercise();
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

    public void saveExercise() {
        List<String> temp = new ArrayList<>();
        if(type.contentEquals("exercise level one")) {
            for (int i = 0; i < oriExerciseList1.length; i++) {
                for (int j = 0; j < currentExerciseList.length; j++) {
                    if (oriExerciseList1[i].contentEquals(currentExerciseList[j])) {
                        temp.add(timeRecord.get(j));
                    }
                }
            }
        }
        else{
            for (int i = 0; i < oriExerciseList2.length; i++) {
                for (int j = 0; j < currentExerciseList.length; j++) {
                    if (oriExerciseList2[i].contentEquals(currentExerciseList[j])) {
                        temp.add(timeRecord.get(j));
                    }
                }
            }
        }

        timeRecord = temp;
        ExerciseRecord er = new ExerciseRecord(date, type, timeRecord, feeling);

        recordKey = mDatabase.child(key).push().getKey();
        mDatabase.child(key).child(recordKey).setValue(er)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Exercise.this, "Add exercise successful!", Toast.LENGTH_LONG).show();
                        savePhotoDialog();
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Exercise.this, "Add exercise fail! Try again later.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    public void savePhotoDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to capture a photo for this exercise?");
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dispatchTakePictureIntent();
                    }
                });

        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>No</font>"),
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

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        StorageReference usersRef = mStorage.child(key).child("images/"+imgUri.getLastPathSegment());
        StorageReference usersRef = mStorage.child(key).child("images/"+recordKey);
        UploadTask uploadTask;
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(imgUri.getLastPathSegment()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        uploadTask = usersRef.putFile(imgUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Exercise.this, "Add image fail! Try again later.", Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Exercise.this, "Add image successful!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                imgUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}

