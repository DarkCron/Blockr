package com.blockr.domain.block;

public class BlockProgramState {
    private final BlockProgram bp;

    public BlockProgramState(BlockProgram bp) throws CloneNotSupportedException{
        this.bp = (BlockProgram) bp.clone();
    }

    public BlockProgram getBp() {
        return bp;
    }
}
