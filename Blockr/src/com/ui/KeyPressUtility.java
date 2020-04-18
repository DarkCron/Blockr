package com.ui;

import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyPressUtility {

    private final HashSet<Integer> pressedKeys = new HashSet<>();

    protected void process(int id, int keyCode, char keyChar){
        if(id == KeyEvent.KEY_PRESSED){
            pressedKeys.add(keyCode);
        }else if(id == KeyEvent.KEY_RELEASED){
            pressedKeys.remove(keyCode);
        }
    }

    public boolean hasPressedUndo(){
        return pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && !pressedKeys.contains(KeyEvent.VK_SHIFT);
    }

    public boolean hasPressedRedo(){
        return pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && pressedKeys.contains(KeyEvent.VK_SHIFT);
    }
}
