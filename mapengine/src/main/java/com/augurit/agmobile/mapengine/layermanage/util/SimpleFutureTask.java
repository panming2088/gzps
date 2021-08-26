package com.augurit.agmobile.mapengine.layermanage.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.util
 * @createTime 创建时间 ：2017-04-27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-27
 * @modifyMemo 修改备注：
 */

public abstract class SimpleFutureTask<T> extends FutureTask<T> {

    public SimpleFutureTask(Callable<T> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        onFinish();
    }

    public abstract void onFinish();


}
