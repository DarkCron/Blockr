package com.ui.mouseevent;

import com.ui.WindowPosition;

import java.util.Arrays;

public class MouseEvent {

    public Type getType(){
        return type;
    }

    private final Type type;
    private final WindowPosition windowPosition;
    public WindowPosition getWindowPosition() {
        return windowPosition;
    }

    public MouseEvent(Type type, WindowPosition windowPosition){
        this.type = type;
        this.windowPosition = windowPosition;
    }



    public static enum Type {
        MOUSE_DOWN(501),
        MOUSE_UP(502),
        MOUSE_CLICKED(500),
        MOUSE_DRAG(506);

        public int getId(){
            return id;
        }

        private final int id;

        Type(int id){
            this.id = id;
        }

        public static Type getTypeById(int id){
            return Arrays.stream(Type.values()).filter(t -> t.getId() == id).findFirst().orElse(null);
        }
    }

}
