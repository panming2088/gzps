package com.augurit.agmobile.gzps.doorno.view;


import com.augurit.agmobile.gzps.doorno.model.SewerageInvestBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;

/**
 * Created by xiaoyu on 2018/4/9.
 */

public class SewerageItemPresenter {
    private ISewerageItemView iSewerageItemView;
    private ISewerageItemBiz iSewerageItemBiz;

    private IGetRoomInfo iGetRoomInfo;
    private IGetFail iGetFail;
    public SewerageItemPresenter(ISewerageItemView iSewerageItemView) {
        this.iSewerageItemView = iSewerageItemView;

        iSewerageItemBiz = IBizFactory.getSewerageItemBiz();

    }
    public void setIGetFail(IGetFail iGetFail){
        this.iGetFail=iGetFail;
    }

    public void setIGetRoomInfoListener(IGetRoomInfo iGetRoomInfo){
        this.iGetRoomInfo=iGetRoomInfo;
    }

    public void getdata(String id, String dzdm, int page, int count, int refresh_item, int refresh_list_type, String add_type){
        iSewerageItemBiz.getdata(new SewerageItemListener() {
            @Override
            public void onSuccess(SewerageItemBean sewerageItemBean) {
                iSewerageItemView.setSewerageItemList(sewerageItemBean);
            }

            @Override
            public void onInvestSuccess(SewerageInvestBean code) {

            }

            @Override
            public void onGetBuildInfo(BuildInfoBySGuid buildInfoBySGuid) {

            }

            @Override
            public void onFailure(String code) {
            }
        },id,dzdm,page,count,refresh_item,refresh_list_type,add_type);
    }

    public void investEnd(String sGuid, String loginName, String add_type){
        if(iSewerageItemView != null){
            iSewerageItemView.onLoadStart();
        }
        iSewerageItemBiz.investEnd(new SewerageItemListener() {
            @Override
            public void onSuccess(SewerageItemBean sewerageItemBean) {
            }

            @Override
            public void onInvestSuccess(SewerageInvestBean code) {
                if(iSewerageItemView != null){
                    iSewerageItemView.onInvestCode(code);
                    iSewerageItemView.onCompelete();
                }
            }

            @Override
            public void onGetBuildInfo(BuildInfoBySGuid buildInfoBySGuid) {

            }

            @Override
            public void onFailure(String code) {
                if(iSewerageItemView != null){
                    iSewerageItemView.onCompelete();
                }
            }
        },sGuid,loginName,add_type);
    }


    public void getdata(String id, String dzdm, int page, int count, int refresh_item, int refresh_list_type, String name, String add_type){
        iSewerageItemBiz.getdata(new SewerageItemListener() {
            @Override
            public void onSuccess(SewerageItemBean sewerageItemBean) {
                iSewerageItemView.setSewerageItemList(sewerageItemBean);
            }

            @Override
            public void onInvestSuccess(SewerageInvestBean bean) {

            }

            @Override
            public void onGetBuildInfo(BuildInfoBySGuid buildInfoBySGuid) {

            }

            @Override
            public void onFailure(String code) {

                iGetFail.onFail(code);
            }
        },id,dzdm,page,count,refresh_item,refresh_list_type,name,add_type);
    }

 /*   public void searchByCompanyName(String id,String dzdm,int page,int count,int refresh_item,int refresh_list_type,String name){
        iSewerageItemBiz.searchByCompanyName(new SewerageItemListener() {
            @Override
            public void onSuccess(SewerageItemBean sewerageItemBean) {
                iGetSearchByCompanyName.setSearchByCompanyName(sewerageItemBean);
            }
            @Override
            public void onFailure(String code) {
            }
        },id,dzdm,page,count,refresh_item,refresh_list_type,name);
    }
    public interface IGetSearchByCompanyName{
        void setSearchByCompanyName(SewerageItemBean sewerageItemList);
    }*/
    public void getBuildInfBySGuid(String sGuid){
       iSewerageItemBiz.getBuildInfBySGuid(new SewerageItemListener() {
           @Override
           public void onSuccess(SewerageItemBean sewerageItemBean) {
           }
           @Override
           public void onInvestSuccess(SewerageInvestBean bean) {
           }
           @Override
           public void onGetBuildInfo(BuildInfoBySGuid buildInfoBySGuid) {
               iGetRoomInfo.getBuildInfBySGuid(buildInfoBySGuid);
           }
           @Override
           public void onFailure(String code) {
           }
       },sGuid);
    }


    public interface IGetRoomInfo{
        void getBuildInfBySGuid(BuildInfoBySGuid buildInfoBySGuid);
    }
    public interface IGetFail{
        void onFail(String code);
    }
}
