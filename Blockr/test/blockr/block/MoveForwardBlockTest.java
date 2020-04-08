package blockr.block;

import com.blockr.domain.block.MoveForwardBlock;
import com.gameworld.Action;
import com.gameworld.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MoveForwardBlockTest {

    @Test
    public void execute(){

        var block = new MoveForwardBlock();
        var next = new MoveForwardBlock();

        block.setNext(next);

        var mockedGameWorld = mock(GameWorld.class);

        var result = block.execute(mockedGameWorld);

        verify(mockedGameWorld).execute(Action.MOVE_FORWARD);
        Assertions.assertEquals(next, result);
    }

}
