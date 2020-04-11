package blockr.block;

import com.blockr.domain.block.*;
import com.gameworld.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IfBlockTest {

    private static final MoveForwardBlock NEXT1 = new MoveForwardBlock();
    private static final TurnBlock NEXT2 = new TurnBlock();
    private GameWorld gameWorld;

    static {
        NEXT1.setNext(NEXT2);
        NEXT2.setPrevious(NEXT1);
    }

    @BeforeEach
    public void setup(){
        gameWorld = mock(GameWorld.class);
    }

    @Test
    public void execute_First_ConditionFalse(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(false);

        var body = mock(StatementBlock.class);

        var ifBlock = new IfBlock();
        ifBlock.setNext(NEXT1);
        ifBlock.setBody(body);
        ifBlock.setCondition(condition);

        var result = ifBlock.execute(gameWorld);

        verify(condition).evaluate(gameWorld);
        Assertions.assertEquals(NEXT1, result);
    }

    @Test
    public void execute_First_ConditionTrue(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var body = mock(StatementBlock.class);

        var ifBlock = new IfBlock();
        ifBlock.setNext(NEXT1);
        ifBlock.setBody(body);
        ifBlock.setCondition(condition);

        var result = ifBlock.execute(gameWorld);

        verify(condition).evaluate(gameWorld);
        Assertions.assertEquals(ifBlock, result);
    }

    @Test
    public void execute_LastBodyBlockActive(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var body1 = mock(StatementBlock.class);
        var body2 = mock(StatementBlock.class);
        when(body1.execute(gameWorld)).thenReturn(body2);

        var ifBlock = new IfBlock();
        ifBlock.setNext(NEXT1);
        ifBlock.setBody(body1);
        ifBlock.setCondition(condition);
        ifBlock.execute(gameWorld);
        ifBlock.execute(gameWorld);

        var result = ifBlock.execute(gameWorld);

        verify(body2).execute(gameWorld);
        Assertions.assertEquals(NEXT1, result);
    }

    @Test
    public void getActive_BeforeExecute(){
        var ifBlock = new IfBlock();
        Assertions.assertEquals(ifBlock, ifBlock.getActive());
    }

    @Test
    public void getActive_FirstExecute(){
        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var ifBlock = new IfBlock();
        ifBlock.setCondition(condition);
        ifBlock.setBody(NEXT1);

        ifBlock.execute(gameWorld);

        var result = ifBlock.getActive();

        Assertions.assertEquals(NEXT1, result);
    }

    @Test
    public void getActive_SecondExecute(){
        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var ifBlock = new WhileBlock();
        ifBlock.setCondition(condition);
        ifBlock.setBody(NEXT1);
        ifBlock.execute(gameWorld);
        ifBlock.execute(gameWorld);

        var result = ifBlock.getActive();

        Assertions.assertEquals(NEXT2, result);
    }

}
