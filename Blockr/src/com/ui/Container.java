package com.ui;

import java.util.List;

public abstract class Container extends Component {

    public abstract List<? extends Component> getChildren();

    public abstract WindowRegion getChildRegion(WindowRegion region, Component child);
}
