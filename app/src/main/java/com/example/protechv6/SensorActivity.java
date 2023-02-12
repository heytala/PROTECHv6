package com.example.protechv6;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.protechv6.databinding.ActivityMainBinding;
import com.example.protechv6.databinding.ActivitySensorBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SensorActivity extends AppCompatActivity implements CreateUserDialog.ExampleDialogListener {

    ActivitySensorBinding binding;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    DatabaseReference motionRef;
    DatabaseReference doorRef;
    DatabaseReference windowRef;
    DatabaseReference flameRef;
    DatabaseReference smokeRef;
    DatabaseReference buzzerRef;
    DatabaseReference passwordRef;

    DatabaseReference motionLtRef;
    DatabaseReference doorLtRef;
    DatabaseReference windowLtRef;
    DatabaseReference flameLtRef;
    DatabaseReference smokeLtRef;

    Credentials credentials = new Credentials();
    String currentUser = credentials.getCurrentUser();

    private TextView currentUserTextView;
    private TextView retrieveTV;
    private TextView switchText;
    private Switch switchView;



    private static int motionVal = 0;
    private static int doorVal = 0;
    private static int windowVal = 0;
    private static int flameVal = 0;
    private static int smokeVal = 0;
    private static String buzzerVal = " ";


    private static String value;
    private static String motion = " ";
    private static String door = " ";
    private static String window = " ";
    private static String flame = " ";
    private static String smoke = " ";
    private static String buzzer = " ";
    private static boolean protech = true;

    private static String ltMotion  = " ";
    private static String ltDoor    = " ";
    private static String ltWindow  = " ";
    private static String ltFlame   = " ";
    private static String ltSmoke   = " ";

    private static int test = R.drawable.a;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ' ' HH:mm");

    private static String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_sensor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel
                    ("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        firebaseDatabase        = FirebaseDatabase.getInstance();

        databaseReference       = firebaseDatabase.getReference("Sensor").child("button_state");
        motionRef               = firebaseDatabase.getReference("motion").child("val");
        doorRef                 = firebaseDatabase.getReference("door").child("val");
        windowRef               = firebaseDatabase.getReference("window").child("val");
        flameRef                = firebaseDatabase.getReference("flame").child("val");
        smokeRef                = firebaseDatabase.getReference("smoke").child("val");
        buzzerRef               = firebaseDatabase.getReference("buzzer").child("val");

        passwordRef             = firebaseDatabase.getReference("User").child("password");

        motionLtRef             = firebaseDatabase.getReference("motion").child("lt");
        doorLtRef               = firebaseDatabase.getReference("door").child("lt");
        windowLtRef             = firebaseDatabase.getReference("window").child("lt");
        flameLtRef              = firebaseDatabase.getReference("flame").child("lt");
        smokeLtRef              = firebaseDatabase.getReference("smoke").child("lt");

        retrieveTV = findViewById(R.id.idTVRetrieveData);

        currentUserTextView = (TextView) findViewById(R.id.currentUser);
        currentUserTextView.setText(currentUser);

//GET DATA FROM FIREBASE
        getData();

//LOG OUT
        Button button = (Button) findViewById(R.id.buttonLogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

//CREATE NEW USER (ONLY APPLICABLE IF CURRENT USER IS ADMIN)
        Button buttonCreateUser = (Button) findViewById(R.id.buttonCreateUser);
        if (currentUser != "Admin") {
            buttonCreateUser.setVisibility(View.GONE);
        }
        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

//LIST ITEM ACTIONS
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    //BUZZER DIALOG FOR DOOR
                if(position == 1) {
                    if (buzzer == "1") {
                        buzzerDialog();
                    }
                }

    //BUZZER DIALOG FOR WINDOW
                else if (position == 2) {
                    if (buzzer == "1") {
                        buzzerDialog();
                    }
                }

    //NEW INTENT FOR VIDEO SURVEILLANCE
                else if (position == 5) {
                    Intent i = new Intent(SensorActivity.this,VideoActivity.class);
                    startActivity(i);
                }
                System.out.println(position);
            }
        });

//SWITCH FOR PROTECH ON/OFF
        switchView = findViewById(R.id.protechSwitch);
        switchText = findViewById(R.id.switchText);
        switchView.setChecked(true);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    protech = true;
                    switchView.setChecked(true);
                    System.out.println(protech);
                } else {
                    protech = false;
                    switchView.setChecked(false);
                    System.out.println(protech);
                }
            }
        });

    }


    private void getData() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class);
                retrieveTV.setText(value);
                dataArr();
            }
         @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

//GET DATE AND TIME LAST TRIGGERED: MOTION
        motionLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltMotion = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: MOTION LT");
            }
        });

//GET DATE AND TIME LAST TRIGGERED: DOOR
        doorLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltDoor = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: DOOR LT");
            }
        });

//GET DATE AND TIME LAST TRIGGERED: WINDOW
        windowLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltWindow = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: WINDOW LT");
            }
        });

//GET DATE AND TIME LAST TRIGGERED: WINDOW
        windowLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltWindow = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: WINDOW LT");
            }
        });

