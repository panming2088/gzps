package com.augurit.agmobile.gzps.componentmaintenance;

import com.augurit.agmobile.patrolcore.common.model.Component;

/**
 * Created by long on 2017/10/24.
 */

public class SelectComponentEvent {

    private Component component;


    public SelectComponentEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}

