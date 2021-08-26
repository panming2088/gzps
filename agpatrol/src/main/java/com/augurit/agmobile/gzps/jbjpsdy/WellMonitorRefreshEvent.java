package com.augurit.agmobile.gzps.jbjpsdy;

public class WellMonitorRefreshEvent {
    private int type;//1是删除，2是更新
    public WellMonitorRefreshEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
