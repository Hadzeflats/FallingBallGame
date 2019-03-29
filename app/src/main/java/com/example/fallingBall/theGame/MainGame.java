package com.example.fallingBall.theGame;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class MainGame extends Activity {
    //TODO Scherm aan staan terwijl gamen
    //https://stackoverflow.com/questions/1114270/android-screen-timeout voor meer info
    /*private static final int DELAY = 3000;
    int defTimeOut = 0;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        setContentView(new GamePanel (this));
        /*defTimeOut = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, DELAY);
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DELAY);*/
    }

   /* @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defTimeOut);
    }*/

}
