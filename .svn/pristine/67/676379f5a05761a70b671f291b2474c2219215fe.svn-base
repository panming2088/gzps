package com.augurit.agmobile.gzps.doorno.view;

import android.util.Log;


import com.augurit.agmobile.gzps.doorno.model.SewerageInvestBean;
import com.augurit.agmobile.gzps.doorno.service.MyRetroService;
import com.augurit.agmobile.gzps.doorno.utils.HttpSubCribe;
import com.augurit.agmobile.gzps.doorno.utils.ReHttpUtils;
import com.augurit.agmobile.gzps.uploadfacility.model.BuildInfoBySGuid;

import rx.Observable;

/**
 * Created by xiaoyu on 2018/4/9.
 */

public class BizSewerageItem implements ISewerageItemBiz {

    private static BizSewerageItem testMoudle;

    public synchronized static BizSewerageItem getInstanse() {
        if (testMoudle == null)
            testMoudle = new BizSewerageItem();
        return testMoudle;
    }

    @Override
    public void getdata(final SewerageItemListener sewerageItemListener, final String id, final String dzdm, final int page, final int count, final int refresh_item, final int refresh_list_type, final String add_type) {
//        ReHttpUtils.instans().httpRequest(new HttpSubCribe<SewerageItemBean>() {
//
//
//            @Override
//            public void onError(Throwable throwable) {
//                try{
//                    fail(sewerageItemListener,throwable);
//                }catch (Exception e){
//                    Log.e("TAG","Exception:"+e.toString());
//                }
//            }
//
//            @Override
//            public void onNext(SewerageItemBean sewerageItemBean) {
//                if (sewerageItemListener != null) {
//                    sewerageItemListener.onSuccess(sewerageItemBean);
//                }
//            }
//
//            @Override
//            public Observable<SewerageItemBean> getObservable(MyRetroService retrofit) {
//                return retrofit.getSewerageItemData(id, dzdm, page, count, refresh_item, refresh_list_type, add_type);
//            }
//        });
//    }
//
//    @Override
//    public void investEnd(final SewerageItemListener sewerageItemListener, final String sGuid, final String loginName, final String add_type) {
//        ReHttpUtils.instans().httpRequest(new HttpSubCribe<SewerageInvestBean>() {
//
//            @Override
//            public void onError(Throwable throwable) {
//            }
//
//            @Override
//            public void onNext(SewerageInvestBean sewerageItemBean) {
//                if (sewerageItemListener != null) {
//                    sewerageItemListener.onInvestSuccess(sewerageItemBean);
//                }
//            }
//
//            @Override
//            public Observable<SewerageInvestBean> getObservable(MyRetroService retrofit) {
//                return retrofit.investEnd(sGuid, loginName, add_type);
//            }
//        });
    }

    @Override
    public void investEnd(SewerageItemListener sewerageItemListener, String sGuid, String loginName, String add_type) {

    }


    @Override
    public void getdata(final SewerageItemListener sewerageItemListener, final String id, final String dzdm, final int page, final int count, final int refresh_item, final int refresh_list_type, final String name, final String add_type) {
//        ReHttpUtils.instans().httpRequest(new HttpSubCribe<SewerageItemBean>() {
//
//            @Override
//            public void onError(Throwable throwable) {
//                try{
//                    fail(sewerageItemListener,throwable);
//                }catch (Exception e){
//                    Log.e("TAG","Exception:"+e.toString());
//                }
//            }
//
//            @Override
//            public void onNext(SewerageItemBean sewerageItemBean) {
//                sewerageItemListener.onSuccess(sewerageItemBean);
//            }
//
//            @Override
//            public Observable<SewerageItemBean> getObservable(MyRetroService retrofit) {
//                return retrofit.getSewerageItemData(id, dzdm, page, count, refresh_item, refresh_list_type, name, add_type);
//            }
//        });
    }

    @Override
    public void getBuildInfBySGuid(final SewerageItemListener sewerageItemListener, final String sGuid) {
//        ReHttpUtils.instans().httpRequest(new HttpSubCribe<BuildInfoBySGuid>() {
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onNext(BuildInfoBySGuid buildInfoBySGuid) {
//                sewerageItemListener.onGetBuildInfo(buildInfoBySGuid);
//            }
//
//            @Override
//            public Observable<BuildInfoBySGuid> getObservable(MyRetroService retrofit) {
//                return retrofit.getBuildInfBySGuid(sGuid);
//            }
//        });
    }
    private void fail(SewerageItemListener sewerageItemListener, Throwable throwable){
        if(throwable.toString().contains("java.net.ConnectException")){
            sewerageItemListener.onFailure("当前网络异常，请重试");
        }else if(throwable.toString().contains("java.net.SocketTimeOutException")){
            sewerageItemListener.onFailure("数据请求超时，请重新请求");
        }
    }
}
