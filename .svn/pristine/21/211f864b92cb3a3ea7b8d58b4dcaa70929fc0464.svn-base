package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerClickListener;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.MapLocateEvent;
import com.augurit.agmobile.patrolcore.layerdownload.adapter.TaskListAdapter;
import com.augurit.agmobile.patrolcore.layerdownload.holder.LayerItemHolder;
import com.augurit.agmobile.patrolcore.layerdownload.holder.LayerTypeHolder;
import com.augurit.agmobile.patrolcore.layerdownload.model.LayerDnlTask;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.ILayerDownloadView;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadPresenter;
import com.augurit.agmobile.patrolcore.layerdownload.service.DownloadTableDBService;
import com.augurit.am.cmpt.widget.segmentControlView.SegmentControlView;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenu;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuCreator;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuItem;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuListView;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuRecyclerView;
import com.augurit.am.cmpt.widget.treeview.model.TreeNode;
import com.augurit.am.cmpt.widget.treeview.view.AndroidTreeView;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.core.geometry.Envelope;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by liangsh on 2016-09-19.
 */
public class LayerDownloadView implements ILayerDownloadView, OnLayerClickListener {

    private Context context;
    protected LayerDownloadPresenter mLayerDownloadPresenter;

    /** 管理界面控件   */
    private ViewGroup mDnlMgrView;
    private View mBackBtn;
    private TextView mTitleTv;
    private SwipeMenuRecyclerView mTaskList;
    private ViewGroup mTaskListLayout;
    private TaskListAdapter mTaskListAdapter;
    private ViewGroup mTreeContainer;
    private DownloadTableDBService mDownloadTableDBService;
    private static LayerDownloadView instance;


    protected final SparseArray<View> mViews = new SparseArray<View>();

