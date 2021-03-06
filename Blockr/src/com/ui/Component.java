package com.ui;

import com.ui.mouseevent.MouseEvent;

import java.awt.*;

public abstract class Component {

    protected ViewContext getViewContext(){
        return viewContext;
    }

    void setViewContext(ViewContext viewContext){
        this.viewContext = viewContext;
    }

    private ViewContext viewContext;

    protected abstract void draw(Graphics graphics);
  
    public void onMouseEvent(MouseEvent mouseEvent){

    }

    /**
     * Propagates a keyboard event to a component
     *
     * @param keyPressUtility KeyPressUtility represents keystrokes in simple booleans
     */
    public void onKeyEvent(KeyPressUtility keyPressUtility){}
}
