package com.blockr.handlers.actions;

import com.blockr.State;
import com.blockr.domain.block.BlockProgramState;
import com.blockr.ui.components.programblocks.ProgramArea;
import com.blockr.ui.components.programblocks.ProgramAreaState;
import com.gameworld.GameWorldSnapshot;
import javafx.util.Pair;
import java.util.Stack;

/*
TODO: rename class
 */
public class UndoRedoStack {

    private final State state;

    private final Stack<Triple<GameWorldSnapshot, ProgramAreaState, BlockProgramState>> undoStack = new Stack<>();
    private final Stack<Triple<GameWorldSnapshot, ProgramAreaState, BlockProgramState>> redoStack = new Stack<>();

    public UndoRedoStack(State state) {
        this.state = state;
    }

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
