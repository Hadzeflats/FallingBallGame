package com.example.fallingBall.multiPlayer;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    //To switch active scene
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
