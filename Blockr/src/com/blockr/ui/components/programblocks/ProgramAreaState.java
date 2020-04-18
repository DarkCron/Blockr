package com.blockr.ui.components.programblocks;

import com.blockr.domain.block.BlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.ui.WindowPosition;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information on both the program area and the block program
 */
public class ProgramAreaState {

    private List<Pair<ReadOnlyBlock, WindowPosition>> rootLocations = new ArrayList<>();
    private ReadOnlyStatementBlock activeBlock;

    public ProgramAreaState(ProgramArea programArea, ReadOnlyBlockProgram readOnlyBlockProgram) {
        for (ReadOnlyBlock rob: readOnlyBlockProgram.getComponents()) {
            rootLocations.add(new Pair<ReadOnlyBlock,WindowPosition>(rob,programArea.locationOf(rob)));
        }

        activeBlock = readOnlyBlockProgram.getActive();
    }

    public List<Pair<ReadOnlyBlock, WindowPosition>> getRootLocations() {
        return rootLocations;
    }

    public ReadOnlyStatementBlock getActiveBlock() {
        return activeBlock;
    }
}
