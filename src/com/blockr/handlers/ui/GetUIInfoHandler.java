package com.blockr.handlers.ui;

import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class GetUIInfoHandler extends HandlerBase<GetUIInfo, UIInfo> {

    public GetUIInfoHandler(State state) {
        super(state);
    }

    @Override
    public UIInfo handle(GetUIInfo getUIInfo) {
        return getState().getUiInfo();
    }
}
