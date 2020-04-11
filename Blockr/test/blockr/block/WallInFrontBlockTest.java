package blockr.block;

import com.blockr.domain.block.WallInFrontBlock;
import com.gameworld.GameWorld;
import com.gameworld.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WallInFrontBlockTest {

    @Test
    public void evaluate(){

        var gameWorld = mock(GameWorld.class);

        var wallInFrontBlock = new WallInFrontBlock();
        wallInFrontBlock.evaluate(gameWorld);

        verify(gameWorld).evaluate(Predicate.WALL_IN_FRONT);
    }

}
