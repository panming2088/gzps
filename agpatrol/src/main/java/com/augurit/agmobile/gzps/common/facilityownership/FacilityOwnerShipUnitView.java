package com.augurit.agmobile.gzps.common.facilityownership;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.facilityownership.service.FaciliyOwnerShipUnitService;
import com.augurit.agmobile.gzps.login.PatrolLoginService;
import com.augurit.agmobile.gzps.uploadfacility.view.SendFacilityOwnerShipUnit;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.model.User;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 设施权属单位控件
 * Created by xcl on 2017/11/27.
 */

public class FacilityOwnerShipUnitView {


    private Context mContext;
    private String originValue;
    private boolean other = false;
    private AMSpinner spinner;
    private EditText tv;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> arr_adapter;
    private List<String> datas;
    private FaciliyOwnerShipUnitService faciliyOwnerShipUnitService;
    private List<String> allOwnerShip;
    private OnItemClickListner onItemClickListner;
    private View root;
    private String textValaue ="";


    public FacilityOwnerShipUnitView(Context context, String loginName, String originValue) {
        this.mContext = context;
        this.originValue = originValue;
//        if(originValue.contains("其他：")){
//            this.originValue = originValue.substring(3);
//            this.other = true;
//        } else if(originValue.contains("其他")){
//            this.originValue = "";
//            this.other = true;
//        } else {
//            this.originValue = originValue;
//        }
        initView(originValue);
        loadData(loginName);
    }

    public FacilityOwnerShipUnitView(Context context, String originValue) {
        this.mContext = context;
        this.originValue = originValue;
//        if(originValue.contains("其他：")){
//            this.originValue = originValue.substring(3);
//            this.other = true;
//        } else if(originValue.contains("其他")){
//            this.originValue = "";
//            this.other = true;
//        } else {
//            this.originValue = originValue;
//        }
        initView(originValue);
        loadDataPaiFangKou();
    }

