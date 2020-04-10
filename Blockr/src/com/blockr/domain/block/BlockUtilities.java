package com.blockr.domain.block;

import com.blockr.domain.block.interfaces.ReadOnlyBlock;
import com.blockr.domain.block.interfaces.ReadOnlyStatementBlock;

import java.util.List;

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
            return readOnlyBlock.getPrevious();
        }
        return readOnlyBlock;
    }
}
