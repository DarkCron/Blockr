package com.gameworld;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Action {

    MOVE_FORWARD,
    TURN_LEFT,
    TURN_RIGHT;

    public String toString(){
        String[] split = name().toLowerCase().split("_");
        return split[0].substring(0, 1).toUpperCase() + split[0].substring(1) + " " + Arrays.stream(split).skip(1).collect(Collectors.joining(" "));
    }

}
