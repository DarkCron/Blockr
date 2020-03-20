package test.blockr.block;

import com.blockr.domain.block.MoveForwardBlock;
import com.blockr.domain.block.TurnBlock;
import com.blockr.domain.gameworld.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TurnBlockTest {

    private static final MoveForwardBlock NEXT = new MoveForwardBlock();

    @Test
    public void execute_Left(){

        var turnBlock = new TurnBlock();
        turnBlock.setDirection(TurnBlock.Direction.LEFT);
        turnBlock.setNext(NEXT);

        var mockedGameWorld = mock(GameWorld.class);

        var result = turnBlock.execute(mockedGameWorld);

        verify(mockedGameWorld).turnLeft();
        Assertions.assertEquals(NEXT, result);
    }

    @Test
    public void execute_Right(){

        var turnBlock = new TurnBlock();
        turnBlock.setDirection(TurnBlock.Direction.RIGHT);
        turnBlock.setNext(NEXT);

        var mockedGameWorld = mock(GameWorld.class);

        var result = turnBlock.execute(mockedGameWorld);

        verify(mockedGameWorld).turnRight();
        Assertions.assertEquals(NEXT, result);
    }

    @Test
    public void setDirection_Valid(){

        var turnBlock = new TurnBlock();

        turnBlock.setDirection(TurnBlock.Direction.RIGHT);

        Assertions.assertEquals(TurnBlock.Direction.RIGHT, turnBlock.getDirection());
    }

    @Test
    public void setDirection_Null(){
        var turnBlock = new TurnBlock();
        Assertions.assertThrows(IllegalArgumentException.class, () -> turnBlock.setDirection(null));
    }

}
