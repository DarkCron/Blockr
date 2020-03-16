package tests.blockprogram;

import com.blockr.domain.block.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectToTests {

    @Test
    public void connectSimpleStatement(){
        BlockProgram testProgram = new BlockProgram(null);
        StatementBlock simpleStatement1 = new MoveForwardBlock();
        StatementBlock simpleStatement2 = new MoveForwardBlock();

        testProgram.addBlock(simpleStatement1);
        testProgram.connectToStatementSocket(simpleStatement1,simpleStatement2);
        assertEquals(simpleStatement2,simpleStatement1.getNext());
        assertEquals(simpleStatement1, simpleStatement2.getPrevious());
        assertEquals(null,simpleStatement1.getPrevious());
        assertEquals(null,simpleStatement2.getNext());

    }

    @Test //Test case1: Connect condition to empty CFB
    public void connectConditionToCFB(){
        BlockProgram testProgram = new BlockProgram(null);
        WhileBlock simpleCFB = new WhileBlock();
        ConditionBlock simpleCondition = new WallInFrontBlock();

        testProgram.addBlock(simpleCFB);
        testProgram.connectToStatementSocket(simpleCFB,simpleCondition);
        assertEquals(simpleCondition,simpleCFB.getCondition());
    }

    /*
    Test case2: If the CFB already has a connected condition you can only add conditions that have both a plug and socket, example NotBlock
    In all other cases you don't modify the existing block.
     */
    @Test
    public void connectConditionToCFB2(){
        BlockProgram testProgram = new BlockProgram(null);
        WhileBlock simpleCFB = new WhileBlock();
        ConditionBlock simpleCondition = new WallInFrontBlock();
        ConditionBlock otherCondition = new WallInFrontBlock();
        ConditionBlock simpleNotCondition = new WallInFrontBlock();
        simpleCFB.setCondition(simpleCondition);

        testProgram.addBlock(simpleCFB);
        testProgram.connectToStatementSocket(simpleCFB,otherCondition);
        assertEquals(simpleCondition,simpleCFB.getCondition());

        testProgram.connectToStatementSocket(simpleCFB,simpleNotCondition);
        assertEquals(simpleNotCondition,simpleCFB.getCondition());
        assertEquals(simpleCondition,((NotBlock)simpleCFB.getCondition()).getCondition());
    }
}
