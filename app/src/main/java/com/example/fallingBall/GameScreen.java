package com.example.fallingBall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameScreen extends AppCompatActivity {

      public Button button10;


     public void init() {
          button10 = findViewById(R.id.button10);
          button10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent b = new Intent(GameScreen.this, GameOverScreen.class);
                    startActivity(b);

                }
            });
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        init();
    }

}