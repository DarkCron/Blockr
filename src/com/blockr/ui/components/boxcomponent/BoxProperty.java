package com.blockr.ui.components.boxcomponent;

public abstract class BoxProperty {

    public int getTop(){
        return property[0];
    }

    public int getRight(){
        return property[1];
    }

    public int getBottom(){
        return property[2];
    }

    public int getLeft(){
        return property[3];
    }

    private int[] property;

    public BoxProperty(int top, int right, int bottom, int left){
        this.property = new int[] { top, right, bottom, left};
    }

    public BoxProperty(int value){
        this(value, value, value, value);
    }
}
