package com.augurit.agmobile.patrolcore.layerdownload.presenter;

import android.content.Context;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.layerdownload.LayerDownloadAreaSelectView;
import com.augurit.agmobile.mapengine.area.service.AreaServiceImpl;
import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.agmobile.mapengine.layerdownload.view.ILayerDownloadAreaSelectView;
import com.augurit.agmobile.mapengine.layerdownload.view.presenter.ILayerDownloadAreaSelectPresenter;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：图层下载默认区域选择Presenter
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload.presenter
 * @createTime 创建时间 ：2016-12-27
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-12-27
 * @modifyMemo 修改备注：
 */
public class LayerDownloadAreaSelectPresenter implements ILayerDownloadAreaSelectPresenter{

    private Context mContext;
    private ViewGroup mContainer;
    private ILayerDownloadAreaSelectView mAreaSelectView;
    private AreaServiceImpl mAreaService;
    private Callback1 backListener;
    private UserArea mUserArea = null;
    private List<Area> mDownloadArea;

    public LayerDownloadAreaSelectPresenter(Context context){
        this.mContext = context;
        init();
    }

    private void init(){
        mDownloadArea = new ArrayList<>();
        mAreaService = new AreaServiceImpl(mContext);
        mAreaSelectView = new LayerDownloadAreaSelectView(mContext, this);
    }

    @Override
    public void goBack(){
        if(backListener != null){
            backListener.onCallback(null);
        }
    }

    @Override
    public void backToParentArea(){
        List<Area> areaList = mUserArea.getSub().get("" + mAreaService.getDiscodeId());
        mAreaSelectView.setAreaList(areaList, true ,"");
    }

    @Override
    public void onAreaClick(Area area){
        List<Area> areaList = mUserArea.getSub().get("" + area.getId());
        mAreaSelectView.setAreaList(areaList, false, area.getDisname());
    }

    @Override
    public void onDownloadClick(Area area){
//        mDownloadArea.add(area);
        mAreaService.getAreaLocate(area.getId(), area.getDiscodeLocate().getId(), new Callback2<AreaLocate>() {
            @Override
            public void onSuccess(AreaLocate areaLocate) {
                if (StringUtil.isEmpty(areaLocate.getWkt())
                        || "null".equals(areaLocate.getWkt())) {
                    ToastUtil.shortToast(mContext, "获取区域信息失败，请重试！");
                    return;
                }
                Geometry geometry = WktUtil.getPolygonGeometryFromWKT(areaLocate.getWkt(), GeographyInfoManager.getInstance().getSpatialReference().getID());
                LayerDownloadPresenter.geometrys.add(geometry);
                ToastUtil.shortToast(mContext, "成功加入到下载队列");
            }

            @Override
            public void onFail(Exception error) {

            }
        });
    }

    @Override
    public void setBackListener(Callback1 backListener){
        this.backListener = backListener;
    }

    @Override
    public void startAreaSelect(ViewGroup container){
        this.mContainer = container;
        mAreaSelectView.show(mContainer);
        mAreaService.getUserArea(new Callback2<UserArea>() {
            @Override
            public void onSuccess(UserArea userArea) {
                mUserArea = userArea;
                List<Area> parent = mUserArea.getSub().get("" + mAreaService.getDiscodeId());
                mAreaSelectView.setAreaList(parent, true, "");
                mAreaSelectView.showContent();
            }

            @Override
            public void onFail(Exception error) {

            }
        });
    }
}
