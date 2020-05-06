package com.blockr.ui.components.programblocks;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.handlers.actions.Pair;
import com.ui.WindowPosition;

import java.util.ArrayList;
import java.util.List;

public class ProgramAreaState {
    private final List<Pair<Block, WindowPosition>> rootPositions;

    public ProgramAreaState(ProgramArea mainProgramArea, BlockProgram rbp) {
        rootPositions = new ArrayList<>();

        for (Block rob: rbp.getComponents()) {
            rootPositions.add(new Pair<>(rob,mainProgramArea.locationOf(rob)));
        }
    }

    public List<Pair<Block, WindowPosition>> getRootLocations() {
        return rootPositions;
    }
}
