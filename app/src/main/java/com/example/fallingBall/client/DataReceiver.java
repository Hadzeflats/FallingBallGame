package com.example.fallingBall.client;

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
                String[] locs = inputLine.split(",");
                int x = Integer.parseInt(locs[1]);
                int y = Integer.parseInt(locs[2]);
                client.updatePlayerPoint(x, y, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}