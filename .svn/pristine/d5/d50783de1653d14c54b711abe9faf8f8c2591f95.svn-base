package com.augurit.agmobile.patrolcore.layer.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.LayerUtils;
import com.augurit.agmobile.mapengine.layermanage.view.ChildViewHolder;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.layermanage.view.ParentViewHolder;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.cmpt.widget.treeview.view.AndroidTreeView;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 仿造百度地图的图层显示界面   管井复核-图层分离带括号的line 291
 * Created by xcl on 2017/10/19.
 */
public class PatrolLayerView3 extends BaseView<ILayerPresenter> implements ILayerView {
    private static final String TAG = "图层模块";

    private View root;
    private ProgressLinearLayout pb_loading;
    private ViewGroup treeview_container;
    protected ProgressDialog mProgressDialog;
    private View btn_global;
    private ViewGroup ll_bottom;
    protected MapView mapView;
    private View view;
    private boolean ifInitialized;
    /**
     * 全选，半选，不选
     */
    protected ViewGroup ll_selectall;
    protected ViewGroup ll_unSelectall;
    protected ViewGroup ll_halfcheck;
    /**
     * 全选的组的个数
     */
    protected int mSelectedGroupSize;

    /**
     * 底图集合
     */
    private Map<String, List<LayerInfo>> baseMapList = new HashMap<>();

    /**
     * key是底图名称，value是对应的view，主要用来当点击一个底图时，让其他底图控件清除高亮显示
     */
    private Map<String, PatrolParentViewHolder2> parentViewHolder2Map = new HashMap<>();

    /**
     * 缓存的子图层view的集合
     */
    private Map<String, PatrolChildViewHolder2> childViewHolder2Map = new HashMap<>();


    protected ViewGroup container;
    private List<LayerInfo> tempLayers = new ArrayList<>();

    public List<LayerInfo> getTempLayers() {
        return tempLayers;
    }

