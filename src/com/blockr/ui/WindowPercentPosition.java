package com.blockr.ui;

public class WindowPercentPosition {
    public float getX(){
        return x;
    }

    private final float x;

    public float getY(){
        return y;
    }

    private final float y;

    public WindowPercentPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
}
