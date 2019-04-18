package com.example.fallingBall.GUI;

import android.app.Activity;
import android.content.Intent;

import com.example.fallingBall.R;

public class themeUtils {

    private static int cTheme;
    public final static int PINK =0;
    public final static int BLUE=1;
    public static void changeToTheme(Activity activity,int theme){
        cTheme=theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    public static void onActivityCreateSetTheme(Activity activity){
        switch (cTheme){
            default:
            case PINK:
                activity.setTheme(R.style.FirstTheme);
                break;
            case BLUE:
                activity.setTheme(R.style.SecondTheme);
        }
    }

}
