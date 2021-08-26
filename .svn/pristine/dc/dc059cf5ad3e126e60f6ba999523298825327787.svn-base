package com.augurit.agmobile.gzps.journal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import com.augurit.agmobile.patrolcore.baiduapi.BaiduApiService;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.am.cmpt.loc.util.LocationUtil;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;

import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

public class WriteJournalActivity2 extends BaseActivity {

    private TextItemTableItem et_address;
    private TextItemTableItem et_road;
    private TextFieldTableItem et_content;
    private TextItemTableItem et_parent_organization;
    private TextItemTableItem et_direct_organization;
    private TextItemTableItem et_team;
    private TextItemTableItem textitem_component_type;
    private View rl_water_level;
    private TextView tv_select_or_check_location;

    private TextItemTableItem et_user;
    private TextItemTableItem et_date;

    private Journal journal;

    private View btn_upload_journal;
    private MultiTakePhotoTableItem take_photo_item;
    //    private LinearLayout ll_select_component;
//    private FlexboxLayout ll_team_member;
//    private View ll_member_total_container;
    private MultiSelectTableItlem selectTableItlem;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal2);

        journal = new Journal();

        initView();

        initUser();

        initCurrentTime();

        //startLocate();

        EventBus.getDefault().register(this);
    }

    protected void initView() {
        take_photo_item = (MultiTakePhotoTableItem) findViewById(R.id.take_photo_item);
        take_photo_item.setPhotoExampleEnable(false);

        ((TextView) findViewById(R.id.tv_title)).setText("填写日志");
        et_address = (TextItemTableItem) findViewById(R.id.textitem_address);
        et_road = (TextItemTableItem) findViewById(R.id.textitem_road);
        et_content = (TextFieldTableItem) findViewById(R.id.textfield_content);
        et_parent_organization = (TextItemTableItem) findViewById(R.id.textitem_parent_organization);
        et_direct_organization = (TextItemTableItem) findViewById(R.id.textitem_direct_organization);
        et_team = (TextItemTableItem) findViewById(R.id.textitem_team);
        et_user = (TextItemTableItem) findViewById(R.id.textitem_user);
        et_date = (TextItemTableItem) findViewById(R.id.textitem_date);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_edit).setVisibility(View.GONE);

        et_user.setVisibility(View.GONE);
        et_date.setVisibility(View.GONE);

        /**
         * 提交
         */
        btn_upload_journal = findViewById(R.id.btn_upload_journal);
        btn_upload_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> selectedItems = selectTableItlem.getSelectedItems();
                if (MapUtils.isEmpty(selectedItems)) {
                    ToastUtil.shortToast(getApplicationContext(), "请选择班组成员");
                    return;
                }
                List<Photo> selectedPhotos = take_photo_item.getSelectedPhotos();

                if (TextUtils.isEmpty(et_address.getText())){
                    ToastUtil.shortToast(getApplicationContext(), "请选择部件或者位置");
                    return;
                }
//                if (ListUtil.isEmpty(selectedPhotos)){
//                    ToastUtil.shortToast(getApplicationContext(), "至少上传一张照片");
//                    return;
//                }

                final ProgressDialog progressDialog = new ProgressDialog(WriteJournalActivity2.this);
                progressDialog.setMessage("正在提交，请等待");
                progressDialog.show();


                journal.setAttachments((ArrayList<Photo>) selectedPhotos);

                journal.setRoad(et_road.getText());
                journal.setAddr(et_address.getText());
                journal.setDescription(et_content.getText());
                journal.setWriterName(et_user.getText());
                journal.setRecordTime(System.currentTimeMillis());

                Set<Map.Entry<String, Object>> entries = selectedItems.entrySet();
                List<TeamMember> teamMembers = new ArrayList<TeamMember>();
                for (Map.Entry<String, Object> entry : entries) {
                    OUser oUser = (OUser) entry.getValue();
                    TeamMember teamMember = new TeamMember();
                    teamMember.setLoginName(oUser.getLoginName());
                    teamMember.setUserName(oUser.getUserName());
                    teamMembers.add(teamMember);
                }

                String json = JsonUtil.getJson(teamMembers);
                journal.setTeamMember(json);

                JournalsService identificationService = new JournalsService(WriteJournalActivity2.this.getApplicationContext());
                identificationService.add(journal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<StringResult2>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                progressDialog.dismiss();
                                ToastUtil.shortToast(WriteJournalActivity2.this, "提交失败");
                            }

                            @Override
                            public void onNext(StringResult2 result) {
                                progressDialog.dismiss();
                                if (result.getCode() != 200) {
                                    ToastUtil.shortToast(WriteJournalActivity2.this, "提交失败");
                                } else {
                                    ToastUtil.shortToast(WriteJournalActivity2.this, "提交成功");
                                    finish();
                                }
                            }
                        });
            }
        });

        /**
         * 选择部件
         */
        findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteJournalActivity2.this, SelectComponentOrAddressActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 部件类型
         */
        textitem_component_type = (TextItemTableItem) findViewById(R.id.textitem_component_type);
        /**
         * 水深
         */
        rl_water_level = findViewById(R.id.rl_water_level);
        FlexRadioGroup rl_water_level = (FlexRadioGroup) findViewById(R.id.rg_water_level);
        rl_water_level.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_full:
                        journal.setWaterLevel("满管");
                        break;
                    case R.id.rb_3_4:
                        journal.setWaterLevel("3/4管");
                        break;
                    case R.id.rb_2_4:
                        journal.setWaterLevel("2/4管");
                        break;
                    case R.id.rb_1_4:
                        journal.setWaterLevel("1/4管");
                        break;
                }
            }
        });

        tv_select_or_check_location = (TextView) findViewById(R.id.tv_select_or_check_location);

        /**
         * 班组成员
         */
        selectTableItlem = (MultiSelectTableItlem) findViewById(R.id.multiselect_table_item);
