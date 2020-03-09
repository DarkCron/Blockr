package com.blockr.domain.block.interfaces.markers;

import com.blockr.domain.block.interfaces.Block;

public interface ReadOnlyNotBlock extends Block {

    ReadOnlyConditionBlock getCondition();

}
