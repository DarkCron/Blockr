package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.CompositeBlock;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.gameworld.GameWorld;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockProgram implements ReadOnlyBlockProgram {

    public BlockProgram(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }

    private final GameWorld gameWorld;

    @Override
    public List<? extends ReadOnlyStatementBlock> getComponents() {
        return components;
    }

    private final List<StatementBlock> components = new LinkedList<>();
    private final Set<StatementBlock> blocks = new HashSet<>();

    private StatementBlock currentBlock;

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

    public ReadOnlyStatementBlock getActiveBlock(){

        if(currentBlock != null)
            return currentBlock.getActive();

        if(canStart())
            return components.get(0).getActive();

        return null;
    }

    public void reset(){
        getCompositeBlocks().forEach(CompositeBlock::reset);
        currentBlock = null;
    }

    public Set<CompositeBlock> getCompositeBlocks(){
        return blocks.stream().filter(b -> b instanceof CompositeBlock).map(b -> (CompositeBlock)b).collect(Collectors.toSet());
    }

    public void addBlock(ReadOnlyStatementBlock statementBlock){
        components.add((StatementBlock) statementBlock);
        blocks.add((StatementBlock)statementBlock);
    }

    public void connectToStatementSocket(ReadOnlyStatementBlock socketBlock, ReadOnlyStatementBlock plugBlock){

        ensureValidStatementBlock(socketBlock, "socketBlock");
        ensureValidStatementBlock(plugBlock, "plugBlock");

        var rwSocketBlock = (StatementBlock)socketBlock;
        var rwPlugBlock = (StatementBlock)plugBlock;

        rwSocketBlock.setNext(rwPlugBlock);
        rwPlugBlock.setPrevious(rwSocketBlock);

        if(blocks.contains(plugBlock))
            return;

        blocks.add(rwPlugBlock);
    }

    private static void ensureValidStatementBlock(ReadOnlyStatementBlock roBlock, String argName){

        if(roBlock == null){
            throw new IllegalArgumentException(String.format("The given %s must be effective", argName));
        }

        if(!(roBlock instanceof StatementBlock))
            throw new IllegalArgumentException(String.format("The given %s must be an instance of StatementBlock", argName));
    }
}
