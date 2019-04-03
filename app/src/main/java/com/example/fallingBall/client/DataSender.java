package com.example.fallingBall.client;

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
                    System.out.println(client.getPlayer().x + "," + client.getPlayer().y);
                    out.println("#" + client.getPlayer().x + "," + client.getPlayer().y);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}