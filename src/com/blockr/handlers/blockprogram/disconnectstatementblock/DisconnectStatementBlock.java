package com.blockr.handlers.blockprogram.disconnectstatementblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class DisconnectStatementBlock implements Command<Voidy> {

    public ReadOnlyStatementBlock getSocketBlock(){
        return socketBlock;
    }

    private final ReadOnlyStatementBlock socketBlock;

    public ReadOnlyStatementBlock getPlugBlock(){
        return plugBlock;
    }

    private final ReadOnlyStatementBlock plugBlock;

    public DisconnectStatementBlock(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock){
        this.socketBlock = socketBlock;
        this.plugBlock = plugBlock;
    }

}