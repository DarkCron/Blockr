package com.ui;

import com.kul.CanvasWindow;
import com.ui.components.divcomponent.Border;
import com.ui.components.divcomponent.DivComponent;
import com.ui.components.divcomponent.Padding;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public class MyCanvasWindow extends CanvasWindow {

    private Container worldDiv =
            DivComponent.builder()
            .withBorder(new Border(Color.BLUE, 3))
            .withPadding(new Padding(3))
            .build();

    private Container palleteDiv =
            DivComponent.builder()
            .withBorder(new Border(Color.BLUE, 3))
            .withPadding(new Padding(3))
            .build();

    private Container programAreaDiv =
            DivComponent.builder()
            .withBorder(new Border(Color.BLUE, 3))
            .withPadding(new Padding(3))
            .build();

    private Component rootComponent =
            DivComponent
                    .builder()
                    .addChildren(worldDiv, palleteDiv, programAreaDiv)
                    .build();

    public MyCanvasWindow(String title) {
        super(title);
    }

    @Override
    protected void paint(Graphics g) {
        drawComponentTree(rootComponent, WindowRegion.toWindowRegion(g), g);
    }

    private void drawComponentTree(Component component, WindowRegion windowRegion, Graphics g){

        component.draw(windowRegion.create(g));

        if(component instanceof Container){

            var container = (Container)component;

            for(var child : container.getChildren()){

                var childRegion = container.getChildRegion(windowRegion, child);

                drawComponentTree(child, childRegion, g);
            }
        }
    }

    private Component getComponentAt(WindowPosition position){

        var region = new WindowRegion(0, 0, getWidth(), getHeight());

        Component component = rootComponent;
        while(true){

            if(!(component instanceof Container))
                return component;

            var container = (Container)component;

            boolean inChild = false;

            for(var child : container.getChildren()){

                var childRegion = container.getChildRegion(region, child);

                if(!childRegion.contains(position))
                    continue;

                inChild = true;
                component = child;
                region = childRegion;
                break;
            }

            if(inChild)
                continue;

            return component;
        }
    }

    @Override
    protected void handleMouseEvent(int id, int x, int y, int clickCount) {

        var type = MouseEvent.Type.getTypeById(id);

        if(type == null)
            return;

        var component = getComponentAt(new WindowPosition(x, y));
        component.onMouseEvent(new MouseEvent(type));
    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {



    }
}
