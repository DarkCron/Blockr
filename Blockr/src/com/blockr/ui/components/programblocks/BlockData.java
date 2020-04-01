package com.blockr.ui.components.programblocks;

import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;

import java.awt.*;

/**
 * Contains static, permanent data for displaying blocks on screen.
 */
public class BlockData {

    static final int BLOCK_WIDTH = 100;
    static final int BLOCK_HEIGHT = 40;
    static final int CONTROL_FLOW_INNER_START = (int)(BLOCK_WIDTH * 0.45f);
    static final int CONDITION_BLOCK_WIDTH = (int)(BLOCK_WIDTH * 0.5f);
    static final int CONDITION_BLOCK_HEIGHT = (int)(BLOCK_HEIGHT * 0.8f);

    static final int FONT_SIZE = 10;


    static final Color STANDARD_COLOR = Color.DARK_GRAY;
    static final Color BG_COLOR = Color.WHITE;
    static final Color HIGHLIGHT_COLOR = Color.GREEN;
    static final Color CONNECTION_COLOR = Color.orange;
    static final Color FONT_COLOR = Color.BLACK;

    public static String getName(Block block){
        if(block instanceof NotBlock){
            return "Not";
        }else if(block instanceof WallInFrontBlock){
            return "Wall in F.";
        }else if(block instanceof MoveForwardBlock){
            return "Move Forward";
        }else if(block instanceof TurnBlock){
            return "Turn "+(((TurnBlock) block).getDirection() == TurnBlock.Direction.LEFT ? "Left" : "Right");
        }else if(block instanceof IfBlock){
            return "If";
        }else if(block instanceof WhileBlock){
            return "While";
        }
        return "";
    }
}
