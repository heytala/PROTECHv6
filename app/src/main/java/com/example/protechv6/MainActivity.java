package com.example.protechv6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
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


public class MainActivity extends AppCompatActivity {

    EditText loginText, passwordText;
    Button btnLogin;
    Credentials credentials = new Credentials();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference passwordRef;
    private static String passwordDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase    = FirebaseDatabase.getInstance();
        passwordRef         = firebaseDatabase.getReference("User").child("password");

        getPassword();

        loginText = (EditText) findViewById(R.id.login);
        passwordText = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = loginText.getText().toString();
                String password = passwordText.getText().toString();

                if(username.equals("Admin") && password.equals("admin")) {
                    credentials.setCurrentUser("Admin");
                    Toast.makeText
                            (MainActivity.this,"Logged in as " + credentials.getCurrentUser(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                    startActivity(intent);
                    finish();

                }else if(username.equals("Guest") && password.equals(passwordDB)) {
                    credentials.setCurrentUser("Guest");
                    Toast.makeText
                            (MainActivity.this,"Logged in as " + credentials.getCurrentUser(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                    startActivity(intent);
                    finish();
                }

                else {
                    Toast.makeText
                            (MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getPassword() {
        passwordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passwordDB = snapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Fail to get data: PASSWORD");
            }
        });
    }

}