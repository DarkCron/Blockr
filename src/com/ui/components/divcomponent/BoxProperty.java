package com.ui.components.divcomponent;

public abstract class BoxProperty {

    public int getTop(){
        return top;
    }

    private final int top;

    public int getRight(){
        return right;
    }

    private final int right;

    public int getBottom(){
        return bottom;
    }

    private final int bottom;

    public int getLeft(){
        return left;
    }

    private final int left;

    public BoxProperty(int top, int right, int bottom, int left){

        throwIfNegative(top, "top");
        throwIfNegative(right, "right");
        throwIfNegative(bottom, "bottom");
        throwIfNegative(left, "left");

        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    private void throwIfNegative(int value, String name){
        if(value >= 0)
            return;
        throw new IllegalArgumentException(String.format("%s must not be negative, value: %d", name, value));
    }

    public BoxProperty(int value){
        this(value, value, value, value);
    }

}
