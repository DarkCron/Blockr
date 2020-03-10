package com.main;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.blockr.domain.State;
import com.blockr.domain.handlers.world.GetWorldHandler;
import com.ui.Component;
import com.ui.Container;
import com.ui.MyCanvasWindow;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.FlexAxis;
import com.ui.components.divcomponent.Padding;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class Main {

    private static Container worldDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4, 2, 4, 4))
                    .withPadding(new Padding(3))
                    .build();

    private static Container palleteDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4, 2, 4, 2))
                    .withPadding(new Padding(0))
                    .addChildren(new TestComponent())
                    .build();

    private static Container programAreaDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4 , 4, 4, 2))
                    .withPadding(new Padding(3))
                    .build();

    private static Component rootComponent =
            DivComponent
                    .builder()
                    .addChildren(worldDiv, palleteDiv, programAreaDiv)
                    .withFlexAxis(FlexAxis.Horizontal)
                    .withPadding(new Padding(0))
                    .build();

    private State getState(){
        return state;
    }

    private final State state = new State();

    private final Pipeline pipeline = new Pipelinr()
            .with(() -> Stream.of(new GetWorldHandler(getState())));

    public static void main(String[] args){
        SwingUtilities.invokeLater(
                () -> new MyCanvasWindow("Hello World", rootComponent).show()
        );
    }
}
