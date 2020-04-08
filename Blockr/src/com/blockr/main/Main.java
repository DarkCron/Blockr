package com.blockr.main;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.State;
import com.blockr.handlers.blockprogram.canstart.CanStartHandler;
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
import com.blockr.handlers.blockprogram.addblock.AddBlockHandler;
import com.gameworld.GameWorld;
import com.ui.MyCanvasWindow;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {



    private static final State state = new State(null);

    private static final Pipeline pipeline = new Pipelinr()
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
                    , new ResetUIStateHandler(state)));

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World", BlockrUi.build(pipeline)).show()
        );
    }
}
