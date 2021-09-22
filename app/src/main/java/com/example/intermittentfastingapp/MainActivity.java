package com.example.intermittentfastingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
        long timeOfLastMeal = myPreferences.getLong("timeOfLastMeal", 0);
        long timeOfLastFast = myPreferences.getLong("timeOfLastFast",System.currentTimeMillis());


        setContentView(R.layout.activity_main);
    }


}