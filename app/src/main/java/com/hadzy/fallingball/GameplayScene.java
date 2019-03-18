package com.hadzy.fallingball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.MotionEvent;

import static com.hadzy.fallingball.SceneManager.ACTIVE_SCENE;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    /*private int score = 0; */ //TODO score
    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(230, 0, 100));
        //Start in the center of the screen (x-value), start on 3/4 of the screen (y-value)
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2,  3*Constants.SCREEN_HEIGHT / 4);
        // updates the location of player to playerPoint
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350, 70, Color.BLACK);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4);
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 70, Color.BLACK);
        // added just to be safe
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;

                //when tapping screen, if game over and time after game over >= 1 sec, start again
                if (gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
                    reset();
                    gameOver = false;
                    //resets reference point for tilt controls
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.YELLOW);
        //TODO Change screen when hitting certain score
        /*score++;
        if (score == 20){
            canvas.drawColor(Color.GREEN);}*/
        player.draw(canvas);
        obstacleManager.draw(canvas);

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Touch to replay");
        }
    }

    @Override
    public void update() {
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;

            int elapsedTime = (int)(System.currentTimeMillis()-frameTime);
            frameTime = System.currentTimeMillis();

            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                //movement y-direction (delta pitch)
                float pitch = orientationData.getOrientation()[1]-orientationData.getStartOrientation()[1];
                //movement x-direction (delta roll)
                float roll = orientationData.getOrientation()[2]-orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/500f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/500f;

                // If number of pixels player is moving > 5, return xSpeed*elapsedTime, otherwise add 0.
                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                //playerPoint.y -=Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }
            //Screen bounds, spring van ene kant naar andere kant op scherm
            if(playerPoint.x < 0 )
                playerPoint.x = Constants.SCREEN_WIDTH;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = 0;

            if(playerPoint.y < 0 )
                gameOver = true;
            else if (playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;


            player.update(playerPoint);
            obstacleManager.update();

            //zwaartekracht of blijven liggen

            if (obstacleManager.playerCollide(player)) {
                for(int i=0; i<obstacleManager.getObstacles().size(); i++) {
                    Rect rect1 = obstacleManager.getObstacles().get(i).getRectangle();
                    Rect rect2 = obstacleManager.getObstacles().get(i).getRectangle2();
                    Rect play = player.getRectangle();
                    int Th = 35;

                    if (Rect.intersects(rect1,play) || (Rect.intersects(rect2,play))){
                        Log.d("yeet", "update: no rec");
                        if (rect1.top+Th < play.bottom) {
                            if ((play.right > rect2.left) || (play.left > rect1.right))
                                playerPoint.x += 0;
                        }
                        /*if (rect1.bottom > play.top) {
                            if ((play.right > rect2.left) || (play.left > rect1.right))
                                playerPoint.x += 0;
                        }*/
                        else
                            playerPoint.y = rect1.top - (play.height()/2);

                    }

                    /* if (obstacleManager.playerCollide(player)) {
                    playerPoint.y += (double) (obstacleManager.speed * obstacleManager.elapsedtime);} */

                }
                //TODO
            }
            else
                playerPoint.y += 10;

        }
    }


    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}

