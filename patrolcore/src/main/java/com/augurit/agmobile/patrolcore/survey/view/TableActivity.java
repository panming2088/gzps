package com.augurit.agmobile.patrolcore.survey.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.table.TableLoadListener;
import com.augurit.agmobile.patrolcore.common.table.TableViewManager;
import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.event.AddSaveRecordEvent;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.ControlType;
import com.augurit.agmobile.patrolcore.common.table.util.TableState;
import com.augurit.agmobile.patrolcore.survey.constant.ServerTableRecordConstant;
import com.augurit.agmobile.patrolcore.survey.model.BasicDanweiInfo;
import com.augurit.agmobile.patrolcore.survey.model.BasicDongInfo;
import com.augurit.agmobile.patrolcore.survey.model.BasicRenKouInfo;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.util.SurveyConstant;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ??????????????????????????????????????????
 *
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.patrolcore.survey
 * @createTime ???????????? ???17/9/2
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/9/2
 * @modifyMemo ???????????????
 */

public class TableActivity extends AppCompatActivity {

    public static String CUR_TAO_NUM = "cur_tao_num";
    public static String NEW_ADD_STATE = "new_add_state";
    public static final String IF_EDITABLE = "IF_EDITABLE";
    public static final String TABLE_NAME_KEY = "TABLE_NAME";

    protected ViewGroup mContentView;
    protected TableViewManager tableViewManager;

    protected static final int NEXTLINK_REQUEST_CODE = 0x111;
    protected int mPosition;
    protected String mTableId;
    protected ArrayList<TableItem> tableItems;
    protected boolean ifEditable;
    //  private String tableKey;
    protected String tableName;
    protected String tableId;
    protected String patrolcode;
    protected String recordId;
    protected String taskId;
    protected String renkouId;

    //????????????????????????????????????????????????????????????????????????
    protected String tableKey;
    protected String standardAddress; //??????????????????????????????????????????????????? = ??????
    protected String dirId; //??????id
    protected String parentRecordId;
    protected String parentRecordName;
    protected String parentRecordType;

    // protected MultiRecordTaskManager multiRecordTaskManager;
    protected ServerTableRecord serverTableRecord;
    protected boolean isSaved = false;

    protected String recordName; //??????????????????
    protected String address;
    protected String recordState;
    protected BasicRenKouInfo basicRenKouInfo;
    protected String dongName; //?????????
    protected String renkouleixing;
    protected BasicDanweiInfo basicDanweiInfo;
    protected String dongId;
    protected String taoId;
    protected String danweiId;
    protected String briefTaskName; //??????????????????
    protected String dataStataAfterUpload;
    protected String dataStataAfterDeleted;
    protected HashMap<String, ArrayList<String>> spinnerDataMap;   // ???????????????spinner?????? <field1, ?????????>
    protected HashMap<String, String> textDataMap;  // ???????????????TextView?????? <field1, ???>
    protected boolean isShowSaveButton = false;

