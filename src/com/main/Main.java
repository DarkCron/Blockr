package com.main;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.State;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.Orientation;
import com.blockr.domain.gameworld.Position;
import com.blockr.domain.gameworld.TileType;
import com.blockr.handlers.world.GetWorldHandler;
import com.blockr.handlers.blockprogram.addblock.AddBlockHandler;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlockHandler;
import com.blockr.handlers.blockprogram.createblockprogram.CreateBlockProgramHandler;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlockHandler;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgramHandler;
import com.blockr.handlers.ui.GetUIInfoHandler;
import com.blockr.handlers.ui.SetUIInfoHandler;
import com.ui.MyCanvasWindow;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.FlexAxis;
import com.ui.components.divcomponent.Padding;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class Main {

    private static final State state = new State();

    private static final Pipeline pipeline = new Pipelinr()
            .with(() -> Stream.of(new GetWorldHandler(state)));

    private static final TileType[][] GRID = {
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Free, TileType.Free, TileType.Free, TileType.Blocked},
            {TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked, TileType.Blocked},
    };

    static {

        var gameWorld = new GameWorld(GRID, new Position(1, 1), Orientation.SOUTH,  new Position(4, 4));

        state.setGameWorld(gameWorld);

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World", BlockrUi.build(pipeline), pipeline).show()
        );
    }
}
