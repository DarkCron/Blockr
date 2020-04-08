package com.blockr.handlers.blockprogram.connectconditionblock;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;

public class ConnectConditionBlock implements Command<Voidy> {

    public ReadOnlyConditionedBlock getConditionedBlock(){
        return conditionedBlock;
    }

    private final ReadOnlyConditionedBlock conditionedBlock;

    public ReadOnlyConditionBlock getConditionBlock(){
        return conditionBlock;
    }

    private final ReadOnlyConditionBlock conditionBlock;

    public ConnectConditionBlock(ReadOnlyConditionedBlock conditionedBlock, ReadOnlyConditionBlock conditionBlock){
        this.conditionedBlock = conditionedBlock;
        this.conditionBlock = conditionBlock;
    }

}
