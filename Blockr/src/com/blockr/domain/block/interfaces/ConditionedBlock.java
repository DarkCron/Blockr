package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.ConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;

public interface ConditionedBlock extends ReadOnlyConditionedBlock {

    ConditionBlock getCondition();

     void setCondition(ConditionBlock conditionBlock);

     boolean isReady();
}
