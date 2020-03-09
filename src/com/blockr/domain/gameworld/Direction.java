package com.blockr.domain.gameworld;

public enum Direction {

    NORTH(new Offset(0, -1)),
    EAST(new Offset(1, 0)),
    SOUTH(new Offset(0, 1)),
    WEST(new Offset(-1, 0));

    public Offset getOffset(){
        return offset;
    }

    private final Offset offset;

    Direction(Offset offset){
        this.offset = offset;
    }

    public Direction turnLeft(){
        return Direction.values()[(indexOf(this) - 1) % Direction.values().length];
    }

    public Direction turnRight(){
        return Direction.values()[(indexOf(this) + 1) % Direction.values().length];
    }

    private static int indexOf(Direction direction){
        for(var i = 0; i < Direction.values().length; i++){
            if(Direction.values()[i] == direction)
                return i;
        }

        return -1;
    }

    public static class Offset {

        public int getX(){
            return x;
        }

        private final int x;

        public int getY(){
            return y;
        }

        private final int y;

        public Offset(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
