package com.example.fallingBall.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fallingBall.R;
import com.example.fallingBall.multiPlayer.MultiGame;

public class ChoosePlayer extends AppCompatActivity {

    public Button button2;
    public Button button3;

         public void init() {
            button2 = findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {

                 @Override
                    public void onClick(View v) {
                        Intent b = new Intent(ChoosePlayer.this, ChooseLevel.class);
                        startActivity(b);

                 }
             });

             button3 = findViewById(R.id.button3);
             button3.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent bb = new Intent(ChoosePlayer.this, MultiGame.class);
                     startActivity(bb);
                 }
             });
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_player);
        init();

    }
}
