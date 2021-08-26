package com.augurit.agmobile.patrolcore.layerdownload.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layerdownload.view.ILayerDownloadToolView;
import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerDownloadListener;
import com.augurit.agmobile.mapengine.layerdownload.view.presenter.ILayerDownloadPresenter;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.util.EncryptTileCacheHelper;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layer.service.PatrolLayerService;
import com.augurit.agmobile.patrolcore.layerdownload.LayerDownloadToolView;
import com.augurit.agmobile.patrolcore.layerdownload.LayerDownloadView;
import com.augurit.agmobile.patrolcore.layerdownload.OnDownloadListener;
import com.augurit.agmobile.patrolcore.layerdownload.activity.LayerDownloadAreaActivity;
import com.augurit.agmobile.patrolcore.layerdownload.activity.LayerDownloadAreaSelectActivity;
import com.augurit.agmobile.patrolcore.layerdownload.model.LayerDnlTask;
import com.augurit.agmobile.patrolcore.layerdownload.service.DownloadTableDBService;
import com.augurit.agmobile.patrolcore.layerdownload.service.LayerDownloadService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.actres.CallbackManagerImpl;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 描述：图层下载默认Prensenter
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload.presenter
 * @createTime 创建时间 ：2016-09-18
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：代码迁移修改
 */
public class LayerDownloadPresenter implements ILayerDownloadPresenter {

    public static List<Geometry> geometrys = new ArrayList<>();
    protected static int REQUEST_CODE = 1234;
    public OnDownloadListener mDownloadListener;
    protected LayerInfo clickLayerInfo;
    protected CallbackManagerImpl callbackManager;
    protected Context mContext;
    protected ILayerDownloadView mLayerDownloadView;
    protected ILayerDownloadToolView mLayerDownloadToolView;
    protected PopupWindow mAreaPopupWindow;
    protected LayerRouter mLayerRouter;
    protected ProjectDataManager mProjectDataManager;
    protected LayerDownloadService mLayerDownloadService;
    protected List<ProjectInfo> mProjectInfos;
    protected Map<String, List<LayerInfo>> mLayerInfosMap;
    protected ArrayList<LayerDnlTask> mLayerDnlTasks;
    protected Callback1 mBackListener;
    protected View max;
    protected View userarea;
    protected View custom;
    protected View cancel;
    protected View areaSelMenu;
    protected ILayersService mILayersService;//获取图层信息
    protected DownloadTableDBService mDownloadTableDBService;
    protected ViewGroup mgrViewContainer;

    public LayerDownloadPresenter(Context context) {
        this.mContext = context;
        this.mLayerDownloadView = new LayerDownloadView(context, this);
        this.mProjectDataManager = ProjectDataManager.getInstance();
        this.mLayerRouter = new LayerRouter();
        this.mLayerDownloadService = LayerDownloadService.getInstance(mContext);
        this.mLayerInfosMap = new HashMap<>();
        LayerServiceFactory.injectLayerService(new PatrolLayerService(context));
        mILayersService = LayerServiceFactory.provideLayerService(context);
        mDownloadTableDBService = new DownloadTableDBService(context);
        mDownloadListener = new OnDownloadListener() {
            @Override
            public void onUpdateTask(int taskId, double total, double downed, boolean done, Throwable e) {
                if (mLayerDownloadView != null) {
                    updateTask(taskId, total, downed, false, null);
                }
            }
        };
        this.mLayerDownloadService.setDownloadListener(mDownloadListener);
    }


    protected View getContentView() {
        return mLayerDownloadView.getDnlMgrView();
    }

    /**
     * 开启图层下载
     *
     * @param mapView           MapView
     * @param mgrViewContainer  管理视图容器
     * @param toolViewContainer 工具视图容器
     */
    @Override
    public void startLayerDownload(MapView mapView, ViewGroup mgrViewContainer, ViewGroup toolViewContainer) {
        if (toolViewContainer != null) {
            showToolView(mapView, toolViewContainer);
        }
        this.mgrViewContainer = mgrViewContainer;
        this.mgrViewContainer.removeAllViews();
        this.mgrViewContainer.addView(mLayerDownloadView.getDnlMgrView());
        mLayerDnlTasks = new ArrayList<>();
        mLayerDownloadView.initTaskList(mLayerDnlTasks);
        //        getProjectInfos();
        getLayerInfos();
    }

    protected void showToolView(MapView mapView, ViewGroup container) {
        this.mLayerDownloadToolView = new LayerDownloadToolView(mContext, mapView, container);
        this.mLayerDownloadToolView.show();
    }

