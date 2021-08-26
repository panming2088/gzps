package com.augurit.am.fw.cache.img.amimageloader;

import com.bumptech.glide.Priority;

/**
 * 加载优先级策略
 * Created by Administrator on 2016-07-12.
 */
public enum  AMLoadPriority {
    LOW(Priority.LOW),

    NORMAL(Priority.NORMAL),

    HIGH(Priority.HIGH),

    IMMEDIATE(Priority.IMMEDIATE),;

    Priority priority;

    AMLoadPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }
}
