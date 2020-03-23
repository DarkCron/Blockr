package test.blockr.block;

import com.blockr.domain.block.*;
import com.blockr.domain.gameworld.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class WhileBlockTest {

    private static final MoveForwardBlock NEXT1 = new MoveForwardBlock();
    private static final TurnBlock NEXT2 = new TurnBlock();
    private GameWorld gameWorld;

    static {
        NEXT1.setNext(NEXT2);
    }

    @BeforeEach
    public void setup(){
        gameWorld = mock(GameWorld.class);
    }

    @Test
    public void execute_First_ConditionFalse(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(false);

        var whileBlock = new WhileBlock();
        whileBlock.setNext(NEXT1);
        whileBlock.setCondition(condition);

        var result = whileBlock.execute(gameWorld);

        verify(condition).evaluate(gameWorld);
        Assertions.assertEquals(NEXT1, result);
    }

    @Test
    public void execute_First_ConditionTrue(){

        //arrange
        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var body = mock(StatementBlock.class);

        var whileBlock = new WhileBlock();
        whileBlock.setBody(body);
        whileBlock.setCondition(condition);
        whileBlock.execute(gameWorld);

        //act
        var result = whileBlock.execute(gameWorld);

        //assert
        verify(body).execute(gameWorld);
        Assertions.assertEquals(whileBlock, result);
    }

    @Test
    public void execute_LastBodyBlock(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(gameWorld)).thenReturn(true);

        var body1 = mock(StatementBlock.class);
        var body2 = mock(StatementBlock.class);
        when(body1.execute(gameWorld)).thenReturn(body2);

        var whileBlock = new WhileBlock();
        whileBlock.setBody(body1);
        whileBlock.setCondition(condition);
        whileBlock.execute(gameWorld);
        whileBlock.execute(gameWorld);

        var result = whileBlock.execute(gameWorld);

        verify(body2).execute(gameWorld);
        Assertions.assertEquals(whileBlock, result);
    }

    @Test
    public void getActive_BeforeExecute(){
        var whileBlock = new WhileBlock();
        Assertions.assertEquals(whileBlock, whileBlock.getActive());
    }

    @Test
    public void getActive_FirstExecute(){

        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var whileBlock = new WhileBlock();
        whileBlock.setCondition(condition);
        whileBlock.setBody(NEXT1);

        whileBlock.execute(gameWorld);

        var result = whileBlock.getActive();

        Assertions.assertEquals(NEXT1, result);
    }

    @Test
    public void getActive_SecondExecute(){
        var condition = mock(ConditionBlock.class);
        when(condition.evaluate(any(GameWorld.class))).thenReturn(true);

        var whileBlock = new WhileBlock();
        whileBlock.setCondition(condition);
        whileBlock.setBody(NEXT1);
        whileBlock.execute(gameWorld);
        whileBlock.execute(gameWorld);

        var result = whileBlock.getActive();

        Assertions.assertEquals(NEXT2, result);
    }
}
