package com.ui;

import com.blockr.handlers.ui.input.SetPaletteSelection;
import com.ui.mouseevent.MouseEvent;

public class ViewContext {



    public void repaint(){
        myCanvasWindow.update();
    }

    private final MyCanvasWindow myCanvasWindow;

    public ViewContext(MyCanvasWindow myCanvasWindow){
        this.myCanvasWindow = myCanvasWindow;
    }
}
