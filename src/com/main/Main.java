package com.main;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.State;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.domain.gameworld.Orientation;
import com.blockr.domain.gameworld.Position;
import com.blockr.domain.gameworld.TileType;
import com.blockr.handlers.blockprogram.addblock.AddBlockHandler;
import com.blockr.handlers.blockprogram.connectstatementblock.ConnectStatementBlockHandler;
import com.blockr.handlers.blockprogram.createblockprogram.CreateBlockProgramHandler;
import com.blockr.handlers.blockprogram.disconnectstatementblock.DisconnectStatementBlockHandler;
import com.blockr.handlers.blockprogram.getblockprogram.GetBlockProgramHandler;
import com.blockr.handlers.ui.GetUIInfoHandler;
import com.blockr.handlers.ui.SetUIInfoHandler;
import com.blockr.handlers.world.GetWorldHandler;
import com.ui.Component;
import com.ui.Container;
import com.ui.areas.PaletteArea;
import com.ui.areas.ProgramArea;
import com.ui.components.GridUI.GridContainerComponent;
import com.ui.MyCanvasWindow;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.FlexAxis;
import com.ui.components.divcomponent.Padding;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class Main {

    private static State getState(){
        return state;
    }

    private static final GameWorld testWorld;
    private final static State state = new State();
    static {
        TileType[][] tempTiles = new TileType[4][4];

        for (int i = 0; i < tempTiles.length; i++) {
            for (int j = 0; j < tempTiles[i].length; j++) {
                if(i == 0 || j == 0 || i == tempTiles.length-1 || j == tempTiles[i].length-1){
                    tempTiles[i][j] = TileType.Blocked;
                    //System.out.println("X: "+j + " , Y: "+i);
                }else{
                    tempTiles[i][j] = TileType.Free;
                }
            }
        }

        testWorld = new GameWorld(tempTiles,new Position(1,1), Orientation.NORTH,new Position(tempTiles.length-2,tempTiles.length-2));
        testWorld.reset();

        state.setGameWorld(testWorld);
    }

    private final static Pipeline pipeline = new Pipelinr()
            .with(() -> Stream.of(new GetWorldHandler(getState())
                    , new SetUIInfoHandler(getState())
                    , new GetUIInfoHandler(getState())
                    , new ConnectStatementBlockHandler(getState())
                    , new GetBlockProgramHandler(getState())
                    , new DisconnectStatementBlockHandler(getState())
                    , new CreateBlockProgramHandler(getState())
                    , new AddBlockHandler(getState())));

    private static Container worldDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4, 2, 4, 4))
                    .withPadding(new Padding(3))
                    .addChildren(DivComponent.builder()
                            .addChildren(new GridContainerComponent(pipeline))
                            .withBorder(new Border(Color.BLUE, 4, 2, 4, 2))
                            .withPadding(new Padding(0))
                            .build()
                            , DivComponent.builder().build())
                    .withFlexAxis(FlexAxis.Vertical)
                    .build();

    private static Container palleteDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4, 2, 4, 2))
                    .withPadding(new Padding(0))
                    .addChildren(new PaletteArea(pipeline))
                    .build();

    private static Container programAreaDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4 , 4, 4, 2))
                    .addChildren(new ProgramArea(pipeline))
                    .withPadding(new Padding(3))
                    .build();

    private static Component rootComponent =
            DivComponent
                    .builder()
                    .addChildren(worldDiv, palleteDiv, programAreaDiv)
                    .withFlexAxis(FlexAxis.Horizontal)
                    .withPadding(new Padding(0))
                    .build();

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World", rootComponent,pipeline).show()
        );
    }
}
