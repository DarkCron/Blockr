package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.markers.ReadOnlyFunctionDefinitionBlock;
import com.gameworld.GameWorld;

public class FunctionDefinitionBlock extends StatementBlock implements ReadOnlyFunctionDefinitionBlock {
    private FunctionBodyBlock functionBody;

    StatementBlock getCurrent(){
        return current;
    }

    void setCurrent(StatementBlock current){
        this.current = current;
    }

    private StatementBlock current;

    @Override
    public StatementBlock execute(GameWorld gameWorld) {
        if(current == null){
            current = functionBody.getBody();
        }

        if(false){
            while(current != null){
                current = current.execute(gameWorld);
            }
        }else{
            if(current != null){
                current = current.execute(gameWorld);
                if(current!=null){
                    return this;
                }
            }
        }



        return getNext();
    }

    public FunctionBodyBlock getFunctionBody() {
        return functionBody;
    }

    public void setFunctionBody(FunctionBodyBlock functionBody) {
        this.functionBody = functionBody;
    }
}
