package com.blockr.handlers.blockprogram.disconnectstatementblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class DisconnectStatementBlock implements Command<Voidy> {

    public ReadOnlyStatementBlock getPlugBlock(){
        return plugBlock;
    }

    private final ReadOnlyStatementBlock plugBlock;

    public DisconnectStatementBlock(ReadOnlyStatementBlock plugBlock){
        this.plugBlock = plugBlock;
    }

}