//GET DATE AND TIME LAST TRIGGERED: FLAME
        flameLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltFlame = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: FLAME LT");
            }
        });

//GET DATE AND TIME LAST TRIGGERED: SMOKE
        smokeLtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ltSmoke = snapshot.getValue(String.class);
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: SMOKE LT");
            }
        });

//GET MOTION VALUE
        motionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                motionVal = snapshot.getValue(int.class);

                if (motionVal == 1) {
                    motion = "Motion Detected!";
                    ltMotion = sdf.format(new Date());
                    motionLtRef.setValue(ltMotion);
                    sendNotification("Motion", "Status: Motion Detected!");
                } else if (motionVal == 0){
                    motion = "No Motion";
                }
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: MOTION", Toast.LENGTH_SHORT).show();
            }
        });

//GET DOOR VALUE
        doorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doorVal = snapshot.getValue(int.class);

                if (doorVal == 1) {
                    door = "Open";
                    ltDoor = sdf.format(new Date());
                    doorLtRef.setValue(ltDoor);
                    sendNotification("Door", "Status: Door Open!");

                    //Triggers buzzer if Protech is ON
                    if (protech) {
                        buzzer = "1";
                        buzzerRef.setValue(buzzer);
                        buzzerDialog();
                    }
                } else if (doorVal == 0){
                    door = "Closed";
                }
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: DOOR", Toast.LENGTH_SHORT).show();
            }
        });

//GET WINDOW VALUE
        windowRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                windowVal = snapshot.getValue(int.class);

                if (windowVal == 1) {
                    window = "Open";
                    ltWindow = sdf.format(new Date());
                    windowLtRef.setValue(ltWindow);
                    sendNotification("Window", "Status: Window Open!");

                    //Triggers buzzer if Protech is ON
                    if (protech) {
                        buzzer = "1";
                        buzzerRef.setValue(buzzer);
                        buzzerDialog();
                    }

                } else if (windowVal == 0){
                    window = "Closed";
                }
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: WINDOW", Toast.LENGTH_SHORT).show();
            }
        });

//GET FLAME VALUE
        flameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flameVal = snapshot.getValue(int.class);

                if (flameVal == 0) {
                    flame = "Flame Detected!";
                    ltFlame = sdf.format(new Date());
                    flameLtRef.setValue(ltFlame);
                    sendNotification("Flame", "Flame Detected!");
                } else if (flameVal == 1){
                    flame = "Normal";
                }
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: FLAME", Toast.LENGTH_SHORT).show();
            }
        });

//GET SMOKE VALUE
        smokeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                smokeVal = snapshot.getValue(int.class);

                if (smokeVal == 1) {
                    smoke = "Lvl 1 (Normal)";
                } else if (smokeVal == 2){
                    smoke = "Lvl 2";
                } else if (smokeVal == 3){
                    smoke = "Lvl 3";
                    ltSmoke = sdf.format(new Date());
                    smokeLtRef.setValue(ltSmoke);
                    sendNotification("Smoke","Status: Smoke Detected!");
                }
                dataArr();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: SMOKE", Toast.LENGTH_SHORT).show();
            }
        });

//GET BUZZER VALUE
        buzzerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buzzerVal = snapshot.getValue(String.class);

                if (buzzer == "1") {

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data: BUZZER", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList dataArr() {
        int[] imageId = {R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a};
        String[] sensor = {"Motion","Door","Window","Flame","Smoke","Video Surveillance"};
        String[] status = {"Status: " + motion, "Status: " + door, "Status: " + window, "Status: " + flame, "Status: " + smoke, "Status: Running"};
        String[] lastTriggered = {ltMotion,ltDoor,ltWindow,ltFlame,ltSmoke, " "};

        ArrayList<Sensors> sensorArrayList = new ArrayList<>();

        for(int i = 0;i< imageId.length;i++){
            Sensors sensorsArr = new Sensors(sensor[i],status[i],lastTriggered[i],imageId[i]);
            sensorArrayList.add(sensorsArr);
        }

        ListAdapter listAdapter = new ListAdapter(SensorActivity.this,sensorArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        return sensorArrayList;

    }


    private void sendNotification(String sensor, String status){

        Intent intent = new Intent(SensorActivity.this, SensorActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (SensorActivity.this,"My Notification");
        builder.setContentTitle(sensor);
        builder.setContentText(status);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setContentIntent(PendingIntent.getActivity(this,1,new Intent(intent),PendingIntent.FLAG_IMMUTABLE));


        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(SensorActivity.this);
        managerCompat.notify(1,builder.build());
        System.out.println("send Notification triggered");
    }

    private void buzzerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);
        builder.setMessage("Buzzer is currently active. Do you want to turn it off?");
        builder.setTitle("Possible Theft!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // When the user click yes button then app will close
            buzzer = "0";
            buzzerRef.setValue(buzzer);
            dialog.dismiss();

        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void openDialog() {
        CreateUserDialog userDialog = new CreateUserDialog();
        userDialog.show(getSupportFragmentManager(), "Create New User");



    }

    @Override
    public void applyTexts(String password) {
        passwordRef.setValue(password);
    }
}
