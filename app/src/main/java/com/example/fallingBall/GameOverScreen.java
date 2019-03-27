package com.example.fallingBall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOverScreen extends AppCompatActivity {

    public Button button8;
    public Button button9;

    public void init() {
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent b = new Intent(GameOverScreen.this, GameScreen.class);
                startActivity(b);

            }
        });

        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bb = new Intent(GameOverScreen.this, MainActivity.class);
                startActivity(bb);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        init();
    }
}
