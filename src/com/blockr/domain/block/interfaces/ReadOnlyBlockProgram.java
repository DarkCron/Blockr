package com.blockr.domain.block.interfaces;

import java.util.List;

public interface ReadOnlyBlockProgram {

    List<? extends ReadOnlyStatementBlock> getComponents();

    ReadOnlyStatementBlock getActive();

}
