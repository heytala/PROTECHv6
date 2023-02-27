package com.example.protechv6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;

public class VideoActivity extends AppCompatActivity {

    WebView web;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference buzzerRef;
    DatabaseReference buzzerRefSingle;
    private static String buzzerVal = " ";
    private String buzzerValSingle = " ";
    private GifImageView red_blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        web = findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new Callback());
        web.loadUrl("https://protech.ap.ngrok.io");

        firebaseDatabase        = FirebaseDatabase.getInstance();
        buzzerRef               = firebaseDatabase.getReference("buzzer").child("val");
        buzzerRefSingle         = firebaseDatabase.getReference("buzzer").child("val");
        red_blink               = (GifImageView) findViewById(R.id.red_blink);

        //LOG OUT
        Button button = (Button) findViewById(R.id.buttonLogout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        getBuzzerData();

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }

    private void getBuzzerData() {

        //GET BUZZER VALUE
        buzzerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buzzerVal = snapshot.getValue(String.class);
                System.out.println("BuzzerValVideo2 " + buzzerVal);

                if (buzzerVal.equals("1")) {
                    red_blink.setVisibility(View.VISIBLE);
                    System.out.println("done 1");
                } else if (buzzerVal.equals("1")) {
                    red_blink.setVisibility(View.INVISIBLE);
                    System.out.println("done 2");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VideoActivity.this, "Fail to get data: BUZZER", Toast.LENGTH_SHORT).show();
            }
        });
    }


}