//        ll_team_member = (FlexboxLayout) findViewById(R.id.fl_team_memeber);
//        ll_member_total_container = findViewById(R.id.ll_member_total_container);
        initTeamMember();
    }

    /**
     * 获取班组成员
     */
    private void initTeamMember() {
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
                            selectTableItlem.setVisibility(View.GONE);
                            //ll_member_total_container.setVisibility(View.GONE);
                            return;
                        }
                        Map<String, Object> items = new LinkedHashMap<String, Object>();
                        for (OUser oUser : oUsers) {
                            items.put(oUser.getUserName(), oUser);
                        }
                        Map<String, Object> selectedItem = new HashMap<String, Object>(2);
                        selectedItem.put(new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName(), new LoginRouter(getApplicationContext(), AMDatabase.getInstance()).getUser().getUserName());
                        selectTableItlem.setMultiChoice(items, selectedItem);
                    }
                });
    }


    private void initUser() {
        User user = new LoginService(this, AMDatabase.getInstance()).getUser();
        et_user.setText(user.getUserName());
        journal.setWriterName(user.getUserName());
        journal.setWriterId(user.getId());
    }

    /**
     * 自动填入当前时间
     */
    private void initCurrentTime() {
//        long currentTimeMillis = System.currentTimeMillis();
//        journal.setRecordTime(currentTimeMillis);
//        et_date.setText(TimeUtil.getStringTimeYMDMChines(new Date(currentTimeMillis)));
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


    private void startLocate() {
        LocationUtil.register(this, 1000, 0, new LocationUtil.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {

            }

            @Override
            public void onLocationChanged(Location location) {
                journal.setX(location.getLongitude());
                journal.setY(location.getLatitude());
                LocationUtil.unregister(WriteJournalActivity2.this);
                requestAddress(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }

    private void requestAddress(Location location) {
        BaiduApiService baiduApiService = new BaiduApiService(this);
        baiduApiService.parseLocation(new LatLng(location.getLatitude(), location.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaiduGeocodeResult>() {
                    @Override
                    public void call(BaiduGeocodeResult baiduGeocodeResult) {
                        journal.setAddr(baiduGeocodeResult.getDetailAddress());
                        et_address.setVisibility(View.VISIBLE);
                        et_address.setText(baiduGeocodeResult.getDetailAddress());
                        et_road.setVisibility(View.VISIBLE);
                        et_road.setText(baiduGeocodeResult.getResult().getAddressComponent().getStreet());
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!LocationUtil.ifUnRegister()) {
            LocationUtil.unregister(WriteJournalActivity2.this);
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
            textitem_component_type.setText(component.getLayerName());
            textitem_component_type.setVisibility(View.VISIBLE);
            /**
             * 如果包含井，那么显示水位字段进行选择
             */
            if (selectComponentFinishEvent.getFindResult().getLayerName().contains("井")) {
                rl_water_level.setVisibility(View.VISIBLE);
            }else {
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
                LocationUtil.unregister(WriteJournalActivity2.this);
            }
        } else {
            if (selectComponentFinishEvent.getDetailAddress() != null) {
                journal.setAddr(selectComponentFinishEvent.getDetailAddress().getDetailAddress());
                journal.setX(selectComponentFinishEvent.getDetailAddress().getX());
                journal.setY(selectComponentFinishEvent.getDetailAddress().getY());
                et_road.setText(selectComponentFinishEvent.getDetailAddress().getStreet());
                et_address.setText(selectComponentFinishEvent.getDetailAddress().getDetailAddress());
                et_road.setVisibility(View.VISIBLE);
                et_address.setVisibility(View.VISIBLE);
                if (!LocationUtil.ifUnRegister()) {
                    LocationUtil.unregister(WriteJournalActivity2.this);
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
