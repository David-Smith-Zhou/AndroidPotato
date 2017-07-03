package com.androidpotato.widget.dto;

/**
 * Created by David on 2017/6/21 0021.
 */

public class Coordinate {
    private float x;
    private float y;
//    private boolean isShow;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
//        this.isShow = true;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

//    public void setShow(boolean show) {
//        isShow = show;
//    }
//
//    public boolean isShow() {
//        return isShow;
//    }
}
