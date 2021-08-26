package com.augurit.agmobile.mapengine.layermanage.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.LayerUtils;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.cmpt.widget.treeview.view.AndroidTreeView;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.jakewharton.rxbinding.view.RxView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 图层列表视图
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.defaultview.layermanage.view
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18
 */

public class LayerView extends BaseView<ILayerPresenter> implements ILayerView {

    private static final String TAG = "图层模块";
    protected MapView mMapView;

    //加载中view
    protected ProgressLinearLayout pb_loading;
    //全选，半选，不选
    protected ViewGroup ll_selectall;
    protected ViewGroup ll_unSelectall;
    protected ViewGroup ll_halfcheck;
    //树形列表容器
    protected ViewGroup treeview_container;

    //整个列表视图容器
    protected ViewGroup container;
    private View root;

    //树形列表相关
    protected boolean mIfInitialized;//是否初始化完树形列表
    protected int mSelectedGroupSize; //全选的组的个数
    protected ProgressDialog mProgressDialog;


    public LayerView(Context context, MapView mapView, ViewGroup container) {
        super(context);
        this.mMapView = mapView;
        this.container = container;
        initView();
    }

    @Override
    public void initView() {
        root = View.inflate(mContext, R.layout.layer_view2, null);
        pb_loading = (ProgressLinearLayout) root.findViewById(R.id.pb_loading);
        treeview_container = (ViewGroup) root.findViewById(R.id.treeview_container);
        ll_selectall = (ViewGroup) root.findViewById(R.id.ll_iv_selectall);
        ll_unSelectall = (ViewGroup) root.findViewById(R.id.ll_iv_unselectall);
        ll_halfcheck = (ViewGroup) root.findViewById(R.id.ll_half_check);
    }

