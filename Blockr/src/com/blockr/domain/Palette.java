package com.blockr.domain;

import com.blockr.domain.block.*;
import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Palette {

    private static final List<Class<? extends Block>> BLOCK_TYPES = Arrays.asList(
            TurnBlock.class,
            MoveForwardBlock.class,
            WhileBlock.class,
            IfBlock.class,
            NotBlock.class,
            WallInFrontBlock.class
    );

    public static List<? extends ReadOnlyBlock> getAvailableBlocks(BlockProgram blockProgram){

        var blocks = new LinkedList<Block>();

        for(var clazz : BLOCK_TYPES){
            try {
                blocks.add(clazz.getDeclaredConstructor().newInstance());
            }catch (Exception ex){
                // ignored
            }
        }

        return blocks;
    }

    public static Block createInstance(Block block){
        try{
            return block.getClass().getDeclaredConstructor().newInstance();
        }catch(Exception ex){
            // ignored
            return null;
        }
    }
}