    protected void getProjectInfos() {
        mProjectInfos = new ArrayList<>();
        ProjectInfo projectInfo =
                mProjectDataManager.getProjectDataById(mContext,
                        mProjectDataManager.getCurrentProjectId(mContext));
        mProjectInfos.add(projectInfo);
        mLayerDownloadView.setProjectName(projectInfo.getProjectName());
    }

    protected void getLayerInfos() {
        //        Observable.from(mProjectInfos)
        //                .map(new Func1<ProjectInfo, Void>() {
        //                    @Override
        //                    public Void call(ProjectInfo projectInfo) {
        //                        try {
        //                            List<LayerInfo> layerInfos = mLayerRouter.getLayerInfos(mContext,
        //                                    projectInfo.getProjectId(), BaseInfoManager.getUserId(mContext));
        //                            List<LayerInfo> actives = LayerUtils.getActiveInBsLayer(
        //                                    LayerUtils.getNormalLayer(LayerUtils.getArcgisServerLayer(layerInfos)));
        //                            mLayerInfosMap.put(projectInfo.getProjectId(), actives);
        //                        } catch (IOException e) {
        //                            e.printStackTrace();
        //                        }
        //                        return null;
        //                    }
        //                }).subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Subscriber<Void>() {
        //                    @Override
        //                    public void onCompleted() {
        //                        LogUtil.d("GetLayerInfos Completed.");
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //                    }
        //
        //                    @Override
        //                    public void onNext(Void aVoid) {
        //                        mLayerDownloadView.setLayerData(null, mLayerInfosMap);
        //                    }
        //                });

        mILayersService.getSortedLayerInfos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("加载图层失败，原因是：" + e.getMessage());
                        //                        mLayerDownloadView.hideLoadingMap();
                        //                        mILayerView.showLoadLayerFailMessage(new Exception(e));
                    }