    public LayerDownloadView(final Context context, LayerDownloadPresenter layerDownloadPresenter){
        this.context = context;
        this.mLayerDownloadPresenter = layerDownloadPresenter;
        mDownloadTableDBService = new DownloadTableDBService(context);
        mDnlMgrView = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.dnl_view_layerdownload, null);
        mBackBtn = mDnlMgrView.findViewById(R.id.btn_back);
        mTitleTv = (TextView) mDnlMgrView.findViewById(R.id.tv_title);
        mTaskList = (SwipeMenuRecyclerView) mDnlMgrView.findViewById(R.id.dnl_rv_tasklist);
        SwipeMenuCreator menuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //查看地图
                SwipeMenuItem mapItem = new SwipeMenuItem(context);
                int colorMap = ResourcesCompat.getColor(context.getResources(), R.color.green, null);
                mapItem.setBackground(new ColorDrawable(colorMap));
                mapItem.setWidth(DensityUtil.dp2px(context, 80));
                mapItem.setTitle("查看地图");
                mapItem.setTitleColor(Color.WHITE);
                mapItem.setTitleSize(16);
                menu.addMenuItem(mapItem);
                // 删除按钮
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                int colorDelete = ResourcesCompat.getColor(context.getResources(), R.color.agmobile_red, null);
                deleteItem.setBackground(new ColorDrawable(colorDelete));
                deleteItem.setWidth(DensityUtil.dp2px(context, 80));
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(16);
                menu.addMenuItem(deleteItem);
            }
        };
        mTaskList.setMenuCreator(menuCreator);
        mTaskList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                LayerDnlTask task = mTaskListAdapter.getTaskItem(position);
                // 删除
                if(index == 0){
                    Envelope envelope = task.getEnvelope();
                    EventBus.getDefault().post(new MapLocateEvent(envelope));
                    mLayerDownloadPresenter.back();
                }else if(index == 1){
                    if(mDownloadTableDBService!=null) {
                        mDownloadTableDBService.deleteItemFromDB(task.getId());
                        mTaskListAdapter.removeTaskItem(position);
                        mTaskList.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        mTaskList.setLayoutManager(new LinearLayoutManager(context));
        mTaskListLayout = (ViewGroup) mDnlMgrView.findViewById(R.id.dnl_ll_tasklist);
        mTreeContainer = (ViewGroup) mDnlMgrView.findViewById(R.id.dnl_treeview_container);
        mTaskListAdapter = new TaskListAdapter(context);
        mTaskList.setAdapter(mTaskListAdapter);

//        AMSpringSwitchButton switchButton = (AMSpringSwitchButton) mDnlMgrView.findViewById(R.id.switch_button);
//        switchButton.setOnToggleListener(new AMSpringSwitchButton.OnToggleListener() {
//            @Override
//            public void onToggle(boolean left) {
//                if (left) {
//                    mTaskListLayout.setVisibility(View.VISIBLE);
//                    mTreeContainer.setVisibility(View.GONE);
//                } else {
//                    mTaskListLayout.setVisibility(View.GONE);
//                    mTreeContainer.setVisibility(View.VISIBLE);
//                }
//            }
//        });   // xjx 替换控件
        SegmentControlView scv = (SegmentControlView) mDnlMgrView.findViewById(R.id.scv);
        scv.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                switch (newSelectedIndex) {
                    case 0:
                        mTaskListLayout.setVisibility(View.VISIBLE);
                        mTreeContainer.setVisibility(View.GONE);
                        break;
                    case 1:
                        mTaskListLayout.setVisibility(View.GONE);
                        mTreeContainer.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayerDownloadPresenter.back();
            }
        });
    }

    @Override
    public void initTaskList(ArrayList<LayerDnlTask> tasks){
        List<LayerDnlTask> ltLayDnlTask = mDownloadTableDBService.getTableItemsByDB();
        Collections.reverse(ltLayDnlTask);
        mTaskListAdapter.notifyDataSetChanged((ArrayList<LayerDnlTask>) ltLayDnlTask);
    }

    @Override
    public void addTaskList(LayerDnlTask task){
        mTaskListAdapter.addTask(task);
    }

    @Override
    public void updateTask(int taskId, double total, double downed, boolean done, Throwable e){
        mTaskListAdapter.updateTask(taskId, total, downed, done, e);
        mTaskList.notifyDataSetChanged();
    }

    @Override
    public void setLayerData(List<ProjectInfo> projectInfos,
                                       Map<String, List<LayerInfo>> layerInfosMap){
        //创建树形列表根结点
        TreeNode root = TreeNode.root();
//        for(ProjectInfo projectInfo : projectInfos){
//            TreeNode projectNode = new TreeNode(projectInfo.getTasktableName())
//                    .setViewHolder(new LayerTypeHolder(context));
            TreeNode tileNode = new TreeNode("瓦片")
                    .setViewHolder(new LayerTypeHolder(context));
            TreeNode featureNode = new TreeNode("矢量")
                    .setViewHolder(new LayerTypeHolder(context));
            List<LayerInfo> layerInfos = layerInfosMap.get("abc");
            if(!ListUtil.isEmpty(layerInfos)){
                for(LayerInfo layerInfo : layerInfos){
                    TreeNode layerNode = new TreeNode(layerInfo)
                            .setViewHolder(new LayerItemHolder(context, this));
                    if(layerInfo.getType() == LayerType.TileLayer || layerInfo.getType() == LayerType.TianDiTu){
                        tileNode.addChild(layerNode);
                    } else if(layerInfo.getType() == LayerType.FeatureLayer) {
                        featureNode.addChild(layerNode);
                    }
                }
            }
//            projectNode.addChildren(tileNode, featureNode);
            root.addChild(tileNode);
            // TODO lsh 暂时隐藏矢量图层
//            root.addChildren(featureNode);
//        }
        AndroidTreeView treeView = new AndroidTreeView(context, root);
        treeView.setDefaultAnimation(true);
        treeView.setDefaultContainerStyle(R.style.dnl_treenode_style, true);
        mTreeContainer.removeAllViews();
        mTreeContainer.addView(treeView.getView());
    }

    @Override
    public void setProjectName(String projectName){
        mTitleTv.setText(projectName);
    }

    @Override
    public View getDnlMgrView(){
        return mDnlMgrView;
    }

    @Override
    public void onClick(View view, LayerInfo layerInfo) {
        mLayerDownloadPresenter.onDownloadBtnClick(layerInfo);
    }
}
