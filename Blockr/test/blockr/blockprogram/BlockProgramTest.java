package blockr.blockprogram;

import com.blockr.domain.block.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockProgramTest {

    private BlockProgram blockProgram;

    @BeforeEach
    public void setup(){
        blockProgram = new BlockProgram(null);
    }

    @Test
    public void addBlock(){

        var moveForwardBlock = new MoveForwardBlock();

        blockProgram.addBlock(moveForwardBlock);

        Assertions.assertEquals(1, blockProgram.getBlockCount());
        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(moveForwardBlock, blockProgram.getComponents().get(0));
    }

    @Test
    public void connectStatementBlock_NewBlock(){

        var socketBlock = new MoveForwardBlock();
        var newBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);

        blockProgram.connectStatementBlock(socketBlock, newBlock);

        Assertions.assertEquals(2, blockProgram.getBlockCount());
        Assertions.assertEquals(1, blockProgram.getComponents().size());

        Assertions.assertEquals(socketBlock.getNext(), newBlock);
        Assertions.assertEquals(newBlock.getPrevious(), socketBlock);
    }

    @Test
    public void connectStatementBlock_ExistingBlock(){

        var socketBlock = new MoveForwardBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.addBlock(plugBlock);

        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(plugBlock, socketBlock.getNext());
        Assertions.assertEquals(socketBlock, plugBlock.getPrevious());
    }

    @Test
    public void connectStatementBlock_FromControlFlowBody(){

        var socketBlock = new MoveForwardBlock();

        var whileBlock = new WhileBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.addBlock(whileBlock);
        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertNull(whileBlock.getBody());
        Assertions.assertEquals(plugBlock, socketBlock.getNext());
        Assertions.assertEquals(socketBlock, plugBlock.getPrevious());
    }

    @Test
    public void connectStatementBlock_DisconnectFromOther(){

        //arrange
        var socketBlock = new MoveForwardBlock();
        var otherSocketBlock = new MoveForwardBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.addBlock(otherSocketBlock);
        blockProgram.connectStatementBlock(otherSocketBlock, plugBlock);

        //act
        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        //assert
        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertNull(otherSocketBlock.getNext());
        Assertions.assertEquals(plugBlock, socketBlock.getNext());
        Assertions.assertEquals(socketBlock, plugBlock.getPrevious());
    }

    @Test
    public void connectStatementBlock_Single_InsideChain(){

        var socketBlock = new MoveForwardBlock();
        var turnBlock = new TurnBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.connectStatementBlock(socketBlock, turnBlock);

        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(plugBlock, socketBlock.getNext());
        Assertions.assertEquals(socketBlock, plugBlock.getPrevious());
        Assertions.assertEquals(turnBlock, plugBlock.getNext());
        Assertions.assertEquals(plugBlock, turnBlock.getPrevious());
    }

    @Test
    public void connectStatementBlock_Chain_InsideChain(){

        var socketBlock = new MoveForwardBlock();
        var turnBlock = new TurnBlock();

        var plugBlock = new MoveForwardBlock();
        var lastBlock = new MoveForwardBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.connectStatementBlock(socketBlock, turnBlock);
        blockProgram.addBlock(plugBlock);
        blockProgram.connectStatementBlock(plugBlock, lastBlock);

        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(plugBlock, socketBlock.getNext());
        Assertions.assertEquals(socketBlock, plugBlock.getPrevious());
        Assertions.assertEquals(turnBlock, lastBlock.getNext());
        Assertions.assertEquals(lastBlock, turnBlock.getPrevious());
    }

    @Test
    public void disconnectStatementBlock(){

        var socketBlock = new MoveForwardBlock();
        var plugBlock = new TurnBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        blockProgram.disconnectStatementBlock(plugBlock);

        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertTrue(blockProgram.getComponents().contains(plugBlock));
        Assertions.assertNull(socketBlock.getNext());
        Assertions.assertNull(plugBlock.getPrevious());
    }

    @Test
    public void disconnectStatementBlock_ControlFlowBody(){

        var whileBlock = new WhileBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        blockProgram.disconnectStatementBlock(plugBlock);

        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertTrue(blockProgram.getComponents().contains(plugBlock));
        Assertions.assertNull(whileBlock.getBody());
        Assertions.assertNull(plugBlock.getPrevious());
    }

    @Test
    public void connectConditionBlock_NewBlock(){

        var ifBlock = new IfBlock();
        var condition = new WallInFrontBlock();

        blockProgram.addBlock(ifBlock);

        blockProgram.connectConditionBlock(ifBlock, condition);

        Assertions.assertEquals(2, blockProgram.getBlockCount());
        Assertions.assertEquals(condition, ifBlock.getCondition());
    }

    @Test
    public void connectConditionBlock_Existing(){

        var ifBlock = new IfBlock();
        var condition = new WallInFrontBlock();

        blockProgram.addBlock(ifBlock);
        blockProgram.addBlock(condition);

        blockProgram.connectConditionBlock(ifBlock, condition);

        Assertions.assertEquals(2, blockProgram.getBlockCount());
        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(condition, ifBlock.getCondition());
    }

    @Test
    public void connectConditionBlock_DisconnectFromOther(){

        var whileBlock = new WhileBlock();
        var ifBlock = new IfBlock();
        var condition = new WallInFrontBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.connectConditionBlock(whileBlock, condition);
        blockProgram.addBlock(ifBlock);

        blockProgram.connectConditionBlock(ifBlock, condition);

        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertNull(whileBlock.getCondition());
        Assertions.assertEquals(condition, ifBlock.getCondition());
    }

    @Test
    public void disconnectConditionBlock(){

        var whileBlock = new WhileBlock();
        var condition = new WallInFrontBlock();


        blockProgram.addBlock(whileBlock);
        blockProgram.connectConditionBlock(whileBlock, condition);

        blockProgram.disconnectConditionBlock(condition);

        Assertions.assertEquals(2, blockProgram.getComponents().size());
        Assertions.assertTrue(blockProgram.getComponents().contains(condition));
        Assertions.assertNull(whileBlock.getCondition());
    }

    @Test
    public void connectControlFlowBody_NewBlock(){

        var whileBlock = new WhileBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);

        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        Assertions.assertEquals(2, blockProgram.getBlockCount());
        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(plugBlock, whileBlock.getBody());
    }

    @Test
    public void connectControlFlowBody_ExistingBlock(){

        var whileBlock = new WhileBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.addBlock(plugBlock);

        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        Assertions.assertEquals(1, blockProgram.getComponents().size());
        Assertions.assertEquals(plugBlock, whileBlock.getBody());
    }

    @Test
    public void connectControlFlowBody_DisconnectFromStatementBlock(){

        var whileBlock = new WhileBlock();
        var moveForwardBlock = new MoveForwardBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.addBlock(moveForwardBlock);
        blockProgram.connectStatementBlock(moveForwardBlock, plugBlock);

        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        Assertions.assertNull(plugBlock.getPrevious());
        Assertions.assertNull(moveForwardBlock.getNext());
        Assertions.assertEquals(plugBlock, whileBlock.getBody());
    }

    @Test
    public void connectControlFlowBlock_DisconnectFromOtherControlFlowBlock(){

        var whileBlock = new WhileBlock();
        var ifBlock = new IfBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.addBlock(ifBlock);
        blockProgram.connectContainerBlockBody(ifBlock, plugBlock);

        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        Assertions.assertNull(ifBlock.getBody());
        Assertions.assertEquals(plugBlock, whileBlock.getBody());
    }

    @Test
    public void removeBlock_SingleSeparateBlock(){

        var block = new MoveForwardBlock();

        blockProgram.addBlock(block);

        blockProgram.removeBlock(block);

        Assertions.assertEquals(0, blockProgram.getBlockCount());
        Assertions.assertEquals(0, blockProgram.getComponents().size());
    }

    @Test
    public void removeBlock_ConnectedStatementBlock(){

        var socketBlock = new MoveForwardBlock();
        var plugBlock = new TurnBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        blockProgram.removeBlock(plugBlock);

        Assertions.assertEquals(1, blockProgram.getBlockCount());
        Assertions.assertNull(socketBlock.getNext());
    }

    @Test
    public void removeBlock_ControlFlowBody(){

        var whileBlock = new WhileBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.connectContainerBlockBody(whileBlock, plugBlock);

        blockProgram.removeBlock(plugBlock);

        Assertions.assertEquals(1, blockProgram.getBlockCount());
        Assertions.assertNull(whileBlock.getBody());
    }

    @Test
    public void removeBlock_StatementBlockChain(){

        var socketBlock = new MoveForwardBlock();
        var plugBlock = new TurnBlock();

        blockProgram.addBlock(socketBlock);
        blockProgram.connectStatementBlock(socketBlock, plugBlock);

        blockProgram.removeBlock(socketBlock);

        Assertions.assertEquals(0, blockProgram.getBlockCount());
        Assertions.assertEquals(0, blockProgram.getComponents().size());
    }

    @Test
    public void removeBlock_Multiple(){

        var whileBlock = new WhileBlock();
        var ifBlock = new IfBlock();
        var condition = new WallInFrontBlock();
        var plugBlock = new MoveForwardBlock();

        blockProgram.addBlock(whileBlock);
        blockProgram.connectContainerBlockBody(whileBlock, ifBlock);
        blockProgram.connectContainerBlockBody(ifBlock, plugBlock);
        blockProgram.connectConditionBlock(ifBlock, condition);

        blockProgram.removeBlock(whileBlock);

        Assertions.assertEquals(0, blockProgram.getBlockCount());
        Assertions.assertEquals(0, blockProgram.getComponents().size());
    }
}
