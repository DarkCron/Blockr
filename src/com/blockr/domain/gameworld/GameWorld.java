package com.blockr.domain.gameworld;

public class GameWorld implements ReadOnlyGameWorld {

    public int getWidth(){
        return grid[0].length;
    }

    public int getHeight(){
        return grid.length;
    }

    public TileType getTileType(int x, int y){

        if(x < 0 || x >= getWidth()){
            throw new IllegalArgumentException(String.format("x must lie between 0 and %s", getWidth()));
        }

        if(y < 0 || y >= getHeight()){
            throw new IllegalArgumentException(String.format("y must lie between 0 and %s", getHeight()));
        }

        return grid[y][x];
    }

    private final TileType[][] grid;

    public Position getStartPosition(){
        return startPosition;
    }

    private final Position startPosition;

    public Direction getStartDirection(){
        return startDirection;
    }

    private final Direction startDirection;

    public Position getGoalPosition(){
        return goalPosition;
    }

    private final Position goalPosition;

    public Position getRobotPosition(){
        return robotPosition;
    }

    private Position robotPosition;

    public Direction getRobotDirection(){
        return robotDirection;
    }

    private Direction robotDirection;

    public GameWorld(TileType[][] grid, Position startPosition, Direction startDirection, Position goalPosition){

        throwIfNull(grid, "grid");
        throwIfNull(startPosition, "startPosition");
        throwIfNull(startDirection, "startDirection");
        throwIfNull(goalPosition, "goalPosition");

        this.grid = grid;
        this.startPosition = startPosition;
        this.startDirection = startDirection;
        this.goalPosition = goalPosition;
    }

    private void throwIfNull(Object object, String name){
        if(object != null)
            return;

        throw new IllegalArgumentException(String.format("%s must be effective", name));
    }

    public void reset(){
        this.robotPosition = getStartPosition();
        this.robotDirection = getStartDirection();
    }

    public void turnLeft(){
        robotDirection = robotDirection.turnLeft();
    }

    public void turnRight(){
        robotDirection = robotDirection.turnRight();
    }

    public void moveForward(){

        var newPosition = robotPosition.translate(getRobotDirection().getOffset());

        if(getTileType(newPosition.getX(), newPosition.getY()) == TileType.Blocked)
            return;

        robotPosition = newPosition;
    }
}
