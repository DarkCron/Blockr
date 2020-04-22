package com.blockr.main;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.State;
import com.blockr.handlers.actions.ActionHandler;
import com.blockr.handlers.actions.redo.DoRedoHandler;
import com.blockr.handlers.actions.reset.DoResetHandler;
import com.blockr.handlers.actions.undo.DoUndoHandler;
import com.blockr.handlers.blockprogram.addblock.AddBlockHandler;
import com.blockr.handlers.blockprogram.canstart.CanStartHandler;
import com.blockr.handlers.blockprogram.connectconditionblock.ConnectConditionBlockHandler;
import com.blockr.handlers.blockprogram.executeprogram.ExecuteProgramHandler;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgramHandler;
import com.blockr.handlers.blockprogram.getrootblock.GetRootBlockHandler;
import com.blockr.handlers.blockprogram.removeblock.RemoveBlockHandler;
import com.blockr.handlers.ui.input.GetPaletteSelectionHandler;
import com.blockr.handlers.ui.input.GetProgramSelectionHandler;
import com.blockr.handlers.ui.input.SetPaletteSelectionHandler;
import com.blockr.handlers.ui.input.SetProgramSelectionHandler;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecordHandler;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouseHandler;
import com.blockr.handlers.ui.input.resetuistate.ResetUIStateHandler;
import com.blockr.handlers.world.GetWorldHandler;
import com.gameworld.GameWorld;
import com.ui.MyCanvasWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if(args.length == 0){
            System.out.println("The gameworld classname must be provided");
            return;
        }

        var className = args[0];
        var gameWorld = (GameWorld)Class.forName(className).getConstructor().newInstance();

        var state = new State(gameWorld);

        SwingUtilities.invokeLater(() -> new MyCanvasWindow("test", BlockrUi.build(buildPipeline(state))));
    }

    private static Pipeline buildPipeline(State state){
        return new Pipelinr()
                .with(() -> Stream.of(
                        new GetWorldHandler(state)
                        , new CanStartHandler(state)
                        , new SetPaletteSelectionHandler(state)
                        , new GetPaletteSelectionHandler(state)
                        , new SetProgramSelectionHandler(state)
                        , new GetProgramSelectionHandler(state)
                        , new GetRootBlockHandler(state)
                        , new AddBlockHandler(state)
                        , new GetMouseRecordHandler(state)
                        , new SetRecordMouseHandler(state)
                        , new ExecuteProgramHandler(state)
                        , new GetBlockProgramHandler(state)
                        , new RemoveBlockHandler(state)
                        , new ResetUIStateHandler(state)
                        , new ConnectConditionBlockHandler(state)
                        , new DoRedoHandler(state)
                        , new DoUndoHandler(state)
                        , new DoResetHandler(state)
                        )
                )
                .with(
                        () -> Stream.of(
                                new ActionHandler(state)
                        )
                )
                ;
    }
}
