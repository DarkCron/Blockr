package gameworld;
import com.robotgame.Orientation;
import com.robotgame.Position;
import com.robotgame.RobotGameWorld;
import com.robotgame.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RobotGameWorldTest {

    private static final TileType[][] GRID = {
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
    };

    private static final int WIDTH = GRID[0].length;
    private static final int HEIGHT = GRID.length;

    private static RobotGameWorld gameWorld;

    @BeforeEach
    public void initializeGameWorld(){
        gameWorld = new RobotGameWorld(GRID, new Position(1, 1), Orientation.SOUTH,  new Position(3, 3));
    }

    @ParameterizedTest
    @MethodSource("getPositionsOutsideOfGrid")
    public void getTileType_OutsideGrid(Position position){
        Assertions.assertThrows(IllegalArgumentException.class, () -> gameWorld.getTileType(position));
    }

    private static Stream<Position> getPositionsOutsideOfGrid(){
        return Stream.of(
                new Position(-1, 0),
                new Position(WIDTH, 0),
                new Position(0, -1),
                new Position(0, HEIGHT)
        );
    }

    @ParameterizedTest
    @MethodSource("getPositionsInsideGrid")
    public void getTileType_InsideGrid(Position position){
        var tileType = gameWorld.getTileType(position);
        Assertions.assertEquals(GRID[position.getX()][position.getY()], tileType);
    }

    public static Stream<Position> getPositionsInsideGrid(){
        return IntStream.range(0, WIDTH).mapToObj(x -> IntStream.range(0, HEIGHT).mapToObj(y -> new Position(x, y))).flatMap(Function.identity());
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void moveForward_NotFacingWall(Orientation orientation){

        final Position CENTER = new Position(2, 2);
        var gameWorld = new RobotGameWorld(GRID, CENTER, orientation, new Position(3, 3));

        gameWorld.moveForward();
        var newPosition = gameWorld.getRobotPosition();

        Assertions.assertEquals(CENTER.translate(orientation.getOffset()), newPosition);
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void moveForward_FacingWall(Orientation orientation){

        final TileType[][] GRID = new TileType[][]{
                {TileType.Blocked, TileType.Blocked, TileType.Blocked},
                {TileType.Blocked, TileType.Free, TileType.Blocked},
                {TileType.Blocked, TileType.Blocked, TileType.Blocked},
        };

        final Position CENTER = new Position(1, 1);

        var gameWorld = new RobotGameWorld(GRID, CENTER, orientation, CENTER);

        gameWorld.moveForward();
        var newPosition = gameWorld.getRobotPosition();

        Assertions.assertEquals(CENTER, newPosition);
    }

    @ParameterizedTest
    @EnumSource(Orientation.class)
    public void moveForward_OutsideOfWorld(Orientation orientation){

        final TileType[][] GRID = new TileType[][]{
                {TileType.Free}
        };

        final Position START_POSITION = new Position(0, 0);

        var gameWorld = new RobotGameWorld(GRID, START_POSITION, orientation, START_POSITION);
        gameWorld.moveForward();

        Assertions.assertEquals(START_POSITION, gameWorld.getRobotPosition());
    }

    @Test
    public void reset() {

        gameWorld.moveForward();
        gameWorld.turnLeft();

        gameWorld.reset();

        Assertions.assertEquals(gameWorld.getStartPosition(), gameWorld.getRobotPosition());
        Assertions.assertEquals(gameWorld.getStartOrientation(), gameWorld.getRobotOrientation());
    }


    private static final TileType[][] GRID2 = new TileType[][]{
            {TileType.Blocked, TileType.Blocked, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free},
            {TileType.Blocked, TileType.Blocked, TileType.Blocked}
    };

    @Test
    public void isWallInFront_FacingWall(){

        var gameWorld = new RobotGameWorld(GRID2, new Position(1, 1), Orientation.NORTH, new Position(1, 1));
        Assertions.assertTrue(gameWorld.isWallInFront());
    }

    @Test
    public void evaluate_NotFacingWall(){

        var gameWorld = new RobotGameWorld(GRID2, new Position(1, 1), Orientation.EAST, new Position(1, 1));
        Assertions.assertFalse(gameWorld.isWallInFront());
    }
}
