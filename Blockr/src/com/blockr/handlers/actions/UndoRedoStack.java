package com.blockr.handlers.actions;

import com.blockr.State;
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

    private final Stack<Pair<GameWorldSnapshot, ProgramAreaState>> undoStack = new Stack<>();
    private final Stack<Pair<GameWorldSnapshot, ProgramAreaState>> redoStack = new Stack<>();

    public UndoRedoStack(State state) {
        this.state = state;
    }

    public void recordWorldStateForUndo() {
        undoStack.push(new Pair<>(state.getGameWorld().takeSnapshot(), ProgramArea.generateProgramAreaState(state.getBlockProgram())));
        redoStack.clear();
    }

    public void recordWorldStateForRedo() {
        redoStack.push(new Pair<>(state.getGameWorld().takeSnapshot(), ProgramArea.generateProgramAreaState(state.getBlockProgram())));
    }

    public void doUndo() {
        if(undoStack.size() == 0){
            return;
        }

        redoStack.push(new Pair<>(state.getGameWorld().takeSnapshot(), ProgramArea.generateProgramAreaState(state.getBlockProgram())));
        var restorePoint = undoStack.pop();

        state.getGameWorld().restoreSnapshot(restorePoint.getKey());
        ProgramArea.restoreProgramAreaState(restorePoint.getValue());
    }

    public void doRedo() {
        if(redoStack.size() == 0){
            return;
        }

        undoStack.push(new Pair<>(state.getGameWorld().takeSnapshot(), ProgramArea.generateProgramAreaState(state.getBlockProgram())));
        var restorePoint = redoStack.pop();

        state.getGameWorld().restoreSnapshot(restorePoint.getKey());
        ProgramArea.restoreProgramAreaState(restorePoint.getValue());
    }
}
