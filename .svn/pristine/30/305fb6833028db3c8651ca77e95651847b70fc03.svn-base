package com.augurit.agmobile.gzps.trackmonitor.view.presenter;

import android.content.Context;

import com.augurit.agmobile.gzps.trackmonitor.model.UserOrg;
import com.augurit.agmobile.gzps.trackmonitor.service.TrackMonitorService;
import com.augurit.agmobile.gzps.trackmonitor.view.ISubordinateListView;
import com.augurit.am.cmpt.common.Callback1;

import java.util.LinkedList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view.presenter
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class SubordinateListPresenter implements ISubordinateListPresenter{

    private Context mContext;
    private ISubordinateListView mSubordinateListView;
    private TrackMonitorService mTrackMonitorService;

    private List<UserOrg> mUserOrgList;   //全部下属数据

    private int curLevel = 0;
    private LinkedList<List<UserOrg>> levelList = new LinkedList<>();  //分层次下属列表，按返回键返回上一级时用到

    private Callback1 mBackListener;

    public SubordinateListPresenter(Context context, ISubordinateListView subordinateListView){
        this.mContext = context;
        this.mSubordinateListView = subordinateListView;
        mSubordinateListView.setPresenter(this);
        mTrackMonitorService = new TrackMonitorService(mContext);
        getSubordinateList();
    }

    /**
     * 获取所有下属列表
     */
    private void getSubordinateList(){
        mTrackMonitorService.getSubordinateList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserOrg>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UserOrg> userOrgs) {
                        curLevel = 0;
                        mUserOrgList = userOrgs;
                        levelList.add(userOrgs);
                        mSubordinateListView.showSubordinateList(userOrgs);
                    }
                });
    }


    /**
     * 返回上一级，当前为第一级时退出当前界面
     */
    @Override
    public void back() {
        if(curLevel == 0){
            if(mBackListener != null){
                mBackListener.onCallback(null);
            }
        } else {
            //删除当前级别的列表，则levelList中的最后一个即为上一级别的列表
            levelList.removeLast();
            mSubordinateListView.showSubordinateList(levelList.getLast());
        }
    }

    @Override
    public void setBackListener(Callback1 backListener) {
        this.mBackListener = backListener;
    }


    @Override
    public void enter(UserOrg userOrg) {
        if(userOrg.getType().equals("dept")){
            levelList.addLast(userOrg.getSub());
            mSubordinateListView.showSubordinateList(userOrg.getSub());
        } else {

        }
    }
}
