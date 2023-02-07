package com.example.protechv6;

public class Sensors {

    String sensor, status, lastTriggered;
    int imageId;

    public Sensors(String sensor, String status, String lastTriggered, int imageId) {
        this.sensor = sensor;
        this.status = status;
        this.lastTriggered = lastTriggered;
        this.imageId = imageId;
    }
}

