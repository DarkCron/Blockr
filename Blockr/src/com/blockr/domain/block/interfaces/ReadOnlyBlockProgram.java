package com.blockr.domain.block.interfaces;

import java.util.List;

public interface ReadOnlyBlockProgram {

    List<? extends ReadOnlyBlock> getComponents();

    ReadOnlyStatementBlock getActive();
}
