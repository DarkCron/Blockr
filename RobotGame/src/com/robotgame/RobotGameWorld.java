package impl.root;

import com.gameworld.Action;
import com.gameworld.GameWorld;
import com.gameworld.GameWorldSnapshot;
import com.gameworld.Predicate;

import java.awt.*;

public class RobotGameWorld implements GameWorld  {

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

    public RobotGameWorld(){
        this(new TileType[][]{
                {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
                {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
                {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
                {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
                {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked}
        }, new Position(1, 1), Orientation.EAST, new Position(3, 3));
    }

    public RobotGameWorld(TileType[][] grid, Position startPosition, Orientation startOrientation, Position goalPosition){

        throwIfNull(grid, "grid");
        throwIfNull(startPosition, "startPosition");
        throwIfNull(startOrientation, "startDirection");
        throwIfNull(goalPosition, "goalPosition");

        this.grid = grid;
        this.startPosition = startPosition;
        this.startOrientation = startOrientation;
        this.goalPosition = goalPosition;
        this.robotPosition = startPosition;
        this.robotOrientation = startOrientation;
    }

    private void throwIfNull(Object object, String name)
    {
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

        if(!isInsideGameWorld(newPosition))
            return;

        if(getTileType(newPosition.getX(), newPosition.getY()) == TileType.Blocked)
            return;

        robotPosition = newPosition;
    }

    public boolean isWallInFront(){
        return getTileType(getRobotPosition().translate(getRobotOrientation().getOffset())) == TileType.Blocked;
    }

    private boolean isInsideGameWorld(Position position){
        return position.getX() > 0 && position.getX() < getWidth() && position.getY() > 0 && position.getY() < getHeight();
    }

    public ExecutionResult execute(Action action) {

        switch(action){

            case MOVE_FORWARD:
                moveForward();
                break;
            case TURN_LEFT:
                turnLeft();
                break;
            case TURN_RIGHT:
                turnRight();
                break;
            default:
                throw new IllegalArgumentException("The given action is not supported");
        }

        if(getGoalPosition().equals(getRobotPosition()))
            return ExecutionResult.EndOfGame;

        return ExecutionResult.Sucesss;
    }

    @Override
    public boolean evaluate(Predicate predicate) {

        switch(predicate){
            case WALL_IN_FRONT:
                return isWallInFront();
            default:
                throw new IllegalStateException("The given predicate is not supported");
        }
    }

    @Override
    public void draw(Graphics graphics) {

        var clip = graphics.getClipBounds();

        var gridWidth = getWidth();
        var gridHeight = getHeight();

        var tileWidth = (int)clip.getWidth() / gridWidth;
        var tileHeight = (int)clip.getHeight() / gridHeight;

        for(int i = 0; i < gridWidth * gridHeight; i++){

            var tileX = i % gridWidth;
            var tileY = i / gridWidth;

            var offsetX = tileX * tileWidth;
            var offsetY = tileY * tileHeight;

            drawTile(graphics.create(offsetX, offsetY, tileWidth, tileHeight), new Position(tileX, tileY));
        }

    }

    @Override
    public GameWorldSnapshot takeSnapshot() {
        return new RobotGameWorldSnapshot(getRobotPosition(), getRobotOrientation());
    }

    @Override
    public void restoreSnapshot(GameWorldSnapshot gameWorldSnapshot) {

        if(!(gameWorldSnapshot instanceof RobotGameWorldSnapshot))
            throw new IllegalArgumentException("the gameWorldSnapshot is not a valid snapshot");

        var snapshot = (RobotGameWorldSnapshot)gameWorldSnapshot;
        this.robotPosition = snapshot.getRobotPosition();
        this.robotOrientation = snapshot.getRobotOrientation();
    }

    private void drawTile(Graphics graphics, Position position){

        var tileType = getTileType(position.getX(), position.getY());
        var clip = graphics.getClipBounds();

        var color = tileTypeToColor(tileType);
        graphics.setColor(color);
        graphics.fillRect(0, 0, (int)clip.getWidth(), (int)clip.getHeight());

        if(getGoalPosition().equals(position)){
            GoalTile.draw(graphics);
        }

        if(getRobotPosition().equals(position)){
            RobotTile.draw(graphics, getRobotOrientation());
        }
    }

    private static Color tileTypeToColor(TileType type){
        switch(type){
            case Free:
                return Color.GREEN;
            case Blocked:
                return Color.RED;
        }

        return null;
    }
}