    @Override
    public void showLoadingMap() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在加载地图中");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoadingMap() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void showLoadingView() {
        if (pb_loading != null) {
            pb_loading.showLoading();
        }
    }

    @Override
    public void hideLoadingView() {
        if (pb_loading != null) {
            pb_loading.showContent();
        }
    }
    @Override
    public void showLoadLayerFailMessage(Exception e) {
        if (pb_loading != null) {
            if (e instanceof SocketTimeoutException) {
                pb_loading.showError("", "服务器连接超时，无法获取图层信息", "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPresenter != null) {
                            mPresenter.showLayerList();
                        }
                    }
                });
            } else {
                pb_loading.showError("", "无法获取图层信息", "重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPresenter != null) {
                            mPresenter.showLayerList();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void addLayerToMapView(Layer layer) {
        if (mMapView != null){
            mMapView.addLayer(layer);
        }else {
            LogUtil.e(TAG,"mapview为空");
        }
    }

    @Override
    public View createLayerView(List<LayerInfo> layerInfoList) {
      // 是否初始化完成，如果没有初始化完成，不响应点击事件
        mIfInitialized = false;

        LinkedHashMap<String, List<LayerInfo>> groups = LayerUtils.getGroups(layerInfoList);
        final List<String> groupNameList = LayerUtils.getGroupNameList(groups);

        TreeNode root = TreeNode.root();

        final List<ParentViewHolder> parentViewHolders = new ArrayList<>();
        //全选的组的个数
        mSelectedGroupSize = 0;
        //图层点击后的事件
        OnLayerItemClickListener onLayerItemClickListener = new OnLayerItemClickListener() {
            @Override
            public void onClickCheckbox(int layerId, String groupName,
                                        LayerInfo currentLayer, boolean ifShow) {

                if (mIfInitialized) {
                    //点击后改变图层的可见性
                    LayerInfo layerInfo = currentLayer;
                    layerInfo.setDefaultVisibility(ifShow);
                    if (parentViewHolders.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < groupNameList.size(); i++) {
                        if (groupNameList.get(i).equals(groupName)) {
                            parentViewHolders.get(i).notifyForMemberStateChange(layerId, ifShow);
                            break;
                        }
                    }

                    //更新总的checkbox
                    updateTotalCheckBox(parentViewHolders);

                    //调用presenter中的点击事件，也就是去真正改变图层的可见度
                    mPresenter.onClickCheckbox(groupName, layerId, currentLayer, ifShow);
                }

            }

            @Override
            public void onOpacityChange(int layerId, String groupName, LayerInfo currentLayer, float opacity) {
                LayerInfo layerInfo = currentLayer;
                layerInfo.setOpacity(opacity);
                mPresenter.onClickOpacityButton(layerId, currentLayer, opacity);
            }
        };


        for (int i = 0; i < groupNameList.size(); i++) {
            int level = 0;
            String layerTitle = groupNameList.get(i);
            List<LayerInfo> infoList = groups.get(layerTitle);
            int size = infoList.size();
            ParentViewHolder parentViewHolder = getParentHolder(infoList,level);
            TreeNode parent = new TreeNode(layerTitle).setViewHolder(parentViewHolder);

            for (int j = 0; j < size; j++) {
                LayerInfo childLayerInfo = infoList.get(j);
                TreeNode child = null;
                if (!ListUtil.isEmpty(childLayerInfo.getChildLayer())){
                    level ++;
                    child = new TreeNode(childLayerInfo.getLayerName()).setViewHolder(getParentHolder(childLayerInfo.getChildLayer(),level));
                    List<LayerInfo> grandChildLayerInfos = childLayerInfo.getChildLayer();
                    for (LayerInfo grandChild : grandChildLayerInfos){
                        TreeNode grandChildNode = new TreeNode(grandChild).setViewHolder(getChildHolder(onLayerItemClickListener, childLayerInfo.getLayerName()));
                        grandChildNode.getViewHolder().getView();
                        child.addChild(grandChildNode);
                    }
                }else {
                    child = new TreeNode(childLayerInfo).setViewHolder(getChildHolder(onLayerItemClickListener, layerTitle));
                }
                child.getViewHolder().getView();
                parent.addChild(child);
            }

            parentViewHolders.add(i, parentViewHolder);
            root.addChild(parent);
        }

        final AndroidTreeView treeView = new AndroidTreeView(mContext, root);
        treeView.setDefaultAnimation(true);
        treeView.setSelectionModeEnabled(true);
        treeView.setDefaultContainerStyle(R.style.AGMobile_Widget_TreeView_TreeNode);
//        treeview_container.addView(treeView.getView(), LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);

        RxView.clicks(ll_halfcheck)
                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        treeView.selectAll(true);
                        ll_selectall.setVisibility(View.VISIBLE);
                        ll_halfcheck.setVisibility(View.GONE);
                        ll_unSelectall.setVisibility(View.GONE);
                    }
                });

        RxView.clicks(ll_selectall)
                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        treeView.deselectAll();
                        // ll_unSelectall.performClick();
                        ll_unSelectall.setVisibility(View.VISIBLE);
                        ll_selectall.setVisibility(View.GONE);
                        ll_halfcheck.setVisibility(View.GONE);
                        // mPresenter.onAllUnSelectedButtonClick();
                    }
                });
        RxView.clicks(ll_unSelectall)
                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        treeView.selectAll(true);
                        //  ll_selectall.performClick();
                        ll_selectall.setVisibility(View.VISIBLE);
                        ll_unSelectall.setVisibility(View.GONE);
                        ll_halfcheck.setVisibility(View.GONE);
                        // mPresenter.onAllSelectedButtonClicked();
                    }
                });
        mIfInitialized = true;
        return treeView.getView();
    }

    @Override
    public void addLayerViewToTreeViewContainer(View view) {
        treeview_container.removeAllViews();
        treeview_container.addView(view);
    }

    @Override
    public void removeAllLayers() {
        if (mMapView != null){
            mMapView.removeAll();
        }
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void addToContainer() {
        if (container != null){
            container.removeAllViews();
            container.addView(root);
        }
    }

    @Override
    public void toggle(LayerInfo layerInfo, boolean checked) {

    }

    @Override
    public void setContainer(ViewGroup viewGroup) {
        this.container = viewGroup;
    }

    //    @Override
//    public void showLoadingMap() {
//
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("正在加载地图中");
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//    }
//
//    @Override
//    public void hideLoadingMap() {
//
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
//    }
//
//    @Override
//    public void showLoadingView() {
//
//        if (mMostOuterContainer == null) {
//            return;
//        }
//
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //正在加载中的视图
//        final View allView = getRootView(inflater);
//
//        pb_loading = allView.findViewById(R.id.pb_layer_loading);
//        tv_loading = allView.findViewById(R.id.tv_layer_loading);
//
//        ll_selectall = (ViewGroup) allView.findViewById(R.id.ll_iv_selectall);
//        ll_unSelectall = (ViewGroup) allView.findViewById(R.id.ll_iv_unselectall);
//        ll_halfcheck = (ViewGroup) allView.findViewById(R.id.ll_half_check);
//        treeview_container = (LinearLayout) allView.findViewById(R.id.treeview_container);
//
//        errorView = allView.findViewById(R.id.ll_common_loadingerror);
//
//
//        mMostOuterContainer.setVisibility(View.VISIBLE);
//        mMostOuterContainer.removeAllViews();
//        mMostOuterContainer.addView(allView);
//    }
//
//    protected View getRootView(LayoutInflater inflater) {
//        return inflater.inflate(R.layout.layer_layerlist_view, null);
//    }
//
//    @Override
//    public void hideLoadingView() {
//        pb_loading.setVisibility(View.GONE);
//        tv_loading.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void showLoadLayerFailMessage(Exception e) {
//
//        if (e instanceof SocketTimeoutException) {
//            ToastUtil.shortToast(mContext.getApplicationContext(), "服务器连接超时，无法获取图层信息");
//        } else {
//            ToastUtil.shortToast(mContext.getApplicationContext(), "加载图层信息失败");
//        }
//
//        if (errorView != null) {
//            errorView.setVisibility(View.VISIBLE);
//
//            treeview_container.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    public void initLayerListView(String projectName, final List<String> groupNameList, java.util.Map<String, List<LayerInfo>> groups) {
//
//
//        //是否初始化完成，如果没有初始化完成，不响应点击事件
//        mIfInitialized = false;
//        TreeNode root = TreeNode.root();
//
//        final List<ParentViewHolder> parentViewHolders = new ArrayList<>();
//        //全选的组的个数
//        mSelectedGroupSize = 0;
//        //图层点击后的事件
//        OnLayerItemClickListener onLayerItemClickListener = new OnLayerItemClickListener() {
//            @Override
//            public void onClickCheckbox(int layerId, String groupName,
//                                        LayerInfo currentLayer, boolean ifShow) {
//
//                if (mIfInitialized) {
//                    //点击后改变图层的可见性
//                    LayerInfo layerInfo = currentLayer;
//                    layerInfo.setDefaultVisibility(ifShow);
//                    if (parentViewHolders.size() == 0) {
//                        return;
//                    }
//                    for (int i = 0; i < groupNameList.size(); i++) {
//                        if (groupNameList.get(i).equals(groupName)) {
//                            parentViewHolders.get(i).notifyForMemberStateChange(layerId, ifShow);
//                            break;
//                        }
//                    }
//
//                    //更新总的checkbox
//                    updateTotalCheckBox(parentViewHolders);
//
//                    //调用presenter中的点击事件，也就是去真正改变图层的可见度
//                    mPresenter.onClickCheckbox(groupName, layerId, currentLayer, ifShow);
//                }
//
//            }
//
//            @Override
//            public void onOpacityChange(int layerId, String groupName, LayerInfo currentLayer, float opacity) {
//                LayerInfo layerInfo = currentLayer;
//                layerInfo.setOpacity(opacity);
//                mPresenter.onClickOpacityButton(layerId, currentLayer, opacity);
//            }
//        };
//
//
//        for (int i = 0; i < groupNameList.size(); i++) {
//            int level = 0;
//            String layerTitle = groupNameList.get(i);
//            List<LayerInfo> infoList = groups.get(layerTitle);
//            int size = infoList.size();
//            ParentViewHolder parentViewHolder = getParentHolder(infoList,level);
//            TreeNode parent = new TreeNode(layerTitle).setViewHolder(parentViewHolder);
//
//            for (int j = 0; j < size; j++) {
//                LayerInfo childLayerInfo = infoList.get(j);
//                TreeNode child = null;
//                if (!ListUtil.isEmpty(childLayerInfo.getChildLayer())){
//                    level ++;
//                    child = new TreeNode(childLayerInfo.getLayerName()).setViewHolder(getParentHolder(childLayerInfo.getChildLayer(),level));
//                    List<LayerInfo> grandChildLayerInfos = childLayerInfo.getChildLayer();
//                    for (LayerInfo grandChild : grandChildLayerInfos){
//                        TreeNode grandChildNode = new TreeNode(grandChild).setViewHolder(getChildHolder(onLayerItemClickListener, childLayerInfo.getLayerName()));
//                        grandChildNode.getViewHolder().getView();
//                        child.addChild(grandChildNode);
//                    }
//                }else {
//                    child = new TreeNode(childLayerInfo).setViewHolder(getChildHolder(onLayerItemClickListener, layerTitle));
//                }
//                child.getViewHolder().getView();
//                parent.addChild(child);
//            }
//
//            parentViewHolders.add(i, parentViewHolder);
//            root.addChild(parent);
//        }
//
//        final AndroidTreeView treeView = new AndroidTreeView(mContext, root);
//        treeView.setDefaultAnimation(true);
//        treeView.setSelectionModeEnabled(true);
//        treeView.setDefaultContainerStyle(R.style.AGMobile_Widget_TreeView_TreeNode);
//        treeview_container.addView(treeView.getView(), LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        RxView.clicks(ll_halfcheck)
//                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        treeView.selectAll(true);
//                        ll_selectall.setVisibility(View.VISIBLE);
//                        ll_halfcheck.setVisibility(View.GONE);
//                        ll_unSelectall.setVisibility(View.GONE);
//                    }
//                });
//
//        RxView.clicks(ll_selectall)
//                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        treeView.deselectAll();
//                        // ll_unSelectall.performClick();
//                        ll_unSelectall.setVisibility(View.VISIBLE);
//                        ll_selectall.setVisibility(View.GONE);
//                        ll_halfcheck.setVisibility(View.GONE);
//                        // mPresenter.onAllUnSelectedButtonClick();
//                    }
//                });
//        RxView.clicks(ll_unSelectall)
//                .throttleFirst(1, TimeUnit.SECONDS)   //1秒钟之内只取一个点击事件，防抖操作
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        treeView.selectAll(true);
//                        //  ll_selectall.performClick();
//                        ll_selectall.setVisibility(View.VISIBLE);
//                        ll_unSelectall.setVisibility(View.GONE);
//                        ll_halfcheck.setVisibility(View.GONE);
//                        // mPresenter.onAllSelectedButtonClicked();
//                    }
//                });
//        mIfInitialized = true;
//    }
//
    @NonNull
    protected ChildViewHolder getChildHolder(OnLayerItemClickListener onLayerItemClickListener, String layerTitle) {
        ChildViewHolder childViewHolder = null;
        if (mPresenter != null && mPresenter.getService() != null){
            LinkedHashMap<Integer, Layer> allLayer = mPresenter.getService().getAllLayer();
            childViewHolder = new ChildViewHolder(mContext, layerTitle, allLayer,onLayerItemClickListener);
        }else{
            childViewHolder = new ChildViewHolder(mContext, layerTitle,null, onLayerItemClickListener);
        }
        return childViewHolder;
    }
//
    @NonNull
    protected ParentViewHolder getParentHolder(List<LayerInfo> infoList,int level) {
        return new ParentViewHolder(mContext, infoList,level);
    }
//
//
    protected void updateTotalCheckBox(List<ParentViewHolder> parentViewHolders) {
        mSelectedGroupSize = 0;
        for (ParentViewHolder parentViewHolder : parentViewHolders) {
            if (parentViewHolder.isIfAllSelected()) {
                mSelectedGroupSize++;
            }
        }
        if (mSelectedGroupSize == parentViewHolders.size()) {
            ll_selectall.setVisibility(View.VISIBLE);
            ll_unSelectall.setVisibility(View.GONE);
            ll_halfcheck.setVisibility(View.GONE);
        } else if (mSelectedGroupSize == 0) {
            ll_selectall.setVisibility(View.GONE);
            ll_unSelectall.setVisibility(View.VISIBLE);
            ll_halfcheck.setVisibility(View.GONE);
        } else {
            ll_selectall.setVisibility(View.GONE);
            ll_unSelectall.setVisibility(View.GONE);
            ll_halfcheck.setVisibility(View.VISIBLE);
        }
    }
//
//    @Override
//    public void addLayerListViewToContainer() {
//
//    }
//
//    @Override
//    public void showLayerListView() {
//
//        errorView.setVisibility(View.GONE);
//
//        treeview_container.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideLayerListView() {
//        treeview_container.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void addLayerToMapView(Layer layer) {
//        if (ifMapviewUsable()) {
//            mMapView.addLayer(layer);
//        }
//    }
//
//    @Override
//    public void moveToMapCenter(Point point, double resolution) {
//        if (ifMapviewUsable()) {
//            mMapView.centerAt(point, true);
//            mMapView.setResolution(resolution);
//        }
//
//    }
//
//    @Override
//    public void removeAllLayers() {
//        if (ifMapviewUsable()) {
//            mMapView.removeAll();
//        }
//    }
//
//    @Override
//    public void setMapOnTouchListener(MapOnTouchListener mapOnTouchListener) {
//        if (mapOnTouchListener != null) {
//            mMapView.setOnTouchListener(mapOnTouchListener);
//        }
//    }
//
//    @Override
//    public void restoreMapOnTouchListener() {
//        if (mMapView != null) {
//            mMapView.setOnTouchListener(new MapOnTouchListener(mContext, mMapView));
//        }
//    }
//
//    @Override
//    public MapView getMapView() {
//        return mMapView;
//    }
//
//    public boolean ifMapviewUsable() {
//        if (mMapView == null) {
//            LogUtil.e("MapView为空，请调用LayerView.setMapView方法");
//            return false;
//        }
//        return true;
//    }

}
