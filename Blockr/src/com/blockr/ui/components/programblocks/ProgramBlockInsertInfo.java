package com.blockr.ui.components.programblocks;

import com.blockr.domain.block.interfaces.Block;
import com.blockr.domain.block.interfaces.ReadOnlyBlock;

/**
* Value class that defines which block is the socket, which is the plug and, optionally, where the plug should be socketed.
* Example, if socket is MoveForward and plug is Turn, than the correct graph order should be MoveForward > Turn
* if socket is If and plug is Not than the Not should be the condition of the If block
* if socket is While and plug is MoveForward and the location is Body, than the While body should point to the MoveForward Block
 */
public class ProgramBlockInsertInfo {
    private final ReadOnlyBlock socket;
    private final ReadOnlyBlock plug;
    private final PlugLocation plugLocation;

    public ReadOnlyBlock getSocket() {
        return socket;
    }

    public ReadOnlyBlock getPlug() {
        return plug;
    }

    public PlugLocation getPlugLocation() {
        return plugLocation;
    }

    //OTHER means that the location to plug can be inferred
    public enum PlugLocation {BODY, OTHER}

    public ProgramBlockInsertInfo(ReadOnlyBlock socket, ReadOnlyBlock plug, PlugLocation plugLocation) {
        this.socket = socket;
        this.plug = plug;
        this.plugLocation = plugLocation;
    }
}
