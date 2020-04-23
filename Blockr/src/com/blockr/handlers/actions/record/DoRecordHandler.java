package com.blockr.handlers.actions.record;

import an.awesome.pipelinr.Voidy;
import com.blockr.State;
import com.blockr.handlers.HandlerBase;

public class DoRecordHandler extends HandlerBase<DoRecord, Voidy> {

    public DoRecordHandler(State state) {
        super(state);
    }

    @Override
    public Voidy handle(DoRecord doRecord) {
        return new Voidy();
    }


}
