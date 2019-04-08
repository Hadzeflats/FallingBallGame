package com.example.fallingBall.client;

import android.graphics.Point;

import com.example.fallingBall.multiPlayer.GameplayScene;

import java.net.Socket;

public class Client extends Thread {

    private Socket clientSocket;
    private GameplayScene gameplayScene;


    public Client(GameplayScene gameplayScene) {
        this.gameplayScene = gameplayScene;
        this.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Connecting");
            clientSocket = new Socket("145.94.223.22", 8069);
            DataSender dataService = new DataSender(this);
            DataReceiver dataReciever = new DataReceiver(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerPoint(int x, int y, int playerID){
        gameplayScene.setPlayerPoint2(x, y);
    }

    public void playerDied(){
        gameplayScene.youWon();
    }

    public void startGame(){
        gameplayScene.startNewGame();
    }

    public Point getPlayer(){
        return gameplayScene.getPlayerPoint();
    }

    public Socket getClientSocket() {
        synchronized (clientSocket) {
            return clientSocket;
        }
    }

    public boolean isGameOver(){
        return gameplayScene.isGameOver();
    }
}