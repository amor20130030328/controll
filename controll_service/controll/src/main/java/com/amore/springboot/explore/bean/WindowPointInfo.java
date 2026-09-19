package com.amore.springboot.explore.bean;

public class WindowPointInfo {

    private String deviceId;
    private double x ;
    private double y ;
    public WindowPointInfo(String deviceId, double x , double y) {
        this.deviceId = deviceId ;
        this.x = x ;
        this.y = y ;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
