package com.blockr.domain.gameworld;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return getX() == position.getX() &&
                getY() == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
