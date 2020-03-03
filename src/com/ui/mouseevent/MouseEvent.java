package com.ui.mouseevent;

import java.util.Arrays;

public class MouseEvent {

    public Type getType(){
        return type;
    }

    private final Type type;

    public MouseEvent(Type type){
        this.type = type;
    }

    public enum Type {
        MOUSE_DOWN(501),
        MOUSE_UP(502);

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
