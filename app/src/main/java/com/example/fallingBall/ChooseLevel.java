package com.example.fallingBall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.fallingBall.singlePlayer.Constants;
import com.example.fallingBall.singlePlayer.GameplayScene;
import com.example.fallingBall.singlePlayer.SingleGame;

public class ChooseLevel extends AppCompatActivity {

    public Button button4;
    public Button button5;
    public Button button6;
    public Button button7;

    public void init() {
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent b = new Intent(ChooseLevel.this, SingleGame.class);
                startActivity(b);
            }
        });

        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb = new Intent(ChooseLevel.this, SingleGame.class);
                startActivity(bb);
            }
        });
        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent b = new Intent(ChooseLevel.this, SingleGame.class);
                startActivity(b);

            }
        });

        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb = new Intent(ChooseLevel.this, SingleGame.class);
                startActivity(bb);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        init();
    }
}
