package com.blockr.ui;

import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.ui.components.programblocks.BlockInformation;

public class TempLogic {
    //Should return the new block graph's root
    public Block addToBlockGraph(Block root, Block blockToAddTo, Block blockToAdd, BlockInformation.BlockClickLocation blockClickLocation){
        if(blockToAddTo instanceof ConditionBlock){
            if(blockToAddTo instanceof NotBlock){
                if(blockToAdd instanceof NotBlock){
                    return connect((NotBlock)blockToAddTo,(NotBlock)blockToAdd);
                }
                return null;
            }else if(blockToAddTo instanceof WallInFrontBlock){
                if(blockToAdd instanceof NotBlock){
                    return connect((WallInFrontBlock) blockToAddTo,(NotBlock) blockToAdd);
                }
            }
        }else if(blockToAddTo instanceof ControlFlowBlock){
            switch (blockClickLocation){
                case LOWER:
                    if(blockToAdd instanceof StatementBlock){
                        return connectLower((StatementBlock)blockToAddTo,(StatementBlock)blockToAdd);
                    }
                    return null;
                case UPPER:
                    if(blockToAdd instanceof StatementBlock){
                        return connectUpper((StatementBlock)blockToAddTo,(StatementBlock)blockToAdd);
                    }
                    return null;
                case FLOW_CONDITION:
                    if(blockToAdd instanceof ConditionBlock){
                        return connectCondition((ControlFlowBlock)blockToAddTo,(ConditionBlock)blockToAdd);
                    }
                    return null;
                case FLOW_BODY_UPPER:
                    if(blockToAdd instanceof StatementBlock){
                        return connectBody((ControlFlowBlock)blockToAddTo,(StatementBlock)blockToAdd);
                    }
                    return null;
            }
        }else if(blockToAddTo instanceof StatementBlock){
            switch (blockClickLocation){
                case LOWER:
                    if(blockToAdd instanceof StatementBlock){
                        return connectLower((StatementBlock)blockToAddTo,(StatementBlock)blockToAdd);
                    }
                    return null;
                case UPPER:
                    if(blockToAdd instanceof StatementBlock){
                        return connectUpper((StatementBlock)blockToAddTo,(StatementBlock)blockToAdd);
                    }
                    return null;
            }
        }
        return null;
    }

    private Block connectBody(ControlFlowBlock blockToAddTo, StatementBlock blockToAdd) {
        var toMove = blockToAddTo.getBody();
        blockToAdd.setNext(toMove);
        blockToAddTo.setBody(blockToAdd);
        return blockToAddTo;
    }

    private Block connectCondition(ControlFlowBlock blockToAddTo, ConditionBlock blockToAdd) {
        if(blockToAdd instanceof WallInFrontBlock){
            if(blockToAddTo.getCondition() != null){
                return null;
            }else{
                blockToAddTo.setCondition(blockToAdd);
                return blockToAddTo;
            }
        }else if(blockToAdd instanceof NotBlock){
            ConditionBlock cb = blockToAddTo.getCondition();
            ((NotBlock) blockToAdd).setCondition(cb);
            blockToAddTo.setCondition(blockToAdd);
            return blockToAddTo;
        }
        return null;
    }

    private Block connectUpper(StatementBlock blockToAddTo, StatementBlock blockToAdd) {
        var toMove = blockToAddTo.getPrevious();
        blockToAdd.setNext(blockToAddTo);
        blockToAdd.setPrevious(toMove);
        return blockToAdd;
    }

    //Connects below blockToAddTo
    private Block connectLower(StatementBlock blockToAddTo, StatementBlock blockToAdd) {
        var toMove = blockToAddTo.getNext();
        blockToAdd.setNext(toMove);
        blockToAddTo.setNext(blockToAdd);
        return blockToAddTo;
    }

    private Block connect(NotBlock root, NotBlock blockToAdd) {
        blockToAdd.setCondition(root);
        return blockToAdd;
    }

    private Block connect(WallInFrontBlock root, NotBlock blockToAdd) {
        blockToAdd.setCondition(root);
        return blockToAdd;
    }


}
