package com.ui;

import com.kul.CanvasWindow;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public class MyCanvasWindow extends CanvasWindow {
    private final Component rootComponent;
    private final ViewContext viewContext;

    public MyCanvasWindow(String title, Component rootComponent) {
        super(title);

        if(rootComponent == null){
            throw new IllegalArgumentException("rootComponent must be effective");
        }

        this.rootComponent = rootComponent;
        this.viewContext = new ViewContext(this);
        initializeViewContext(rootComponent);
    }

    private void initializeViewContext(Component component){

        component.setViewContext(viewContext);

        if(component instanceof Container){

            var container = (Container)component;

            for(var child : container.getChildren()){
                initializeViewContext(child);
            }
        }
    }

    @Override
    protected void paint(Graphics g) {
        drawComponentTree(rootComponent, WindowRegion.fromGraphics(g), g);
    }

    public void update(){
        this.repaint();
    }

    private void drawComponentTree(Component component, WindowRegion windowRegion, Graphics g){
        traverseComponentTree(component, windowRegion, (c, w) -> c.draw(w.create(g)));
    }

    private void traverseComponentTree(Component component, WindowRegion windowRegion, ComponentAction componentAction){

        componentAction.execute(component, windowRegion);

        if(component instanceof Container){

            var container = (Container)component;

            for(var child : container.getChildren()){
                var childRegion = container.getChildRegion(windowRegion, child);
                traverseComponentTree(child, childRegion, componentAction);
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
        component.onMouseEvent(new MouseEvent(type,new WindowPosition(x, y)));
    }

    @Override
    protected void handleKeyEvent(int id, int keyCode, char keyChar) {

    }

    interface ComponentAction {
        void execute(Component component, WindowRegion windowRegion);
    }
}
