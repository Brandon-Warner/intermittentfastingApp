package com.example.intermittentfastingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final long HOUR_TO_MILLIS = 60 * 60 * 1000;

    private long timeOfLastMeal;
    private long timeOfLastFast;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private ScheduledFuture scheduledFuture;
    private long durationOfFast;
    private long durationOfEat;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
        timeOfLastMeal = myPreferences.getLong("timeOfLastMeal", 0);
        timeOfLastFast = myPreferences.getLong("timeOfLastFast",System.currentTimeMillis());
        running = myPreferences.getBoolean("isRunning",false);
        boolean sixteenOnEightOff = myPreferences.getBoolean("sixteenOnEightOff",false);
        boolean twoTwentyFourOnWithinOneWeek = myPreferences.getBoolean("twoTwentyFourOnWithinOneWeek",false);
        Switch toggle = (Switch)findViewById(R.id.sixteenoffeighton);
        toggle.setChecked(sixteenOnEightOff);
        toggle = (Switch)findViewById(R.id.twotwentyfouronwithinoneweek);
        toggle.setChecked(twoTwentyFourOnWithinOneWeek);
        if (sixteenOnEightOff) {
            makeSixteenOnTrue();
        } else if (twoTwentyFourOnWithinOneWeek) {
            makeToTwentyFourTrue();
        } else {
            running = false;
        }
        if (running) {
            Button eatButton = findViewById(R.id.eatButton);
            eatButton.setEnabled(false);
        }

        runCounter();
        setContentView(R.layout.activity_main);
    }

    private void runCounter() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        // TODO Redesign to work with different intervals
                        //update the counter
                        long currentTime = System.currentTimeMillis();
                        Long timeSinceEat = currentTime - timeOfLastMeal;
                        TextView timeCounter = (TextView) findViewById(R.id.eatTimer);
                        String formattedTime = String.format("%d:%02d",timeSinceEat/(1000*60),timeSinceEat % (1000*60) /1000);
                        timeCounter.setText(formattedTime);
                    }
                }, 1, 1, TimeUnit.SECONDS
        );
    }

    public void clickEat(View view) {
        // TODO Redesign to work with different intervals
        timeOfLastMeal = System.currentTimeMillis();
        Button eatButton = (Button) findViewById(R.id.eatButton);
        eatButton.setEnabled(false);
        SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putLong("timeOfLastMeal",timeOfLastMeal);
        editor.commit();
    }

    public void clickReset(View view) {
        reset();
    }

    public void reset() {
        Button eatButton = (Button) findViewById(R.id.eatButton);
        eatButton.setEnabled(true);
    }

    public void clickSixteenToEightToggle(View view) {
        Switch thisToggle = (Switch) findViewById(R.id.sixteenoffeighton);
        Switch other = (Switch) findViewById(R.id.twotwentyfouronwithinoneweek);
        if (thisToggle.isChecked()) {
            other.setChecked(false);
            makeSixteenOnTrue();
            SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putBoolean("sixteenOnEightOff",true);
            editor.putBoolean("twoTwentyFourOnWithinOneWeek",false);
            editor.commit();
        } else {
            SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putBoolean("twoTwentyFourOnWithinOneWeek",false);
            editor.commit();
        }
        reset();
    }

    public void makeSixteenOnTrue() {
        durationOfEat = 8 * HOUR_TO_MILLIS;
        durationOfFast = 16 * HOUR_TO_MILLIS;

    }


    public void clicktwotwentyfourToOneWeek(View view) {
        Switch thisToggle = (Switch) findViewById(R.id.twotwentyfouronwithinoneweek);
        Switch other = (Switch) findViewById(R.id.sixteenoffeighton);
        if (thisToggle.isChecked()) {
            other.setChecked(false);
            makeToTwentyFourTrue();
            SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putBoolean("twoTwentyFourOnWithinOneWeek",true);
            editor.putBoolean("sixteenOnEightOff",false);
            editor.commit();
        } else {
            SharedPreferences myPreferences = getSharedPreferences("FastingPrefs",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putBoolean("twoTwentyFourOnWithinOneWeek",false);
            editor.commit();
        }
        reset();
    }

    public void makeToTwentyFourTrue() {
        durationOfEat = 120 * HOUR_TO_MILLIS;
        durationOfFast = 48 * HOUR_TO_MILLIS;
    }

}