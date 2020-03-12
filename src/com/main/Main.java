package com.main;
import com.ui.Component;
import com.ui.Container;
import com.ui.areas.ProgramArea;
import com.ui.components.GridUI.GridContainerComponent;
import com.ui.MyCanvasWindow;
import com.ui.components.TestComponent;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.FlexAxis;
import com.ui.components.divcomponent.Padding;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static Container worldDiv =
            DivComponent.builder()
                    .withBorder(new Border(Color.BLUE, 4, 2, 4, 4))
                    .withPadding(new Padding(3))
                    .addChildren(new Component[]{DivComponent.builder()
                            .addChildren(new GridContainerComponent(null))
                            .withBorder(new Border(Color.BLUE, 4, 2, 4, 2))
                            .withPadding(new Padding(0))
                            .build()
                            , DivComponent.builder().build()})
                    .withFlexAxis(FlexAxis.Vertical)
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
                    .addChildren(new ProgramArea())
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
                () -> new MyCanvasWindow("Hello World", rootComponent).show()
        );
    }
}
