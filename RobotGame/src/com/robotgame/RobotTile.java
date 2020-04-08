package com.robotgame;

import java.awt.*;

public class RobotTile {

    public static void draw(Graphics graphics, Orientation robotOrientation) {

        Rectangle clip = graphics.getClipBounds();

        float antennaCenterRadius = 0.2f;
        FloatPosition antennaCenter = new FloatPosition(0.5f - antennaCenterRadius / 2, 1f - 0.1f - antennaCenterRadius / 2);

        FloatPosition antennaPoleStart = new FloatPosition(0.5f, 0.9f - antennaCenterRadius / 2);
        FloatPosition antennaPoleEnd = new FloatPosition(0.5f, 0.6f);

        FloatPosition robotFaceStart = new FloatPosition(0.15f, 0.15f);
        FloatPosition robotFaceEnd = new FloatPosition(0.85f, 0.6f);
        FloatPosition robotFaceSize = new FloatPosition(robotFaceEnd.x - robotFaceStart.x, robotFaceEnd.y - robotFaceStart.y);

        float robotEyeSize = 0.3f;
        FloatPosition robotLeftEye = new FloatPosition(0.35f - robotEyeSize / 2, 0.25f);
        FloatPosition robotRightEye = new FloatPosition(0.65f - robotEyeSize / 2, 0.25f);

        float innerRobotEyeSize = 0.15f;
        FloatPosition innerRobotLeftEye = new FloatPosition(0.35f - innerRobotEyeSize / 2, 0.25f);
        FloatPosition innerRobotRightEye = new FloatPosition(0.65f - innerRobotEyeSize / 2, 0.25f);

        switch (robotOrientation){
            case NORTH:
                break;
            case EAST:
                innerRobotLeftEye = new FloatPosition(0.45f - innerRobotEyeSize / 2, 0.35f);
                innerRobotRightEye = new FloatPosition(0.75f - innerRobotEyeSize / 2, 0.35f);
                break;
            case SOUTH:
                innerRobotLeftEye = new FloatPosition(0.35f - innerRobotEyeSize / 2, 0.40f);
                innerRobotRightEye = new FloatPosition(0.65f - innerRobotEyeSize / 2, 0.40f);
                break;
            case WEST:
                innerRobotLeftEye = new FloatPosition(0.25f - innerRobotEyeSize / 2, 0.35f);
                innerRobotRightEye = new FloatPosition(0.55f - innerRobotEyeSize / 2, 0.35f);
                break;
        }
        graphics.setColor(Color.ORANGE);
        graphics.fillArc((int)(clip.getWidth()*antennaCenter.x),
                (int)(clip.getHeight()*antennaCenter.y),
                (int)(clip.getWidth()*antennaCenterRadius),
                (int)(clip.getHeight()*antennaCenterRadius),
                0,360);
        graphics.setColor(Color.BLACK);
        graphics.drawArc((int)(clip.getWidth()*antennaCenter.x),
                (int)(clip.getHeight()*antennaCenter.y),
                (int)(clip.getWidth()*antennaCenterRadius),
                (int)(clip.getHeight()*antennaCenterRadius),
                0,360);
        graphics.drawLine(antennaPoleStart.getX((int)clip.getWidth()),antennaPoleStart.getY((int)clip.getHeight()),
                antennaPoleEnd.getX((int)clip.getWidth()),antennaPoleEnd.getY((int)clip.getHeight()));
        graphics.setColor(Color.CYAN);
        graphics.fillRect(robotFaceStart.getX((int)clip.getWidth()),robotFaceStart.getY((int)clip.getHeight()),
                robotFaceSize.getX((int)clip.getWidth()),robotFaceSize.getY((int)clip.getHeight()));
        graphics.setColor(Color.BLACK);
        graphics.drawRect(robotFaceStart.getX((int)clip.getWidth()),robotFaceStart.getY((int)clip.getHeight()),
                robotFaceSize.getX((int)clip.getWidth()),robotFaceSize.getY((int)clip.getHeight()));
        graphics.setColor(Color.white);
        graphics.fillArc(robotLeftEye.getX((int)clip.getWidth()),
                robotLeftEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*robotEyeSize),
                (int)(clip.getHeight()*robotEyeSize),
                0,360);
        graphics.fillArc(robotRightEye.getX((int)clip.getWidth()),
                robotRightEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*robotEyeSize),
                (int)(clip.getHeight()*robotEyeSize),
                0,360);
        graphics.setColor(Color.black);
        graphics.drawArc(robotLeftEye.getX((int)clip.getWidth()),
                robotLeftEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*robotEyeSize),
                (int)(clip.getHeight()*robotEyeSize),
                0,360);
        graphics.drawArc(robotRightEye.getX((int)clip.getWidth()),
                robotRightEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*robotEyeSize),
                (int)(clip.getHeight()*robotEyeSize),
                0,360);
        graphics.fillArc(innerRobotLeftEye.getX((int)clip.getWidth()),
                innerRobotLeftEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*innerRobotEyeSize),
                (int)(clip.getHeight()*innerRobotEyeSize),
                0,360);
        graphics.fillArc(innerRobotRightEye.getX((int)clip.getWidth()),
                innerRobotRightEye.getY((int)clip.getHeight()),
                (int)(clip.getWidth()*innerRobotEyeSize),
                (int)(clip.getHeight()*innerRobotEyeSize),
                0,360);
    }

    public static class FloatPosition {

        public int getX(int regionWidth) {
            return ((int)(x * regionWidth));
        }

        private final float x;

        public int getY(int regionHeight) {
            return ((int)(y * regionHeight));
        }

        private final float y;

        FloatPosition(float x, float y){
            this.x = x;
            this.y = y;
        }


    }

}
