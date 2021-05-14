package com.example.recyclemania;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

public class CheckSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_splash);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(CheckSplash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}