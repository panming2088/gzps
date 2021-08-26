package com.augurit.am.fw.db.liteorm.db.annotation;




import com.augurit.am.fw.db.liteorm.db.enums.Strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 冲突策略
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Conflict {
    Strategy value();
}
