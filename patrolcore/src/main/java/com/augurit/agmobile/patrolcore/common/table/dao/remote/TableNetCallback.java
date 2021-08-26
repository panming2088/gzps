package com.augurit.agmobile.patrolcore.common.table.dao.remote;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model
 * @createTime 创建时间 ：17/3/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/7
 * @modifyMemo 修改备注：
 */

public interface TableNetCallback<T> {
    void onSuccess(T data);
    void onError(String msg);
}
