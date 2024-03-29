package com.example.fallingBall.multiPlayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.fallingBall.client.Client;
import com.example.fallingBall.client.DataReceiver;

import static com.example.fallingBall.singlePlayer.SceneManager.ACTIVE_SCENE;

public class GameplayScene implements Scene {

    private Rect r = new Rect();
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private RectPlayer indicator;
    private Point indicatorPoint;
    private boolean belowScreen = false;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private boolean gameWin = false;
    private boolean gameLose = false;
    private long gameOverTime;
    private boolean paused = true;

    //multiplayer
    private RectPlayer player2;
    private Point playerPoint2;
    private RectPlayer indicator2;
    private Point indicatorPoint2;
    private boolean belowScreen2 = false;

    private OrientationData orientationData;
    private DataReceiver dataReceiver;
    private long frameTime;

    public GameplayScene() {
        player = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 25, Constants.SCREEN_HEIGHT / 25), Color.rgb(230, 0, 100));
        //Start in the center of the screen (x-value), start on 3/4 of the screen (y-value)
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3);

        //When below screen, show indicator
        indicator = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 50, Constants.SCREEN_HEIGHT / 50), Color.rgb(230, 0, 100));
        indicatorPoint = new Point(playerPoint.x, Constants.SCREEN_HEIGHT - 60);
        indicator2 = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 50, Constants.SCREEN_HEIGHT / 50), Color.rgb(0, 100, 230));
        indicatorPoint2 = new Point(playerPoint.x, Constants.SCREEN_HEIGHT - 60);

        obstacleManager = new ObstacleManager(Constants.SCREEN_HEIGHT / 10, Constants.SCREEN_HEIGHT / 7, Constants.SCREEN_HEIGHT / 30, Color.BLACK);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

        //multi
        Client client = new Client(this);
        player2 = new RectPlayer(new Rect(0, 0, Constants.SCREEN_HEIGHT / 25, Constants.SCREEN_HEIGHT / 25), Color.rgb(0, 100, 230));
        playerPoint2 = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT + 100);
    }

    //When one player wins, reset game
    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3);
        player.update(playerPoint);
        playerPoint2 = new Point(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT + 100);
        player2.update(playerPoint2);
        indicator.update(indicatorPoint);
        indicator2.update(indicatorPoint2);

        obstacleManager = new ObstacleManager(Constants.SCREEN_HEIGHT / 10, Constants.SCREEN_HEIGHT / 7, Constants.SCREEN_HEIGHT / 30, Color.BLACK);
        movingPlayer = false;
        paused = true;
        gameLose = false;
        gameWin = false;
        Client client = new Client(this);
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
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
                    break;
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

        player.draw(canvas);
        obstacleManager.draw(canvas);

        //multi
        player2.draw(canvas);

        if (belowScreen)
            indicator.draw(canvas);

        if (belowScreen2)
            indicator2.draw(canvas);


        if (gameLose) {
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "You lost :(");
        }

        if (gameWin) {
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "You win :)");
        }


        if (paused /*&& System.currentTimeMillis()<100*/) {
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Waiting for player 2");
        }
    }


    @Override
    public void update() {
        boolean TouchSide = false;
        boolean TouchTop = false;
//        MultiGameStart();

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

            if (playerPoint.y < 0) {
                gameOver = true;
                gameLose = true;
                gameWin = false;
            }

            // updates the location of player to playerPoint
            player.update(playerPoint);
            //multi
            player2.update(playerPoint2);

            obstacleManager.update();
            indicator.update(indicatorPoint);
            indicator2.update(indicatorPoint2);

            //collision (+y movement)
            Rect colRect = obstacleManager.playerCollide(player);
            if (colRect != null) {

                double Th = obstacleManager.accel * 55;
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

            if (!TouchTop)
                playerPoint.y += 18 * (obstacleManager.accel * 6 / 10);

            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null && !TouchSide) {
                //movement y-direction (delta pitch)
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                //movement x-direction (delta roll)
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 700f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / 500f;

                // If number of pixels player is moving > 5, return xSpeed*elapsedTime, otherwise add 0.
                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                indicatorPoint.x = playerPoint.x;
                indicatorPoint2.x = playerPoint2.x;
                //playerPoint.y -=Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }
            //When below screen, show indicator
            if (playerPoint.y > Constants.SCREEN_HEIGHT) {
                belowScreen = true;
            } else belowScreen = false;
            if (playerPoint2.y > Constants.SCREEN_HEIGHT) {
                belowScreen2 = true;
            } else belowScreen2 = false;
            //playerPoint can't go beneath 2 obstacles. Preventing the player to fall beneath spawn point of obstacles.
            if (playerPoint.y > Constants.SCREEN_HEIGHT + 2 * obstacleManager.getObstacleGap()) {
                playerPoint.y = (Constants.SCREEN_HEIGHT + 2 * obstacleManager.getObstacleGap());
            }
        } else
            obstacleManager.StartTime();
    }

    public void setPlayerPoint2(int x, int y) {
        synchronized (playerPoint2) {
            playerPoint2.x = x;
            playerPoint2.y = y;
        }
    }

    public void youWon() {
        if (!gameLose) {
            gameWin = true;
            gameLose = false;
            gameOver = true;
        }
    }

    public void startNewGame() {
        paused = false;

    }

    public boolean isGameOver() {
        return gameOver;
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

    public Point getPlayerPoint() {
        return playerPoint;
    }
}
