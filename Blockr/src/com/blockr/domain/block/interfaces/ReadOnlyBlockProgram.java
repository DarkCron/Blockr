package com.blockr.domain.block.interfaces;

import java.util.List;

public interface ReadOnlyBlockProgram {

    List<? extends Block> getComponents();

    ReadOnlyStatementBlock getActive();
}
