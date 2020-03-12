package com.ui;

public class ViewContext {

    private static WindowPosition windowSize;




    public void repaint(){
        myCanvasWindow.update();
    }

    private final MyCanvasWindow myCanvasWindow;

    public ViewContext(MyCanvasWindow myCanvasWindow){
        this.myCanvasWindow = myCanvasWindow;
        this.windowSize = new WindowPosition(myCanvasWindow.getWidth(),myCanvasWindow.getHeight());
    }

    public static WindowPercentPosition screenSpaceToPercent(WindowPosition position){
        if(position == null){
            return null;
        }
        float x = ((float)position.getX())/((float)windowSize.getX());
        float y = ((float)position.getY())/((float)windowSize.getY());
        return new WindowPercentPosition(x,y);
    }

    public static WindowPosition percentToScreenSpace(WindowPercentPosition position){
        if(position == null){
            return null;
        }
        return new WindowPosition((int)(position.getX() * windowSize.getX()), (int)(position.getY() * windowSize.getY()));
    }

    public static float heightToPercent(int height) {
        return ((float)height)/((float)windowSize.getY());
    }
    public static float widthToPercent(int width) {
        return ((float)width)/((float)windowSize.getX());
    }
    public static int percentToWidth(float x) {
        return (int)(x * windowSize.getX());
    }
    public static int percentToHeight(float y) {
        return (int)(y * windowSize.getY());
    }

    public void update() {
        this.windowSize = new WindowPosition(myCanvasWindow.getWidth(),myCanvasWindow.getHeight());
    }
}
