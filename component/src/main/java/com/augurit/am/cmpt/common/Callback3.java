package com.augurit.am.cmpt.common;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.common
 * @createTime 创建时间 ：2017-05-04
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-04
 * @modifyMemo 修改备注：
 */

public interface Callback3<T> {
    void onLoading(T t);
    void onSuccess();
    void onFail(Exception error);
}
