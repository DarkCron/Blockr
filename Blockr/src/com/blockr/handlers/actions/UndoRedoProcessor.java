package com.blockr.handlers.actions;

import com.blockr.State;
import com.blockr.domain.block.BlockProgramState;
import com.blockr.ui.components.programblocks.ProgramArea;
import com.blockr.ui.components.programblocks.ProgramAreaState;
import com.gameworld.GameWorldSnapshot;
import java.util.Stack;

/**
 * Class for processing Undo and Redo commands
 */
public class UndoRedoProcessor {

    private final State state;
    private final Stack<Triple<GameWorldSnapshot, ProgramAreaState, BlockProgramState>> undoStack = new Stack<>();
    private final Stack<Triple<GameWorldSnapshot, ProgramAreaState, BlockProgramState>> redoStack = new Stack<>();


    /**
     * @param state reference to the domain state
     */
    public UndoRedoProcessor(State state) {
        this.state = state;
    }

    /**
     * Generates and saves information to put on the Undo stack, information is generated based on the program's current state.
     */
    public void recordWorldStateForUndo() {
        try {
            undoStack.push(new Triple<>(
                    state.getGameWorld().takeSnapshot(),
                    ProgramArea.generateProgramAreaState(state.getBlockProgram()),
                    new BlockProgramState(state.getBlockProgram())));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        redoStack.clear();
    }

    /**
     * Generates and saves information to put on the Redo stack, information is generated based on the program's current state.
     */
    public void recordWorldStateForRedo() {
        try {
            redoStack.push(new Triple<>(
                    state.getGameWorld().takeSnapshot(),
                    ProgramArea.generateProgramAreaState(state.getBlockProgram()),
                    new BlockProgramState(state.getBlockProgram())));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs an Undo if undo snapshots are available in the stack.
     * Does effectively nothing if no such snapshots are available.
     */
    public void doUndo() {
        if(undoStack.size() == 0){
            return;
        }

        try {
            redoStack.push(new Triple<>(
                    state.getGameWorld().takeSnapshot(),
                    ProgramArea.generateProgramAreaState(state.getBlockProgram()),
                    new BlockProgramState(state.getBlockProgram())));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        var restorePoint = undoStack.pop();

        state.getGameWorld().restoreSnapshot(restorePoint.getFirst());
        ProgramArea.restoreProgramAreaState(restorePoint.getSecond());
        state.setBlockProgram(restorePoint.getThird().getBp());
    }

    /**
     * Performs a Redo if redo snapshots are available in the stack.
     * Does effectively nothing if no such snapshots are available.
     */
    public void doRedo() {
        if(redoStack.size() == 0){
            return;
        }

        try {
            undoStack.push(new Triple<>(
                    state.getGameWorld().takeSnapshot(),
                    ProgramArea.generateProgramAreaState(state.getBlockProgram()),
                    new BlockProgramState(state.getBlockProgram())));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        var restorePoint = redoStack.pop();

        state.getGameWorld().restoreSnapshot(restorePoint.getFirst());
        ProgramArea.restoreProgramAreaState(restorePoint.getSecond());
        state.setBlockProgram(restorePoint.getThird().getBp());
    }
}