    public void initService() {
        if (faciliyOwnerShipUnitService == null) {
            faciliyOwnerShipUnitService = new FaciliyOwnerShipUnitService(mContext);
        }
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public void setReadOnly() {
        autoCompleteTextView.setEnabled(false);
    }

    private void loadData(String loginName) {
        int originValuePosition = -1;
        int userOrgPosition = -1;

        String userOrg = BaseInfoManager.getUserOrg(mContext);

        User user = new PatrolLoginService(mContext, AMDatabase.getInstance()).getUser();

        String[] dataArray = mContext.getResources().getStringArray(R.array.parentDepts);
        /**
         * 判断逻辑：1. 如果OriginValue等于空，那么权属单位 = 用户当前所属单位
         *          2. 如果OriginValue不等于空，但是没有命中，那么此时 权属单位 = 用户所属单位 ，并且发送消息，将OriginValue传递给"管理状态"字段；
         *          3. 如果originValue不等于空，并且命中 ，那么此时 权属单位 = originvalue
         */
        for (int i = 0; i < dataArray.length; i++) {
            spinner.addItems(dataArray[i], dataArray[i]);

            //判断 用户所属单位有没有命中
            if (StringUtil.isEmpty(user.getOrgPath())) {
                if (dataArray[i].contains(userOrg)
                        && !(dataArray[i].contains("花都")
                        && dataArray[i].contains("净水"))) {
                    userOrgPosition = i;
                }
            } else if (user.getOrgPath().contains("花都")
                    && user.getOrgPath().contains("净水")
                    && dataArray[i].contains("花都")
                    && dataArray[i].contains("净水")) {
                //orgPath字段只有在用户是“花都净水公司”时才返回值“花都净水”，其余情况皆为空字符串
                //匹配“花都净水公司”
                userOrgPosition = i;
            }

            //判断originValue 有没有命中
            if (!StringUtil.isEmpty(originValue) && dataArray[i].equals(originValue)) {
                originValuePosition = i;
            }
        }

        int finalSelectPosition = -1;
        if (StringUtil.isEmpty(originValue) || originValuePosition == -1) {
            finalSelectPosition = userOrgPosition;
        } else {
            finalSelectPosition = originValuePosition;
        }

        //去掉其他
        // spinner.addItems("其他", "其他");
        if (finalSelectPosition != -1) {
            spinner.selectItem(finalSelectPosition);
        }

        /**
         * 当所在设施权属单位不为空，但是下拉框中找不到这个选项的时候，填到"管理状态"字段中去；
         */
        if (!StringUtil.isEmpty(originValue) && originValuePosition == -1) {
            //发送事件
            //修改：2018-04-19 xcl  : 没有命中，不发送事件，让用户自己选择当前设施属于哪个权属单位
            //EventBus.getDefault().post(new SendFacilityOwnerShipUnit(originValue));
        }

//        else {
//            spinner.selectItem("其他");
//            tv.setText(originValue);
//        }


        /*initService();
        faciliwnerShipUnitService.getFacilityOwnerShipUnit(loginName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FacilityOwnerShipUnit>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(FacilityOwnerShipUnit facilityOwnerShipUnit) {

                       // datas.clear();
                        datas = new ArrayList<String>();
                        int selPosition = -1;
                        List<FacilityOwnerShipUnit.OwnerShipUnit> rows = facilityOwnerShipUnit.getRows();
                        if (!ListUtil.isEmpty(rows)){
                            for (FacilityOwnerShipUnit.OwnerShipUnit ownerShipUnit : rows){
                                datas.add(ownerShipUnit.getParentOrgName());
                            }
                            if(facilityOwnerShipUnit.getData() != null){
                                datas.add(facilityOwnerShipUnit.getData().getParentOrgName());
                            }
                            arr_adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, datas);
                            autoCompleteTextView.setAdapter(arr_adapter);
                           // arr_adapter.notifyDataSetChanged();

                            String userOrg = BaseInfoManager.getUserOrg(mContext);

                            for(int i=0; i<datas.size(); i++){
                                spinner.addItems(datas.get(i), datas.get(i));
                                if(StringUtil.isEmpty(originValue)
                                        && !other){
                                    if(datas.get(i).contains(userOrg)){
                                        selPosition = i;
                                    }
                                } else if(datas.get(i).equals(originValue)){
                                    selPosition = i;
                                }
                            }
                        }

                        spinner.addItems("其他", "其他");
                        if(selPosition != -1){
                            spinner.selectItem(selPosition);
                        } else {
                            spinner.selectItem("其他");
                            tv.setText(originValue);
                        }

                         if (TextUtils.isEmpty(autoCompleteTextView.getText().toString()) && facilityOwnerShipUnit.getData() != null){
                             autoCompleteTextView.setText(facilityOwnerShipUnit.getData().getParentOrgName());
                         }
                    }
                });*/
        }

    private void loadDataPaiFangKou() {
        int originValuePosition = -1;
        int userOrgPosition = -1;

        String userOrg = BaseInfoManager.getUserOrg(mContext);

        User user = new PatrolLoginService(mContext, AMDatabase.getInstance()).getUser();

        String[] dataArray = mContext.getResources().getStringArray(R.array.parentPFKDepts);
        /**
         * 判断逻辑：1. 如果OriginValue等于空，那么权属单位 = 用户当前所属单位
         *          2. 如果OriginValue不等于空，但是没有命中，那么此时 权属单位 = 用户所属单位 ，并且发送消息，将OriginValue传递给"管理状态"字段；
         *          3. 如果originValue不等于空，并且命中 ，那么此时 权属单位 = originvalue
         */
        if(!StringUtil.isEmpty(originValue) && originValue.contains("其他：")){
            textValaue = originValue.substring(3);
            originValue = "其他";
        }
        for (int i = 0; i < dataArray.length; i++) {
            spinner.addItems(dataArray[i], dataArray[i]);

            //判断 用户所属单位有没有命中
            if (StringUtil.isEmpty(user.getOrgPath())) {
                if (dataArray[i].contains(userOrg)
                        && !(dataArray[i].contains("花都")
                        && dataArray[i].contains("净水"))) {
                    userOrgPosition = i;
                }
            } else if (user.getOrgPath().contains("花都")
                    && user.getOrgPath().contains("净水")
                    && dataArray[i].contains("花都")
                    && dataArray[i].contains("净水")) {
                //orgPath字段只有在用户是“花都净水公司”时才返回值“花都净水”，其余情况皆为空字符串
                //匹配“花都净水公司”
                userOrgPosition = i;
            }

            //判断originValue 有没有命中

            if (!StringUtil.isEmpty(originValue) && dataArray[i].equals(originValue)) {
                originValuePosition = i;
            }
        }

        int finalSelectPosition = -1;
        if (StringUtil.isEmpty(originValue) || originValuePosition == -1) {
            finalSelectPosition = userOrgPosition;
        } else {
            finalSelectPosition = originValuePosition;
        }

        //去掉其他
        // spinner.addItems("其他", "其他");
        if (finalSelectPosition != -1) {
            spinner.selectItem(finalSelectPosition);
        }
        tv.setText(textValaue);
        /**
         * 当所在设施权属单位不为空，但是下拉框中找不到这个选项的时候，填到"管理状态"字段中去；
         */
        if (!StringUtil.isEmpty(originValue) && originValuePosition == -1) {
            //发送事件
            //修改：2018-04-19 xcl  : 没有命中，不发送事件，让用户自己选择当前设施属于哪个权属单位
            //EventBus.getDefault().post(new SendFacilityOwnerShipUnit(originValue));
        }

//        else {
//            spinner.selectItem("其他");
//            tv.setText(originValue);
//        }


        /*initService();
        faciliwnerShipUnitService.getFacilityOwnerShipUnit(loginName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FacilityOwnerShipUnit>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(FacilityOwnerShipUnit facilityOwnerShipUnit) {

                       // datas.clear();
                        datas = new ArrayList<String>();
                        int selPosition = -1;
                        List<FacilityOwnerShipUnit.OwnerShipUnit> rows = facilityOwnerShipUnit.getRows();
                        if (!ListUtil.isEmpty(rows)){
                            for (FacilityOwnerShipUnit.OwnerShipUnit ownerShipUnit : rows){
                                datas.add(ownerShipUnit.getParentOrgName());
                            }
                            if(facilityOwnerShipUnit.getData() != null){
                                datas.add(facilityOwnerShipUnit.getData().getParentOrgName());
                            }
                            arr_adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, datas);
                            autoCompleteTextView.setAdapter(arr_adapter);
                           // arr_adapter.notifyDataSetChanged();

                            String userOrg = BaseInfoManager.getUserOrg(mContext);

                            for(int i=0; i<datas.size(); i++){
                                spinner.addItems(datas.get(i), datas.get(i));
                                if(StringUtil.isEmpty(originValue)
                                        && !other){
                                    if(datas.get(i).contains(userOrg)){
                                        selPosition = i;
                                    }
                                } else if(datas.get(i).equals(originValue)){
                                    selPosition = i;
                                }
                            }
                        }

                        spinner.addItems("其他", "其他");
                        if(selPosition != -1){
                            spinner.selectItem(selPosition);
                        } else {
                            spinner.selectItem("其他");
                            tv.setText(originValue);
                        }

                         if (TextUtils.isEmpty(autoCompleteTextView.getText().toString()) && facilityOwnerShipUnit.getData() != null){
                             autoCompleteTextView.setText(facilityOwnerShipUnit.getData().getParentOrgName());
                         }
                    }
                });*/
    }


    private void initView(String originValue) {
        root = LayoutInflater.from(mContext).inflate(R.layout.view_facilityownershipuint, null);
        spinner = (AMSpinner) root.findViewById(R.id.spinner);
        tv = (EditText) root.findViewById(R.id.tv);
        spinner.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                if ("其他".equals(item.toString())) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.GONE);
                }
                if (item instanceof String){
                    EventBus.getDefault().post(new SendFacilityOwnerShipUnit(item.toString()));
                }
            }
        });
        autoCompleteTextView = (AutoCompleteTextView) root.findViewById(R.id.auto_complete_text_view);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (onItemClickListner != null) {
                    onItemClickListner.onClick(datas.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (!TextUtils.isEmpty(originValue)) {
            autoCompleteTextView.setText(originValue);
        }
        autoCompleteTextView.setThreshold(1);

        // int px = DensityUtil.dp2px(mContext, 350);
        //autoCompleteTextView.setDropDownHeight(px);
//        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus){
//                    if (arr_adapter != null && arr_adapter.getCount() != 0){
//                        autoCompleteTextView.showDropDown();
//                    }
//                }
//            }
//        });
//        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (arr_adapter != null && arr_adapter.getCount() != 0){
//                    autoCompleteTextView.showDropDown();
//                }
//            }
//        });
    }


    public interface OnItemClickListner {
        void onClick(String selectedItem);
    }


    public String getText() {
        if ("其他".equals(spinner.getText())) {
            String value = tv.getText().toString();
            if (StringUtil.isEmpty(value)) {
                return "其他";
            } else {
                return "其他：" + value;
            }
        } else {
            return spinner.getText();
        }
//        return autoCompleteTextView.getText().toString();
    }

    public void addTo(ViewGroup viewGroup) {
        viewGroup.addView(root);
    }

    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
