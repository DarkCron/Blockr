package blockr.block;

import com.blockr.domain.block.WallInFrontBlock;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.Orientation;
import com.blockr.domain.gameworld.Position;
import com.blockr.domain.gameworld.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WallInFrontBlockTest {

    private static final TileType[][] GRID = new TileType[][]{
            {TileType.Blocked, TileType.Blocked, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free},
            {TileType.Blocked, TileType.Blocked, TileType.Blocked}
    };

    @Test
    public void evaluate_FacingWall(){

        var gameWorld = new GameWorld(GRID, new Position(1, 1), Orientation.NORTH, new Position(1, 1));

        var wallInFrontBlock = new WallInFrontBlock();

        var result = wallInFrontBlock.evaluate(gameWorld);

        Assertions.assertTrue(result);
    }

    @Test
    public void evaluate_NotFacingWall(){

        var gameWorld = new GameWorld(GRID, new Position(1, 1), Orientation.EAST, new Position(1, 1));

        var wallInFrontBlock = new WallInFrontBlock();

        var result = wallInFrontBlock.evaluate(gameWorld);

        Assertions.assertFalse(result);
    }

}
