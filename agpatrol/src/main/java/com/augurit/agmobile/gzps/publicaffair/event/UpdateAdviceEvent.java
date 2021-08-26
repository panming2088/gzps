package com.augurit.agmobile.gzps.publicaffair.event;

import com.augurit.agmobile.gzps.uploadevent.model.EventDetail;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.publicaffair.event
 * @createTime 创建时间 ：2017/12/5
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/12/5
 * @modifyMemo 修改备注：
 */

public class UpdateAdviceEvent {
    private EventDetail.OpinionBean opinion;

    public UpdateAdviceEvent(EventDetail.OpinionBean opinion) {
        this.opinion = opinion;
    }

    public EventDetail.OpinionBean getOpinion() {
        return opinion;
    }

    public void setOpinion(EventDetail.OpinionBean opinion) {
        this.opinion = opinion;
    }
}
