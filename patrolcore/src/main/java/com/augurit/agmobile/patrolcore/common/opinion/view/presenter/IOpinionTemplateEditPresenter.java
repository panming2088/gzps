package com.augurit.agmobile.patrolcore.common.opinion.view.presenter;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.am.cmpt.common.Callback1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view.presenter
 * @createTime 创建时间 ：2017-07-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-18
 * @modifyMemo 修改备注：
 */

public interface IOpinionTemplateEditPresenter {
    void saveOpinionTemplate(OpinionTemplate opinionTemplate);
    void setBackListener(Callback1 callback);
}
