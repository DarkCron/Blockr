package com.blockr.handlers.ui;

import com.ui.WindowPercentPosition;
import com.ui.WindowPosition;
import com.ui.WindowRegion;

public class UIInfo {
    private final WindowRegion canvasRegion;
    private final WindowRegion paletteRegion;
    private final WindowRegion gameAreaRegion;

    private static WindowPosition windowSize;

    public UIInfo(WindowRegion canvasRegion, WindowRegion paletteRegion, WindowRegion gameAreaRegion) {
        this.canvasRegion = canvasRegion;
        this.paletteRegion = paletteRegion;
        this.gameAreaRegion = gameAreaRegion;

        windowSize = getWindowSize();
    }

    //TODO: experimental
    public WindowPosition getWindowSize(){
        return new WindowPosition(canvasRegion.getWidth(),canvasRegion.getHeight());
    }

    public WindowPosition getPalettePosition(){
        return new WindowPosition(paletteRegion.getMinX(),paletteRegion.getMinY());
    }

    public WindowPosition getGameAreaPosition(){
        return new WindowPosition(gameAreaRegion.getMinX(),gameAreaRegion.getMinY());
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
}
