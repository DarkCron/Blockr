package com.blockr.ui.components.programblocks;

import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.handlers.actions.Pair;
import com.ui.WindowPosition;

import java.util.ArrayList;
import java.util.List;

public class ProgramAreaState {
    private final List<Pair<ReadOnlyBlock, WindowPosition>> rootPositions;

    public ProgramAreaState(ProgramArea mainProgramArea, ReadOnlyBlockProgram rbp) {
        rootPositions = new ArrayList<>();

        for (ReadOnlyBlock rob: rbp.getComponents()) {
            rootPositions.add(new Pair<>(rob,mainProgramArea.locationOf(rob)));
        }
    }

    public List<Pair<ReadOnlyBlock, WindowPosition>> getRootLocations() {
        return rootPositions;
    }
}
