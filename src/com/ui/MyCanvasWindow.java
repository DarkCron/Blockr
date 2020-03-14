package com.ui;

import an.awesome.pipelinr.Pipeline;
import com.blockr.handlers.ui.SetUIInfo;
import com.kul.CanvasWindow;
import com.blockr.ui.PaletteArea;
import com.blockr.ui.ProgramArea;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public class MyCanvasWindow extends CanvasWindow {
    private final Component rootComponent;
    private final ViewContext viewContext;
    private final Pipeline mediator;

    public MyCanvasWindow(String title, Component rootComponent, Pipeline mediator) {
        super(title);

        if(rootComponent == null){
            throw new IllegalArgumentException("rootComponent must be effective");
        }

        this.mediator = mediator;
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

        if(component == rootComponent && component instanceof Container){
            WindowRegion programAreaRegion = null;
            WindowRegion paletteAreaRegion = null;
            WindowRegion canvasRegion = windowRegion;

            for(var child : ((Container) component).getChildren()){
                if(programAreaRegion != null && paletteAreaRegion != null){
                    break;
                }
                var containerRegion = ((Container)component).getChildRegion(windowRegion, child);
                if(child instanceof Container){
                    for(var c : ((Container)child).getChildren()) {
                        var childRegion = ((Container)child).getChildRegion(containerRegion, c);
                        if(programAreaRegion != null && paletteAreaRegion != null){
                            break;
                        }
                        if (c instanceof ProgramArea && c != null) {
                            programAreaRegion = childRegion;
                        } else if (c instanceof PaletteArea && c != null) {
                            paletteAreaRegion = childRegion;
                        }
                    }
                }
            }

            mediator.send(new SetUIInfo(canvasRegion,paletteAreaRegion,programAreaRegion));
        }

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
