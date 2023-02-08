package com.example.protechv6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.protechv6.databinding.ActivityMainBinding;
import com.example.protechv6.databinding.ActivitySensorBinding;

import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity {

    ActivitySensorBinding binding;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    DatabaseReference motionRef;
    DatabaseReference doorRef;
    DatabaseReference windowRef;
    DatabaseReference triggerEmergencyRef;
    DatabaseReference smokeRef;


    private TextView retrieveTV;

    private static String value;
    private static int motion;
    private static int door;
    private static int window;
    private static int triggerEmergency;
    private static int smoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_sensor);

        firebaseDatabase        = FirebaseDatabase.getInstance();

        databaseReference      = firebaseDatabase.getReference("Sensor").child("button_state");
        motionRef              = firebaseDatabase.getReference("Sensor").child("motion");
        doorRef                = firebaseDatabase.getReference("Sensor").child("door");
        windowRef              = firebaseDatabase.getReference("Sensor").child("window");
        triggerEmergencyRef    = firebaseDatabase.getReference("Sensor").child("triggerEmergency");
        smokeRef               = firebaseDatabase.getReference("Sensor").child("smoke");

        retrieveTV = findViewById(R.id.idTVRetrieveData);
        getdata();

        Button button = (Button) findViewById(R.id.buttonLogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

/*        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(SensorActivity.this,VideoActivity.class);
                i.putExtra("name",name[position]);
                i.putExtra("phone",phoneNo[position]);
                i.putExtra("country",country[position]);
                i.putExtra("imageid",imageId[position]);
                startActivity(i);

            }
        });*/



    }


    private void getdata() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class);
                retrieveTV.setText(value);

                int[] imageId = {R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a, R.drawable.a};
                String[] sensor = {"Motion","Door","Window","Flame","Smoke","Video Surveillance"};
                String[] status = {"Status: " + value, "Status: " + value, "Status: " + value, "Status: " + value, "Status: " + value, "Status: " + value};
                String[] lastTriggered = {"8:45 pm","9:00 am","7:34 pm","6:32 am","5:76 am", "5:00 am"};

                ArrayList<Sensors> sensorArrayList = new ArrayList<>();

                for(int i = 0;i< imageId.length;i++){
                    Sensors sensorsArr = new Sensors(sensor[i],status[i],lastTriggered[i],imageId[i]);
                    sensorArrayList.add(sensorsArr);
                }

                ListAdapter listAdapter = new ListAdapter(SensorActivity.this,sensorArrayList);
                binding.listview.setAdapter(listAdapter);
                binding.listview.setClickable(true);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SensorActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
            


        });
    }




}