                    @Override
                    public void onNext(List<LayerInfo> amLayerInfos) {
                        mLayerInfosMap.put("abc", amLayerInfos);
                        mLayerDownloadView.setLayerData(null, mLayerInfosMap);
                    }
                });
    }

    /**
     * 下载按钮点击
     *
     * @param layerInfo LayerInfo
     */
    @Override
    public void onDownloadBtnClick(final LayerInfo layerInfo) {
        if (mAreaPopupWindow != null
                && mAreaPopupWindow.isShowing()) {
            return;
        }
        this.clickLayerInfo = layerInfo;
        areaSelMenu = LayoutInflater.from(mContext).inflate(R.layout.dnl_pop_area, null);
        mAreaPopupWindow = new PopupWindow(areaSelMenu, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAreaPopupWindow.setTouchable(true);
        mAreaPopupWindow.setOutsideTouchable(true);
        mAreaPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 全国
        max = areaSelMenu.findViewById(R.id.dnl_pop_area_max);
        //行政
        userarea = areaSelMenu.findViewById(R.id.dnl_pop_area_userarea);
        //自定义范围
        custom = areaSelMenu.findViewById(R.id.dnl_pop_area_custom);
        //取消
        cancel = areaSelMenu.findViewById(R.id.dnl_pop_area_cancel);
        if (LayerType.FeatureLayer == layerInfo.getType()) {
            userarea.setVisibility(View.GONE);
            custom.setVisibility(View.GONE);
        }
        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                Geometry geometry = GeographyInfoManager.getInstance().getMaxExtent();
                geometrys.clear();
                geometrys.add(geometry);
                startDownload();
            }
        });
        userarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                geometrys.clear();
                Intent intent = new Intent(mContext, LayerDownloadAreaSelectActivity.class);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);

            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                Intent intent = new Intent(mContext, LayerDownloadAreaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("layerinfo", layerInfo);
                intent.putExtras(bundle);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
            }
        });
        mAreaPopupWindow.showAtLocation(mLayerDownloadView.getDnlMgrView(), Gravity.BOTTOM, 0, 0);
    }

    protected void startDownload() {
        if (ListUtil.isEmpty(geometrys)) {
            return;
        }
        LayerInfo layerInfo = this.clickLayerInfo;
        for (Geometry currentGeometry : geometrys) {
            if (layerInfo.getType() == LayerType.FeatureLayer) {
                currentGeometry = GeographyInfoManager.getInstance().getMaxExtent();
            } else if (layerInfo.getType() == LayerType.TileLayer
                    && currentGeometry == null) {
                ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_geometry_is_null));
                return;
            } else if (layerInfo.getType() == LayerType.TianDiTu
                    && currentGeometry == null) {
                ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_geometry_is_null));
                return;
            }
            Envelope envelope = new Envelope();
            currentGeometry.queryEnvelope(envelope);
            donwloadLayerData(layerInfo, envelope, GeographyInfoManager.getInstance().getSpatialReference());
        }
        geometrys.clear();
    }

    /*public void onDownloadBtnClick(LayerInfo layerInfo){
        if(mLayerDownloadToolView==null){

            return;
        }
        Geometry currentGeometry = mLayerDownloadToolView.getCurrentGeometry();
        if(layerInfo.getType()==LayerType.FeatureLayer){
            currentGeometry = GeographyInfoManager.getInstance().getMaxExtent();
        } else if (layerInfo.getType()==LayerType.TileLayer
                &&currentGeometry == null) {
            ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_geometry_is_null));
            return;
        }
        Envelope envelope = new Envelope();
        currentGeometry.queryEnvelope(envelope);
        donwloadLayerData(layerInfo, envelope, GeographyInfoManager.getInstance().getSpatialReference());
    }*/

    protected void donwloadLayerData(LayerInfo layerInfo, Envelope envelope, SpatialReference spatialReference) {
        ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_start_download));
        LayerType layerType = layerInfo.getType();
        if (layerType == LayerType.TileLayer) {
            downloadTileLayer(layerInfo, envelope, spatialReference);
        } else if (layerType == LayerType.FeatureLayer) {
            downloadFeatureLayer(layerInfo, envelope, spatialReference);
        } else if (layerType == LayerType.TianDiTu) {
            downloadTdtLayer(layerInfo, envelope, spatialReference);
        }
    }

    protected void downloadTileLayer(LayerInfo layerInfo, Envelope envelope, SpatialReference spatialReference) {
        final LayerDnlTask layerDnlTask = new LayerDnlTask();
        java.util.Random r = new java.util.Random();
        final int taskId = r.nextInt(10000);
        layerDnlTask.setId(taskId);
        layerDnlTask.setLayerName(layerInfo.getLayerName() + taskId);
        layerDnlTask.setType(0);
        layerDnlTask.setTpk(false);
        layerDnlTask.setProjectId(1565);
        layerDnlTask.setLayerId(layerInfo.getLayerId());
        //        layerDnlTask.setEnvelope(envelope);
        layerDnlTask.setEnvelopeJson(envelope, spatialReference);
        layerDnlTask.setServiceURL(layerInfo.getUrl());
        layerDnlTask.setDone(false);
        layerDnlTask.setTotal(100);
        layerDnlTask.setDowned(0);
        mLayerDownloadView.addTaskList(layerDnlTask);
        mDownloadTableDBService.setTableItemToDB(layerDnlTask);
        ITileCacheHelper cacheHelper = new EncryptTileCacheHelper(mContext, String.valueOf(layerInfo.getLayerId()));
        //        IOffLineFileHelper fileHelper = new AMFileHelper2(mContext);
        mLayerDownloadService.downloadTiles(layerInfo.getUrl(), envelope, cacheHelper, new OnLayerDownloadListener() {
            @Override
            public void onSuccess(Object o) {
                layerDnlTask.setDowned(100);
                layerDnlTask.setTotal(100);
                mDownloadTableDBService.setTableItemToDB(layerDnlTask);
                if (mLayerDownloadService.getDownloadHandler() != null) {
                    Message msg = mLayerDownloadService.getDownloadHandler().obtainMessage();
                    msg.getData().putInt("taskid", taskId);
                    msg.getData().putDouble("total", 100);
                    msg.getData().putDouble("downed", 100);
                    mLayerDownloadService.getDownloadHandler().sendMessageDelayed(msg, 100);
                }
                ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_download_success));
            }

            @Override
            public void onDownloading(double total, double downed) {
                if (mLayerDownloadService.getDownloadHandler() != null) {
                    Message msg = mLayerDownloadService.getDownloadHandler().obtainMessage();
                    msg.getData().putInt("taskid", taskId);
                    msg.getData().putDouble("total", total);
                    msg.getData().putDouble("downed", downed);
                    mLayerDownloadService.getDownloadHandler().sendMessageDelayed(msg, 100);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    /**
     * 天地图下载
     *
     * @param layerInfo
     * @param envelope
     * @param spatialReference
     */
    protected void downloadTdtLayer(LayerInfo layerInfo, Envelope envelope, SpatialReference spatialReference) {
        final LayerDnlTask layerDnlTask = new LayerDnlTask();
        java.util.Random r = new java.util.Random();
        final int taskId = r.nextInt();
        layerDnlTask.setId(taskId);
        layerDnlTask.setLayerName(layerInfo.getLayerName() + taskId);
        layerDnlTask.setType(0);
        layerDnlTask.setTpk(false);
        layerDnlTask.setProjectId(1565);
        layerDnlTask.setLayerId(layerInfo.getLayerId());
        layerDnlTask.setEnvelopeJson(envelope, spatialReference);
        layerDnlTask.setServiceURL(layerInfo.getUrl());
        layerDnlTask.setDone(false);
        layerDnlTask.setTotal(100);
        layerDnlTask.setDowned(0);
        mLayerDownloadView.addTaskList(layerDnlTask);
        mDownloadTableDBService.setTableItemToDB(layerDnlTask);
        //        ITileCachePathHelper pathHelper = new CustomTileCachePath(mContext, String.valueOf(layerInfo.getLayerId()));
        ITileCacheHelper fileHelper = new EncryptTileCacheHelper(mContext,String.valueOf(layerInfo.getLayerId()));
        mLayerDownloadService.downloadTdts(layerInfo.getUrl(),String.valueOf(layerInfo.getLayerId()), envelope, null,                 new OnLayerDownloadListener() {
                    @Override
                    public void onSuccess(Object o) {
                        layerDnlTask.setDowned(100);
                        layerDnlTask.setTotal(100);
                        mDownloadTableDBService.setTableItemToDB(layerDnlTask);
                        if (mLayerDownloadService.getDownloadHandler() != null) {
                            Message msg = mLayerDownloadService.getDownloadHandler().obtainMessage();
                            msg.getData().putInt("taskid", taskId);
                            msg.getData().putDouble("total", 100.0);
                            msg.getData().putDouble("downed", 100.0);
                            mLayerDownloadService.getDownloadHandler().sendMessageDelayed(msg, 100);
                        }
                        ToastUtil.shortToast(mContext, mContext.getString(R.string.dnl_string_download_success));
                    }

                    @Override
                    public void onDownloading(double total, double downed) {
                        if (mLayerDownloadService.getDownloadHandler() != null) {
                            Message msg = mLayerDownloadService.getDownloadHandler().obtainMessage();
                            msg.getData().putInt("taskid", taskId);
                            msg.getData().putDouble("total", total);
                            msg.getData().putDouble("downed", downed);
                            mLayerDownloadService.getDownloadHandler().sendMessageDelayed(msg, 100);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    protected void downloadFeatureLayer(LayerInfo layerInfo, Envelope envelope, SpatialReference spatialReference) {
        LayerDnlTask layerDnlTask = new LayerDnlTask();
        java.util.Random r = new java.util.Random();
        final int taskId = r.nextInt();
        layerDnlTask.setId(taskId);
        layerDnlTask.setLayerName(layerInfo.getLayerName());
        layerDnlTask.setType(1);
        layerDnlTask.setTpk(false);
        layerDnlTask.setProjectId(Integer.valueOf(ProjectDataManager.getInstance().getCurrentProjectId(mContext)));
        layerDnlTask.setLayerId(layerInfo.getLayerId());
        layerDnlTask.setServiceURL(layerInfo.getUrl());
        layerDnlTask.setDone(false);
        layerDnlTask.setTotal(100);
        layerDnlTask.setDowned(0);
        FilePathUtil pathUtil = new FilePathUtil(mContext);
        mLayerDownloadService.downloadFeatures(layerInfo.getUrl(),
                pathUtil.getSavePath(),
                layerInfo.getLayerId(),
                envelope, spatialReference, new OnLayerDownloadListener() {
                    @Override
                    public void onSuccess(Object o) {
                        updateTask(taskId, 100, 100, true, null);
                        LogUtil.i("LayerDownloadPresenter featureLayerDownloader success");
                    }

                    @Override
                    public void onDownloading(double total, double downed) {
                        updateTask(taskId, total, downed, false, null);
                        LogUtil.i("featureLayerDownloader total:" + total + " downed:" + downed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtil.i("LayerDownloadPresenter featureLayerDownloader" + e.getMessage());
                    }
                });
    }

    protected void updateTask(int taskId, double total, double downed, boolean done, Throwable e) {
        mLayerDownloadView.updateTask(taskId, total, downed, done, e);
    }

    @Override
    public void setCallbackManagerImpl(CallbackManagerImpl callbackManager) {
        this.callbackManager = callbackManager;
        try {
            callbackManager.registerCallback(REQUEST_CODE, new CallbackManagerImpl.Callback() {
                @Override
                public boolean onActivityResult(int resultCode, Intent data) {
                    startDownload();
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void back() {
        if (mBackListener != null) {
            mBackListener.onCallback(null);
        }
        //        this.mLayerDownloadView = null;
        this.mgrViewContainer.removeAllViews();
    }

    @Override
    public void setBackListener(Callback1 callback) {
        this.mBackListener = callback;
    }

    @Override
    public void onClose() {
        if (this.mLayerDownloadToolView != null) {
            this.mLayerDownloadToolView.dismiss();
        }
    }
}
