package com.blockr.domain.block.interfaces;

import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;

public interface ReadOnlyControlFlowBlock extends ReadOnlyStatementBlock {

    ReadOnlyConditionBlock getCondition();

    ReadOnlyStatementBlock getBody();

}
