package com.blockr.ui.components;

import purecollections.PList;

import java.util.Set;

public interface Container extends Component {

    PList<Component> getChildComponents();


}
