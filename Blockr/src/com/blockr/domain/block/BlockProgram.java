package com.blockr.domain.block;

import com.blockr.domain.Palette;
import com.blockr.domain.block.interfaces.*;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyFunctionBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyFunctionDefinitionBlock;
import com.blockr.ui.components.programblocks.ProgramArea;
import com.gameworld.GameWorld;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SuspiciousMethodCalls")
public class BlockProgram implements ReadOnlyBlockProgram, Cloneable {

    public BlockProgram(GameWorld gameWorld){
        this.gameWorld = gameWorld;
        this.components = new LinkedList<>();
        this.blocks = new HashSet<>();
    }

    private BlockProgram(GameWorld gameWorld, List<Block> components, Set<Block> blocks, StatementBlock currentBlock, boolean bHasStartedExecute){
        this.gameWorld = gameWorld;
        this.components = components;
        this.blocks = blocks;
        this.currentBlock = currentBlock;
        this.bHasStartedExecute = bHasStartedExecute;
    }

    private final GameWorld gameWorld;
    private boolean bHasStartedExecute = false;

    @Override
    public List<? extends Block> getComponents() {
        return Collections.unmodifiableList(components);
    }

    /**
     * Returns the total amount of blocks in this program
     */
    public int getBlockCount(){
        return blocks.size();
    }

    /**
     * Returns the block which will be executed next
     */
    public ReadOnlyStatementBlock getActive() {

        if(currentBlock != null)
            return currentBlock.getActive();

        if(canStart())
        {
            var startBlock = (StatementBlock)components.get(0);
            return startBlock.getActive();
        }

        return null;
    }

    private final List<Block> components;

    private final Set<Block> blocks;

    private StatementBlock currentBlock;

    /**
     * Returns whether or not the program is in a valid state and can be started
     */
    public boolean canStart(){

        if(components.size() != 1)
            return false;

        //check if each conditioned block has a conditionBlock
        return getBlocksOfType(ConditionedBlock.class).stream().allMatch(ConditionedBlock::isReady) &&
               getBlocksOfType(ControlFlowBlock.class).stream().allMatch(ControlFlowBlock::isReady);
    }

    /**
     * Executes one statement of this program
     */
    public void executeNext(){

        if(!canStart())
            throw new RuntimeException("The program must be in a valid state");

        if(currentBlock == null){
            if(!bHasStartedExecute){
                bHasStartedExecute = true;
                currentBlock = (StatementBlock)components.get(0);
            }else{
                return;
            }
        }

        currentBlock = currentBlock.execute(gameWorld);
    }

    /**
     * Removes all blocks from the program
     */
    public void clear() {
        reset();

        blocks.clear();
        components.clear();
    }

    /**
     * Resets the current execution of the program
     */
    public void reset(){
        getBlocksOfType(ControlFlowBlock.class).forEach(ControlFlowBlock::reset);
        currentBlock = null;
        bHasStartedExecute = false;
        //gameWorld.resetWorldSnapshot();
    }

