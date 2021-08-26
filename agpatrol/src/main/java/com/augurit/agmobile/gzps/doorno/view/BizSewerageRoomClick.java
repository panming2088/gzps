package com.augurit.agmobile.gzps.doorno.view;



import com.augurit.agmobile.gzps.doorno.model.SewerageRoomClickItemBean;
import com.augurit.agmobile.gzps.doorno.service.MyRetroService;
import com.augurit.agmobile.gzps.doorno.utils.HttpSubCribe;
import com.augurit.agmobile.gzps.doorno.utils.ReHttpUtils;

import rx.Observable;

/**
 * Created by xiaoyu on 2018/4/9.
 */

public class BizSewerageRoomClick implements ISewerageRoomClickBiz {
    private static BizSewerageRoomClick testMoudle;

    public synchronized static BizSewerageRoomClick getInstanse() {
        if (testMoudle == null)
            testMoudle = new BizSewerageRoomClick();
        return testMoudle;
    }

    @Override
    public void getdata(final SewerageRoomClickListener sewerageRoomClickListener, final String id, final int page, final int count, final int refresh_item, final int refresh_list_type) {
//        ReHttpUtils.instans().httpRequest(new HttpSubCribe<SewerageRoomClickItemBean>() {
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onNext(SewerageRoomClickItemBean sewerageRoomClickItemBean) {
//               sewerageRoomClickListener.onSuccess(sewerageRoomClickItemBean);
//            }
//
//            @Override
//            public Observable<SewerageRoomClickItemBean> getObservable(MyRetroService retrofit) {
//                return retrofit.getSewerageRoomClickItem(id,page,count,refresh_item,refresh_list_type);
//            }
//        });
    }
}
