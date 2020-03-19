package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.*;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.gameworld.GameWorld;
import com.blockr.ui.components.programblocks.ProgramBlockInsertInfo;

import javax.management.RuntimeErrorException;
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

    public int getBlockCount(){
        return blocks.size();
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
    private final Set<Block> blocks = new HashSet<>();

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

        if(components.contains(plugBlock)){
            components.remove(plugBlock);
        }

        if(!blocks.contains(socketBlock)){
            addBlock(socketBlock);
            //throw new IllegalArgumentException("The given socketBlock does not exist");
        }

        var rwSocketBlock = (StatementBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        //Check if the socketBlock was part of a CFB's body
        {
            for(Block b : blocks){
                if(b instanceof ControlFlowBlock){
                    if(((ControlFlowBlock) b).getBody() == rwPlugBlock){
                        ((ControlFlowBlock) b).setBody(rwSocketBlock);
                        rwSocketBlock.setPrevious((StatementBlock) b);
                    }
                }
            }
        }

        if(rwSocketBlock.getNext() != null){
            var tempPlugNext = rwPlugBlock;
            while (tempPlugNext.getNext() != null){
                tempPlugNext = tempPlugNext.getNext();
            }
            rwSocketBlock.getNext().setPrevious(tempPlugNext);
            tempPlugNext.setNext(rwSocketBlock.getNext());
            rwSocketBlock.setNext(null);
        }

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
     * Connect CFB with Condition
     */
    public void connectToStatementSocket(ReadOnlyControlFlowBlock socketBlock, ReadOnlyConditionBlock plugBlock){

        ensureValidCFB(socketBlock, "socketBlock");
        ensureValidCondition(plugBlock, "plugBlock");

        if(!blocks.contains(socketBlock)){
            addBlock(socketBlock);
            //throw new IllegalArgumentException("The given socketBlock does not exist");
        }

        var rwSocketBlock = (ControlFlowBlock)socketBlock;
        var rwPlugBlock = (ConditionBlock)plugBlock;

        if(rwPlugBlock instanceof NotBlock){
            if(rwSocketBlock.getCondition() != null){
                ((NotBlock) rwPlugBlock).setCondition(rwSocketBlock.getCondition());
                rwSocketBlock.setCondition(null);
            }
        }else if(rwPlugBlock instanceof WallInFrontBlock){
            if(rwSocketBlock.getCondition() != null){
                return; //We should not connect this new condition
            }
        }

        rwSocketBlock.setCondition(rwPlugBlock);

        if(blocks.contains(plugBlock))
            return;

        blocks.add(rwPlugBlock);
    }
    /**
     * Connect Statement to CFB Body, the boolean is a marker to indicate we need to connect the body
     * @param socketBlock
     * @param plugBlock
     */
    public void connectToStatementSocket(ReadOnlyControlFlowBlock socketBlock, ReadOnlyStatementBlock plugBlock, boolean bIsBody){
        ensureValidCFB(socketBlock, "socketBlock");
        ensureValidStatementBlock(plugBlock, "plugBlock");

        if(components.contains(plugBlock)){
            components.remove(plugBlock);
        }

        if(!blocks.contains(socketBlock)){
            addBlock(socketBlock);
            //throw new IllegalArgumentException("The given socketBlock does not exist");
        }

        var rwSocketBlock = (ControlFlowBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        if(rwSocketBlock.getBody() != null){
            rwPlugBlock.setNext(rwSocketBlock);
            rwSocketBlock.setBody(null);
        }

        rwSocketBlock.setBody(rwPlugBlock);
        rwPlugBlock.setPrevious(rwSocketBlock);

        if(blocks.contains(plugBlock))
            return;

        blocks.add(rwPlugBlock);
    }

    private void connectToConditionBlock(ReadOnlyConditionBlock socket, ReadOnlyConditionBlock plug) {
        ensureValidCondition((ReadOnlyConditionBlock) socket,"socket");
        ensureValidCondition((ReadOnlyConditionBlock) plug,"plug");

        var socketCondition = (ConditionBlock)socket;
        var plugCondition = (ConditionBlock)plug;

        if(socketCondition instanceof NotBlock){
            //Check if the plug is the condition of anything else:
            var conditionFor = blocks.stream().filter(b ->
                    (b instanceof ControlFlowBlock && ((ControlFlowBlock) b).getCondition() == plugCondition)
                         || ((b instanceof NotBlock) && ((NotBlock) b).getCondition() == plugCondition)  ).findFirst().orElse(null);
            if(conditionFor != null){
                if(conditionFor instanceof ControlFlowBlock){
                    ((ControlFlowBlock) conditionFor).setCondition( socketCondition);
                    ((NotBlock) socketCondition).setCondition(plugCondition);
                }else if(conditionFor instanceof NotBlock){
                    ((NotBlock) conditionFor).setCondition(socketCondition);
                    ((NotBlock) socketCondition).setCondition(plugCondition);
                }
            }else{
                if(plug instanceof NotBlock){
                    ((NotBlock) plug).setCondition(((NotBlock) socket).getCondition());
                    ((NotBlock) socket).setCondition((ConditionBlock) plug);
                }else{
                    ((NotBlock) socket).setCondition((ConditionBlock) plug);
                }
            }
        }else{
            throw new IllegalArgumentException("Cannot connect with this condition socket.");
        }

        if(!blocks.contains(socketCondition)){
            blocks.add(socketCondition);
        }
        if(!blocks.contains(plugCondition)){
            blocks.add(plugCondition);
        }
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
    private static void ensureValidCFB(ReadOnlyControlFlowBlock roBlock, String argName){

        if(roBlock == null){
            throw new IllegalArgumentException(String.format("The given %s must be effective", argName));
        }

        if(!(roBlock instanceof ReadOnlyControlFlowBlock))
            throw new IllegalArgumentException(String.format("The given %s must be an instance of StatementBlock", argName));
    }
    private static void ensureValidCondition(ReadOnlyConditionBlock roBlock, String argName){

        if(roBlock == null){
            throw new IllegalArgumentException(String.format("The given %s must be effective", argName));
        }

        if(!(roBlock instanceof ReadOnlyConditionBlock))
            throw new IllegalArgumentException(String.format("The given %s must be an instance of StatementBlock", argName));
    }

    public ReadOnlyStatementBlock getRootBlock(Block blockOfChain){
        //ensureValidStatementBlock(blockOfChain, "block");
        if(blockOfChain instanceof ConditionBlock){
            for(Block b : blocks){
                if(b instanceof ControlFlowBlock){
                    if(((ControlFlowBlock) b).getCondition() == blockOfChain){
                        return getRootBlock(b);
                    }
                }else if(b instanceof NotBlock){
                    if(((NotBlock) b).getCondition() == blockOfChain){
                        return getRootBlock(b);
                    }
                }
            }
        }

        if(blockOfChain instanceof ConditionBlock){
            System.out.println();
        }
        for(StatementBlock block : components){
            if(containsRoot(block, (ReadOnlyStatementBlock) blockOfChain)){
                return block;
            }
        }
        return null;
    }

    private boolean containsRoot(ReadOnlyStatementBlock block ,ReadOnlyStatementBlock blockOfChain){
        if(block == null){
            return false;
        }
        if(block instanceof ControlFlowBlock){
            return containsRoot(((ControlFlowBlock) block).getBody(),blockOfChain) || (block == blockOfChain) || containsRoot(block.getNext(), blockOfChain);
        }else{
            return (block == blockOfChain) || containsRoot(block.getNext(), blockOfChain);
        }
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
        if(location == ProgramBlockInsertInfo.PlugLocation.BODY){
            ensureValidCFB((ReadOnlyControlFlowBlock) socket,"socket");
            ensureValidStatementBlock((ReadOnlyStatementBlock) plug,"plug");
            connectToStatementSocket((ReadOnlyControlFlowBlock) socket, (ReadOnlyStatementBlock) plug,true);
        }else{
            if(socket instanceof ReadOnlyControlFlowBlock){
                ensureValidCFB((ReadOnlyControlFlowBlock) socket,"socket");
                if(plug instanceof ReadOnlyConditionBlock){
                    ensureValidCondition((ReadOnlyConditionBlock) plug,"plug");
                    connectToStatementSocket((ReadOnlyControlFlowBlock)socket, (ReadOnlyConditionBlock) plug);
                }else{
                    ensureValidStatementBlock((ReadOnlyStatementBlock) plug,"plug");
                    connectToStatementSocket((ReadOnlyStatementBlock) socket, (ReadOnlyStatementBlock) plug);
                }
            }else if(socket instanceof ReadOnlyStatementBlock){
                ensureValidStatementBlock((ReadOnlyStatementBlock) socket,"socket");
                ensureValidStatementBlock((ReadOnlyStatementBlock) plug,"plug");
                connectToStatementSocket((ReadOnlyStatementBlock)socket,(ReadOnlyStatementBlock)plug);
            }
            if(socket instanceof ConditionBlock && plug instanceof ConditionBlock){
                ensureValidCondition((ReadOnlyConditionBlock) socket,"socket");
                ensureValidCondition((ReadOnlyConditionBlock) plug,"plug");
                connectToConditionBlock((ReadOnlyConditionBlock)socket,(ReadOnlyConditionBlock)plug);
            }
        }

        if(blocks.contains(socket) && blocks.contains(plug)){
            return getRootBlock(socket);
        }else{
            throw new RuntimeException("Error adding plug to socket.");
        }
    }
}
