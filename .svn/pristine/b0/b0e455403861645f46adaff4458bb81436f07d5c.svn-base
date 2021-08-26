package com.augurit.agmobile.gzps.uploadfacility.service;

import android.content.Context;

import com.augurit.agmobile.gzps.uploadfacility.dao.DeleteFacilityApi;
import com.augurit.agmobile.gzps.uploadfacility.dao.ISearchRoadApi;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.net.AMNetwork;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 *
 * 模糊搜索道路
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.service
 * @createTime 创建时间 ：18/1/25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/25
 * @modifyMemo 修改备注：
 */

public class SearchRoadService {

    private Context mContext;

    private AMNetwork amNetwork;
    private ISearchRoadApi searchRoadApi;

    public SearchRoadService(Context mContext) {
        this.mContext = mContext;
    }


    private void initAMNetwork(String serverUrl) {
        if (amNetwork == null) {
            this.amNetwork = new AMNetwork(serverUrl);
            this.amNetwork.addLogPrint();
            this.amNetwork.addRxjavaConverterFactory();
            this.amNetwork.build();
            this.amNetwork.registerApi(ISearchRoadApi.class);
            this.searchRoadApi = (ISearchRoadApi) this.amNetwork.getServiceApi(DeleteFacilityApi.class);
        }
    }


    public Observable<List<String>> getRoads(String key){
        String loginName = new LoginRouter(mContext, AMDatabase.getInstance()).getUser().getLoginName();
        String supportUrl = BaseInfoManager.getSupportUrl(mContext);
        initAMNetwork(supportUrl);

        return searchRoadApi.getRoads(key,loginName)
                .onErrorReturn(new Func1<Throwable, List<String>>() {
                    @Override
                    public List<String> call(Throwable throwable) {
                        return new ArrayList<>();
                    }
                });
    }

}
