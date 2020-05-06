package com.blockr.handlers.blockprogram.connectfunctionblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.markers.ReadOnlyFunctionBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

public class ConnectFunctionBlock implements Command<Voidy> {

    public ReadOnlyFunctionBlock getSocketBlock(){
        return socketBlock;
    }

    private final ReadOnlyFunctionBlock socketBlock;

    public ReadOnlyStatementBlock getPlugBlock(){
        return plugBlock;
    }

    private final ReadOnlyStatementBlock plugBlock;

    public ConnectFunctionBlock(ReadOnlyFunctionBlock socketBlock, ReadOnlyStatementBlock plugBlock){
        this.socketBlock = socketBlock;
        this.plugBlock = plugBlock;
    }

}
