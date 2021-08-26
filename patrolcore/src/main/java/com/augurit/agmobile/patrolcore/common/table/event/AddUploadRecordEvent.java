package com.augurit.agmobile.patrolcore.common.table.event;


import com.augurit.agmobile.patrolcore.common.table.model.ClientTaskRecord;


/**
 * 描述：每向服务器增加一行记录,产生该消息进行记录
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.event
 * @createTime 创建时间 ：2017/8/24
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/24
 * @modifyMemo 修改备注：
 */

public class AddUploadRecordEvent {
    private ClientTaskRecord serverRecord;//记录ID

    public ClientTaskRecord getRecordId() {
        return serverRecord;
    }

    public void setRecordId(ClientTaskRecord serverRecord) {
        this.serverRecord = serverRecord;
    }

    public AddUploadRecordEvent(ClientTaskRecord serverRecord) {
        this.serverRecord = serverRecord;
    }
}
