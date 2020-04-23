package com.blockr.domain.block;

public class BlockProgramState {
    private final BlockProgram bp;

    public BlockProgramState(BlockProgram bp) throws CloneNotSupportedException{
        this.bp = bp;
    }

    public BlockProgram getBp() {
        return bp;
    }
}
