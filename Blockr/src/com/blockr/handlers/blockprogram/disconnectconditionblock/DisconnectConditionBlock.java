package com.blockr.handlers.blockprogram.disconnectconditionblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;

public class DisconnectConditionBlock implements Command<Voidy> {

    public ReadOnlyConditionBlock getConditionBlock(){
        return conditionBlock;
    }

    private final ReadOnlyConditionBlock conditionBlock;

    public DisconnectConditionBlock(ReadOnlyConditionBlock conditionBlock){
        this.conditionBlock = conditionBlock;
    }

}
