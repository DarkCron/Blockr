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

    public boolean hasPressedResetProgram(){
        return pressedKeys.size() == 1 && pressedKeys.contains(KeyEvent.VK_ESCAPE);
    }

    public boolean hasPressedExecute(){
        return pressedKeys.size() == 1 && pressedKeys.contains(KeyEvent.VK_F5);
    }

    public boolean hasPressedUndo(){
        return pressedKeys.size() == 2 && pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && !pressedKeys.contains(KeyEvent.VK_SHIFT);
    }

    public boolean hasPressedRedo(){
        return pressedKeys.size() == 3 && pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && pressedKeys.contains(KeyEvent.VK_SHIFT);
    }
}
