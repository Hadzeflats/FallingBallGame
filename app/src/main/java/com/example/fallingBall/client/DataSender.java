package com.example.fallingBall.client;

import com.example.fallingBall.multiPlayer.Constants;

import java.io.PrintWriter;

class DataSender extends Thread {
    private PrintWriter out;
    private Client client;

    DataSender(Client client) throws Exception {
        this.client = client;
        out = new PrintWriter(client.getClientSocket().getOutputStream(), true);
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!client.isGameOver()) {
                    Thread.sleep(25);
//                    System.out.println(((double) client.getPlayer().x / (double) Constants.SCREEN_WIDTH) + "," + ((double)client.getPlayer().x / (double)Constants.SCREEN_HEIGHT));
                    out.println("#" + ((double) client.getPlayer().x / (double) Constants.SCREEN_WIDTH) + "," + ((double)client.getPlayer().y / (double)Constants.SCREEN_HEIGHT));
                }
                else {
                    out.println("@ded");
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}