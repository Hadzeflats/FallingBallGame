package com.example.fallingBall.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fallingBall.R;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public Button button1;

        public void init(){
            button= findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent a = new Intent(MainActivity.this,ChoosePlayer.class);

                   startActivity(a);
                }
            });

            button1= findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aa = new Intent (MainActivity.this,Options.class);

                    startActivity(aa);
                }
            });

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
}
