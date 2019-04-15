package com.example.fallingBall.multiPlayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

public class ObstacleManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;


    public long startTime;
    public long initTime;
    public double speed;
    public int elapsedTime;
    public double accel = (float) (Math.sqrt(1 + (startTime - initTime) / 50.0));
    private int Seed = -1;
    private Random generator /* = new Random(Seed)*/;

    public int getObstacleGap() {
        return obstacleGap;
    }

    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();
    }

    public double Speed(double Speed) {
        speed = Speed;
        //TODO speed not accurate enough
        accel =(Math.sqrt(1 + (startTime - initTime) / 20000.0));
        speed = (accel * - (double) Constants.SCREEN_HEIGHT) / 5000.0;
        return speed;
    }


    public Rect playerCollide(RectPlayer player) {
        for (Obstacle ob : obstacles) {
            Rect colRect = ob.playerCollide(player);
            if (colRect != null)
                return colRect;
        }
        return null;
    }

    public void populateObstacles(int seed) {
        Seed = seed;
        generator = new Random(Seed);

        int currY = Constants.SCREEN_HEIGHT + 5 * Constants.SCREEN_HEIGHT / 4;
        while (currY > Constants.SCREEN_HEIGHT)
        //while bottom of obstacle < 0 (hasn't gone onto the screen yet), keep generating obstacles. (currY <0) i.p.v. (obstacles.get(obstacles.size() - 1).getRectangle().bottom < 0, dit werkte niet)
        {
            // (-playerGap): if not, could generate gap off-screen
//            int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
            int xStart = (int) (generator.nextDouble() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY -= obstacleHeight + obstacleGap;
        }
    }

    public long StartTime() {
        startTime = System.currentTimeMillis();
        return startTime;
    }

    public void update() {
        //restarts start time whenever you get back on the app.
        if (startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        this.elapsedTime = elapsedTime;
        startTime = StartTime();
        //It takes 5 seconds for 1 obstacle to move across the entire screen
        //(float)(Math.sqrt((startTime-initTime)/1000.0)): increases speed over time every x seconds
        //TODO make speed dependent of score (if score has certain value, increase speed)
        for (Obstacle ob : obstacles) {
            Speed(speed);
            ob.incrementY(speed * elapsedTime);
        }
        //if last obstacle >= screen height, generate new obstacle (rectangle)
        if (obstacles.get(obstacles.size() - 1).getRectangle().bottom <= 0) {
            int xStart = (int) (generator.nextDouble() * (Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart, obstacles.get(0).getRectangle().bottom + obstacleHeight + obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            score++;
        }

    }

    public void draw(Canvas canvas) {
        for (Obstacle ob : obstacles)
            ob.draw(canvas);
        //draw score
        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(Color.MAGENTA);
        //+paint.descent()-paint.ascent(): gets distance between baseline of the screen and top of the text, helps get rid of corner cropping
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}