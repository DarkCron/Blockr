package tests.blockprogram;

import com.blockr.domain.block.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ConnectToTests {

    @Test
    public void defensiveTest(){
        BlockProgram testProgram = new BlockProgram(null);
        StatementBlock simpleStatement1 = new MoveForwardBlock();
        StatementBlock simpleStatement2 = new MoveForwardBlock();

        try {
            testProgram.addBlock(null);
            fail();
        }catch (Exception e){
        }

        try {
            testProgram.connectToStatementSocket((StatementBlock)null, (StatementBlock)null);
            fail();
        }catch (Exception e){
        }
        try {
            testProgram.connectToStatementSocket((ControlFlowBlock) null, (ConditionBlock)null);
            fail();
        }catch (Exception e){
        }

        try {
            testProgram.connectToStatementSocket(null, new MoveForwardBlock());
            fail();
        }catch (Exception e){
        }

        try {
            testProgram.connectToStatementSocket(new MoveForwardBlock(), null);
            fail();
        }catch (Exception e){
        }

        testProgram.addBlock(simpleStatement1);
        try {
            testProgram.connectToStatementSocket(simpleStatement1, null);
            fail();
        }catch (Exception e){
        }

        try {
            testProgram.connectToStatementSocket(simpleStatement2, null);
            fail();
        }catch (Exception e){
        }

        testProgram.connectToStatementSocket(simpleStatement1, simpleStatement2);
    }

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

    @Test
    public void connectStatements(){
        BlockProgram testProgram = new BlockProgram(null);
        StatementBlock simpleStatement1 = new MoveForwardBlock();
        StatementBlock simpleStatement2 = new MoveForwardBlock();
        StatementBlock simpleStatement3 = new TurnBlock();
        StatementBlock simpleStatement4 = new WhileBlock();

        testProgram.addBlock(simpleStatement1);
        testProgram.connectToStatementSocket(simpleStatement1,simpleStatement2);
        assertEquals(simpleStatement2,simpleStatement1.getNext());
        assertEquals(simpleStatement1, simpleStatement2.getPrevious());
        assertEquals(null,simpleStatement1.getPrevious());
        assertEquals(null,simpleStatement2.getNext());

        testProgram.connectToStatementSocket(simpleStatement2,simpleStatement3);
        assertEquals(simpleStatement2,simpleStatement3.getPrevious());
        assertEquals(simpleStatement3,simpleStatement2.getNext());
        assertEquals(simpleStatement2,simpleStatement1.getNext());
        assertEquals(simpleStatement1, simpleStatement2.getPrevious());
        assertEquals(null,simpleStatement1.getPrevious());
        assertEquals(null,simpleStatement3.getNext());

        //Insert statement4 between 1 and 2, so that: Statement1 > Statement 4 > Statement 2  > statement 3
        testProgram.connectToStatementSocket(simpleStatement1,simpleStatement4);
        assertEquals(simpleStatement1,simpleStatement4.getPrevious());
        assertEquals(simpleStatement4, simpleStatement1.getNext());
        assertEquals(simpleStatement2 ,simpleStatement4.getNext());
        assertEquals(simpleStatement3, simpleStatement2.getNext());
        assertEquals(simpleStatement4, simpleStatement2.getPrevious());
        assertEquals(null, simpleStatement1.getPrevious());
        assertEquals(null, simpleStatement3.getNext());
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
        ConditionBlock simpleNotCondition = new NotBlock();
        simpleCFB.setCondition(simpleCondition);

        testProgram.addBlock(simpleCFB);
        testProgram.connectToStatementSocket(simpleCFB,otherCondition);
        assertEquals(simpleCondition,simpleCFB.getCondition());

        testProgram.connectToStatementSocket(simpleCFB,simpleNotCondition);
        assertEquals(simpleNotCondition,simpleCFB.getCondition());
        assertEquals(simpleCondition,((NotBlock)simpleCFB.getCondition()).getCondition());
    }

    @Test
    public void connectConditions(){
        BlockProgram testProgram = new BlockProgram(null);
        NotBlock notBlock = new NotBlock();
        ConditionBlock wallInFront = new WallInFrontBlock();
        WhileBlock whileBlock = new WhileBlock();

        testProgram.addBlock(whileBlock);
        testProgram.connectToStatementSocket(whileBlock,wallInFront);
        assertEquals(wallInFront, whileBlock.getCondition());

        testProgram.connectToStatementSocket(whileBlock,notBlock);
        assertEquals(notBlock, whileBlock.getCondition());
        assertEquals(wallInFront, notBlock.getCondition());
    }

    @Test
    public void connectStatementToCFB(){
        BlockProgram testProgram = new BlockProgram(null);
        MoveForwardBlock moveForwardBlock = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock2 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock3 = new MoveForwardBlock();
        WhileBlock whileBlock = new WhileBlock();

        //Let's use this boolean as 3rd argument to mark a connection to the body, so the arguments are:
        //(ControlFlowBLock, StatementBlock, boolean)
        testProgram.addBlock(whileBlock);
        testProgram.connectToStatementSocket(whileBlock,moveForwardBlock,true);
        assertEquals(moveForwardBlock,whileBlock.getBody());
        assertEquals(whileBlock, moveForwardBlock.getPrevious()); //Important!
        assertEquals(null, moveForwardBlock.getNext());

        testProgram.connectToStatementSocket(moveForwardBlock,moveForwardBlock2);
        assertEquals(moveForwardBlock,whileBlock.getBody());
        assertEquals(whileBlock, moveForwardBlock.getPrevious());
        assertEquals(moveForwardBlock2,moveForwardBlock.getNext());
        assertEquals(null, moveForwardBlock2.getNext());

        //IMPORTANT!
        //At this point the WhileBlock has the body: moveForwardBlock > moveForwardBlock2, where whileBlock.getBody() == moveForwardBlock
        //After adding a block in front of moveForwardBlock the WhileBlock should look like:
        //  WhileBlock has the body: moveForwardBlock3 > moveForwardBlock > moveForwardBlock2, where whileBlock.getBody() == moveForwardBlock3
        testProgram.addBlock(moveForwardBlock3);
        testProgram.connectToStatementSocket(moveForwardBlock3,moveForwardBlock);
        assertEquals(moveForwardBlock3,whileBlock.getBody());
        assertEquals(whileBlock, moveForwardBlock3.getPrevious());
        assertEquals(null, moveForwardBlock2.getNext());
        assertEquals(moveForwardBlock,moveForwardBlock3.getNext());
        assertEquals(moveForwardBlock2,moveForwardBlock.getNext());
        assertEquals(moveForwardBlock,moveForwardBlock2.getPrevious());
        assertEquals(moveForwardBlock3,moveForwardBlock.getPrevious());
    }

    //Connecting 2 existing blockprograms to 1 bigger program
    @Test
    public void advancedConnect(){
        BlockProgram testProgram = new BlockProgram(null);
        MoveForwardBlock moveForwardBlock = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock2 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock3 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock4 = new MoveForwardBlock();

        testProgram.addBlock(moveForwardBlock);
        testProgram.connectToStatementSocket(moveForwardBlock,moveForwardBlock2);
        assertEquals(moveForwardBlock2,moveForwardBlock.getNext());
        assertEquals(moveForwardBlock,moveForwardBlock2.getPrevious());

        testProgram.addBlock(moveForwardBlock3);
        testProgram.connectToStatementSocket(moveForwardBlock3,moveForwardBlock4);
        assertEquals(moveForwardBlock4,moveForwardBlock3.getNext());
        assertEquals(moveForwardBlock3,moveForwardBlock4.getPrevious());

        assertEquals(2, testProgram.getComponents().size());

        testProgram.connectToStatementSocket(moveForwardBlock,moveForwardBlock3);
        assertEquals(moveForwardBlock3,moveForwardBlock.getNext());
        assertEquals(moveForwardBlock4,moveForwardBlock3.getNext());
        assertEquals(moveForwardBlock2,moveForwardBlock4.getNext());
        assertEquals(moveForwardBlock,moveForwardBlock3.getPrevious());
        assertEquals(moveForwardBlock3,moveForwardBlock4.getPrevious());
        assertEquals(moveForwardBlock4,moveForwardBlock2.getPrevious());
        assertEquals(null,moveForwardBlock2.getNext());
        assertEquals(null,moveForwardBlock.getPrevious());

        assertEquals(1, testProgram.getComponents().size());
    }

    @Test
    public void advancedConnectProgramToBody(){
        BlockProgram testProgram = new BlockProgram(null);
        WhileBlock whileBlock = new WhileBlock();
        MoveForwardBlock moveForwardBlock2 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock3 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock4 = new MoveForwardBlock();

        testProgram.addBlock(whileBlock);
        testProgram.connectToStatementSocket(whileBlock,moveForwardBlock2);
        assertEquals(moveForwardBlock2,whileBlock.getNext());
        assertEquals(whileBlock,moveForwardBlock2.getPrevious());

        testProgram.addBlock(moveForwardBlock3);
        testProgram.connectToStatementSocket(moveForwardBlock3,moveForwardBlock4);
        assertEquals(moveForwardBlock4,moveForwardBlock3.getNext());
        assertEquals(moveForwardBlock3,moveForwardBlock4.getPrevious());

        assertEquals(2, testProgram.getComponents().size());

        testProgram.connectToStatementSocket(whileBlock,moveForwardBlock3,true);

        assertEquals(moveForwardBlock2,whileBlock.getNext());
        assertEquals(whileBlock,moveForwardBlock2.getPrevious());
        assertEquals(moveForwardBlock3,whileBlock.getBody());
        assertEquals(whileBlock,moveForwardBlock3.getPrevious());
        assertEquals(moveForwardBlock4, moveForwardBlock3.getNext());
        assertEquals(moveForwardBlock3,moveForwardBlock4.getPrevious());
        assertEquals(null, moveForwardBlock4.getNext());
        assertEquals(1, testProgram.getComponents().size());
    }

    @Test
    public void advanceConnectCFBToOtherCFBBody(){
        BlockProgram testProgram = new BlockProgram(null);
        WhileBlock whileBlock = new WhileBlock();
        MoveForwardBlock moveForwardBlock2 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock3 = new MoveForwardBlock();
        MoveForwardBlock moveForwardBlock4 = new MoveForwardBlock();
        WhileBlock whileBlock2 = new WhileBlock();

        testProgram.addBlock(whileBlock);
        testProgram.connectToStatementSocket(whileBlock,moveForwardBlock2,true);
        testProgram.connectToStatementSocket(moveForwardBlock2,moveForwardBlock3);

        testProgram.addBlock(whileBlock2);
        testProgram.connectToStatementSocket(whileBlock2,moveForwardBlock4,true);

        assertEquals(2, testProgram.getComponents().size());


        testProgram.connectToStatementSocket(moveForwardBlock2,whileBlock2);
        //Result should be WhileBlock body: MoveForwardBlock2 > WhileBlock2 body: MoveForwardBlock4 > MoveForwardBlock 3
        assertEquals(null,whileBlock.getNext());
        assertEquals(null,whileBlock.getPrevious());
        assertEquals(moveForwardBlock2,whileBlock.getBody());
        assertEquals(whileBlock2,moveForwardBlock2.getNext());
        assertEquals(moveForwardBlock3,whileBlock2.getNext());
        assertEquals(moveForwardBlock2,whileBlock2.getPrevious());
        assertEquals(whileBlock2,moveForwardBlock3.getPrevious());
        assertEquals(moveForwardBlock4,whileBlock2.getBody());
        assertEquals(whileBlock2,moveForwardBlock4.getPrevious());

        assertEquals(1, testProgram.getComponents().size());
    }
}
