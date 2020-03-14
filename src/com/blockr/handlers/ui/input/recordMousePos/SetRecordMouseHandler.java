package com.blockr.handlers.ui.input.recordMousePos;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class SetRecordMouseHandler extends HandlerBase<SetRecordMouse, Voidy> {
    public SetRecordMouseHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(SetRecordMouse recordMouse) {
        getState().setRecordMouse(recordMouse.getMousePosition());
        return new Voidy();
    }
}