    private <T> Set<T> getBlocksOfType(Class<T> clazz){
        return blocks.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toSet());
    }

    /**
     * Removes the block from the program
     */
    public void removeBlock(Block block){

        if(!blocks.contains(block)){
            throw new IllegalArgumentException("The given block doesn't exist");
        }

        reset();

        getBlocksOfType(StatementBlock.class).stream().filter(b -> b.getNext() == block).forEach(b -> b.setNext(null));
        getBlocksOfType(ControlFlowBlock.class).stream().filter(b -> b.getBody() == block).forEach(b -> b.setBody(null));
        getBlocksOfType(ConditionedBlock.class).stream().filter(b -> b.getCondition() == block).forEach(b -> b.setCondition(null));

        Set<Block> toRemove = getBlocksToRemove((Block)block);

        blocks.removeAll(toRemove);
        components.remove(block);
    }

    private Set<Block> getBlocksToRemove(Block block){

        var blocks = new HashSet<Block>();

        if(block == null){
            return blocks;
        }

        blocks.add(block);

        if(block instanceof ConditionedBlock){
            var conditionedBlock = (ConditionedBlock)block;
            blocks.addAll(getBlocksToRemove(conditionedBlock.getCondition()));
        }

        if(block instanceof ControlFlowBlock){
            var controlFlowBlock = (ControlFlowBlock)block;
            blocks.addAll(getBlocksToRemove(controlFlowBlock.getBody()));
        }

        if(block instanceof StatementBlock){
            var statementBlock = (StatementBlock)block;
            blocks.addAll(getBlocksToRemove(statementBlock.getNext()));
        }

        return blocks;
    }

    /**
     * Add a new block to the program, not connected to any other blocks
     */
    public void addBlock(Block block){

        if(blocks.contains(block)){
            throw new IllegalArgumentException("The given block already exists");
        }

        reset();

        blocks.add((Block)block);
        components.add((Block)block);
    }

    /**
     * Connects a statementBlock to an existing statementBlock. This method should be called when:
     * - A new statementBlock is connected to a statementBlock
     * - An existing chain of statementBlocks is connected to a statementBlock
     * - An existing statementBlock which is the body of a controlFlowBlock is connected to a different statementBlock
     * - An existing statementBlock is disconnected from another statementBlock and connected to a different statementBlock
     */
    public void connectStatementBlock(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock) {

        ensureValidBlock(socketBlock, StatementBlock.class, "socketBlock");
        ensureValidBlock(plugBlock, StatementBlock.class,"plugBlock");

        if(!blocks.contains(socketBlock)){
            //throw new IllegalArgumentException("The given socketBlock does not exist");
            addBlock(socketBlock);
        }

        reset();

        components.remove(plugBlock);
        blocks.add(plugBlock);

        var rwSocketBlock = (StatementBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        //if the plugBlock was part of a cf block body, disconnect it
        getBlocksOfType(ControlFlowBlock.class).stream().filter(b -> b.getBody() == rwPlugBlock).forEach(b -> b.setBody(null));

        var previous = rwPlugBlock.getPrevious();
        if(previous != null){
            previous.setNext(null);
        }

        //the (chain) of statementBlock(s) might be inserted between rwSocketBlock and rwSocketBlock.getNext()
        var nextPlugBlock = rwPlugBlock;
        while(nextPlugBlock.getNext() != null){
            nextPlugBlock = nextPlugBlock.getNext();
        }

        var next = rwSocketBlock.getNext();

        if(next != null){
            nextPlugBlock.setNext(next);
            next.setPrevious(nextPlugBlock);
        }

        //update the links between rwSocketBlock and rwPlugBlock
        rwPlugBlock.setPrevious(rwSocketBlock);
        rwSocketBlock.setNext(rwPlugBlock);
    }

    /**
     * Disconnects a statementBlock from another statementBlock. This method should be called when:
     * - A statementBlock is disconnected from another statementBlock
     * - A statementBlock which is the body of a controlFlowBlock is disconnected
     */
    public void disconnectStatementBlock(ReadOnlyStatementBlock plugBlock){

        ensureValidBlock(plugBlock, StatementBlock.class, "plugBlock");

        var socketBlock = blocks.stream().filter(b -> (b instanceof StatementBlock && ((StatementBlock)b).getNext() == plugBlock) || (b instanceof ControlFlowBlock && ((ControlFlowBlock)b).getBody() == plugBlock)).findFirst();

        if(socketBlock.isEmpty()){
            throw new IllegalArgumentException("The given plugBlock is not connected to any block");
        }

        reset();

        if(socketBlock.get() instanceof ControlFlowBlock){
            var cfBlock = (ControlFlowBlock)socketBlock.get();

            cfBlock.setBody(null);
            components.add(plugBlock);
            return;
        }

        var rwSocketBlock = (StatementBlock)socketBlock.get();
        var rwPlugBlock = (StatementBlock)plugBlock;

        rwSocketBlock.setNext(null);
        rwPlugBlock.setPrevious(null);

        components.add(rwPlugBlock);
    }

    /**
     * Connects a conditionBlock to a conditionedBlocked. This method should be called when:
     * - A new conditionBlock is connected to a conditionedBlock
     * - A existing conditionBlock separate from a conditionedBlock is connected to a conditionedBlock
     * - A existing conditionBlock is disconnected from a conditionedBlock and connected to another conditionedBlock
     */
    public void connectConditionBlock(ReadOnlyConditionedBlock conditionedBlock, ReadOnlyConditionBlock conditionBlock){

        ensureValidBlock(conditionedBlock, ConditionedBlock.class, "conditionedBlock");
        ensureValidBlock(conditionBlock, ConditionBlock.class, "conditionBlock");

        if(!blocks.contains(conditionedBlock)){
            throw new IllegalArgumentException("The given conditionedBlock does not exist");
        }

        reset();

        components.remove(conditionBlock);
        blocks.add(conditionBlock);

        //disconnect the condition block
        getBlocksOfType(ConditionedBlock.class).stream().filter(b -> b.getCondition() == conditionBlock).forEach(b -> b.setCondition(null));

        var rwConditionedBlock = (ConditionedBlock)conditionedBlock;
        var rwConditionBlock = (ConditionBlock)conditionBlock;

        rwConditionedBlock.setCondition(rwConditionBlock);
    }

    /**
     * Disconnects a conditionBlock from a conditionedBlock
     */
    public void disconnectConditionBlock(ReadOnlyConditionBlock conditionBlock){

        ensureValidBlock(conditionBlock, ConditionBlock.class, "conditionBlock");

        var socketBlock = blocks.stream()
                .filter(b -> b instanceof ConditionedBlock)
                .map(b -> (ConditionedBlock)b)
                .filter(b -> b.getCondition() == conditionBlock).findFirst();

        if(socketBlock.isEmpty()){
            throw new IllegalArgumentException("The given conditionBlock is not connected to any block");
        }

        reset();

        socketBlock.get().setCondition(null);

        components.add(conditionBlock);
    }

    /**
     * Connects a statementBlock to a containerBlock as its body. This method should be called when:
     * - A new statementBlock is connected as the body of a containerBlock
     * - An existing statementBlock is connected to a containerBlock
     * - An existing statementBlock is disconnected from a statementBlock and connected to a containerBlock
     * - An existing statementBlock is disconnected from a containerBlock and connected to another containerBlock
     */
    public void connectContainedBlockBody(ReadOnlyContainerBlock containerBlock, ReadOnlyStatementBlock statementBlock){

        ensureValidBlock(containerBlock, ContainerBlock.class, "containerBlock");
        ensureValidBlock(statementBlock, StatementBlock.class, "statementBlock");

        if(!blocks.contains(containerBlock)){
            throw new IllegalArgumentException("The given controlFlowBlock does not exist");
        }

        reset();

        blocks.add(statementBlock);

        var rwContainerBlock = (ControlFlowBlock)containerBlock;
        var rwStatementBlock = (StatementBlock)statementBlock;

        assert rwContainerBlock.getBody() == null;

        getBlocksOfType(ContainerBlock.class).stream().filter(b -> b.getBody() == rwStatementBlock).forEach(b -> b.setBody(null));

        if(rwStatementBlock.getPrevious() != null){
            rwStatementBlock.getPrevious().setNext(null);
        }

        rwStatementBlock.setPrevious(null);

        components.remove(rwStatementBlock);
        rwContainerBlock.setBody(rwStatementBlock);
    }

    /**
     * Connects a statementBlock to a controlFlowBlock as its body. This method should be called when:
     * - A new statementBlock is connected as the body of a controlFlowBlock
     * - An existing statementBlock is connected to a controlFlowBlock
     * - An existing statementBlock is disconnected from a statementBlock and connected to a controlFlowBlock
     * - An existing statementBlock is disconnected from a controlFlowBlock and connected to another controlFlowBlock
     */
    public void connectControlFlowBodyAndCondition(ReadOnlyControlFlowBlock controlFlowBlock, ReadOnlyConditionBlock conditionBlock){

        ensureValidBlock(controlFlowBlock, ControlFlowBlock.class, "controlFlowBlock");
        ensureValidBlock(conditionBlock, ConditionBlock.class, "statementBlock");

        if(!blocks.contains(controlFlowBlock)){
            throw new IllegalArgumentException("The given controlFlowBlock does not exist");
        }

        reset();

        blocks.add(conditionBlock);

        var rwControlFlowBlock = (ControlFlowBlock)controlFlowBlock;
        var rwConditionBlock = (ConditionBlock)conditionBlock;

        assert rwControlFlowBlock.getBody() == null;

        getBlocksOfType(ControlFlowBlock.class).stream().filter(b -> b.getCondition() == rwConditionBlock).forEach(b -> b.setCondition(null));

        rwControlFlowBlock.setCondition(rwConditionBlock);
    }

    private static <T> void ensureValidBlock(Block block, Class<T> type, String argName){

        if(block == null){
            throw new IllegalArgumentException(String.format("The given %s must be effective", argName));
        }

        if(!type.isInstance(block)){
            throw new IllegalArgumentException(String.format("The given %s must be an instance of %s", argName, type.getSimpleName()));
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        Map<Block, Block> newBlocks = new HashMap<>();
        List<Block> newComponents = new LinkedList<>();

        for(Block block : components){
            var copy = copyComponent(block, newBlocks);
            if(block instanceof TurnBlock && copy instanceof TurnBlock){
                ((TurnBlock)copy).setDirection(((TurnBlock)block).getDirection());
            }
            newComponents.add(copy);
            ProgramArea.CarryOverCopy(block,copy);
        }

        StatementBlock newCurrent = null;

        if(currentBlock != null){
            newCurrent = (StatementBlock)newBlocks.get(currentBlock);
        }

        return new BlockProgram(gameWorld, newComponents, new HashSet<>(newBlocks.values()), newCurrent, bHasStartedExecute);
    }

    private static Block copyComponent(Block block, Map<Block, Block> allBlocks){

        var clone = Palette.createInstance(block);
        allBlocks.put(block, clone);

        if(block instanceof TurnBlock && clone instanceof TurnBlock){
            ((TurnBlock)clone).setDirection(((TurnBlock)block).getDirection());
        }

        if(block instanceof ControlFlowBlock){

            var cfBlock = (ControlFlowBlock)block;
            var newCfBlock = (ControlFlowBlock)clone;

            if(cfBlock.getBody() != null){
                var copy = (StatementBlock)copyComponent(cfBlock.getBody(), allBlocks);
                newCfBlock.setBody(copy);
                ProgramArea.CarryOverCopy(cfBlock.getBody(),copy);
            }

            if(cfBlock.getCurrent() != null){
                newCfBlock.setCurrent((StatementBlock)allBlocks.get(cfBlock.getCurrent()));
            }
        }

        if(block instanceof ConditionedBlock){

            var cBlock = (ConditionedBlock)block;
            var newConditionedBlock = (ConditionedBlock)clone;

            if(cBlock.getCondition() != null){
                var copy = (ConditionBlock)copyComponent(cBlock.getCondition(), allBlocks);
                newConditionedBlock.setCondition(copy);
                ProgramArea.CarryOverCopy(cBlock.getCondition(),copy);
            }
        }

        if(!(block instanceof StatementBlock)){
            return clone;
        }

        var statementBlock = (StatementBlock)block;

        if(statementBlock.getNext() == null)
            return clone;

        var newStatementBlock = (StatementBlock)clone;

        var next = (StatementBlock)copyComponent(statementBlock.getNext(), allBlocks);
        newStatementBlock.setNext(next);
        next.setPrevious(newStatementBlock);
        ProgramArea.CarryOverCopy(statementBlock.getNext(),next);

        return clone;
    }
}
