package com.amore.springboot.explore.bean;

import lombok.*;

@Data
public class App {

    private String id;
    private String appName;
    private String appPackageName ;
    private String appKey;
    private String appActivity;
    private int sleep;
    private int moveCount;
    private boolean move;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(String appActivity) {
        this.appActivity = appActivity;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }


    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getSleep() {
        return sleep;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public String toString() {
        return "App{" +
                "id='" + id + '\'' +
                ", appName='" + appName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", appActivity='" + appActivity + '\'' +
                '}';
    }

}
