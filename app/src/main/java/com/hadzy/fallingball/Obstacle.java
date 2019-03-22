package com.hadzy.fallingball;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    public Rect getRectangle() { return rectangle; }

    public Rect getRectangle2() {
        return rectangle2;
    }

    public void incrementY (float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        //left,top,right,bottom
        rectangle = new Rect(0,startY,startX,startY +rectHeight);
        rectangle2 =new Rect(startX+playerGap, startY,Constants.SCREEN_WIDTH,startY+rectHeight);
    }

    public Rect playerCollide(RectPlayer player) {
       //return Rect.intersects(rectangle,player.getRectangle())|| Rect.intersects(rectangle2,player.getRectangle());
        if(Rect.intersects(rectangle,player.getRectangle())){
            return rectangle;
        }
        if(Rect.intersects(rectangle2,player.getRectangle())) {
            return rectangle2;
        }

            return null;

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
