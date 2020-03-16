package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;

public class BlockCreator {
    public enum BlockType{
        MOVEFORWARD, TURN_LEFT, TURN_RIGHT, IF, WHILE, NOT, WALLINFRONT;
        static public BlockType getType(Block b){
            if(b instanceof MoveForwardBlock){
                return MOVEFORWARD;
            }else if(b instanceof TurnBlock){
                if(((TurnBlock) b).getDirection() == TurnBlock.Direction.LEFT){
                    return TURN_LEFT;
                }else{
                    return TURN_RIGHT;
                }
            }else if(b instanceof WhileBlock){
                return WHILE;
            }else if(b instanceof IfBlock){
                return IF;
            }else if(b instanceof NotBlock){
                return NOT;
            }else if(b instanceof WallInFrontBlock){
                return WALLINFRONT;
            }
            throw new IllegalArgumentException("Give Block Type not implemented.");
        }
    }

    public static Block build(BlockType blockType){
        switch (blockType){
            case MOVEFORWARD:
                return new MoveForwardBlock();
            case IF:
                return new IfBlock();
            case NOT:
                return new NotBlock();
            case WHILE:
                return new WhileBlock();
            case TURN_LEFT:
                TurnBlock turn = new TurnBlock();
                turn.setDirection(TurnBlock.Direction.LEFT);
                return turn;
            case TURN_RIGHT:
                turn = new TurnBlock();
                turn.setDirection(TurnBlock.Direction.RIGHT);
                return turn;
            case WALLINFRONT:
                return new WallInFrontBlock();
            default:
                throw new IllegalArgumentException("Give Block Type not implemented.");
        }
    }
}
