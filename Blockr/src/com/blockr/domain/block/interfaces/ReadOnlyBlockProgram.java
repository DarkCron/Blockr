package com.blockr.domain.block.interfaces;

import java.util.List;

public interface ReadOnlyBlockProgram {

    List<? extends ReadOnlyStatementBlock> getComponents();

    ReadOnlyStatementBlock getActive();

    ReadOnlyStatementBlock executeNextFromThread(ReadOnlyStatementBlock currentBlock);

    public ReadOnlyStatementBlock getRootBlock(Block blockOfChain);

    public void disconnectStatementBlock(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock);

}
