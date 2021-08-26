package com.augurit.agmobile.gzps.doorno.view;


import com.augurit.agmobile.gzps.doorno.model.SewerageInvestBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;

/**
 * Created by xiaoyu on 2018/4/9.
 */

public interface SewerageItemListener extends BaseOnlistener{

    void onSuccess(SewerageItemBean sewerageItemBean);
    void onInvestSuccess(SewerageInvestBean bean);
    void onGetBuildInfo(BuildInfoBySGuid buildInfoBySGuid);
}
