package com.example.intermittentfastingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private long timeOfLastMeal;
    private long timeOfLastFast;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private ScheduledFuture scheduledFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
        timeOfLastMeal = myPreferences.getLong("timeOfLastMeal", 0);
        timeOfLastFast = myPreferences.getLong("timeOfLastFast",System.currentTimeMillis());


        setContentView(R.layout.activity_main);
        runCounter();
    }

    private void runCounter() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        //update the counter
                        long currentTime = System.currentTimeMillis();
                        Long timeSinceEat = currentTime - timeOfLastMeal;
                        TextView timeCounter = (TextView) findViewById(R.id.timer);
                        String formattedTime = String.format("%d:%02d",timeSinceEat/(1000*60),timeSinceEat % (1000*60) /1000);
                        timeCounter.setText(formattedTime);
                    }
                }, 1, 1, TimeUnit.SECONDS
        );
    }

    public void clickEat(View view) {
        timeOfLastMeal = System.currentTimeMillis();
        SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putLong("timeOfLastMeal",timeOfLastMeal);
        editor.commit();
    }

}