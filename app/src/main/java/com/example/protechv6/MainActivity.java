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


public class MainActivity extends AppCompatActivity {

    EditText loginText, passwordText;
    Button btnLogin;
    Credentials credentials = new Credentials();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginText = (EditText) findViewById(R.id.login);
        passwordText = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        System.out.println("Width: " + width + "  " + "Height: " + height);

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

                }else if(username.equals("Guest") && password.equals("guest")) {
                    credentials.setCurrentUser("Guest");
                    Toast.makeText
                            (MainActivity.this,"Logged in as " + credentials.getCurrentUser(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SensorActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText
                            (MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}