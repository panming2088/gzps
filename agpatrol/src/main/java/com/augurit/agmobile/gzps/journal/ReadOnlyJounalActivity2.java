package com.augurit.agmobile.gzps.journal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.constant.LayerUrlConstant;
import com.augurit.agmobile.gzps.common.model.OUser;
import com.augurit.agmobile.gzps.common.model.StringResult2;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentOrAddressActivity;
import com.augurit.agmobile.gzps.common.service.GzpsService;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.MultiSelectTableItlem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.journal.model.Journal;
import com.augurit.agmobile.gzps.journal.model.TeamMember;
import com.augurit.agmobile.gzps.journal.service.JournalsService;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.agmobile.patrolcore.selectlocation.view.SelectLocationActivity;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;
import com.google.common.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.journal
 * @createTime 创建时间 ：17/11/4
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/4
 * @modifyMemo 修改备注：
 */

public class ReadOnlyJounalActivity2 extends BaseActivity {

    private TextView btn_edit;
    /**
     * 是否处于编辑状态
     */
    private boolean ifInEditMode = false;

    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem et_address;
    private TextItemTableItem et_road;
    private TextFieldTableItem et_content;
    private TextItemTableItem et_parent_organization;
    private TextItemTableItem et_direct_organization;
    private TextItemTableItem et_team;
    private TextItemTableItem et_user;
    private TextItemTableItem et_date;
    private TextItemTableItem textitem_component_type;
    private View btn_upload_journal;

    private Journal journal;
    //private EditText et_team_memeber;

    private GzpsService mGzpsService;
    private EditText et_water_level;
    private View rl_water_level;
    private MultiSelectTableItlem multiselect_table_item;
    private TextView tv_select_or_check_location;
    private FlexRadioGroup rg_water_level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal2);
        mGzpsService = new GzpsService(this.getApplicationContext());
        journal = (Journal) getIntent().getSerializableExtra("journal");

        initView();

        initData();

        EventBus.getDefault().register(this);
    }

    private void initData() {

        if (journal == null) {
            return;
        }

        take_photo_item.setSelectedPhotos(journal.getAttachments());
        et_address.setText(journal.getAddr());
        et_road.setText(journal.getRoad());
        et_content.setText(journal.getDescription());

        if (TextUtils.isEmpty(journal.getParentOrgName())) {
            et_parent_organization.setVisibility(View.GONE);
        } else {
            et_parent_organization.setText(journal.getParentOrgName());
        }

        if (TextUtils.isEmpty(journal.getTeamOrgName())) {
            et_team.setVisibility(View.GONE);
        } else {
            et_team.setText(journal.getTeamOrgName());
        }

        if (TextUtils.isEmpty(journal.getDirectOrgName())) {
            et_direct_organization.setVisibility(View.GONE);
        } else {
            et_direct_organization.setText(journal.getDirectOrgName());
        }

        //et_direct_organization.setText(journal.getDirectOrgName());
        et_team.setText(journal.getTeamOrgName());
        et_user.setText(journal.getWriterName());
        et_date.setText(TimeUtil.getStringTimeYMDMChines(new Date(journal.getRecordTime())));


        if (!TextUtils.isEmpty(journal.getWaterLevel())) {
            rl_water_level.setVisibility(View.VISIBLE);
            et_water_level.setVisibility(View.VISIBLE);
            rg_water_level.setVisibility(View.GONE);
            et_water_level.setText(journal.getWaterLevel());
        } else {
            rl_water_level.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(journal.getRoad())) {
            et_road.setVisibility(View.VISIBLE);
            et_road.setText(journal.getRoad());
        } else {
            et_road.setVisibility(View.GONE);
        }


    }

    protected void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("日志详情");

        btn_edit = (TextView) findViewById(R.id.btn_edit);
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        et_address = (TextItemTableItem) findViewById(R.id.textitem_address);
        et_road = (TextItemTableItem) findViewById(R.id.textitem_road);
        et_content = (TextFieldTableItem) findViewById(R.id.textfield_content);
        et_parent_organization = (TextItemTableItem) findViewById(R.id.textitem_parent_organization);
        et_direct_organization = (TextItemTableItem) findViewById(R.id.textitem_direct_organization);
        et_team = (TextItemTableItem) findViewById(R.id.textitem_team);
        et_user = (TextItemTableItem) findViewById(R.id.textitem_user);
        et_date = (TextItemTableItem) findViewById(R.id.textitem_date);
        textitem_component_type = (TextItemTableItem) findViewById(R.id.textitem_component_type);
        tv_select_or_check_location = (TextView) findViewById(R.id.tv_select_or_check_location);
        multiselect_table_item = (MultiSelectTableItlem) findViewById(R.id.multiselect_table_item);
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        btn_upload_journal.setVisibility(View.GONE);
        rg_water_level = (FlexRadioGroup) findViewById(R.id.rg_water_level);

        et_address.setVisibility(View.VISIBLE);
        et_road.setVisibility(View.VISIBLE);
        textitem_component_type.setVisibility(View.VISIBLE);

        et_water_level = (EditText) findViewById(R.id.et_water_level);
        rl_water_level = findViewById(R.id.rl_water_level);
        findViewById(R.id.rg_water_level).setVisibility(View.GONE);
