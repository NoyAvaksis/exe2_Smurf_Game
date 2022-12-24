package com.example.exe1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Activity_Records extends AppCompatActivity {
    private MaterialButton top_BTN_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        MapsFragment mapsFragment= new MapsFragment();
        Fragment_top10 fragment_top10= new Fragment_top10();

        getSupportFragmentManager().beginTransaction().add(R.id.top_LAY_details, fragment_top10).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.top_LAY_maps, mapsFragment).commit();
        findViews();
        initViews();
    }

    private void initViews() {
        top_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGamePage();
            }
        });
    }
    private void openGamePage() {
        Intent intent = new Intent(this, Activity_Menu.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        top_BTN_back=findViewById(R.id.top_BTN_back);

    }


}