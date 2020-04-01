package com.ui;

public class WindowPosition {

    public int getX(){
        return x;
    }

    private final int x;

    public int getY(){
        return y;
    }

    private final int y;

    public WindowPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public WindowPosition plus(WindowPosition other){
        return new WindowPosition(x + other.getX(), y + other.getY());
    }

    public WindowPosition minus(WindowPosition other){
        return new WindowPosition(x - other.getX(), y - other.getY());
    }
}
