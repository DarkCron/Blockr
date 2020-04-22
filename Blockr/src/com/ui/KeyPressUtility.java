package com.ui;

import java.awt.event.KeyEvent;
import java.util.HashSet;

public class KeyPressUtility {

    private final HashSet<Integer> pressedKeys = new HashSet<>();

    /**
     * Process a given input and store its information
     * @param id    Key event id: KeyEvent.KEY_PRESSED OR KeyEvent.KEY_RELEASED as examples
     * @param keyCode int representation of the pressed keyboard key
     * @param keyChar a char representation of the keyCode
     */
    protected void process(int id, int keyCode, char keyChar){
        if(id == KeyEvent.KEY_PRESSED){
            pressedKeys.add(keyCode);
        }else if(id == KeyEvent.KEY_RELEASED){
            pressedKeys.remove(keyCode);
        }
    }


    /**
     * @return true if ESC is pressed and nothing else
     *          false in all other cases
     */
    public boolean hasPressedResetProgram(){
        return pressedKeys.size() == 1 && pressedKeys.contains(KeyEvent.VK_ESCAPE);
    }

    /**
     * @return true if F5 is pressed and nothing else
     *          false in all other cases
     */
    public boolean hasPressedExecute(){
        return pressedKeys.size() == 1 && pressedKeys.contains(KeyEvent.VK_F5);
    }

    /**
     * @return true if CTRL + Z is pressed and nothing else
     *          false in all other cases
     */
    public boolean hasPressedUndo(){
        return pressedKeys.size() == 2 && pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && !pressedKeys.contains(KeyEvent.VK_SHIFT);
    }

    /**
     * @return true if CTRL + SHIFT + Z is pressed and nothing else
     *          false in all other cases
     */
    public boolean hasPressedRedo(){
        return pressedKeys.size() == 3 && pressedKeys.contains(KeyEvent.VK_CONTROL) && pressedKeys.contains(KeyEvent.VK_Z) && pressedKeys.contains(KeyEvent.VK_SHIFT);
    }
}
