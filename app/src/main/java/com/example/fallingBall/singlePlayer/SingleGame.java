package com.example.fallingBall.singlePlayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.fallingBall.ChooseLevel;

public class SingleGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ObstacleManager obstacleManager;

        //TODO
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        // get data via the key
        String diff1 = bundle.getString(Intent.EXTRA_TEXT);
        if (diff1 != null) {

        }
        String diff2 = bundle.getString(Intent.EXTRA_TEXT);
        if (diff2 != null) {

        }
        String diff3 = bundle.getString(Intent.EXTRA_TEXT);
        if (diff3 != null) {

        }
        String diff4 = bundle.getString(Intent.EXTRA_TEXT);
        if (diff4 != null) {

        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        setContentView(new GamePanel(this));
    }
}
