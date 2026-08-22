package com.example.trashtotreasure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private StreakManager streakManager;
    private TextView streakText;
    private TextView levelText;
    private TextView rankText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize backend point/streak logic class
        streakManager = new StreakManager(this);

        //find UI components
        streakText = findViewById(R.id.streakText);
        levelText = findViewById(R.id.levelText);
        rankText = findViewById(R.id.rankText);

        Button btnScanner = findViewById(R.id.btnScanner);
        Button btnGallery = findViewById(R.id.btnGallery);
        Button btnStreaks = findViewById(R.id.btnStreaks);
        Button btnLocation = findViewById(R.id.btnLocation);

        //update the streak, level, and rank display on launch
        updateUI();

        //checks for button press
        btnScanner.setOnClickListener(v -> {
            //simulate a scan. for testing purposes
            streakManager.recordScan();
            updateUI();
        });

        btnGallery.setOnClickListener(v -> {
            //image gallery module goes here!
        });

        btnStreaks.setOnClickListener(v -> {
            //streaks module goes here!
        });

        btnLocation.setOnClickListener(v -> {
            //location tracker module goes here!
        });
    }

    //shows updated ui
    private void updateUI() {
        int streak = streakManager.getStreakCount();
        int level = streakManager.getLevel();
        int xpInLevel = streakManager.getXpInLevel();
        String rank = streakManager.getRank();

        streakText.setText("Current Streak: " + streak + " Days");
        levelText.setText("Level: " + level + " (" + xpInLevel + "/100 XP)");
        rankText.setText("Rank: " + rank);
    }
}