package com.blockr.handlers.actions;

import an.awesome.pipelinr.Command;
import com.blockr.State;
import com.blockr.handlers.actions.record.DoRecord;
import com.blockr.handlers.actions.redo.DoRedo;
import com.blockr.handlers.actions.undo.DoUndo;
import com.blockr.handlers.blockprogram.connectconditionblock.ConnectConditionBlock;
import com.blockr.handlers.blockprogram.connectcontainerblockbody.ConnectContainerBlockBody;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlock;
import com.blockr.handlers.blockprogram.disconnectconditionblock.DisconnectConditionBlock;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlock;
import com.blockr.handlers.blockprogram.executeprogram.ExecuteProgram;

/**
 * Middleware for various game manipulating actions
 */
public class ActionHandler implements Command.Middleware {
    private static UndoRedoProcessor undoRedoStack;

    public ActionHandler(State state) {
        if(undoRedoStack == null){ //Singleton pattern
            this.undoRedoStack = new UndoRedoProcessor(state);
        }
    }

    @Override
    public <R, C extends Command<R>> R invoke(C c, Next<R> next) {
        if(c instanceof ConnectConditionBlock){
            //undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof ConnectContainerBlockBody){
            //undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof ConnectStatementBlock){
            //undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof DisconnectConditionBlock){
            //undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof DisconnectStatementBlock){
            //undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof ExecuteProgram){
            undoRedoStack.recordWorldStateForUndo();
        }else if(c instanceof DoRedo){
            undoRedoStack.doRedo();
        }else if(c instanceof DoUndo){
            undoRedoStack.doUndo();
        }else if(c instanceof DoRecord){
            undoRedoStack.recordWorldStateForUndo();
        }

        return next.invoke();
    }
}
