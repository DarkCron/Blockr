package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ContainerBlock;

public class FunctionBodyBlock implements ContainerBlock {

    private StatementBlock body;

    @Override
    public void setBody(StatementBlock statementBlock) {
        body = statementBlock;
    }

    @Override
    public StatementBlock getBody() {
        return body;
    }
}
