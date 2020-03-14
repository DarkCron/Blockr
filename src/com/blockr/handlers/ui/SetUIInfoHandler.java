package com.blockr.handlers.ui;

import an.awesome.pipelinr.Voidy;
import com.blockr.domain.State;
import com.blockr.handlers.HandlerBase;

public class SetUIInfoHandler extends HandlerBase<SetUIInfo, Voidy> {

    public SetUIInfoHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(SetUIInfo setUIInfo) {
        getState().setUiInfo(setUIInfo.build());
        return new Voidy();
    }
}