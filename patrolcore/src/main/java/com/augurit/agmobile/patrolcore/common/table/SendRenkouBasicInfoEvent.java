package com.augurit.agmobile.patrolcore.common.table;

import java.util.Map;

/**
 *
 * 发送人口信息表的基本信息
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.shiyourenkou
 * @createTime 创建时间 ：17/9/11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/11
 * @modifyMemo 修改备注：
 */

public class SendRenkouBasicInfoEvent {

    private Map<String,String> renkoubasicInfos;

    public SendRenkouBasicInfoEvent(Map<String,String> map) {
        this.renkoubasicInfos = map;
    }

    public Map<String, String> getRenkoubasicInfos() {
        return renkoubasicInfos;
    }

    public void setRenkoubasicInfos(Map<String, String> renkoubasicInfos) {
        this.renkoubasicInfos = renkoubasicInfos;
    }
}
