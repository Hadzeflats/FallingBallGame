package com.example.fallingBall.GUI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.fallingBall.R;

public class Options extends AppCompatActivity implements View.OnClickListener {

    public void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_options);
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        init();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button12:
                themeUtils.changeToTheme(this,themeUtils.PINK);
                break;
            case R.id.button11:
                themeUtils.changeToTheme(this,themeUtils.BLUE);
                break;
        }
    }
}
