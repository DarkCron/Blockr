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

    public TileType getTileType(Position position){
        return getTileType(position.getX(), position.getY());
    }

    private final TileType[][] grid;

    public Position getStartPosition(){
        return startPosition;
    }

    private final Position startPosition;

    public Orientation getStartOrientation(){
        return startOrientation;
    }

    private final Orientation startOrientation;

    public Position getGoalPosition(){
        return goalPosition;
    }

    private final Position goalPosition;

    public Position getRobotPosition(){
        return robotPosition;
    }

    private Position robotPosition;

    public Orientation getRobotOrientation(){
        return robotOrientation;
    }

    private Orientation robotOrientation;

    public GameWorld(TileType[][] grid, Position startPosition, Orientation startOrientation, Position goalPosition){

        throwIfNull(grid, "grid");
        throwIfNull(startPosition, "startPosition");
        throwIfNull(startOrientation, "startDirection");
        throwIfNull(goalPosition, "goalPosition");

        this.grid = grid;
        this.startPosition = startPosition;
        this.startOrientation = startOrientation;
        this.goalPosition = goalPosition;
    }

    private void throwIfNull(Object object, String name){
        if(object != null)
            return;

        throw new IllegalArgumentException(String.format("%s must be effective", name));
    }

    public void reset(){
        this.robotPosition = getStartPosition();
        this.robotOrientation = getStartOrientation();
    }

    public void turnLeft(){
        robotOrientation = robotOrientation.turnLeft();
    }

    public void turnRight(){
        robotOrientation = robotOrientation.turnRight();
    }

    public void moveForward(){

        var newPosition = robotPosition.translate(getRobotOrientation().getOffset());

        if(getTileType(newPosition.getX(), newPosition.getY()) == TileType.Blocked)
            return;

        robotPosition = newPosition;
    }
}
