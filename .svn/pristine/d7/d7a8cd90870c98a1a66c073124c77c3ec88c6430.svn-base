package com.augurit.agmobile.gzps.componentmaintenance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.gzps.componentmaintenance.model.QueryFeatureSet;
import com.augurit.agmobile.gzps.componentmaintenance.service.ComponentService;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.layer.service.EditLayerService;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.loadinglayout.ProgressLinearLayout;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * 部件维护列表界面
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class ComponentMaintenanceListFragment extends Fragment {

    private ComponetListAdapter mSearchResultAdapter;
    private ProgressLinearLayout pb_loading;
    private XRecyclerView rv_component_list;
    private TextView tv_component_counts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_componentlist, null);

        return inflate;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb_loading = (ProgressLinearLayout) view.findViewById(R.id.pb_loading);
        if(showAddButton()){
            view.findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddButtonClick();
                }
            });
        } else {
            view.findViewById(R.id.btn_add).setVisibility(View.GONE);
        }

        rv_component_list = (XRecyclerView) view.findViewById(R.id.rv_component_list);
        rv_component_list.setPullRefreshEnabled(true);
        rv_component_list.setLoadingMoreEnabled(false);
        rv_component_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchResultAdapter = new ComponetListAdapter(getActivity(), new ArrayList<Component>());
        rv_component_list.setAdapter(mSearchResultAdapter);
        mSearchResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {

                if (getActivity() instanceof IChangeTabListener){
                    IChangeTabListener tabListener = (IChangeTabListener) getActivity();
                    tabListener.changeToTab(1); //跳到地图界面
                    final Component component = mSearchResultAdapter.getDataList().get(position);
//                    loadCompleteData(component);
//                    EventBus.getDefault().post(new HighlightEvent(new Integer[]{component.getObjectId()}, Color.RED));
                    new Thread(){
                        @Override
                        public void run(){
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            EventBus.getDefault().post(new SelectComponentEvent(component));
                        }
                    }.start();

                }
            }
        });

        rv_component_list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mSearchResultAdapter.clear();
                loadDatas(false);
            }

            @Override
            public void onLoadMore() {

            }
        });

        tv_component_counts = (TextView) view.findViewById(R.id.tv_component_counts);

        loadDatas(true);
    }


    public void showLoadedError(String errorReason) {
        pb_loading.showError("获取数据失败", errorReason, "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });

    }

    public void onAddButtonClick(){

    }

    public void showLoadedEmpty() {

        pb_loading.showError("", "暂无数据", "刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDatas(true);
            }
        });

    }

    public boolean showAddButton(){
        return false;
    }

    public void loadDatas(boolean ifShowPb) {
        if (ifShowPb)
            pb_loading.showLoading();

        final ComponentService componentMaintenanceService = new ComponentService(getActivity().getApplicationContext());
        componentMaintenanceService.queryAllComponents2(getComponentType())
                .map(new Func1<List<QueryFeatureSet>, List<Component>>() {
                    @Override
                    public List<Component> call(List<QueryFeatureSet> queryFeatureSetList) {

                        if(ListUtil.isEmpty(queryFeatureSetList)){
                            return new ArrayList<Component>();
                        }
                        List<Component> components = new ArrayList<Component>();
                        for(QueryFeatureSet queryFeatureSet : queryFeatureSetList){
                            if(queryFeatureSet == null
                                    || queryFeatureSet.getFeatureSet().getGraphics().length == 0){
                                continue;
                            }
                            FeatureSet featureSet = queryFeatureSet.getFeatureSet();
                            //发送高亮事件
//                            EventBus.getDefault().post(new HighlightEvent(featureSet.getObjectIds(),Color.YELLOW));

                            Graphic[] graphics = featureSet.getGraphics();
                            if (graphics!= null && graphics.length >0){
                                for (Graphic graphic : graphics){
                                    Component component = new Component();
                                    component.setLayerUrl(queryFeatureSet.getLayerUrl());
                                    component.setLayerName(queryFeatureSet.getLayerName());
                                    component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                                    component.setFieldAlias(featureSet.getFieldAliases());
//                                    component.setFields(featureSet.getFields());
                                    component.setGraphic(graphic);
                                    String errorInfo = componentMaintenanceService.getErrorInfo(graphic.getAttributes());
                                    if (errorInfo != null){
                                        component.setErrorInfo(errorInfo);
                                    }
                                    Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
                                    if (o != null && o instanceof Integer)
                                        component.setObjectId((Integer) o); //按照道理objectId一定是integer的

                                    components.add(component);
                                }
                            }
                        }
                        Collections.sort(components, new Comparator<Component>() {
                            @Override
                            public int compare(Component o1, Component o2) {
                                Object date1 = o1.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE);
                                Object date2 = o2.getGraphic().getAttributes().get(ComponentFieldKeyConstant.UPDATEDATE);
                                if(StringUtil.isEmpty(date1)
                                        || StringUtil.isEmpty(date2)){
                                    return 1;
                                }
                                try {
                                    long time1 = Long.valueOf(date1.toString());
                                    long time2 = Long.valueOf(date2.toString());
                                    if(time1 > time2){
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                } catch (Exception e) {
                                    return 1;
                                }
                            }
                        });

                        return components;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Component>>() {
                    @Override
                    public void onCompleted() {

                        if (ListUtil.isEmpty(mSearchResultAdapter.getDataList())) {
                            showLoadedEmpty();
                            return;
                        }

                        tv_component_counts.setText("一共有：" +  mSearchResultAdapter.getDataList().size() + "条数据");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoadedError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Component> components) {
                        if (ListUtil.isEmpty(components)){
                            return;
                        }
                        pb_loading.showContent();
                        rv_component_list.refreshComplete();
                        mSearchResultAdapter.addData(components);
                    }
                });
    }

//    public void loadDatas2(boolean ifShowPb) {
//        if (ifShowPb)
//        pb_loading.showLoading();
//
//        final ComponentService componentMaintenanceService = new ComponentService(getActivity().getApplicationContext());
//        componentMaintenanceService.queryAllComponents(getComponentType())
//                .map(new Func1<QueryFeatureSet, List<Component>>() {
//                    @Override
//                    public List<Component> call(QueryFeatureSet queryFeatureSet) {
//
//                        if(queryFeatureSet == null){
//                            return new ArrayList<Component>();
//                        }
//
//                        FeatureSet featureSet = queryFeatureSet.getFeatureSet();
//
//                        //发送高亮事件
//                        EventBus.getDefault().post(new HighlightEvent(featureSet.getObjectIds(),Color.YELLOW));
//
//                        List<Component> components = new ArrayList<Component>();
//                        Graphic[] graphics = featureSet.getGraphics();
//                        if (graphics!= null && graphics.length >0){
//                            for (Graphic graphic : graphics){
//                                Component component = new Component();
//                                component.setLayerUrl(queryFeatureSet.getLayerUrl());
//                                component.setLayerName(queryFeatureSet.getLayerName());
//                                component.setDisplayFieldName(featureSet.getDisplayFieldName());
//                                component.setFieldAlias(featureSet.getFieldAliases());
//                                component.setFields(featureSet.getFields());
//                                component.setGraphic(graphic);
//                                String errorInfo = componentMaintenanceService.getErrorInfo(featureSet.getFieldAliases());
//                                if (errorInfo != null){
//                                    component.setErrorInfo(errorInfo);
//                                }
//                                Object o = graphic.getAttributes().get(featureSet.getObjectIdFieldName());
//                                if (o != null && o instanceof Integer)
//                                component.setObjectId((Integer) o); //按照道理objectId一定是integer的
//
//                                components.add(component);
//                            }
//                        }
//
//                        return components;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Component>>() {
//                    @Override
//                    public void onCompleted() {
//
//                        if (ListUtil.isEmpty(mSearchResultAdapter.getDataList())) {
//                            showLoadedEmpty();
//                            return;
//                        }
//
//                        tv_component_counts.setText("一共有：" +  mSearchResultAdapter.getDataList().size() + "条数据");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showLoadedError(e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Component> components) {
//                        if (ListUtil.isEmpty(components)){
//                            return;
//                        }
//                        pb_loading.showContent();
//                        rv_component_list.refreshComplete();
//                        mSearchResultAdapter.addUpData(components);
//                    }
//                });
//    }


    private List<TableItem> tableItems = null;

    ProgressDialog pd;

    private void loadCompleteData(final Component component){
        pd = new ProgressDialog(getContext());
        pd.setMessage("正在加载详细信息...");
        pd.show();
        final TableDataManager tableDataManager = new TableDataManager(getActivity().getApplicationContext());
        List<Project> projects = tableDataManager.getProjectFromDB();
        Project project = null;
        for(Project p : projects){
            if(p.getName().equals(component.getLayerName())){
                project = p;
            }
        }
        if(project == null){
            ToastUtil.shortToast(getContext(), "加载详细信息失败！");
            return;
        }
        final String projectId = project.getId();
        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(getContext()) + "rest/report/rptform";
        tableDataManager.getTableItemsFromNet(project.getId(), getFormStructureUrl, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                TableItems tmp = (TableItems) data;
                if (tmp.getResult() != null) {
                    tableItems = tmp.getResult();
                    //   tableDataManager.setTableItemsToDB(tableItems);
                    //缓存在数据库中
                    Realm realm = Realm.getDefaultInstance();
                    ProjectTable projectTable = new ProjectTable();
                    projectTable.setId(projectId);
                    realm.beginTransaction();
                    projectTable.setTableItems(new RealmList<TableItem>(tableItems.toArray(new TableItem[tableItems.size()])));
                    realm.commitTransaction();
                    tableDataManager.setProjectTableToDB(projectTable);
                    ArrayList<TableItem> completeTableItems = EditLayerService.getCompleteTableItem(component.getGraphic(), tableItems);
                    TableViewManager.isEditingFeatureLayer = true;
                    TableViewManager.graphic = component.getGraphic();
                    TableViewManager.geometry = component.getGraphic().getGeometry();
//                    TableViewManager.featueLayerUrl = component.getLayerUrl();
                    TableViewManager.featueLayerUrl = LayerUrlConstant.getNewLayerUrlByUnknownLayerUrl(component.getLayerUrl());
                    if(false){
                        Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
                        intent.putExtra("tableitems", completeTableItems);
                        intent.putExtra("projectId", projectId);
                        startActivity(intent);
                    } else {
                        queryAttachmentInfos(component.getLayerUrl(), component.getGraphic(), completeTableItems, projectId);
                    }
                } else {
                }
            }

            @Override
            public void onError(String msg) {
                pd.dismiss();
                ToastUtil.shortToast(getContext(), "加载详细信息失败!");
            }
        });
    }

    private void queryAttachmentInfos(String layerUrl, final Graphic graphic, final ArrayList<TableItem> completeTableItems, final String projectId){
        final ArcGISFeatureLayer arcGISFeatureLayer = new ArcGISFeatureLayer(layerUrl, ArcGISFeatureLayer.MODE.ONDEMAND);
        arcGISFeatureLayer.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    final int objectid = Integer.valueOf(graphic.getAttributes().get(arcGISFeatureLayer.getObjectIdField()).toString());
                    arcGISFeatureLayer.queryAttachmentInfos(objectid, new CallbackListener<AttachmentInfo[]>() {
                        @Override
                        public void onCallback(AttachmentInfo[] attachmentInfos) {
                            Intent intent = new Intent(getActivity(), ReEditTableActivity.class);
                            intent.putExtra("tableitems", completeTableItems);
                            intent.putExtra("projectId", projectId);
                            if(attachmentInfos == null
                                    || attachmentInfos.length == 0){

                            } else {
                                ArrayList<Photo> photoList = new ArrayList<Photo>();
                                Map<String, Long> map = new HashMap<>();
                                for (AttachmentInfo attachmentInfo : attachmentInfos) {
                                    if(map.containsKey(attachmentInfo.getName())){
                                        continue;
                                    }
                                    if(!attachmentInfo.getContentType().contains("image")){
                                        continue;
                                    }
                                    try {
                                        InputStream inputStream = arcGISFeatureLayer.retrieveAttachment(objectid,
                                                Integer.valueOf(String.valueOf(attachmentInfo.getId())));
                                        String tempFile = new FilePathUtil(getContext()).getSavePath() + "/images/" + attachmentInfo.getName() + ".jpg";
                                        File targetFile = new File(tempFile);
                                        if (!targetFile.getParentFile().exists()) {
                                            targetFile.getParentFile().mkdirs();
                                        }
                                        if (!targetFile.exists()) {
                                            targetFile.createNewFile();
                                        }
//                                        OutputStream  outputStream = new FileOutputStream(targetFile);
                                        /*int bytesWritten = 0;
                                        int byteCount = 0;
                                        byte[] bytes = new byte[1024];
                                        while ((byteCount = inputStream.read(bytes)) != -1)
                                        {
                                            outputStream.write(bytes, bytesWritten, byteCount);
                                            bytesWritten += byteCount;
                                        }*/
                                        AMFileUtil.saveInputStreamToFile(inputStream, targetFile);
                                        inputStream.close();
//                                        outputStream.close();
                                        Photo photo = new Photo();
                                        photo.setPhotoPath("file://" + tempFile);
                                        photo.setLocalPath(tempFile);
                                        photo.setField1("photo");
                                        photo.setHasBeenUp(1);
                                        photoList.add(photo);
                                        map.put(attachmentInfo.getName(), attachmentInfo.getId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(!ListUtil.isEmpty(photoList)){
                                    intent.putExtra("photos", photoList);
                                }
                            }
                            pd.dismiss();
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            pd.dismiss();
                            ToastUtil.shortToast(getContext(), "加载详细信息失败!");
                        }
                    });
                }
            }
        });
    }

    protected String getComponentType(){
        return ComponentTypeConstant.OLD_COMPONENT2;
    }
}
