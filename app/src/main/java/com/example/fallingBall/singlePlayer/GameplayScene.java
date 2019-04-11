package com.example.fallingBall.singlePlayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.fallingBall.ChooseLevel;
import com.example.fallingBall.client.DataReceiver;

import static com.example.fallingBall.singlePlayer.SceneManager.ACTIVE_SCENE;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    // private int score = obstacleManager.getScore();

    private RectPlayer indicator;
    private Point indicatorPoint;
    private boolean belowScreen = false;

    private RectPlayer pauseLine1;
    private RectPlayer pauseLine2;
    private Point pausePoint1;
    private Point pausePoint2;
    private RectPlayer pauseScreen;
    private RectPlayer pauseScreenOutline;
    private Point pauseScreenPoint;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;
    private boolean paused = false;

    private OrientationData orientationData;
    private long frameTime;


    public GameplayScene() {
        player = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 25, Constants.SCREEN_HEIGHT / 25), Color.rgb(230, 0, 100));
        //Start in the center of the screen (x-value), start on 1/3 of the screen (y-value)
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3);

        //pause button
        pauseLine1 = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 120, Constants.SCREEN_HEIGHT / 26), Color.MAGENTA);
        pauseLine2 = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 120, Constants.SCREEN_HEIGHT / 26), Color.MAGENTA);
        pausePoint1 = new Point((Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 10 - 25), Constants.SCREEN_WIDTH / 9);
        pausePoint2 = new Point((Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 10 + 25), Constants.SCREEN_WIDTH / 9);

        //pause screen
        pauseScreenPoint = new Point(Constants.SCREEN_WIDTH/2,Constants.SCREEN_HEIGHT/2);
        pauseScreen = new RectPlayer(new Rect(0,0,Constants.SCREEN_WIDTH*2/3,Constants.SCREEN_WIDTH/2),Color.YELLOW);
        pauseScreenOutline = new RectPlayer(new Rect(0,0,Constants.SCREEN_WIDTH*2/3+15,Constants.SCREEN_WIDTH/2+15),Color.BLACK);

        //When below screen, show indicator, if (belowScreen)
        indicator = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 50, Constants.SCREEN_HEIGHT / 50), Color.rgb(230, 0, 100));
        indicatorPoint = new Point(playerPoint.x, Constants.SCREEN_HEIGHT - 60);

        //Obstacle values; playerGap (Gap in platform), obstacleGap (Gap between platforms), obstacleHeight (Height of the obstacles), Color
        obstacleManager = new ObstacleManager(Constants.SCREEN_HEIGHT / 10, Constants.SCREEN_HEIGHT / 7, Constants.SCREEN_HEIGHT / 30, Color.BLACK);

        //Tilt controls
        orientationData = new OrientationData();
        orientationData.register();

        frameTime = System.currentTimeMillis();
    }

    //Reset after game over
    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3);
        player.update(playerPoint);
        indicator.update(indicatorPoint);
        obstacleManager = new ObstacleManager(Constants.SCREEN_HEIGHT / 10, Constants.SCREEN_HEIGHT / 7, Constants.SCREEN_HEIGHT / 30, Color.BLACK);
        movingPlayer = false;
        paused = false;
    }


    @Override
    public void terminate() {
        ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        float mx = event.getX();
        float my = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY()))
                    movingPlayer = true;
                //when tapping screen, if game over and time after game over >= 1 sec, start again

                break;
            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;

                if (gameOver && System.currentTimeMillis() - gameOverTime >= 1000) {
                    reset();
                    gameOver = false;
                    //resets reference point for tilt controls
                    orientationData.newGame();
                    break;
                }

                if (paused) {
                    paused = false;
                    obstacleManager.pauseTime += System.currentTimeMillis() - obstacleManager.pauseStart;
                    break;
                }
                //TODO MotionEvent waarden niet helemaal gelijk aan de zichtbare pauze knop, werkt goed genoeg
                if (!paused && mx >= (Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 12) - ((Constants.SCREEN_HEIGHT / 18) / 2)
                        && mx <= (Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 12) + ((Constants.SCREEN_HEIGHT / 18) / 2)) {
                    if (my >= Constants.SCREEN_WIDTH / 12 - ((Constants.SCREEN_HEIGHT / 18) / 2) && my <= Constants.SCREEN_WIDTH / 12 + ((Constants.SCREEN_HEIGHT / 18) / 2)) {
                        paused = true;
                        obstacleManager.pauseStart = System.currentTimeMillis();

                        break;
                    }
                }
                break;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.YELLOW);
        //TODO Change screen when hitting certain score
      /* if (score == 2){
            canvas.drawColor(Color.GREEN);}*/
      /*if (score == 2) {
            background.draw(canvas);
        }*/

        player.draw(canvas);
        obstacleManager.draw(canvas);
        pauseLine1.draw(canvas);
        pauseLine2.draw(canvas);

        if (belowScreen) {
            indicator.draw(canvas);
        }

        if (gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Touch to replay");
        }

        if (paused) {
            pauseScreenOutline.draw(canvas);
            pauseScreen.draw(canvas);
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Touch to resume");
        }
    }

    @Override
    public void update() {
        boolean TouchSide = false;
        boolean TouchTop = false;

        if (!gameOver && !paused) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;

            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();

            //Screen bounds, spring van ene kant naar andere kant op scherm
            if (playerPoint.x < 0)
                playerPoint.x = Constants.SCREEN_WIDTH;
            else if (playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = 0;

            if (playerPoint.y < 0)
                gameOver = true;

            // updates the location of player to playerPoint
            player.update(playerPoint);

            obstacleManager.update();
            indicator.update(indicatorPoint);
            pauseLine1.update(pausePoint1);
            pauseLine2.update(pausePoint2);



            //collision (+y movement)
            Rect colRect = obstacleManager.playerCollide(player);
            if (colRect != null) {

                float Th = obstacleManager.accel * 55;
                Rect play = player.getRectangle();

                if (colRect.top + Th < play.bottom) {
                    if (colRect.left > 0) {
                        playerPoint.x = colRect.left - (play.width() / 2) + 2;
                        TouchSide = true;
                    } else {
                        playerPoint.x = colRect.right + (play.width() / 2) - 2;
                        TouchSide = true;
                    }
                } else {
                    playerPoint.y = colRect.top - (play.height() / 2);
                    TouchSide = false;
                    TouchTop = true;
                }
            }

            if (!TouchTop) {
                //TODO refereer naar data van ChooseLevel
                //if(ChooseLevel.diff = 1) {
                    playerPoint.y += 18 * (obstacleManager.accel * 6 / 10);
                //} else if (ChooseLevel.diff = 2)


            }

            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null && !TouchSide) {
                //movement y-direction (delta pitch)
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                //movement x-direction (delta roll)
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                //adjust xSpeed for sensitivity of tilt controls
                //TODO add sensitivity option
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 700f;

                // If number of pixels player is moving > 5, return xSpeed*elapsedTime, otherwise add 0.
                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                indicatorPoint.x = playerPoint.x;
                //playerPoint.y -=Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }
            //When below screen, show indicator
            if (playerPoint.y > Constants.SCREEN_HEIGHT) {
                belowScreen = true;
            } else belowScreen = false;
            //playerPoint can't go beneath 2 obstacles. Preventing the player to fall beneath spawn point of obstacles.
            if (playerPoint.y > Constants.SCREEN_HEIGHT + 2 * obstacleManager.getObstacleGap()) {
                playerPoint.y = (Constants.SCREEN_HEIGHT + 2 * obstacleManager.getObstacleGap());
            }
        } else {
            obstacleManager.StartTime();
            frameTime = System.currentTimeMillis();
            pauseScreen.update(pauseScreenPoint);
            pauseScreenOutline.update(pauseScreenPoint);
        }
    }

    //draw text in center
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
