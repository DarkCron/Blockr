package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.block.interfaces.ReadOnlyBlockProgram;
import com.blockr.domain.block.interfaces.ReadOnlyControlFlowBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;
import com.blockr.domain.block.interfaces.markers.ReadOnlyConditionedBlock;

import java.util.List;
import java.util.Stack;

public class BlockUtilities {
    public static ReadOnlyBlock getRootFrom(List<? extends ReadOnlyBlock> components){
        if(components.size() == 0){
            return null;
        }
        for (int i = 0; i < components.size(); i++) {
            if(components.get(i) instanceof ReadOnlyStatementBlock){
                return getRootFrom((ReadOnlyStatementBlock) components.get(i));
            }
        }
        return null;
    }

    public static ReadOnlyBlock getRootFrom(ReadOnlyStatementBlock readOnlyBlock) {
        if(readOnlyBlock.getPrevious() != null){
            return getRootFrom(readOnlyBlock.getPrevious());
        }
        return readOnlyBlock;
    }

    public static ReadOnlyBlock getRootFrom(ReadOnlyBlock componentsToMove) {
        if(componentsToMove instanceof ReadOnlyStatementBlock){
            return getRootFrom((ReadOnlyStatementBlock) componentsToMove);
        }else {
            return null;
        }
    }

    public static ReadOnlyBlock getRootFrom(ReadOnlyBlock socket, ReadOnlyBlockProgram blockProgram) {
        if(socket instanceof ReadOnlyStatementBlock){
            return getRootFrom(socket);
        }else{
            for (var comp : blockProgram.getComponents()) {
                if(comp instanceof ReadOnlyStatementBlock){
                    if(comp instanceof ReadOnlyControlFlowBlock){
                        if(isPartOf((ConditionBlock) socket,(ReadOnlyControlFlowBlock)comp)){
                            return getRootFrom(comp);
                        }
                    }

                    var next = ((ReadOnlyStatementBlock) comp).getNext();
                    while (next != null){
                        if(next instanceof ReadOnlyControlFlowBlock){
                            if(isPartOf((ConditionBlock) socket,(ReadOnlyControlFlowBlock)next)){
                                return getRootFrom(comp);
                            }
                        }
                        next = next.getNext();
                    }
                }
            }
        }
        return null;
    }

    private static boolean isPartOf(ConditionBlock socket, ReadOnlyControlFlowBlock comp) {
        if(comp.getCondition() == socket){
            return true;
        }
        var cnd = comp.getCondition();
        while(cnd != null && cnd instanceof ReadOnlyConditionedBlock) {
            if(((ReadOnlyConditionedBlock) cnd).getCondition() == socket){
                return true;
            }
            cnd = ((ReadOnlyConditionedBlock) cnd).getCondition();
        }

        var body = comp.getBody();
        while (body != null){
            if(body instanceof ReadOnlyControlFlowBlock){
                return isPartOf(socket, (ReadOnlyControlFlowBlock) body);
            }
            body = body.getNext();
        }
        return false;
    }
}