//        findViewById(R.id.take_photo_item).setVisibility(View.GONE);

        et_parent_organization.setReadOnly();
        et_parent_organization.setVisibility(View.GONE);
        et_direct_organization.setVisibility(View.GONE);
        et_direct_organization.setReadOnly();
        et_team.setReadOnly();
        et_user.setReadOnly();
        et_date.setReadOnly();
        et_water_level.setEnabled(false);
        //et_team_memeber.setEnabled(false);
        //et_team_memeber.setVisibility(View.VISIBLE);

        setEditable(false);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifInEditMode) {
                    setEditable(true);
                    btn_edit.setText("放弃编辑");
                    ifInEditMode = true;
                } else {
                    setEditable(false);
                    btn_edit.setText("编辑");
                    ifInEditMode = false;
                }
            }
        });

        et_direct_organization.setVisibility(View.VISIBLE);
        et_team.setVisibility(View.VISIBLE);
        et_parent_organization.setVisibility(View.VISIBLE);


        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 提交
         */
        btn_upload_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(ReadOnlyJounalActivity2.this);
                progressDialog.setMessage("正在提交，请等待");
                progressDialog.show();

                List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();
                journal.setAttachments((ArrayList<Photo>) selectedPhotos);
                journal.setRoad(et_road.getText());
                journal.setAddr(et_address.getText());
                journal.setDescription(et_content.getText());
                journal.setWriterName(et_user.getText());

                JournalsService identificationService = new JournalsService(ReadOnlyJounalActivity2.this.getApplicationContext());
                identificationService.update(journal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<StringResult2>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(ReadOnlyJounalActivity2.this, "提交失败");
                            }

                            @Override
                            public void onNext(StringResult2 result) {
                                progressDialog.dismiss();
                                if (result.getCode() != 200) {
                                    ToastUtil.shortToast(ReadOnlyJounalActivity2.this, "提交失败");
                                } else {
                                    ToastUtil.shortToast(ReadOnlyJounalActivity2.this, "提交成功");
                                    finish();
                                }
                            }
                        });
            }
        });

        btn_edit.setVisibility(View.GONE);
    }


    private void setEditable(boolean editable) {
        take_photo_item.setAddPhotoEnable(editable);
        et_address.setEditable(editable);
        et_road.setEditable(editable);
        et_content.setEnableEdit(editable);
        textitem_component_type.setEditable(editable);
        if (editable) {
            findViewById(R.id.btn_upload_journal).setVisibility(View.VISIBLE);
            /**
             * 地图选择控件
             */
            tv_select_or_check_location.setText("重新选择部件");
            tv_select_or_check_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ReadOnlyJounalActivity2.this, SelectComponentOrAddressActivity.class);
                    intent.putExtra("geometry", new Point(journal.getX(), journal.getY()));
                    startActivity(intent);
                }
            });
            /**
             * 班组成员
             */
            if (!TextUtils.isEmpty(journal.getTeamMember())) {
                Map<String, Object> choices = new LinkedHashMap<>();
                List<TeamMember> members = JsonUtil.getObject(journal.getTeamMember(), new TypeToken<List<TeamMember>>() {
                }.getType());

                for (TeamMember teamMember : members) {
                    choices.put(teamMember.getUserName(), teamMember);
                }
                initTeamMember(choices);
            } else {
                multiselect_table_item.setVisibility(View.GONE);
            }
        } else {
            /**
             * 班组成员(不可编辑)
             */
            if (!TextUtils.isEmpty(journal.getTeamMember())) {
                List<String> choices = new ArrayList<>();
                List<TeamMember> members = JsonUtil.getObject(journal.getTeamMember(), new TypeToken<List<TeamMember>>() {
                }.getType());

                for (TeamMember teamMember : members) {
                    choices.add(teamMember.getUserName());
                }
                multiselect_table_item.setReadOnly(choices);
            } else {
                multiselect_table_item.setVisibility(View.GONE);
            }

            /**
             * 判断是否有部件
             */
            if (journal.getX() == 0 || journal.getY() == 0) {
                findViewById(R.id.rl_select_location).setVisibility(View.GONE);
                textitem_component_type.setVisibility(View.VISIBLE);
            } else {
                textitem_component_type.setText(journal.getLayerName());
                textitem_component_type.setReadOnly();
                tv_select_or_check_location.setText("在地图上查看位置");
                tv_select_or_check_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ReadOnlyJounalActivity2.this, SelectLocationActivity.class);
                        intent.putExtra(SelectLocationConstant.IF_READ_ONLY, true);
                        intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, new LatLng(Double.valueOf(journal.getY()),
                                Double.valueOf(journal.getX())));
                        intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, journal.getAddr());
                        startActivity(intent);
                    }
                });
            }
        }
    }


    /**
     * 获取班组成员
     */
    private void initTeamMember(final Map<String, Object> defaultSelectedItems) {
        GzpsService gzpsService = new GzpsService(this.getApplicationContext());
        gzpsService.getTeamMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OUser>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OUser> oUsers) {
                        if (ListUtil.isEmpty(oUsers)) {
                            //隐藏该项
                            multiselect_table_item.setVisibility(View.GONE);
                            //ll_member_total_container.setVisibility(View.GONE);
                            return;
                        }
                        Map<String, Object> items = new LinkedHashMap<String, Object>();
                        for (OUser oUser : oUsers) {
                            items.put(oUser.getUserName(), oUser);
                        }
                        //  Map<String,Object>  selectedItem = new HashMap<String, Object>(2);
                        //  selectedItem.put(new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getLoginName(),new LoginRouter(getApplicationContext(),AMDatabase.getInstance()).getUser().getLoginName());
                        multiselect_table_item.setMultiChoice(items, defaultSelectedItems);
                    }
                });
    }


    /**
     * 主要用于拍照、打开照片、地图浏览等返回Activity的刷新操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {

        if (take_photo_item != null) {
            take_photo_item.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(ReadOnlyJounalActivity2.this);
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent2 selectComponentFinishEvent) {

        if (selectComponentFinishEvent.getFindResult() != null) {
            Component component = selectComponentFinishEvent.getFindResult();
            journal.setLayerName(component.getLayerName());

            setObjectId(component);
            setXY(selectComponentFinishEvent);
            journal.setLayerUrl(selectComponentFinishEvent.getFindResult().getLayerUrl());

            String name = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));
            String type = LayerUrlConstant.getLayerNameByUnknownLayerUrl(component.getLayerUrl());
            String title = StringUtil.getNotNullString(name, "") + "  " + StringUtil.getNotNullString(type, "");
            tv_select_or_check_location.setText(title);
            if (TextUtils.isEmpty(component.getLayerName())) {
                textitem_component_type.setVisibility(View.GONE);
            } else {
                textitem_component_type.setText(component.getLayerName());
                textitem_component_type.setVisibility(View.VISIBLE);
            }

            /**
             * 如果包含井，那么显示水位字段进行选择
             */
            if (selectComponentFinishEvent.getFindResult().getLayerName().contains("井")) {
                rl_water_level.setVisibility(View.VISIBLE);
            } else {
                rl_water_level.setVisibility(View.GONE);
            }
        } else {
            /**
             * 如果没有选择部件，那么隐藏部件类型
             */
            textitem_component_type.setVisibility(View.GONE);
        }

        /**
         * 填充位置
         */
        if (selectComponentFinishEvent.getFindResult() != null && selectComponentFinishEvent.getFindResult().getGraphic() != null) {
            Map<String, Object> attributes = selectComponentFinishEvent.getFindResult().getGraphic().getAttributes();
            Object o = attributes.get(ComponentFieldKeyConstant.ADDR);
            journal.setAddr(o.toString());
            et_address.setText(o.toString());
            et_address.setVisibility(View.VISIBLE);
            et_road.setVisibility(View.GONE);
            // et_road.setText(o.toString());
            if (!LocationUtil.ifUnRegister()) {
                LocationUtil.unregister(ReadOnlyJounalActivity2.this);
            }
        } else {
            if (selectComponentFinishEvent.getDetailAddress() != null) {
                journal.setAddr(selectComponentFinishEvent.getDetailAddress().getDetailAddress());
                et_road.setText(selectComponentFinishEvent.getDetailAddress().getStreet());
                et_address.setText(selectComponentFinishEvent.getDetailAddress().getDetailAddress());
                et_road.setVisibility(View.VISIBLE);
                et_address.setVisibility(View.VISIBLE);
                if (!LocationUtil.ifUnRegister()) {
                    LocationUtil.unregister(ReadOnlyJounalActivity2.this);
                }
            }
        }
    }

    private void setObjectId(Component component) {
        Integer objectid = component.getObjectId();
        journal.setObjectId(String.valueOf(objectid));
    }

    private void setXY(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        if (selectComponentFinishEvent.getFindResult().getGraphic().getGeometry() instanceof Point) {
            Point point = (Point) selectComponentFinishEvent.getFindResult().getGraphic().getGeometry();
            journal.setX(point.getX());
            journal.setY(point.getY());
        }
    }
}
