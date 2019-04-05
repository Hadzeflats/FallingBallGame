package com.example.fallingBall.client;

import com.example.fallingBall.multiPlayer.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DataReceiver extends Thread {
    private BufferedReader in;
    private Client client;

    DataReceiver(Client client) throws Exception {
        this.client = client;
        in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
        this.start();
    }

    @Override
    public void run() {
        try {
            String inputLine = null;
            System.out.println("Reading");
            while ((inputLine = in.readLine()) != null) {
//                if (inputLine.startsWith("[INFO]")){
//                    //TODO maak hub scherm
//                }
                if (inputLine.equals("start")){
                    client.startGame();
                    break;
                }
            }
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("&")) {
                    String[] locs = inputLine.split(",");
                    int x = (int)((Double.parseDouble(locs[1])* (double) Constants.SCREEN_WIDTH));
                    int y = (int)((Double.parseDouble(locs[2])* (double) Constants.SCREEN_HEIGHT));
                    client.updatePlayerPoint(x, y, 0);
                }
                if (inputLine.startsWith("@")) {
                    client.playerDied();
                    return;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}