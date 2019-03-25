package com.example.fallingmaze2.theGame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class RectPlayer implements GameObject {

    private Rect rectangle;
    private int color;

    private Animation idle;
    private Animation walkRight;
    private Animation walkLeft;

    public Rect getRectangle() {
        return rectangle;
    }

    public RectPlayer(Rect rectangle, int color){
        this.rectangle = rectangle;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);

    }

    @Override
    public void update(){

    }

    public void update (Point point){
        //rectangle.set(left, top, right bottom). Top van scherm y=0, naarbeneden op het schermm y gaat omhoog.
        rectangle.set(point.x-rectangle.width()/2,point.y -rectangle.height()/2,point.x+rectangle.width()/2,point.y +rectangle.height()/2);
    }
}
