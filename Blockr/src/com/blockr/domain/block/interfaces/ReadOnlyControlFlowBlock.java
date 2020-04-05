package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;

public interface ReadOnlyControlFlowBlock extends ReadOnlyStatementBlock, ReadOnlyConditionedBlock {

    ReadOnlyConditionBlock getCondition();

    ReadOnlyStatementBlock getBody();

}
