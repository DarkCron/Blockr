package com.blockr.handlers.ui.input.recordMousePos;

import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;
import com.ui.WindowPosition;

public class GetMouseRecordHandler extends HandlerBase<GetMouseRecord, WindowPosition> {
    public GetMouseRecordHandler(State state) {
        super(state);
    }

    @Override
    public WindowPosition handle(GetMouseRecord paletteSelection) {
        return getState().getRecordMouse();
    }
}
