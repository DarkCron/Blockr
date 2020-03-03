package com.ui;

public class ViewContext {

    public void repaint(){
        myCanvasWindow.update();
    }

    private final MyCanvasWindow myCanvasWindow;

    public ViewContext(MyCanvasWindow myCanvasWindow){
        this.myCanvasWindow = myCanvasWindow;
    }
}
