package com.ui.components.divcomponent;

public class Padding extends BoxProperty {

    public static final Padding NONE = new Padding(0);

    public Padding(int top, int right, int bottom, int left) {
        super(top, right, bottom, left);
    }

    public Padding(int padding){
        super(padding);
    }
}
