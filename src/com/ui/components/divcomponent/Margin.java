package com.ui.components.divcomponent;

public class Margin extends BoxProperty {

    public static final Margin NONE = new Margin(0);

    public Margin(int top, int right, int bottom, int left) {
        super(top, right, bottom, left);
    }

    public Margin(int margin){
        super(margin);
    }
}
