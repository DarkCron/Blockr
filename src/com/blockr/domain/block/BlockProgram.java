package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.CompositeBlock;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.ui.components.programblocks.ProgramBlockInsertInfo;

import java.util.*;
import java.util.stream.Collectors;

public class BlockProgram implements ReadOnlyBlockProgram {

    public BlockProgram(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }

    private final GameWorld gameWorld;

    @Override
    public List<? extends ReadOnlyStatementBlock> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public ReadOnlyStatementBlock getActive() {

        if(currentBlock != null)
            return currentBlock.getActive();

        if(canStart())
            return components.get(0).getActive();

        return null;
    }

    //First block of every chain program
    private final List<StatementBlock> components = new LinkedList<>();
    //All blocks in the programArea
    private final Set<StatementBlock> blocks = new HashSet<>();

    private StatementBlock currentBlock;

    /**
     * Returns whether or not the program is in a valid state and can be started
     */
    public boolean canStart(){

        if(components.size() != 1)
            return false;

        return getCompositeBlocks().stream().allMatch(CompositeBlock::isReady);
    }

    public void executeNext(){

        //TODO: add exception message
        if(!canStart())
            throw new RuntimeException("");

        if(currentBlock == null){
            currentBlock = components.get(0);
        }

        currentBlock = currentBlock.execute(gameWorld);
    }

    public void clear(){
        blocks.clear();
        components.clear();
    }

    public void reset(){
        getCompositeBlocks().forEach(CompositeBlock::reset);
        currentBlock = null;
    }

    private Set<CompositeBlock> getCompositeBlocks(){
        return blocks.stream().filter(b -> b instanceof CompositeBlock).map(b -> (CompositeBlock)b).collect(Collectors.toSet());
    }

    /**
     * Add a new block to the program, not connected to any other blocks
     */
    public void addBlock(ReadOnlyStatementBlock statementBlock){

        ensureValidStatementBlock(statementBlock, "statementBlock");

        //noinspection SuspiciousMethodCalls
        if(blocks.contains(statementBlock)){
            throw new IllegalArgumentException("The given statementBlock already exists");
        }

        components.add((StatementBlock) statementBlock);
        blocks.add((StatementBlock)statementBlock);
    }

    /**
     * Connects the plug of plugBlock to the socket of socketBlock
     */
    public void connectToStatementSocket(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock){

        ensureValidStatementBlock(socketBlock, "socketBlock");
        ensureValidStatementBlock(plugBlock, "plugBlock");

        if(!blocks.contains(socketBlock)){
            throw new IllegalArgumentException("The given socketBlock does not exist");
        }

        var rwSocketBlock = (StatementBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        rwSocketBlock.setNext(rwPlugBlock);

        if(rwPlugBlock.getPrevious() != null){

            rwPlugBlock.getPrevious().setNext(null);
        }

        rwPlugBlock.setPrevious(rwSocketBlock);

        if(blocks.contains(plugBlock))
            return;

        blocks.add(rwPlugBlock);
    }

    /**
     * Disconnects the plug of plugBlock from the socket of socketBlock
     */
    public void disconnectStatementBlock(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock){

        ensureValidStatementBlock(socketBlock, "socketBlock");
        ensureValidStatementBlock(plugBlock, "plugBlock");

        if(!blocks.contains(socketBlock)){
            throw new IllegalArgumentException("The given socketBlock does not exist");
        }

        if(!blocks.contains(plugBlock)){
            throw new IllegalArgumentException("The given plugBlock does not exist");
        }

        var rwSocketBlock = (StatementBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        rwSocketBlock.setNext(null);

        components.add(rwPlugBlock);
    }

    private static void ensureValidStatementBlock(ReadOnlyStatementBlock roBlock, String argName){

        if(roBlock == null){
            throw new IllegalArgumentException(String.format("The given %s must be effective", argName));
        }

        if(!(roBlock instanceof StatementBlock))
            throw new IllegalArgumentException(String.format("The given %s must be an instance of StatementBlock", argName));
    }

    public ReadOnlyStatementBlock getRootBlock(ReadOnlyStatementBlock blockOfChain){
        ensureValidStatementBlock(blockOfChain, "block");

        for(StatementBlock block : components){
            var currentBlock = block;
            while (currentBlock != null){
                if(currentBlock == blockOfChain){
                    return block;
                }
                currentBlock = currentBlock.getNext();
            }
        }

        return null;
    }

    /*
    Function that inserts a given plug block into a socket block, at least 1 of these 2 blocks already exists in the blocks
    set (don't know which). PlugLocation returns the location where the socket should be plugged if it can't be inferred.
    @return Should return the root component of the program that was modified so the visual graph can be rebuild
     */
    public Block processInsertBlock(ProgramBlockInsertInfo programBlockInsertInfo){
        Block plug = programBlockInsertInfo.getPlug();
        Block socket = programBlockInsertInfo.getSocket();
        var location = programBlockInsertInfo.getPlugLocation();
        //TODO: rest
        return null;
    }
}
