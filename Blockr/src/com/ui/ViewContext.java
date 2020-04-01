package com.ui;

import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public class ViewContext {

    private WindowPosition mousePosition;
    private Graphics graphicsDevice;

    public void repaint(){
        myCanvasWindow.update();
    }

    private final MyCanvasWindow myCanvasWindow;

    public ViewContext(MyCanvasWindow myCanvasWindow){
        this.myCanvasWindow = myCanvasWindow;
    }

    public Graphics getGraphicsDevice() {
        return graphicsDevice;
    }

    public void setGraphicsDevice(Graphics graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
    }

    public WindowPosition getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(WindowPosition mousePosition) {
        this.mousePosition = mousePosition;
    }
}