    public PatrolLayerView3(Context context, MapView mapView, ViewGroup container) {
        super(context);
        this.mapView = mapView;
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
        btn_global = root.findViewById(R.id.btn_global);
        btn_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setExtent(mapView.getMaxExtent());
                btn_global.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO lsh 暂时写死，如果在Listener中需要这三个参数，不能这么写
                        if (mapView.getOnZoomListener() != null) {
                            mapView.getOnZoomListener().postAction(0, 0, 0);
                        }
                    }
                }, 200);

            }
        });
        ll_bottom = (ViewGroup) root.findViewById(R.id.ll_bottom);
    }

    public ViewGroup getBottomLayout() {
        return ll_bottom;
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
        if (mapView != null) {
            mapView.addLayer(layer);
        } else {
            LogUtil.e(TAG, "mapview为空");
        }
    }


    /**
     * 图层列表中单个图层的点击事件
     */
    private OnLayerItemClickListener onLayerItemClickListener = new OnLayerItemClickListener() {
        @Override
        public void onClickCheckbox(int layerId, String groupName,
                                    LayerInfo currentLayer, boolean ifShow) {

            if (ifInitialized) {
                //调用presenter中的点击事件，也就是去真正改变图层的可见度
                mPresenter.onClickCheckbox(groupName, layerId, currentLayer, ifShow);
//                if(groupName.indexOf("中心城区")!=-1) {
//                    for (LayerInfo layerInfo : tempLayers) {
//                        if (groupName.equals("排水管井(中心城区)") && layerInfo.getLayerName().contains("排水管井(黄埔管井)")) {
//                            mPresenter.onClickCheckbox(layerInfo.getLayerName(), layerInfo.getLayerId(), layerInfo, ifShow);
//                        } else if (groupName.equals("排水管道(中心城区)") && layerInfo.getLayerName().contains("排水管道(黄埔管道)")) {
//                            mPresenter.onClickCheckbox(layerInfo.getLayerName(), layerInfo.getLayerId(), layerInfo, ifShow);
//                        }
//                    }
//                }
//                for(LayerInfo layerInfo:tempLayers){
//                    if(layerInfo.getLayerName().contains(groupName)){
//                        mPresenter.onClickCheckbox(layerInfo.getLayerName(), layerInfo.getLayerId(), layerInfo, ifShow);
//                        break;
//                    }
//                }
            }
        }

        @Override
        public void onOpacityChange(int layerId, String groupName, LayerInfo currentLayer, float opacity) {
            LayerInfo layerInfo = currentLayer;
            layerInfo.setOpacity(opacity);
            mPresenter.onClickOpacityButton(layerId, currentLayer, opacity);
            for (LayerInfo layerInfo1 : tempLayers) {
                if (layerInfo1.getLayerName().contains(groupName)) {
                    layerInfo1.setOpacity(opacity);
                    mPresenter.onClickOpacityButton(layerInfo1.getLayerId(), layerInfo1, opacity);
                    break;
                }
            }
        }
    };

    /**
     * 图层列表中底图的点击事件
     */
    private PatrolParentViewHolder2.OnParentViewHolderClickListener onParentViewHolderClickListener =
            new PatrolParentViewHolder2.OnParentViewHolderClickListener() {
                @Override
                public void onClick(String groupName, List<LayerInfo> layerInfos) {
                    //显示选中的底图，隐藏其他底图
                    Set<Map.Entry<String, List<LayerInfo>>> entries = baseMapList.entrySet();
                    for (Map.Entry<String, List<LayerInfo>> entry : entries) {
                        if (entry.getKey().equals(groupName)) { //可见图层
                            ChangeLayerVisibility(groupName, layerInfos, true);
                        } else { //要隐藏的图层
                            List<LayerInfo> tobeHideLayers = entry.getValue();
                            ChangeLayerVisibility(groupName, tobeHideLayers, false);
                            clearBorder(entry.getKey());
                        }
                    }
                }
            };

    private void clearBorder(String groupName) {
        if (parentViewHolder2Map.get(groupName) != null) {
            parentViewHolder2Map.get(groupName).clearBorder();
        }
    }


    @Override
    public View createLayerView(List<LayerInfo> layerInfoList) {
        if (view != null) {
            return view;
        }

        //进行分组
        LinkedHashMap<String, List<LayerInfo>> groups = LayerUtils.getGroups(layerInfoList);
        //  List<String> groupNameList = LayerUtils.getGroupNameList(groups);

        //是否初始化完成，如果没有初始化完成，不响应点击事件
        ifInitialized = false;

        view = View.inflate(mContext, R.layout.view_patrol_layer_container, null);
        LinearLayout scrollItemContainer = (LinearLayout) view.findViewById(R.id.ll_scroll_item_container);
        LinearLayout normalLayerContainer = (LinearLayout) view.findViewById(R.id.ll_normal_layer_container);

        /**
         * 非底图的图层
         */
        List<LayerInfo> normalLayers = new ArrayList<>();

        // 1. 分离出底图和其他图层
        Set<Map.Entry<String, List<LayerInfo>>> entrySet = groups.entrySet();
        for (Map.Entry<String, List<LayerInfo>> entry : entrySet) {
            List<LayerInfo> value = entry.getValue();
            //判断是否有一个图层是底图，只要有一个图层是底图，那么默认这个文件夹是底图文件夹
            boolean isBaseMapDir = isBaseMapDir(value);
            //如果是底图
            if (isBaseMapDir) {
                Map<String, List<LayerInfo>> baseMap = new HashMap<>();
                baseMap.put(entry.getKey(), value);
                baseMapList.putAll(baseMap);
                PatrolParentViewHolder2 patrolParentViewHolder2 = new PatrolParentViewHolder2(mContext, onParentViewHolderClickListener);
                View nodeView = patrolParentViewHolder2.createNodeView(baseMap);
                parentViewHolder2Map.put(entry.getKey(), patrolParentViewHolder2);
                scrollItemContainer.addView(nodeView);
            } else {
                normalLayers.addAll(entry.getValue());
            }
        }
        tempLayers.clear();
        for (LayerInfo layerInfo : normalLayers) {
//            if(layerInfo.getLayerName().contains("(")){//相同名称但有括号的图层分离出来
//                tempLayers.add(layerInfo);
//                continue;
//            }
//            if (layerInfo.getLayerName().contains("黄埔管")) {//黄埔管井和黄埔管线分离出来
//                tempLayers.add(layerInfo);
//                continue;
//            }

            PatrolChildViewHolder2 patrolChildViewHolder = new PatrolChildViewHolder2(mContext, layerInfo.getLayerName(), null, onLayerItemClickListener);
            View nodeView = patrolChildViewHolder.createNodeView(layerInfo);
            normalLayerContainer.addView(nodeView);
            childViewHolder2Map.put(String.valueOf(layerInfo.getLayerId()), patrolChildViewHolder);
        }
        ifInitialized = true;

//        /**
//         * 如果不是底图的话，首先分组，按照子图层分组，如果不是存在名称相同的子图层，那么把这两个子图层分成组合成一个组，然后根据截取父图层的名称"_"后面的来区分他们；
//         * 比如：有两个子图层都叫『阀门』，但是它们一个来自图层『部件』，一个来自图层『部件_新』，那么这两个图层组合后的名称分别是：『阀门』、『阀门_新』；
//         */
//        Map<String, List<LayerInfo>> normalGroups = new LinkedHashMap<>();
//
//
//        if (!ListUtil.isEmpty(normalLayers)) {
//            for (LayerInfo layerInfo : normalLayers) {
//
//                List<LayerInfo> childLayer = layerInfo.getChildLayer();
//                /**
//                 * 没有子图层
//                 */
//                if (ListUtil.isEmpty(childLayer)) {
//
//                    if (normalGroups.get(layerInfo.getLayerName()) == null) {
//                        List<LayerInfo> layerInfos = new ArrayList<>();
//                        layerInfos.add(layerInfo);
//                        normalGroups.put(layerInfo.getLayerName(), layerInfos);
//                    } else {
//                        normalGroups.get(layerInfo.getLayerName()).add(layerInfo);
//                    }
//
//                } else {
//                    /**
//                     * 有子图层
//                     */
//                    for (LayerInfo child : childLayer) {
//
//                        if (normalGroups.get(child.getLayerName()) == null) {
//
//                            List<LayerInfo> layerInfos = new ArrayList<>();
//                            layerInfos.add(child);
//                            /***
//                             * 根据父图层的名称修改孩子名称
//                             */
//                            String oldLayerName = changeChildName(layerInfo, child);
//                            normalGroups.put(oldLayerName, layerInfos);
//                        } else {
//                            /***
//                             * 根据父图层的名称修改孩子名称
//                             */
//                            String oldLayerName = changeChildName(layerInfo, child);
//                            normalGroups.get(oldLayerName).add(child);
//                        }
//                    }
//                }
//            }
//            List<String> groupNames = new ArrayList<>();
//            Set<Map.Entry<String, List<LayerInfo>>> entries = normalGroups.entrySet();
//            for (Map.Entry<String, List<LayerInfo>> entry : entries) {
//                groupNames.add(entry.getKey());
//            }
//
//            buildTreeView(groupNames, normalGroups,normalLayerContainer);
//        }
        return view;
    }

    /**
     * 构造treeView
     *
     * @param groupNameList
     * @param groups
     */
    public void buildTreeView(final List<String> groupNameList, Map<String, List<LayerInfo>> groups, LinearLayout linearLayout) {


        ifInitialized = false;

        TreeNode root = TreeNode.root();

        final List<ParentViewHolder> parentViewHolders = new ArrayList<>();
        //全选的组的个数
        mSelectedGroupSize = 0;
        //图层点击后的事件
        OnLayerItemClickListener onLayerItemClickListener = new OnLayerItemClickListener() {
            @Override
            public void onClickCheckbox(int layerId, String groupName,
                                        LayerInfo currentLayer, boolean ifShow) {

                if (ifInitialized) {
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
            ParentViewHolder parentViewHolder = getParentHolder(infoList, level);
            TreeNode parent = new TreeNode(layerTitle).setViewHolder(parentViewHolder);

            for (int j = 0; j < size; j++) {
                LayerInfo childLayerInfo = infoList.get(j);
                TreeNode child = null;
                if (!ListUtil.isEmpty(childLayerInfo.getChildLayer())) {
                    level++;
                    child = new TreeNode(childLayerInfo.getLayerName()).setViewHolder(getParentHolder(childLayerInfo.getChildLayer(), level));
                    List<LayerInfo> grandChildLayerInfos = childLayerInfo.getChildLayer();
                    for (LayerInfo grandChild : grandChildLayerInfos) {
                        TreeNode grandChildNode = new TreeNode(grandChild).setViewHolder(getChildHolder(onLayerItemClickListener, childLayerInfo.getLayerName()));
                        grandChildNode.getViewHolder().getView();
                        child.addChild(grandChildNode);
                    }
                } else {
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
        treeView.setDefaultContainerStyle(com.augurit.agmobile.mapengine.R.style.AGMobile_Widget_TreeView_TreeNode);
        linearLayout.addView(treeView.getView(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ifInitialized = true;
    }


    @NonNull
    protected ParentViewHolder getParentHolder(List<LayerInfo> infoList, int level) {
        return new ParentViewHolder(mContext, infoList, level);
    }

    /**
     * 树形结构
     *
     * @param onLayerItemClickListener
     * @param layerTitle
     * @return
     */

    @NonNull
    protected ChildViewHolder getChildHolder(OnLayerItemClickListener onLayerItemClickListener, String layerTitle) {
        ChildViewHolder childViewHolder = null;
        if (mPresenter != null && mPresenter.getService() != null) {
            LinkedHashMap<Integer, Layer> allLayer = mPresenter.getService().getAllLayer();
            childViewHolder = new ChildViewHolder(mContext, layerTitle, allLayer, onLayerItemClickListener);
        } else {
            childViewHolder = new ChildViewHolder(mContext, layerTitle, null, onLayerItemClickListener);
        }
        return childViewHolder;
    }

    private String changeChildName(LayerInfo layerInfo, LayerInfo child) {
        String oldLayerName = child.getLayerName();
        String[] split = layerInfo.getLayerName().split("_");
        if (split.length == 2) {
            child.setLayerName(child.getLayerName() + "_" + split[1]);
        }
        return oldLayerName;
    }

    /**
     * 改变图层的可见性
     *
     * @param groupName
     * @param layerInfos
     * @param visible    true 表示可见，false表示不可见
     */
    private void ChangeLayerVisibility(String groupName, List<LayerInfo> layerInfos, boolean visible) {
        for (LayerInfo layerInfo : layerInfos) {
            //调用presenter中的点击事件，也就是去真正改变图层的可见度
            mPresenter.onClickCheckbox(groupName, layerInfo.getLayerId(), layerInfo, visible);
        }
    }

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


    /**
     * 判断是否有一个图层是底图，只要有一个图层是底图，那么默认这个文件夹是底图文件夹
     *
     * @param value
     * @return
     */
    private boolean isBaseMapDir(List<LayerInfo> value) {
        boolean isBaseMapDir = false;
        if (!ListUtil.isEmpty(value)) {
            for (LayerInfo layerInfo : value) {
                if (layerInfo.isBaseMap()) {
                    isBaseMapDir = true;
                    break;
                }
            }
        }
        return isBaseMapDir;
    }

    @Override
    public void addLayerViewToTreeViewContainer(View view) {
        treeview_container.removeAllViews();
        treeview_container.addView(view);
    }

    @Override
    public void removeAllLayers() {
        if (mapView != null) {
            mapView.removeAll();
        }
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public View getRootView() {
        return root;
    }

    @Override
    public void addToContainer() {
        if (container != null) {
            container.removeAllViews();
            container.addView(root);
        }
    }

    @Override
    public void toggle(LayerInfo layerInfo, boolean checked) {
        if (childViewHolder2Map.get(String.valueOf(layerInfo.getLayerId())) != null) {
            childViewHolder2Map.get(String.valueOf(layerInfo.getLayerId())).changeCheckState(checked);
        }
        ;
    }

    @Override
    public void setContainer(ViewGroup viewGroup) {
        this.container = viewGroup;
    }

    public void destroy() {
        hideLoadingMap();
    }
}
