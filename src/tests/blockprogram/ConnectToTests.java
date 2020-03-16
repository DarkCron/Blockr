package tests.blockprogram;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.block.ConditionBlock;
import com.blockr.domain.block.WallInFrontBlock;
import com.blockr.domain.block.WhileBlock;
import org.junit.jupiter.api.Test;

public class ConnectToTests {
    @Test
    public void connectToCFB(){
        BlockProgram testProgram = new BlockProgram(null);
        WhileBlock simpleCFB = new WhileBlock();
        ConditionBlock simpleCondition = new WallInFrontBlock();

        testProgram.addBlock(simpleCFB);
        testProgram.connectToStatementSocket(simpleCFB,simpleCondition);

    }
}
