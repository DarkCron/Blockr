package tests;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.block.*;
import com.blockr.ui.components.programblocks.BlockData;
import com.blockr.ui.components.programblocks.ProgramBlockComponent;
import com.blockr.ui.components.programblocks.ProgramBlockInsertInfo;
import com.ui.WindowPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UIBlockComponentTests {
    private List<ProgramBlockComponent> testBlocks = new ArrayList<>();
    private Pipeline unusedMediator = new Pipelinr();

    private static int STATEMENT_HEIGHT = 40; //refer to BlockData
    private static int STATEMENT_WIDTH = 100; //refer to BlockData
    private static int CONDITION_WIDTH = STATEMENT_WIDTH/2;
    private static int CFB_INNER_START = (int)(STATEMENT_WIDTH * 0.45f); //refer to BlockData


    @BeforeEach
    public void init(){
        testBlocks.clear();
        testBlocks.add(new ProgramBlockComponent(new MoveForwardBlock(),unusedMediator, new WindowPosition(20,20)));
        testBlocks.add(new ProgramBlockComponent(new WhileBlock(),unusedMediator, new WindowPosition(200,20)));
        testBlocks.add(new ProgramBlockComponent(new NotBlock(),unusedMediator, new WindowPosition(400,20)));
        testBlocks.add(new ProgramBlockComponent(new WhileBlock(),unusedMediator, new WindowPosition(600,20)));
        ((ControlFlowBlock)testBlocks.get(3).getSource()).setCondition(new WallInFrontBlock());
    }

    @Test
    public void testValidClick(){
        //TEST A GOOD CLICK
        var info = testBlocks.get(0).getSocketAndPlug(new WindowPosition(30,30),new MoveForwardBlock());
        assertEquals(testBlocks.get(0).getSource() ,info.getPlug());

        //TEST AN INVALID CLICK
        info = testBlocks.get(0).getSocketAndPlug(new WindowPosition(0,0),new MoveForwardBlock());
        assertEquals(null, info);

        //TEST CLICK WHERE ORIGINAL BLOCK IS SOCKET
        info = testBlocks.get(0).getSocketAndPlug(new WindowPosition(30,30+ STATEMENT_HEIGHT/2),new MoveForwardBlock());
        assertEquals(testBlocks.get(0).getSource(), info.getSocket());

        //TEST CLICK WHERE ORIGINAL BLOCK IS PLUG
        info = testBlocks.get(0).getSocketAndPlug(new WindowPosition(90,30),new MoveForwardBlock());
        assertEquals(testBlocks.get(0).getSource() ,info.getPlug());
    }

    @Test
    public void testValidClickCondition(){
        //TEST A BAD CLICK
        var info = testBlocks.get(1).getSocketAndPlug(new WindowPosition(210 ,30),new NotBlock());
        assertEquals(null,info);

        //TEST A GOOD CLICK: Condition in empty CFB
        info = testBlocks.get(1).getSocketAndPlug(new WindowPosition(210+ STATEMENT_WIDTH - 20,30 ),new NotBlock());
        assertEquals(NotBlock.class,info.getPlug().getClass());

        //TEST A GOOD CLICK: Condition in empty CFB
        info = testBlocks.get(1).getSocketAndPlug(new WindowPosition(210+ STATEMENT_WIDTH - 20,30 ),new WallInFrontBlock());
        assertEquals(WallInFrontBlock.class,info.getPlug().getClass());

        //TEST A BAD CLICK: NON-CFB Statement next to condition
        info = testBlocks.get(2).getSocketAndPlug(new WindowPosition(410 ,30),new WallInFrontBlock());
        assertEquals(null,info);

        //TEST A GOOD CLICK: WallInFront right of Not
        info = testBlocks.get(2).getSocketAndPlug(new WindowPosition(410 + CONDITION_WIDTH / 2,30),new WallInFrontBlock());
        assertEquals(WallInFrontBlock.class,info.getPlug().getClass());

        //TEST A GOOD CLICK: Not To the right of NOT
        info = testBlocks.get(2).getSocketAndPlug(new WindowPosition(410 ,30),new NotBlock());
        assertEquals(NotBlock.class,info.getSocket().getClass());

        //TEST A BAD CLICK: WallInFront left of Not
        info = testBlocks.get(2).getSocketAndPlug(new WindowPosition(410,30),new WallInFrontBlock());
        assertEquals(null, info);

        //TEST A BAD CLICK: CFB with a condition and trying to insert WallInFront
        info = testBlocks.get(3).getSocketAndPlug(new WindowPosition(610 + STATEMENT_WIDTH -20,30),new WallInFrontBlock());
        assertEquals(null, info);

        //TEST A GOOD CLICK: CFB with a condition and trying to insert NOT
        info = testBlocks.get(3).getSocketAndPlug(new WindowPosition(610 + STATEMENT_WIDTH -20,30),new NotBlock());
        assertEquals(NotBlock.class,info.getPlug().getClass());
    }

    @Test
    public void testValidClickControlFlow(){
        //TEST A GOOD CLICK: Clicked Body of CFB
        var info = testBlocks.get(1).getSocketAndPlug(new WindowPosition(210+ STATEMENT_WIDTH - 20,30 + 15),new MoveForwardBlock());
        assertEquals(MoveForwardBlock.class,info.getPlug().getClass());
        assertEquals(info.getPlugLocation(), ProgramBlockInsertInfo.PlugLocation.BODY);
    }
}
