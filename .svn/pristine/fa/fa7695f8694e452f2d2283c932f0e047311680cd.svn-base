package com.augurit.agmobile.gzps.doorno.view;

import android.content.Context;
import android.widget.Toast;


import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.service.PSHAffairService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * com.augurit.agmobile.gzpssb.pshpublicaffair.view.affairdetail
 * Created by sdb on 2018/4/12  16:10.
 * Desc：
 */

public class PSHAffairDetailPresenter implements PSHAffairDetailContract.Presenter {
    private PSHAffairDetailContract.View view;
    private Context context;
    private PSHAffairService pshAffairService;

    public PSHAffairDetailPresenter(PSHAffairDetailContract.View view, Context context) {
        this.view = view;
        this.context = context;
        pshAffairService = new PSHAffairService(context);
    }


    @Override
    public void getPSHAffairDetail(int affaridId) {

        pshAffairService.getPSHAffairDetail(affaridId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<PSHAffairDetail>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                        view.onGetPSHAffairDetail(null);
                    }

                    @Override
                    public void onNext(PSHAffairDetail pshAffairDetail) {
                        view.onGetPSHAffairDetail(pshAffairDetail);
                    }
                });


    }

    @Override
    public void getPSHUnitDetail(int unitId) {
        pshAffairService.getPSHUnitDetail(unitId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<PSHAffairDetail>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
                        view.onGetPSHAffairDetail(null);
                    }

                    @Override
                    public void onNext(PSHAffairDetail pshAffairDetail) {
                        view.onGetPSHAffairDetail(pshAffairDetail);
                    }
                });
    }
}
