package test.blockr.block;

import com.blockr.domain.block.ConditionBlock;
import com.blockr.domain.block.NotBlock;
import com.blockr.domain.gameworld.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.Mockito.*;

public class NotBlockTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void evaluate(boolean returnValue){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(returnValue);

        var gameWorld = mock(GameWorld.class);

        var notBlock = new NotBlock();
        notBlock.setCondition(condition);

        var result = notBlock.evaluate(gameWorld);

        Assertions.assertEquals(!returnValue, result);
    }

}
