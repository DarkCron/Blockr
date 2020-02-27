package com.ui.components.divcomponent;

public abstract class BoxProperty {

    public int getTop(){
        return top;
    }

    private int top;

    public int getRight(){
        return right;
    }

    private int right;

    public int getBottom(){
        return bottom;
    }

    private int bottom;

    public int getLeft(){
        return left;
    }

    private int left;

    public BoxProperty(int top, int right, int bottom, int left){
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public BoxProperty(int value){
        this(value, value, value, value);
    }

}
