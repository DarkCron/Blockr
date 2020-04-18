package com.blockr.domain.block.interfaces;

public interface Block extends ReadOnlyBlock, Cloneable {

    Block clone();

}
