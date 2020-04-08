package com.robotgame;

import java.awt.*;

public class GoalTile {

    public static void draw(Graphics graphics) {

        var clip = graphics.getClipBounds();

        float radius = 0.9f;
        float mod = 0.1f;
        for (int i = 1; i < 10; i++) {
            if(i % 2 == 0){
                graphics.setColor(Color.white);
            }else{
                graphics.setColor(Color.red);
            }
            graphics.fillArc((int)((0.5-radius/2) * clip.getWidth()),(int)((0.5-radius/2) * clip.getHeight()),(int)(radius * clip.getWidth()),(int)(radius * clip.getHeight()),0,360);
            radius -= mod;
        }
    }

}
