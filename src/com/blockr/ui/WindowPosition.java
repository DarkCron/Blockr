package com.blockr.ui.components;

public class ScreenPosition {

    public int getX(){
        return x;
    }

    private final int x;

    public int getY(){
        return y;
    }

    private final int y;

    public ScreenPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

}
