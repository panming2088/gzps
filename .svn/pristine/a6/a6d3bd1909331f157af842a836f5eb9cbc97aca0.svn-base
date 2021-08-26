package com.augurit.agmobile.patrolcore.localhistory.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.UploadTableItemResult;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.common.RefreshLocalEvent;
import com.augurit.agmobile.patrolcore.localhistory.util.ConvertTableUtils;
import com.augurit.agmobile.patrolcore.upload.view.ReEditTableActivity;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter;
import com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceRecyclerView;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenu;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuCreator;
import com.augurit.am.cmpt.widget.swipemenulistview.SwipeMenuItem;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.google.gson.Gson;
import com.augurit.agmobile.patrolcore.R;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class LocalProblemView implements ILocalProblemView {
    private Context context;
    private MultiChoiceRecyclerView listView;
    private UnCommitProblemAdapter adapter;
    private ILocalProblemPresenter problemPresenter;
    private LocalTable localTable;
    private ViewGroup menuContainer;
    private ViewGroup multiContainer;
    private TextView mTitle;
    protected TableDataManager tableDataManager;
    private Map<String, String> valueMap;
    private TableState tableState;
    private Button btn_upload_all;
    private List<LocalTable> mProblems;

    public LocalProblemView(Context context, ILocalProblemPresenter presenter) {
        this.context = context;
        problemPresenter = presenter;
        tableDataManager = new TableDataManager(context);
    }

    @Override
    public void onShowLocalProblemListView(final List<LocalTable> problems, final ViewGroup container, final ViewGroup menuContainer, final ViewGroup multiContainer) {
        this.menuContainer = menuContainer;
        this.multiContainer = multiContainer;
        this.mProblems = problems;
        mTitle = (TextView) multiContainer.findViewById(R.id.select_text);
        ViewGroup mainView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.local_problems_list_view, null);

        /*
        SwipeMenuCreator mMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 阅览按钮
                SwipeMenuItem readItem = new SwipeMenuItem(context);
                int colorRead = ResourcesCompat.getColor(context.getResources(), R.color.agmobile_grey_0, null);
                readItem.setBackground(new ColorDrawable(colorRead));   // 设置背景颜色
                readItem.setWidth(DensityUtil.dp2px(context, 80));      // 宽度,目前没有适配屏幕
                readItem.setTitle("阅览");
                readItem.setTitleColor(Color.WHITE);
                readItem.setTitleSize(16);
                menu.addMenuItem(readItem);
                // 编辑按钮
                SwipeMenuItem editItem = new SwipeMenuItem(context);
                int colorEdit = ResourcesCompat.getColor(context.getResources(), R.color.agmobile_blue, null);
                editItem.setBackground(new ColorDrawable(colorEdit));
                editItem.setWidth(DensityUtil.dp2px(context, 80));
                editItem.setTitle("编辑");
                editItem.setTitleColor(Color.WHITE);
                editItem.setTitleSize(16);
                menu.addMenuItem(editItem);

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
        */

        listView = (MultiChoiceRecyclerView) mainView.findViewById(R.id.list_view);

//        listView.setMenuCreator(mMenuCreator);
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener(){
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
//                 localTable = problems.get(position);
//                String key = localTable.getKey();
//
//                ArrayList<TableItem> tableItems = convert(localTable);
//                TableDataManager tableDataManager = new TableDataManager(context.getApplicationContext());
//                ArrayList<Photo> photos = (ArrayList<Photo>) tableDataManager.getPhotoFormDB(localTable.getKey());
//                Intent intent =null;
//                switch (index){
//                    case 0: //阅览
//                        intent = new Intent(context, ReadTableActivity.class);
//                        intent.putExtra("tableitems", tableItems);
//                        intent.putExtra("photos", photos);
//                        context.startActivity(intent);
//                        break;
//                    case 1: //编辑
//                        intent = new Intent(context, ReEditTableActivity.class);
//                        intent.putExtra("tableitems", tableItems);
//                        intent.putExtra("photos", photos);
//                        intent.putExtra("projectId",localTable.getId());
//                        intent.putExtra("tableKey",localTable.getKey());
//                        context.startActivity(intent);
//                        break;
//                    case 2://删除
//                        problemPresenter.deleteLocalTable(key, new TableDBCallback() {
//                            @Override
//                            public void onSuccess(Object data) {
//                                problems.remove(position);
//                                onDeleteLocalProbelm();
//                                ToastUtil.longToast(context,"删除成功!");
//                            }
//
//                            @Override
//                            public void onError(String msg) {
//
//                            }
//                        });
//                        break;
//                }
//                ((Activity)context).finish();
//                return false;//// 返回false表示收起菜单
//            }
//        });
        adapter = new UnCommitProblemAdapter(mProblems,context);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.attachActivity((AppCompatActivity) context, new Callback());
        listView.setMultiChoiceEnable(true);
//        listView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiChoiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                localTable = mProblems.get(position);
                String key = localTable.getKey();

                ArrayList<TableItem> tableItems = ConvertTableUtils.convert(localTable);
                TableDataManager tableDataManager = new TableDataManager(context.getApplicationContext());
                ArrayList<Photo> photos = (ArrayList<Photo>) tableDataManager.getPhotoFormDB(localTable.getKey());
                Intent intent = new Intent(context, ReEditTableActivity.class);
                intent.putExtra("tableitems", tableItems);
                intent.putExtra("photos", photos);
                intent.putExtra("projectId", localTable.getId());
                intent.putExtra("tableKey", localTable.getKey());
                intent.putExtra("tableName",localTable.getIndustryTableName());
                context.startActivity(intent);
            }
        });

        /*
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                LocalTable localTable = problems.get(position);
                ArrayList<TableItem> tableItems = convert(localTable);
                TableDataManager tableDataManager = new TableDataManager(context.getApplicationContext());
                ArrayList<Photo> photos = (ArrayList<Photo>) tableDataManager.getPhotoFormDB(localTable.getId());
                Intent intent = new Intent(context, ReadTableActivity.class);
                intent.putExtra("tableitems", tableItems);
                intent.putExtra("photos", photos);
                context.startActivity(intent);
            }
        });
        */
        //全选按钮
        multiContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitle.getText().equals("全选")) {
                    mTitle.setText("全不选");
                    adapter.setAll(true);
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        adapter.setItemCheck(i, true);
                    }
                } else {
                    mTitle.setText("全选");
                    adapter.setAll(false);
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        adapter.setItemCheck(i, false);
                    }
                }
            }
        });

        //取消菜单按钮
        menuContainer.findViewById(R.id.menu_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                adapter.clearChoices();
                menuContainer.setVisibility(View.GONE);
                multiContainer.setVisibility(View.GONE);
                adapter.setSelectable(false);
                adapter.notifyDataSetChanged();
            }
        });

        //删除菜单按钮
        menuContainer.findViewById(R.id.menu_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除
                final SparseBooleanArray selectedPositions = adapter.getSelectedPositions();
                boolean isMove = false;
                for (int i = selectedPositions.size() - 1; i >= 0; i--) {
                    if (selectedPositions.valueAt(i)) {
                        isMove = true;
                        final int position = selectedPositions.keyAt(i);
                        LocalTable localTable = mProblems.get(position);
                        String key = localTable.getKey();
                        problemPresenter.deleteLocalTable(key, new TableDBCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                mProblems.remove(position);
                                selectedPositions.delete(position);

                                //更新主界面
                                EventBus.getDefault().post(new RefreshLocalEvent());
                            }

                            @Override
                            public void onError(String msg) {

                            }
                        });
                    }
                }
                if (isMove) {
                    onDeleteLocalProbelm();
                    ToastUtil.longToast(context, "删除成功!");
                }
            }
        });

        //提交菜单按钮
        menuContainer.findViewById(R.id.menu_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交
                SparseBooleanArray selectedPositions = adapter.getSelectedPositions();
                List<LocalTable> chosedList = new ArrayList<LocalTable>();
                for (int i = selectedPositions.size() - 1; i >= 0; i--) {
                    if (selectedPositions.valueAt(i)) {
                        localTable = mProblems.get(selectedPositions.keyAt(i));
                        chosedList.add(localTable);

                    }
                }
                //开始批量上传
                problemPresenter.uploadAllTable(chosedList);

                //恢复默认界面
                adapter.clearChoices();
                menuContainer.setVisibility(View.GONE);
                multiContainer.setVisibility(View.GONE);
                adapter.setSelectable(false);
                adapter.notifyDataSetChanged();

            }
        });

        container.removeAllViews();
        container.addView(mainView);
    }


    @Override
    public void onDeleteLocalProbelm() {
        adapter.notifyDataSetChanged();
    }

    private class Callback implements MultiChoiceRecyclerView.ActionModeCallback {
        ViewGroup menuView;

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menuContainer.setVisibility(View.VISIBLE);
            multiContainer.setVisibility(View.VISIBLE);
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onItemCheckedStateChanged(int position, boolean checked) {
            adapter.notifyItemChanged(position);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    /**
     * 提交编辑到服务器
     */
    public void uploadEdit(List<TableItem> list, String projectId, final String key, final ArrayList<Photo> photos) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        String url = serverUrl + "rest/report/save";
        if (list == null) return;
        setMapValue(list);
        final ProgressDialog progressDialog = ProgressDialog.show(context, "提示", "正在提交数据");
        tableDataManager.uploadTableItems(url, projectId, valueMap, list, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                String result = null;
                try {
                    result = ((ResponseBody) data).string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UploadTableItemResult uploadTableItemResult = gson.fromJson(result, UploadTableItemResult.class);
                if (uploadTableItemResult.getSuccess().equals("true")) {
                    ToastUtil.longToast(context, "提交成功!");
                }
                progressDialog.dismiss();
                if (photos == null || photos.size() <= 0) {
                    afterUpload(key);
                } else {
                    if (uploadTableItemResult.getPatrolId() != null) {
                        uploadFiles(uploadTableItemResult.getPatrolId(),key,photos);
                    }
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

    private void setMapValue(List<TableItem> list) {
        for (TableItem tableItem : list) {
            valueMap.put(tableItem.getField1(), tableItem.getValue());
        }
    }

    /**
     * 再编辑模式,编辑后删除本地保存数据
     */
    public void afterUpload(String tableKey) {
        if (tableKey == null) return;
        tableDataManager.deleteEditedTableItemsFromBD(tableKey, new TableDBCallback() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void uploadFiles(String patrolCode, final String key, final ArrayList<Photo> photos) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        String url = serverUrl + "rest/upload/add";
        url = url + "/" + patrolCode;
        //   http://210.21.98.71:8088/agweb14/rest/upload/add/{patrolCode}
        if (photos == null || photos.size() <= 0) {
            return;
        }
        final ProgressDialog progressDialog = ProgressDialog.show(context, "提示", "正在提交照片");
        tableDataManager.uploadPhotos(url, patrolCode, photos, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                // ToastUtil.longToast(mContext,((ResponseBody)data).toString());
                ToastUtil.longToast(context, "照片上传成功!");
                afterUpload(key);

                progressDialog.dismiss();
            }
            @Override
            public void onError(String msg) {
                ToastUtil.longToast(context, msg);
            }
        });
    }

    @Override
    public void refreshListView(LocalTable position) {
        for(LocalTable localTable : mProblems){
            if(localTable.getKey().equals(position.getKey())){
                mProblems.remove(localTable);
                break;
            }
        }
        //更新列表界面
        adapter.notifyDataSetChanged();

        //更新主界面
        EventBus.getDefault().post(new RefreshLocalEvent());
    }
}
