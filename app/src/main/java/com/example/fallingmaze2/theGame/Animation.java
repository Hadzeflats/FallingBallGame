package com.example.fallingmaze2.theGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;

    private boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    public void stop() {
        isPlaying = false;
    }

    //Time in between frames;
    private float frameTime;

    private long lastFrame;

    public Animation(Bitmap[] frames, float animationTime) {
        this.frames = frames;
        frameIndex = 0;

        frameTime = animationTime / frames.length;

        lastFrame = System.currentTimeMillis();
    }
    public void draw(Canvas canvas, Rect destination){
        if(!isPlaying)
            return;

        canvas.drawBitmap(frames[frameIndex],null,destination, new Paint());
    }

    public void update() {
        if (!isPlaying)
            return;

        // frameTime*1000, counting in seconds
        if (System.currentTimeMillis() - lastFrame > frameTime * 1000) {
            frameIndex++;
            //if exceeded highest index, then = 0, otherwise keep it at frameIndex. If true, set to 0, if not: frameIndex.
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }
}
