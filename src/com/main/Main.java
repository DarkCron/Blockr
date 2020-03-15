package com.main;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.State;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.Orientation;
import com.blockr.domain.gameworld.Position;
import com.blockr.domain.gameworld.TileType;
import com.blockr.handlers.blockprogram.getrootblock.GetRootBlockHandler;
import com.blockr.handlers.blockprogram.insertBlockInProgram.InsertBlockInProgramHandler;
import com.blockr.handlers.ui.input.GetPaletteSelectionHandler;
import com.blockr.handlers.ui.input.SetPaletteSelectionHandler;
import com.blockr.handlers.ui.input.recordMousePos.GetMouseRecordHandler;
import com.blockr.handlers.ui.input.recordMousePos.SetRecordMouseHandler;
import com.blockr.handlers.ui.input.resetuistate.ResetUIStateHandler;
import com.blockr.handlers.world.GetWorldHandler;
import com.blockr.handlers.blockprogram.addblock.AddBlockHandler;
import com.blockr.handlers.blockprogram.createblockprogram.CreateBlockProgramHandler;
import com.ui.MyCanvasWindow;

import javax.swing.*;
import java.util.stream.Stream;

public class Main {

    private static final State state = new State();

    private static final Pipeline pipeline = new Pipelinr()
            .with(() -> Stream.of(new GetWorldHandler(state)
                    , new SetPaletteSelectionHandler(state)
                    , new GetPaletteSelectionHandler(state)
                    , new GetRootBlockHandler(state)
                    , new AddBlockHandler(state)
                    , new GetMouseRecordHandler(state)
                    , new SetRecordMouseHandler(state)
                    , new InsertBlockInProgramHandler(state)
                    , new ResetUIStateHandler(state)
                    , new CreateBlockProgramHandler(state)));

    private static final TileType[][] GRID = {
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
    };

    static {

        var gameWorld = new GameWorld(GRID, new Position(1, 1), Orientation.SOUTH,  new Position(3, 3));

        state.setGameWorld(gameWorld);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World", BlockrUi.build(pipeline)).show()
        );
    }
}
