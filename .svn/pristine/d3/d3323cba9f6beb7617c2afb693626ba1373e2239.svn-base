package com.augurit.agmobile.patrolcore.common.table;

import java.util.Map;

/**
 * 描述：开放监听器接口给扩展
 * 通过此接口 可以额外修改上传给服务器的字段数据
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.util
 * @createTime 创建时间 ：2017/6/30
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/6/30
 * @modifyMemo 修改备注：
 */

public interface TableValueListener {
    /**
     * 注意 此接口调用处理后的数据即将马上上传或者存储到本地
     * @param valueMap
     */
    void changeValueForUpload(Map<String,String> valueMap,TableViewManager tableViewManager);
}
