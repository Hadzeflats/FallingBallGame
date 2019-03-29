package com.hadzy.fallingball.client;

import android.graphics.Point;

import com.hadzy.fallingball.GameplayScene;
import com.hadzy.fallingball.RectPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            clientSocket = new Socket("145.94.234.155", 8069);
            DataSender dataService = new DataSender(this);
            DataReceiver dataReciever = new DataReceiver(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerPoint(int x, int y, int playerID){
        gameplayScene.setPlayerPoint2(x, y);
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
