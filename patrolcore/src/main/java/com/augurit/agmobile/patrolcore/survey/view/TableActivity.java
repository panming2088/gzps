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
 * 通用表单界面（四标四实专用）
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey
 * @createTime 创建时间 ：17/9/2
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/2
 * @modifyMemo 修改备注：
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

    //本地保存再次编辑需要该键来保存更新到本地是同一个
    protected String tableKey;
    protected String standardAddress; //任务名称，在番禺四标四实项目中任务 = 地址
    protected String dirId; //目录id
    protected String parentRecordId;
    protected String parentRecordName;
    protected String parentRecordType;

    // protected MultiRecordTaskManager multiRecordTaskManager;
    protected ServerTableRecord serverTableRecord;
    protected boolean isSaved = false;

    protected String recordName; //当前记录名称
    protected String address;
    protected String recordState;
    protected BasicRenKouInfo basicRenKouInfo;
    protected String dongName; //楼栋号
    protected String renkouleixing;
    protected BasicDanweiInfo basicDanweiInfo;
    protected String dongId;
    protected String taoId;
    protected String danweiId;
    protected String briefTaskName; //简要任务名称
    protected String dataStataAfterUpload;
    protected String dataStataAfterDeleted;
    protected HashMap<String, ArrayList<String>> spinnerDataMap;   // 主动设置的spinner数据 <field1, 下拉值>
    protected HashMap<String, String> textDataMap;  // 主动设置的TextView数据 <field1, 值>
    protected boolean isShowSaveButton = false;

    protected TextView tv_copy;
    protected int cur_tao_num; //同栋中当前套的数量
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


        if (!ifEditable) { //不可编辑时，不显示上传按钮
            findViewById(R.id.ll_upload).setVisibility(View.GONE);
        }
    }

    private void getBundleData() {
        /**
         * 当新增的时候传递tableId，标准地址，TABLE_NAME，PARENT_RECORD_ID（上一级记录的id），PARENT_RECORD_TYPE（上一级记录的类型，是栋，套，还是单位）以及其他要自动填充的数据
         */
        tableId = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_ID);
        tableName = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_NAME); //表单名称
        patrolcode = getIntent().getStringExtra(SurveyConstant.BundleKey.PATROL_CODE); //工单编号
        ifEditable = getIntent().getBooleanExtra(IF_EDITABLE, true);   //是否可编辑
        taskId = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_ID);
        standardAddress = getIntent().getStringExtra(SurveyConstant.BundleKey.TASK_NAME);//任务名称 = 标准地址
        tableKey = getIntent().getStringExtra(SurveyConstant.BundleKey.TABLE_KEY);//tableKey，用于本地
        dirId = getIntent().getStringExtra(SurveyConstant.BundleKey.DIR_ID);
        recordName = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_NAME);
        recordState = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_STATE); //记录状态，有：待提交、待审核、已提交

        tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.TABLE_ITEM);
        recordId = getIntent().getStringExtra(SurveyConstant.BundleKey.RECORD_ID);
        parentRecordId = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_ID);
        parentRecordName = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_NAME);
        parentRecordType = getIntent().getStringExtra(SurveyConstant.BundleKey.PARENT_RECORD_TYPE);
        dongName = getIntent().getStringExtra(SurveyConstant.BundleKey.DONG_HAO);

        renkouleixing = getIntent().getStringExtra(SurveyConstant.BundleKey.REN_KOU_LEI_XING); //人口类型，用于学生信息表和从业人员信息表
        //单位基本信息
        basicDanweiInfo = (BasicDanweiInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.BASIC_DANWEI_INFO);

        //基本栋信息（给套表使用）
        basicDongInfo = (BasicDongInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.BASIC_DONG_INFO);

        spinnerDataMap = (HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.SPINNER_DATA_MAP);  // 不从字典获取的spinner数据
        textDataMap = (HashMap<String, String>) getIntent().getSerializableExtra(SurveyConstant.BundleKey.TEXT_DATA_MAP);  // 不从字典获取的spinner数据

        ServerTableRecord serverTableRecord = (ServerTableRecord) getIntent().getSerializableExtra(SurveyConstant.BundleKey.RECORD);

        if (serverTableRecord != null) {
            tableId = serverTableRecord.getTableId();
            tableName = serverTableRecord.getProjectName();
            taskId = serverTableRecord.getTaskId();
            standardAddress = serverTableRecord.getStandardaddress(); //优先采用通过TASK_NAME传递过来的任务名称，因为记录中的任务名称有可能是简要的，而不是完整的
            tableKey = serverTableRecord.getTableKey();
            recordState = serverTableRecord.getState();
            recordId = serverTableRecord.getRecordId();
            tableItems = serverTableRecord.getItems();
            recordName = serverTableRecord.getName();
            tableKey = serverTableRecord.getTableKey();
        }

        basicRenKouInfo = (BasicRenKouInfo) getIntent().getSerializableExtra(SurveyConstant.BundleKey.RENKOU_INFO);

        /**********************************本地保存时需要传入的参数**************************************************/

        //栋id，当新增或者修改套时，需要传入
        dongId = getIntent().getStringExtra(SurveyConstant.BundleKey.DONG_RECORD_ID);
        //套id,当新增或者修改单位和人口时，需要传入
        taoId = getIntent().getStringExtra(SurveyConstant.BundleKey.TAO_RECORD_ID);
        //单位的id，当新增或者修改流动人口，境外人口，从业人员和学生时需要传入
        danweiId = getIntent().getStringExtra(SurveyConstant.BundleKey.DANWEI_ID);
        //人口的id，当修改流动人口，境外人口时需要传入
        renkouId = getIntent().getStringExtra(SurveyConstant.BundleKey.RENKOU_ID);
        //简要任务名称，当新增栋的时候需要传入
        briefTaskName = getIntent().getStringExtra(SurveyConstant.BundleKey.BRIEF_TASK_NAME);
        /**********************************本地保存时需要传入的参数**************************************************/


        /**
         * 提交给服务端的数据状态
         */
        dataStataAfterUpload = getIntent().getStringExtra(SurveyConstant.BundleKey.DATA_STATE_AFTER_UPLOAD);
        dataStataAfterDeleted = getIntent().getStringExtra(SurveyConstant.BundleKey.DATA_STATE_AFTER_DELETED);
        newAddState = getIntent().getStringExtra(NEW_ADD_STATE);


        cur_tao_num = getIntent().getIntExtra(CUR_TAO_NUM, 0);
    }


    /**
     * 加载表格项
     */
    protected void loadTable() {

        String getFormStructureUrl = BaseInfoManager.getBaseServerUrl(this) + "am/report/rptform";
        List<Photo> copyList = new ArrayList<>();

        if (tableItems == null) {
            tableViewManager = new TableViewManager(this, mContentView, true, TableState.EDITING, getFormStructureUrl, tableId);
        } else {
            List<Photo> photoList = new ArrayList<>();

            for (TableItem tableItem : tableItems) {
                //进行拼接图片的url
                if (tableItem.getControl_type().equals(ControlType.IMAGE_PICKER)) {
                    if (!ServerTableRecordConstant.UNUPLOAD.equals(recordState)) {
                        String field1 = tableItem.getField1();
                        if (!TextUtils.isEmpty(tableItem.getValue())) {
                            // 如果在这里new photoList 会导致多图片控件只显示一个的图片
                            String[] photoNames = tableItem.getValue().split("\\|");

                            for (String photoName : photoNames) {
                                //进行拼接完整的url
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

                //判断当前item中是否包含"居住地址"/"标准地址/在华住址"等字样，如果是，自动填充地址
                if (tableItem.getHtml_name().contains("居住地址") || tableItem.getHtml_name().contains("标准地址")
                        || tableItem.getHtml_name().contains("在华住址")
                        || tableItem.getField1().equals("NOW_ADD")
                        || tableItem.getField1().equals("chn_add")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(standardAddress);
                    }
                }


                //判断当前item中是否包含门牌/房间号，如果是，自动填充记录名称
                if (tableItem.getHtml_name().contains("门牌") || tableItem.getHtml_name().contains("房间号") || tableItem.getField1().equals("MPFJH")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(parentRecordName);
                    }
                }

                //判断是否包含填表人
                if (tableItem.getHtml_name().contains("填表人")) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        String loginName = new LoginService(this, AMDatabase.getInstance()).getUser().getLoginName();
                        tableItem.setValue(loginName);
                    }
                }


                //屋主电话
                if (tableItem.getHtml_name().equals("屋主（代理人）电话") || tableItem.getHtml_name().contains("屋主电话")
                        || tableItem.getField1().equals("host_tel")
                        && basicRenKouInfo != null
                        && basicRenKouInfo.getHouseOwnerPhone() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwnerPhone());
                    }
                }


                //留宿人证件号
                if (tableItem.getHtml_name().contains("留宿人证件号")
                        && basicRenKouInfo != null
                        && basicRenKouInfo.getHouseOwnerIdentifyId() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwnerIdentifyId());
                    }
                }

                //住所类型
                if (tableItem.getHtml_name().contains("住所类型") || tableItem.getField1().equals("lod_type") && basicRenKouInfo != null && basicRenKouInfo.getHouseType() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseType());
                    }
                }

                //屋主
                if (tableItem.getField1().equals("host") || tableItem.getField1().equals("wz_name")
                        && basicRenKouInfo != null && basicRenKouInfo.getHouseOwner() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getHouseOwner());
                    }
                }


                //学校名称
                if (tableItem.getHtml_name().contains("学校名称")
                        && basicDanweiInfo != null && basicDanweiInfo.getDanweiName() != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicDanweiInfo.getDanweiName());
                    }
                }


                if (tableItem.getHtml_name().contains("房屋使用性质") && basicDongInfo != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicDongInfo.getHouseType());
                    }
                }

                if (tableItem.getHtml_name().contains("联系电话") && basicRenKouInfo != null) {
                    if (TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(basicRenKouInfo.getTelephone());
                    }
                }

                if (newAddState == null) { //在复制新增的时候不能也会传递过来recordId，但是此时不能使用，否则当提交的时候由于id相同，会产生覆盖的结果
                    /**
                     * 手动设置id的原因：比如本地保存了一条栋记录以及该栋下面的一条套记录，此时，用户首先上传了套记录，然后再上传栋记录，此时由于栋记录是我们在本地的时候新增的，
                     * 服务端没有，因此服务端会将这条记录插入，并且自动生成一个id（注：我们上传的recordId服务端并没有使用），但是此时套记录的d_id里面绑定的还是我们在本地保存时栋的recordId,
                     * 由此造成的结果就是栋和套没有关联起来。下次点击栋的时候是看不到这条套记录的。
                     *
                     * 所以我们要手动设置id，不让服务端自动生成id，造成本地新增后上传无法关联的问题。
                     */
                    if (tableItem.getField1().toLowerCase().equals("id") && TextUtils.isEmpty(tableItem.getValue())) {
                        tableItem.setValue(recordId);
                    }
                }
            }
            if (ServerTableRecordConstant.UNUPLOAD.equals(recordState)) { //本地保存状态下
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

                        //复制新建的时候,复制图片
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

            //复制新建的时候,把原来的tableKey置空
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
            tableViewManager.setAddressName(standardAddress);//任务名称，在番禺四标四实项目中任务 = 地址
        }

        if (!TextUtils.isEmpty(parentRecordName)) {
            tableViewManager.setRoomName(parentRecordName);//房间号/门牌
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

        // spinner设置字典中没有的值、text设置值 (目前只有标准地址表用到)
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
                            // 给spinner重新设值
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
                            // 选中已有值
                            String value = tableItem.getValue();
                            if (!TextUtils.isEmpty(value)) {
                                if (tableItem.getField1().equals("grid_name")) {
                                    // 网格名称
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

        //上传
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
     * 上传单条记录
     */
    private void uploadSingleRecord() {
        tableViewManager.uploadEditMultiWithUserName(new Callback1<Boolean>() {
            @Override
            public void onCallback(Boolean aBoolean) {

                EventBus.getDefault().post(new RefreshListEvent(null)); //提交了，数据发生了变动，通知界面进行更新

                if (recordId != null) {
                    TableDBService tableDBService = new TableDBService(TableActivity.this);
                    tableDBService.deleteLocalServerTableRecordByRecordIdAndTableKey(recordId, tableKey);
                }

                exitActivity();
            }
        });
    }

    /**
     * 删除该条记录
     */
    private void delete() {
        findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tableViewManager.deleteMultiWithUserName(new Callback1<Boolean>() {
                    @Override
                    public void onCallback(Boolean aBoolean) {

                        EventBus.getDefault().post(new RefreshListEvent(null)); //提交了，数据发生了变动，通知界面进行更新

                        if (recordId != null) {
                            TableDBService tableDBService = new TableDBService(TableActivity.this);
                            tableDBService.deleteLocalServerTableRecordByRecordId(recordId);
                        }
                        //todo 离线下载下来的数据应该被更新，状态也应该变成已提交

                        exitActivity();
                    }
                });
            }
        });
    }

    /**
     * 本地保存
     */
    private void save() {
        //保存
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isSaved = true;

                //不能放在保存成功后，因为valueMap会被清空
                //if (recordName == null) {

                if (TextUtils.isEmpty(address)) {
                    address = tableViewManager.getValueMap().get("name");//说明是单位或者人口表，将名称作为记录名称
                }

                /**
                 * 如果是房屋套，那么是房间号; 由于"房间号"人口表和单位表里面也有这个字段(field1也相同)，所以为了避免在人口表或者单位表中拿房间号做名称，加多一个条件限制
                 */
                if (TextUtils.isEmpty(address) && dongId != null) {
                    address = tableViewManager.getValueMap().get("fjh");
                }

                if (TextUtils.isEmpty(address)) {
                    //将姓名作为记录名称
                    address = tableViewManager.getValueMap().get("ldh");//如果是房屋栋，那么是楼栋号
                }

                if (TextUtils.isEmpty(address)) {
                    address = tableViewManager.getValueMap().get("sjsydw");//如果是消防表
                }

                //jumpToBuildingListActvity();
                if (tableKey == null) {
                    //初次编辑保存
                    tableViewManager.saveEdited2(recordId);
                } else {
                    //再次编辑保存
                    tableViewManager.saveEdited(tableKey);
                }

                EventBus.getDefault().post(new RefreshListEvent(null));//发送刷新列表事件,使用『实际使用单位』
            }
        });
    }

    protected void exitActivity() {
        finish();
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
     * 响应本地保存记录
     * 因为网络问题而上传不了的记录内容先保存下来
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAddSaveRecordEvent(AddSaveRecordEvent event) {


        final String tableKeyTmp = event.getLocalTaskRecord().getKey();
        if (tableKeyTmp == null) return;

        //为原先的数据数据绑定本地存储主键
        //   serverTableRecord.setTableKey(tableKeyTmp);

        final LocalServerTableRecord2 localServerTableRecord2 = new LocalServerTableRecord2();
        localServerTableRecord2.setTableKey(tableKeyTmp);
        if (briefTaskName != null) {
            localServerTableRecord2.setTaskName(briefTaskName); //xcl 9.16 不要使用taskName，taskName是标准地址（完整地址），而在显示记录的时候采用的是简要地址
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

        localServerTableRecord2.setLastModifyTime(System.currentTimeMillis()); //保存本次修改时间

        TableDBService tableDBService = new TableDBService(this);

        localServerTableRecord2.setName(address);//2017-09-07 xcl 只要是本地保存,统一取最新数据作为名称


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

                    Toast.makeText(TableActivity.this, "保存成功!", Toast.LENGTH_LONG).show();
                    //EventBus.getDefault().post(new RefreshListEvent()); //编辑后保存了，数据发生了变动，通知界面进行更新

                    /***********xcl 2017.9.6 发送事件通知列表进行更新数据************/


                    if (recordName == null) {

                        EventBus.getDefault().post(new AddRecordEvent(localServerTableRecord2.getName(), localServerTableRecord2.getRecordId(), tableId)); //发送新增数据事件
                    } else {
                        EventBus.getDefault().post(new RefreshListEvent(tableKeyTmp));//发送刷新列表事件
                    }
                    /***********发送事件通知列表进行更新数据************************/
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
