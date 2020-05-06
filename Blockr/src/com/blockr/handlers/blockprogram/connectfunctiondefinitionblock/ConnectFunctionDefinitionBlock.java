package com.blockr.handlers.blockprogram.connectfunctiondefinitionblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class ConnectFunctionDefinitionBlock implements Command<Voidy> {

    public ReadOnlyStatementBlock getSocketBlock(){
        return socketBlock;
    }

    private final ReadOnlyStatementBlock socketBlock;

    public ReadOnlyStatementBlock getPlugBlock(){
        return plugBlock;
    }

    private final ReadOnlyStatementBlock plugBlock;

    public ConnectFunctionDefinitionBlock(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock){
        this.socketBlock = socketBlock;
        this.plugBlock = plugBlock;
    }

}
