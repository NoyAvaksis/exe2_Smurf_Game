package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Activity_Menu extends AppCompatActivity {

    private MaterialButton menu_BTN_slowMode;
    private MaterialButton menu_BTN_fastMode;
    private MaterialButton menu_BTN_sensorMode;
    private MaterialButton menu_BTN_topTen;
    private eGameMode gameMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
    }

    private void initViews() {
        menu_BTN_slowMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            gameMode = eGameMode.SLOW_ARROWS;
            startGame();
            }
        });

        menu_BTN_fastMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            gameMode = eGameMode.FAST_ARROWS;
            startGame();
            }


        });

        menu_BTN_sensorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            gameMode = eGameMode.SENSOR;
            startGame();
            }
        });

       menu_BTN_topTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecords();
            }
        });


    }


    private void findViews() {
        menu_BTN_slowMode = findViewById(R.id.menu_BTN_slowMode);
        menu_BTN_fastMode = findViewById(R.id.menu_BTN_fastMode);
        menu_BTN_sensorMode = findViewById(R.id.menu_BTN_sensorMode);
        menu_BTN_topTen = findViewById(R.id.menu_BTN_topTen);
    }

    private void startGame() {
        Intent intent = new Intent(this, Activity_Game.class);
        intent.putExtra(DataManager.GAME_MODE, gameMode.name());
        startActivity(intent);
        finish();
    }

    private void openRecords() {
        Intent intent = new Intent(this, Activity_Records.class);
        startActivity(intent);
        finish();
    }

}