    protected TextView tv_copy;
    protected int cur_tao_num; //???????????????????????????
    private String newAddState;
    private BasicDongInfo basicDongInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commontable);

        getBundleData();

        EventBus.getDefault().register(this);

        initView();

        loadTable();
    }

    protected void initView() {

        tv_copy = (TextView) findViewById(R.id.tv_copy);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(tableName);
        mContentView = (ViewGroup) findViewById(R.id.mainView);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (!ifEditable) { //???????????????????????????????????????
            findViewById(R.id.ll_upload).setVisibility(View.GONE);
        }
    }

    private void getBundleData() {
        /**
         * ????????????????????????tableId??????????????????TABLE_NAME???PARENT_RECORD_ID?????????????????????id??????PARENT_RECORD_TYPE????????????????????????????????????????????????????????????????????????????????????????????????
         */
        tableId = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_ID);
        tableName = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_NAME); //????????????
        patrolcode = getIntent().getStringExtra(SurveyConstant.BundleKey.PATROL_CODE); //????????????
        ifEditable = getIntent().getBooleanExtra(IF_EDITABLE, true);   //???????????????
        taskId = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_ID);
        standardAddress = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_NAME);//???????????? = ????????????
        tableKey = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_KEY);//tableKey???????????????
        dirId = getIntent().getStringExtra(SurveyConstant.BundleKey.DIR_ID);
        recordName = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_NAME);
        recordState = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_STATE); //??????????????????????????????????????????????????????

        tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.TABLE_ITEM);
        recordId = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_ID);
        parentRecordId = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_ID);
        parentRecordName = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_NAME);
        parentRecordType = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_TYPE);
        dongName = getIntent().getStringExtra(SurveyConstant.BundleKey.DONG_HAO);

        renkouleixing = getIntent().getStringExtra(SurveyConstant.BundleKey.REN_KOU_LEI_XING); //????????????????????????????????????????????????????????????
        //??????????????????
        basicDanweiInfo = (BasicDanweiInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.BASIC_DANWEI_INFO);

        //????????????????????????????????????
        basicDongInfo = (BasicDongInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.BASIC_DONG_INFO);

        spinnerDataMap = (HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.SPINNER_DATA_MAP);  // ?????????????????????spinner??????
        textDataMap = (HashMap<String, String>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.TEXT_DATA_MAP);  // ?????????????????????spinner??????

        ServerTableRecord serverTableRecord = (ServerTableRecord) getIntent().getSerializableExtra(SurveyConstant.BundleKey.RECORD);

        if (serverTableRecord != null) {
            tableId = serverTableRecord.getTableId();
            tableName = serverTableRecord.getProjectName();
            taskId = serverTableRecord.getTaskId();
            standardAddress = serverTableRecord.getStandardaddress(); //??????????????????TASK_NAME??????????????????????????????????????????????????????????????????????????????????????????????????????
            tableKey = serverTableRecord.getTableKey();
            recordState = serverTableRecord.getState();
            recordId = serverTableRecord.getRecordId();
            tableItems = serverTableRecord.getItems();
            recordName = serverTableRecord.getName();
            tableKey = serverTableRecord.getTableKey();
        }

        basicRenKouInfo = (BasicRenKouInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.RENKOU_INFO);

        /**********************************????????????????????????????????????**************************************************/

        //???id?????????????????????????????????????????????
        dongId = getIntent().getStringExtra(SurveyConstant.BundleKey.DONG_RECORD_ID);
        //???id,??????????????????????????????????????????????????????
        taoId = getIntent().getStringExtra(SurveyConstant.BundleKey.TAO_RECORD_ID);
        //?????????id??????????????????????????????????????????????????????????????????????????????????????????
        danweiId = getIntent().getStringExtra(SurveyConstant.BundleKey.DANWEI_ID);
        //?????????id??????????????????????????????????????????????????????
        renkouId = getIntent().getStringExtra(SurveyConstant.BundleKey.RENKOU_ID);
        //??????????????????????????????????????????????????????
        briefTaskName = getIntent().getStringExtra(SurveyConstant.BundleKey.BRIEF_TASK_NAME);
        /**********************************????????????????????????????????????**************************************************/


        /**
         * ?????????????????????????????????
         */
        dataStataAfterUpload = getIntent().getStringExtra(SurveyConstant.BundleKey.DATA_STATE_AFTER_UPLOAD);
        dataStataAfterDeleted = getIntent().getStringExtra(SurveyConstant.BundleKey.DATA_STATE_AFTER_DELETED);
        newAddState = getIntent().getStringExtra(NEW_ADD_STATE);


        cur_tao_num = getIntent().getIntExtra(CUR_TAO_NUM, 0);
    }


    /**
     * ???????????????
     */
    protected void loadTable() {

        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(this) + "am/report/rptform";
        List<Photo> copyList = new ArrayList<>();

        if (tableItems == null) {
            tableViewManager = new TableViewManager(this, mContentView, true, TableState.EDITING, getFormStructureUrl, tableId);
        } else {
            List<Photo> photoList = new ArrayList<>();

            for (TableItem tableItem : tableItems) {
                //?????????????????????url
                if (tableItem.getControl_type().equals(ControlType.IMAGE_PICKER)) {
                    if (!ServerTableRecordConstant.UNUPLOAD.equals(recordState)) {
                        String field1 = tableItem.getField1();
                        if (!TextUtils.isEmpty(tableItem.getValue())) {
                            // ???????????????new photoList ????????????????????????????????????????????????
                            String[] photoNames = tableItem.getValue().split("\\|");

                            for (String photoName : photoNames) {
                                //?????????????????????url
                                String photoUrl = BaseInfoManager.getBaseServerUrl(this) + "img/" + photoName;
                                Photo photo = new Photo();
                                photo.setPhotoPath(photoUrl);
                                photo.setField1(field1);
                                photo.setPhotoName(photoName);
                                photoList.add(photo);
                            }
                        }
                    }
                }

                //????????????item???????????????"????????????"/"????????????/????????????"??????????????????????????????????????????
                if (tableItem.getHtml_name().contains("????????????") || tableItem.getHtml_name().contains("????????????")
                        || tableItem.getHtml_name().contains("????????????")
                        || tableItem.getField1().equals("NOW_ADD")
                        || tableItem.getField1().equals("chn_add")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(standardAddress);
                    }
                }


                //????????????item?????????????????????/????????????????????????????????????????????????
                if (tableItem.getHtml_name().contains("??????") || tableItem.getHtml_name().contains("?????????") || tableItem.getField1().equals("MPFJH")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(parentRecordName);
                    }
                }

                //???????????????????????????
                if (tableItem.getHtml_name().contains("?????????")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        String loginName = new LoginService(this, AMDatabase.getInstance()).getUser().getLoginName();
                        tableItem.setValue(loginName);
                    }
                }


                //????????????
                if (tableItem.getHtml_name().equals("???????????????????????????") || tableItem.getHtml_name().contains("????????????")
                        || tableItem.getField1().equals("host_tel")
                        && basicRenKouInfo != null
                        && basicRenKouInfo.getHouseOwnerPhone() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwnerPhone());
                    }
                }


                //??????????????????
                if (tableItem.getHtml_name().contains("??????????????????")
                        && basicRenKouInfo != null
                        && basicRenKouInfo.getHouseOwnerIdentifyId() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwnerIdentifyId());
                    }
                }

                //????????????
                if (tableItem.getHtml_name().contains("????????????") || tableItem.getField1().equals("lod_type") && basicRenKouInfo != null && basicRenKouInfo.getHouseType() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseType());
                    }
                }

                //??????
                if (tableItem.getField1().equals("host") || tableItem.getField1().equals("wz_name")
                        && basicRenKouInfo != null && basicRenKouInfo.getHouseOwner() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwner());
                    }
                }


                //????????????
                if (tableItem.getHtml_name().contains("????????????")
                        && basicDanweiInfo != null && basicDanweiInfo.getDanweiName() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicDanweiInfo.getDanweiName());
                    }
                }


                if (tableItem.getHtml_name().contains("??????????????????") && basicDongInfo != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicDongInfo.getHouseType());
                    }
                }

                if (tableItem.getHtml_name().contains("????????????") && basicRenKouInfo != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getTelephone());
                    }
                }

                if (newAddState == null) { //????????????????????????????????????????????????recordId????????????????????????????????????????????????????????????id?????????????????????????????????
                    /**
                     * ????????????id????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     * ????????????????????????????????????????????????????????????????????????????????????id????????????????????????recordId??????????????????????????????????????????????????????d_id???????????????????????????????????????????????????recordId,
                     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     *
                     * ???????????????????????????id??????????????????????????????id??????????????????????????????????????????????????????
                     */
                    if (tableItem.getField1().toLowerCase().equals("id") && TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(recordId);
                    }
                }
            }
            if (ServerTableRecordConstant.UNUPLOAD.equals(recordState)) { //?????????????????????
                TableDataManager tableDataManager = new TableDataManager(this);
                TableDBService tableDBService = new TableDBService(this);
                LocalServerTableRecord2 localServerTableRecord2 = tableDBService.getLocalServerTableRecordByRecordIdAndTableKey(recordId, tableKey);
                if (localServerTableRecord2 != null) {
                    String localServerTableRecord2TableKey = localServerTableRecord2.getTableKey();
                    if (localServerTableRecord2TableKey != null) {
                        LocalTable localTable = tableDBService.getEditedTableItemsByTableKey(localServerTableRecord2TableKey);
                        if (localTable != null) {
                            photoList = tableDataManager.getPhotoFormDB(localTable.getKey());
                        }

                        //?????????????????????,????????????
                        if (newAddState != null) {
                            if (newAddState.equals("true")) {
                                //tableKey = null;
                                //recordId = System.currentTimeMillis() + "";
                                //localServerTableRecord2.setTableKey(null);
                                //localServerTableRecord2.setRecordId(System.currentTimeMillis() + "");

                                if (!ListUtil.isEmpty(photoList)) {

                                    for (Photo photo : photoList) {
                                        Photo copy = new Photo();
                                        copy.setLocalPath(photo.getLocalPath());
                                        copy.setPhotoPath(photo.getPhotoPath());
                                        copy.setPhotoName(photo.getPhotoName());
                                        copy.setPhotoTime(photo.getPhotoTime());
                                        copy.setField1(photo.getField1());
                                        copyList.add(copy);
                                    }
                                }
                            }
                        }

                    }
                }
            }

            //?????????????????????,????????????tableKey??????
            if (newAddState != null) {
                if (newAddState.equals("true")) {
                    tableKey = null;
                    recordId = System.currentTimeMillis() + "";
                }
            }

            if (ListUtil.isEmpty(copyList)) {
                tableViewManager = new TableViewManager(this, mContentView, false, TableState.REEDITNG, tableItems, photoList, tableId, tableKey, null);
            } else {
                tableViewManager = new TableViewManager(this, mContentView, false, TableState.REEDITNG, tableItems, copyList, tableId, tableKey, null);
            }
        }


        if (!TextUtils.isEmpty(recordId)) {
            tableViewManager.setRecordId(recordId);
        }

        if (!TextUtils.isEmpty(taskId)) {
            tableViewManager.setTaskId(taskId);
        }


        if (!TextUtils.isEmpty(patrolcode)) {
            tableViewManager.setPatrolCode(patrolcode);
        }

        if (!TextUtils.isEmpty(standardAddress)) {
            tableViewManager.setAddressName(standardAddress);//??????????????????????????????????????????????????? = ??????
        }

        if (!TextUtils.isEmpty(parentRecordName)) {
            tableViewManager.setRoomName(parentRecordName);//?????????/??????
        }

        if (!TextUtils.isEmpty(parentRecordId)) {
            tableViewManager.setParentRecordId(parentRecordId);
        }

        if (!TextUtils.isEmpty(parentRecordType)) {
            tableViewManager.setParentRecordType(parentRecordType);
        }

        if (basicRenKouInfo != null) {
            tableViewManager.setBasicRenKouInfo(basicRenKouInfo);
        }

        if (tableName != null) {
            tableViewManager.setShiyouDanweiTableName(tableName);
        }

        if (dongName != null) {
            tableViewManager.setDongName(dongName);
        }

        if (renkouleixing != null) {
            tableViewManager.setRenkouleibei(renkouleixing);
        }

        if (basicDanweiInfo != null) {
            tableViewManager.setBasicDanweiInfo(basicDanweiInfo);
        }

        if (dataStataAfterUpload != null) {
            tableViewManager.setDataStateAfterUpload(dataStataAfterUpload); //
        }

        if (dataStataAfterDeleted != null) {
            tableViewManager.setDataStateAfterDeleted(dataStataAfterDeleted);
        }

        if (basicDongInfo != null){
            tableViewManager.setBasicDongInfo(basicDongInfo);
        }

        // spinner??????????????????????????????text????????? (?????????????????????????????????)
        tableViewManager.addLoadListener(new TableLoadListener() {
            @Override
            public void onFinishedLoad() {
                // spinner
                if (spinnerDataMap != null) {
                    Map<String, View> map = tableViewManager.getMap();
                    for (final Map.Entry<String, ArrayList<String>> entry : spinnerDataMap.entrySet()) {
                        TableItem tableItem = tableViewManager.getTableItemByField1(entry.getKey());
                        if (tableItem == null) continue;
                        View view = map.get(tableItem.getId());
                        if (view != null) {
                            // ???spinner????????????
                            final EditText editText = (EditText) view.findViewById(R.id.et_);
                            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TableActivity.this,
                                    android.R.layout.simple_spinner_item, entry.getValue());
                            adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
                            spinner.setAdapter(adapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (editText != null) {
                                        if (position == 0) {
                                            editText.setText("");
                                        } else {
                                            editText.setText(entry.getValue().get(position));
                                        }
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            // ???????????????
                            String value = tableItem.getValue();
                            if (!TextUtils.isEmpty(value)) {
                                if (tableItem.getField1().equals("grid_name")) {
                                    // ????????????
                                    spinner.setVisibility(View.GONE);
                                    editText.setText(value);
                                    editText.setEnabled(false);
                                } else if (entry.getValue().contains(value)) {
                                    spinner.setSelection(entry.getValue().indexOf(value));
                                }
                            }
                        }
                    }
                }
                // Text
                if (textDataMap != null) {
                    Map<String, View> map = tableViewManager.getMap();
                    for (Map.Entry<String, String> entry : textDataMap.entrySet()) {
                        TableItem tableItem = tableViewManager.getTableItemByField1(entry.getKey());
                        if (tableItem == null) continue;
                        View view = map.get(tableItem.getId());
                        if (view != null) {
                            EditText editText = (EditText) view.findViewById(R.id.et_);
                            editText.setText(entry.getValue());
                        }
                    }
                }
            }
        });

        //??????
        upload();

        save();

        delete();

    }

    private void upload() {
        findViewById(R.id.ll_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadSingleRecord();
            }
        });
    }


    /**
     * ??????????????????
     */
    private void uploadSingleRecord() {
        tableViewManager.uploadEditMultiWithUserName(new Callback1<Boolean>() {
            @Override
            public void onCallback(Boolean aBoolean) {

                EventBus.getDefault().post(new RefreshListEvent(null)); //????????????????????????????????????????????????????????????

                if (recordId != null) {
                    TableDBService tableDBService = new TableDBService(TableActivity.this);
                    tableDBService.deleteLocalServerTableRecordByRecordIdAndTableKey(recordId, tableKey);
                }

                exitActivity();
            }
        });
    }

    /**
     * ??????????????????
     */
    private void delete() {
        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tableViewManager.deleteMultiWithUserName(new Callback1<Boolean>() {
                    @Override
                    public void onCallback(Boolean aBoolean) {

                        EventBus.getDefault().post(new RefreshListEvent(null)); //????????????????????????????????????????????????????????????

                        if (recordId != null) {
                            TableDBService tableDBService = new TableDBService(TableActivity.this);
                            tableDBService.deleteLocalServerTableRecordByRecordId(recordId);
                        }
                        //todo ???????????????????????????????????????????????????????????????????????????

                        exitActivity();
                    }
                });
            }
        });
    }

    /**
     * ????????????
     */
    private void save() {
        //??????
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isSaved = true;

                //????????????????????????????????????valueMap????????????
                //if (recordName == null) {

                if (TextUtils.isEmpty(address)) {
                    address = tableViewManager.getValueMap().get("name");//????????????????????????????????????????????????????????????
                }

                /**
                 * ???????????????????????????????????????; ??????"?????????"?????????????????????????????????????????????(field1?????????)???????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                if (TextUtils.isEmpty(address) && dongId != null) {
                    address = tableViewManager.getValueMap().get("fjh");
                }

                if (TextUtils.isEmpty(address)) {
                    //???????????????????????????
                    address = tableViewManager.getValueMap().get("ldh");//???????????????????????????????????????
                }

                if (TextUtils.isEmpty(address)) {
                    address = tableViewManager.getValueMap().get("sjsydw");//??????????????????
                }

                //jumpToBuildingListActvity();
                if (tableKey == null) {
                    //??????????????????
                    tableViewManager.saveEdited2(recordId);
                } else {
                    //??????????????????
                    tableViewManager.saveEdited(tableKey);
                }

                EventBus.getDefault().post(new RefreshListEvent(null));//????????????????????????,??????????????????????????????
            }
        });
    }

    protected void exitActivity() {
        finish();
    }


    /**
     * ?????????????????????????????????????????????????????????Activity???????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == NEXTLINK_REQUEST_CODE) {
            if (resultCode == 0x111) {
                finish();
            }
            return;
        }
        tableViewManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        /*if (multiRecordTaskManager != null) {
            multiRecordTaskManager.destory();
        }*/
    }

    /**
     * ????????????????????????
     * ???????????????????????????????????????????????????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddSaveRecordEvent(AddSaveRecordEvent event) {


        final String tableKeyTmp = event.getLocalTaskRecord().getKey();
        if (tableKeyTmp == null) return;

        //????????????????????????????????????????????????
        //   serverTableRecord.setTableKey(tableKeyTmp);

        final LocalServerTableRecord2 localServerTableRecord2 = new LocalServerTableRecord2();
        localServerTableRecord2.setTableKey(tableKeyTmp);
        if (briefTaskName != null) {
            localServerTableRecord2.setTaskName(briefTaskName); //xcl 9.16 ????????????taskName???taskName???????????????????????????????????????????????????????????????????????????????????????
        } else {
            localServerTableRecord2.setTaskName(standardAddress);
        }

        localServerTableRecord2.setStandardAddress(standardAddress);

        localServerTableRecord2.setTableId(tableId);
        localServerTableRecord2.setTaskId(taskId);
        if (dongId != null) {
            localServerTableRecord2.setDongId(dongId);
        }

        if (taoId != null) {
            localServerTableRecord2.setTaoId(taoId);
        }

        if (danweiId != null) {
            localServerTableRecord2.setDanweiId(danweiId);
        }

        if (renkouId != null) {
            localServerTableRecord2.setRenkouId(renkouId);
        }

        localServerTableRecord2.setLastModifyTime(System.currentTimeMillis()); //????????????????????????

        TableDBService tableDBService = new TableDBService(this);

        localServerTableRecord2.setName(address);//2017-09-07 xcl ?????????????????????,?????????????????????????????????


        if (recordId != null) {
            localServerTableRecord2.setRecordId(recordId);
        } else {
            recordId = String.valueOf(System.currentTimeMillis());
            localServerTableRecord2.setRecordId(recordId);
        }


        tableDBService.saveLocalServerTableRecord(localServerTableRecord2, tableKey, new TableDBCallback() {
            @Override
            public void onSuccess(Object data) {
                if (isSaved) {

                    Toast.makeText(TableActivity.this, "????????????!", Toast.LENGTH_LONG).show();
                    //EventBus.getDefault().post(new RefreshListEvent()); //?????????????????????????????????????????????????????????????????????

                    /***********xcl 2017.9.6 ??????????????????????????????????????????************/


                    if (recordName == null) {

                        EventBus.getDefault().post(new AddRecordEvent(localServerTableRecord2.getName(), localServerTableRecord2.getRecordId(), tableId)); //????????????????????????
                    } else {
                        EventBus.getDefault().post(new RefreshListEvent(tableKeyTmp));//????????????????????????
                    }
                    /***********??????????????????????????????????????????************************/
                    finish();

                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(TableActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
