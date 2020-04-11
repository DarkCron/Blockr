package com.blockr.handlers.actions;

import an.awesome.pipelinr.Command;
import com.blockr.State;
import com.blockr.handlers.blockprogram.connectconditionblock.ConnectConditionBlock;
import com.blockr.handlers.blockprogram.connectcontrolflowbody.ConnectControlFlowBody;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlock;
import com.blockr.handlers.blockprogram.disconnectconditionblock.DisconnectConditionBlock;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlock;
import com.blockr.handlers.blockprogram.executeprogram.ExecuteProgram;

public class ActionHandler implements Command.Middleware {
    private State state;

    public ActionHandler(State state) {
        this.state = state;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C c, Next<R> next) {
        if(c instanceof ConnectConditionBlock){
            state.recordWorldStateForUndo();
            return next.invoke();
        }else if(c instanceof ConnectControlFlowBody){
            state.recordWorldStateForUndo();
            return next.invoke();
        }else if(c instanceof ConnectStatementBlock){
            state.recordWorldStateForUndo();
            return next.invoke();
        }else if(c instanceof DisconnectConditionBlock){
            state.recordWorldStateForUndo();
            return next.invoke();
        }else if(c instanceof DisconnectStatementBlock){
            state.recordWorldStateForUndo();
            return next.invoke();
        }else if(c instanceof ExecuteProgram){
            state.recordWorldStateForUndo();
            return next.invoke();
        }
        return next.invoke();
    }
}
