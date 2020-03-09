package com.blockr.domain.gameworld;

public class Position {

    public int getX(){
        return x;
    }

    private final int x;

    public int getY(){
        return y;
    }

    private final int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position translate(Orientation.Offset offset) {
        return new Position(getX() + offset.getX(), getY() + offset.getY());
    }
}
