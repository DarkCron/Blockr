package com.main;

import an.awesome.pipelinr.Pipeline;
import com.blockr.ui.PaletteArea;
import com.blockr.ui.ProgramArea;
import com.blockr.ui.components.gameworld.GameWorldComponent;
import com.ui.Component;
import com.ui.Container;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.FlexAxis;
import com.ui.components.divcomponent.Padding;

import java.awt.*;

public class BlockrUi {

    public static Component build(Pipeline pipeline){
        Container worldDiv =
                DivComponent.builder()
                        .withBorder(new Border(Color.BLUE, 4, 2, 4, 4))
                        .withPadding(new Padding(0))
                        .addChildren(
                                new GameWorldComponent(pipeline),
                                DivComponent.builder().build())
                        .withFlexAxis(FlexAxis.Vertical)
                        .build();

        Container palleteDiv =
                DivComponent.builder()
                        .withBorder(new Border(Color.BLUE, 4, 2, 4, 2))
                        .withPadding(new Padding(0))
                        .addChildren(new PaletteArea())
                        .build();

        Container programAreaDiv =
                DivComponent.builder()
                        .withBorder(new Border(Color.BLUE, 4 , 4, 4, 2))
                        .addChildren(new ProgramArea())
                        .withPadding(new Padding(3))
                        .build();

        return DivComponent
                .builder()
                .addChildren(worldDiv, palleteDiv, programAreaDiv)
                .withFlexAxis(FlexAxis.Horizontal)
                .withPadding(new Padding(0))
                .build();
    }

}
