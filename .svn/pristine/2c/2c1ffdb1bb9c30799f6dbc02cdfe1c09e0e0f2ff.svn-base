package com.augurit.agmobile.gzps.uploadfacility.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.OnSpinnerChangedEvent;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.widget.PhoneItemTableItem;
import com.augurit.agmobile.gzps.common.widget.SpinnerTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.OnMenPaiDeleteEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyAttributeView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyGJJDView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyGuaPaiNoView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyMenPaiView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlySfCzwsclView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlySfHxView;
import com.augurit.agmobile.gzps.uploadfacility.view.doorno.UploadDoorNoMapActivity;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.esri.core.geometry.Point;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.util
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

public class YinjingAttributeViewUtil {

    /**
     * 属性名称
     */
    public static final String ATTRIBUTE_ONE = "窨井类型";
    public static final String ATTRIBUTE_MHP = "关联门牌号";
    public static final String ATTRIBUTE_TWO = "雨污类别";
    public static final String ATTRIBUTE_THREE = "井盖材质";
    public static final String ATTRIBUTE_FOUR = "权属单位";
    public static final String ATTRIBUTE_FIVE = "已挂牌编号";
    public static final String YJ_BH = "窨井编号";
    //相片
    public static final String ATTRIBUTE_SIX = "相片";
    public static final String ATTRIBUTE_Seven = "相片缩略图";
//    private List<Photo> photos;
//    private List<Photo> thumbnailPhotos;
    public static final String ATTRIBUTE_SFCZWSCL = "是否属于";
    public static final String ATTRIBUTE_SFGJJD = "是否关键节点";
    public static final String ATTRIBUTE_GJJDBH = "节点编号";
    public static final String ATTRIBUTE_GJJDZRR = "责任人";
    public static final String ATTRIBUTE_GJJDLXDH = "联系电话";
    public static final String ATTRIBUTE_SFHX = "红线内设施";

    /**
     * 属性在设施图层中的key
     */
    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.SUBTYPE;
    public static final String ATTRIBUTE_TWO_KEY = ComponentFieldKeyConstant.SORT;
    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.MATERIAL;
    public static final String ATTRIBUTE_FOUR_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_FIVE_KEY = ComponentFieldKeyConstant.LICENSE;
    public static final String ATTRIBUTE_SFCZWSCL_KEY = ComponentFieldKeyConstant.SFCZWSCL;
    public static final String ATTRIBUTE_SFHXN_KEY = ComponentFieldKeyConstant.SFPSDYHXN;
    private YinjingAttributes yinjingAttributes;

    private FacilityOwnerShipUnitView facilityOwnerShipUnitView;

//    private TextItemTableItem attributeFiveItem;

    private EditText attributeFiveItem;
    private boolean fiveItemChecked = true;
    private boolean gjjdItemChecked = false;

    private Map<String, String> originAttributeValue = new HashMap<>(5);
    private View mMenPaiView;
    private TagFlowLayout mFlowLayout;
    private List<DoorNOBean> mVals = new ArrayList<>();
    private List<DoorNOBean> mOldVals = new ArrayList<>();
    private Button btn_add_attached;
    private int currentIndex = -1;
    private TextItemTableItem mItem_jdbh;
    private TextItemTableItem mItem_zrr;
    private PhoneItemTableItem mItem_lxdh;
    private TextItemTableItem mYjBh;

    public YinjingAttributeViewUtil() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 排序下拉字典数组
     *
     * @param list
     */
    private static void processOrder(List<DictionaryItem> list) {
        for (DictionaryItem item : list) {
            String code = item.getCode();
            String target = code;
            if (code.length() > 1) {
                target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
            }
            item.setCode(target);
        }
        //再排序
        Collections.sort(list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem t1, DictionaryItem t2) {
                int num1 = Integer.valueOf(t1.getCode());
                int num2 = Integer.valueOf(t2.getCode());
                int result = 0;
                if (num1 > num2) {
                    result = 1;
                }

                if (num1 < num2) {
                    result = -1;
                }
                return result;
            }
        });

    }


    //新增校核详情处理
    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer, List<DoorNOBean> mphBeen, Point point,ModifiedFacility modifiedFacility) {
        //属性一
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //门牌
        if ("接驳井".equals(yinjingAttributes.getAttributeOne())) {
            ReadOnlyMenPaiView readOnlyMenPaiView = new ReadOnlyMenPaiView(context, mphBeen, point);
            readOnlyMenPaiView.addTo(llContainer);
        }

        //属性二
        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, attributeTwo, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);
        //窨井编号
        ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                YJ_BH, yinjingAttributes.getAttributeYjBh());
        readOnlyAttributeView6.addTo(llContainer);

        //关键节点
        ReadOnlyGJJDView sfGjjdView = new ReadOnlyGJJDView(context,yinjingAttributes.getAttributesfGjjd()
                ,yinjingAttributes.getGjjdBh() ,yinjingAttributes.getGjjdZrr() ,yinjingAttributes.getLxdh());
        sfGjjdView.addTo(llContainer);

        //是否属于排水单元红线内
        String attributesfHx= getComponentOriginValue(originValue, ATTRIBUTE_SFHX);
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,attributesfHx,yinjingAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);

        //是否属于纳入城镇污水处理系统农污管网
        String attributeXXX= getComponentOriginValue(originValue, ATTRIBUTE_SFCZWSCL);
        ReadOnlySfCzwsclView readOnlySfCzwsclView = new ReadOnlySfCzwsclView(context,
                ATTRIBUTE_SFCZWSCL, attributeXXX, yinjingAttributes.getAttributesfCzwscl());
        readOnlySfCzwsclView.addTo(llContainer);
        //属性3
//        String componentOriginValue3 = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE, componentOriginValue3, yinjingAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);、

        //属性4
//        String componentOriginValue4 = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
//        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FOUR, componentOriginValue4, yinjingAttributes.getAttributeFour());
//        readOnlyAttributeView4.addTo(llContainer);

        //属性5
//        String componentOriginValue5 = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
//        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FIVE, componentOriginValue5, yinjingAttributes.getAttributeFive());

        ReadOnlyGuaPaiNoView readOnlyGuaPaiNoView5 = new ReadOnlyGuaPaiNoView(context,
                ATTRIBUTE_FIVE, yinjingAttributes.getAttributeFive(),modifiedFacility.getWellPhotos());
        readOnlyGuaPaiNoView5.addTo(llContainer);
    }

    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer) {
        //属性一
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);


        //属性二
        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, attributeTwo, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);
        //是否属于排水单元红线内
        String attributesfHx= getComponentOriginValue(originValue, ATTRIBUTE_SFHX);
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,attributesfHx,yinjingAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);

        //是否属于纳入城镇污水处理系统农污管网
        String attributesfCzwscl= getComponentOriginValue(originValue, ATTRIBUTE_SFCZWSCL);
//        ReadOnlyAttributeView readOnlyAttributeViewfCzwscl = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_SFCZWSCL, attributesfCzwscl, yinjingAttributes.getAttributesfCzwscl());
//        readOnlyAttributeViewfCzwscl.addTo(llContainer);

        ReadOnlySfCzwsclView  sfCzwsclView = new ReadOnlySfCzwsclView(context,ATTRIBUTE_SFCZWSCL,attributesfCzwscl,yinjingAttributes.getAttributesfCzwscl());
        sfCzwsclView.addTo(llContainer);
        //属性3
//        String componentOriginValue3 = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE, componentOriginValue3, yinjingAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);

        //属性4
//        String componentOriginValue4 = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
//        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FOUR, componentOriginValue4, yinjingAttributes.getAttributeFour());
//        readOnlyAttributeView4.addTo(llContainer);

        //属性5
        String componentOriginValue5 = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, componentOriginValue5, yinjingAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);
    }


    public static void addReadOnlyAttributesForJournal(Context context,
                                             Map<String, Object> originValue,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer) {
        //属性一
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二
        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, attributeTwo, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);
    }


    public static void addReadOnlyAttributes(Context context,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer) {
        //属性一
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);

        //是否属于排水单元红线内
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,"",yinjingAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);

        //是否属于纳入城镇污水处理系统农污管网

        ReadOnlySfCzwsclView  sfCzwsclView = new ReadOnlySfCzwsclView(context,ATTRIBUTE_SFCZWSCL,"",yinjingAttributes.getAttributesfCzwscl());
        sfCzwsclView.addTo(llContainer);
//        ReadOnlyAttributeView readOnlyAttributeView3= new ReadOnlyAttributeView(context,
//                ATTRIBUTE_SFCZWSCL, yinjingAttributes.getAttributesfCzwscl());
//        readOnlyAttributeView3.addTo(llContainer);
        //属性3
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE, yinjingAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);

        //属性4

//        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FOUR, yinjingAttributes.getAttributeFour());
//        readOnlyAttributeView4.addTo(llContainer);

        //属性5

        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, yinjingAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);
    }

    //反显属性
    public static void addReadOnlyAttributes(Context context,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer, List<DoorNOBean> mphBeen, Point point,UploadedFacility uploadedFacility) {
        //属性一
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //门牌
        if ("接驳井".equals(yinjingAttributes.getAttributeOne())) {
            ReadOnlyMenPaiView readOnlyMenPaiView = new ReadOnlyMenPaiView(context, mphBeen, point);
            readOnlyMenPaiView.addTo(llContainer);
        }

        //属性二
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);
        //窨井编号
        ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                YJ_BH, yinjingAttributes.getAttributeYjBh());
        readOnlyAttributeView6.addTo(llContainer);

        //关键节点
        ReadOnlyGJJDView sfGjjdView = new ReadOnlyGJJDView(context,yinjingAttributes.getAttributesfGjjd()
                ,yinjingAttributes.getGjjdBh() ,yinjingAttributes.getGjjdZrr() ,yinjingAttributes.getLxdh());
        sfGjjdView.addTo(llContainer);

        //是否属于排水单元红线内
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,"",yinjingAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);
        //是否属于纳入城镇污水处理系统农污管网

        ReadOnlySfCzwsclView  sfCzwsclView = new ReadOnlySfCzwsclView(context,ATTRIBUTE_SFCZWSCL,"",yinjingAttributes.getAttributesfCzwscl());
        sfCzwsclView.addTo(llContainer);
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_SFCZWSCL, yinjingAttributes.getAttributesfCzwscl());
//        readOnlyAttributeView3.addTo(llContainer);
        //属性3
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE, yinjingAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);

        //属性4

//        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FOUR, yinjingAttributes.getAttributeFour());
//        readOnlyAttributeView4.addTo(llContainer);

        //属性5

//        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FIVE, yinjingAttributes.getAttributeFive());
//        readOnlyAttributeView5.addTo(llContainer);
         ReadOnlyGuaPaiNoView readOnlyGuaPaiNoView5 = new ReadOnlyGuaPaiNoView(context,
          ATTRIBUTE_FIVE, yinjingAttributes.getAttributeFive(),uploadedFacility.getWellPhotos());
        readOnlyGuaPaiNoView5.addTo(llContainer);
    }

    public static Map<String, String> getDefaultValue(Component component) {

        Map<String, String> defaultSelectValue = new HashMap<>(5);
        String five = StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE), "");
        defaultSelectValue.put(ATTRIBUTE_ONE, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE), ""));
        defaultSelectValue.put(ATTRIBUTE_TWO, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_TWO), ""));
        defaultSelectValue.put(YJ_BH, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_YJBH), ""));
        defaultSelectValue.put(ATTRIBUTE_THREE, "");
//        defaultSelectValue.put(ATTRIBUTE_THREE, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_THREE), ""));
        defaultSelectValue.put(ATTRIBUTE_SFHX, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.SFPSDYHXN), ""));
        defaultSelectValue.put(ATTRIBUTE_FOUR, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FOUR), ""));
        defaultSelectValue.put(ATTRIBUTE_FIVE, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_FIVE), ""));
        if (StringUtil.isEmpty(five)) {
            defaultSelectValue.put(ATTRIBUTE_FIVE, "");
        } else {
            defaultSelectValue.put(ATTRIBUTE_FIVE, five);
        }
        return defaultSelectValue;
    }

    public static Map<String, Object> getOriginValue(Component component) {
        Map<String, Object> originValue = new HashMap<>(5);

        originValue.put(ATTRIBUTE_ONE_KEY, "");
        originValue.put(ATTRIBUTE_ONE_KEY, StringUtil.getNotNullString(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.ATTR_ONE), ""));

        originValue.put(ATTRIBUTE_TWO_KEY, "");
        originValue.put(ATTRIBUTE_SFCZWSCL_KEY, "");
        originValue.put(ATTRIBUTE_SFHXN_KEY, "");
        originValue.put(ATTRIBUTE_THREE_KEY, "");

        originValue.put(ATTRIBUTE_FOUR_KEY, "");

        originValue.put(ATTRIBUTE_FIVE_KEY, "");

        return originValue;
    }

    public void addYinjingAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                     Map<String, String> defaultSelectedValue, Point point,Object facility) {

        this.yinjingAttributes = new YinjingAttributes();

        if (defaultSelectedValue != null) {
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
            addMenPaiView(context, null, point, false, attributeContainer);
            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
            addYJBH(context, originValue, defaultSelectedValue.get(YJ_BH), attributeContainer);
            addAttributeGjjd(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFGJJD), attributeContainer,facility);
            //是否属于排水单元红线内设施

            addAttributeSfHX(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFHX), attributeContainer, facility);
            addAttributeSfCzwscl(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFCZWSCL), attributeContainer, facility);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
            addAttributeFive(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FIVE), attributeContainer, facility);

        } else {
            addAttributeOne(context, originValue, null, attributeContainer);
            addMenPaiView(context, null, point, false, attributeContainer);
            addAttributeTwo(context, originValue, null, attributeContainer);
            addYJBH(context, originValue, null, attributeContainer);
            addAttributeGjjd(context, originValue, null, attributeContainer,facility);
            addAttributeSfHX(context, originValue, null, attributeContainer, facility);
            addAttributeSfCzwscl(context, originValue, null, attributeContainer, facility);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
            addAttributeFive(context, originValue, null, attributeContainer, facility);
        }

    }

    private void addAttributeSfCzwscl(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup attributeContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_sfczwscl, null);
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        ((TextView) view.findViewById(R.id.tv_name)).setText(ATTRIBUTE_SFCZWSCL);
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_SFCZWSCL);
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_SFCZWSCL, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_SFCZWSCL, defaultSelectedValue);
        }
        String text = "";
        if (defaultSelectedValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultSelectedValue;
        }
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                text = facility.getSfCzwscl();
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                text = facility.getSfCzwscl();
            }
        }

        if ("否".equals(text)
                || StringUtil.isEmpty(text)) {
            yinjingAttributes.setAttributesfCzwscl("否");
        } else {
            yinjingAttributes.setAttributesfCzwscl("是");
        }

        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yinjingAttributes.setAttributesfCzwscl("是");
                } else {
                    yinjingAttributes.setAttributesfCzwscl("否");
                }
            }
        });
        if ("否".equals(text)
                || StringUtil.isEmpty(text)) {
            cb_yes.setChecked(false);
        } else {
            cb_yes.setChecked(true);
        }
        attributeContainer.addView(view);
    }

    /**
     * 是否属于排水单元红线内
     * @param context
     * @param originValue
     * @param defaultSelectedValue
     * @param attributeContainer
     * @param uploadedFacility
     */
    private void addAttributeSfHX(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup attributeContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_sfhx, null);
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        ((TextView) view.findViewById(R.id.tv_name)).setText(ATTRIBUTE_SFHX);
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_SFHX);
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_SFHX, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_SFHX, defaultSelectedValue);
        }
        String text = "";
        if (defaultSelectedValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultSelectedValue;
        }
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                text = facility.getSfpsdyhxn();
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                text = facility.getSfpsdyhxn();
            }
        }

        if ("0".equals(text)
                || StringUtil.isEmpty(text)) {
            yinjingAttributes.setSfpsdyhxn("0");
        } else {
            yinjingAttributes.setSfpsdyhxn("1");
        }

        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yinjingAttributes.setSfpsdyhxn("1");
                } else {
                    yinjingAttributes.setSfpsdyhxn("0");
                }
            }
        });
        if ("0".equals(text)
                || StringUtil.isEmpty(text)) {
            cb_yes.setChecked(false);
        } else {
            cb_yes.setChecked(true);
        }
        attributeContainer.addView(view);
    }

    public void addYinjingAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                     Map<String, String> defaultSelectedValue,Object uploadedFacility) {

        this.yinjingAttributes = new YinjingAttributes();

        if (defaultSelectedValue != null) {
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
            addYJBH(context, originValue, defaultSelectedValue.get(YJ_BH), attributeContainer);
            addAttributeGjjd(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFGJJD), attributeContainer,uploadedFacility);
            addAttributeSfHX(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFHX), attributeContainer, uploadedFacility);
            addAttributeSfCzwscl(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFCZWSCL), attributeContainer, uploadedFacility);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
            addAttributeFive(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FIVE), attributeContainer, uploadedFacility);

        } else {
            addAttributeOne(context, originValue, null, attributeContainer);
            addAttributeTwo(context, originValue, null, attributeContainer);
            addYJBH(context, originValue, null, attributeContainer);
            addAttributeGjjd(context, originValue, null, attributeContainer,uploadedFacility);
            addAttributeSfHX(context, originValue, null, attributeContainer, uploadedFacility);
            addAttributeSfCzwscl(context, originValue, null, attributeContainer, uploadedFacility);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
            addAttributeFive(context, originValue, null, attributeContainer, uploadedFacility);
        }

    }

    //显示实体属性
    public void addYinjingAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                     Map<String, String> defaultSelectedValue, List<DoorNOBean> mpBeen, Point point, Object uploadedFacility) {

        this.yinjingAttributes = new YinjingAttributes();

        if (defaultSelectedValue != null) {
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
            if ("接驳井".equals(defaultSelectedValue.get(ATTRIBUTE_ONE))) {
                addMenPaiView(context, mpBeen, point, true, attributeContainer);
            } else {
                addMenPaiView(context, mpBeen, point, false, attributeContainer);
            }
            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
            addYJBH(context, originValue, defaultSelectedValue.get(YJ_BH), attributeContainer);
            addAttributeGjjd(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFGJJD), attributeContainer,uploadedFacility);
            addAttributeSfHX(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFHX), attributeContainer,uploadedFacility);
            addAttributeSfCzwscl(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFCZWSCL), attributeContainer,uploadedFacility);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
            addAttributeFive(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FIVE), attributeContainer,uploadedFacility);

        } else {
            addAttributeOne(context, originValue, null, attributeContainer);
            addMenPaiView(context, mpBeen, point, false, attributeContainer);
            addAttributeTwo(context, originValue, null, attributeContainer);
            addYJBH(context, originValue, null, attributeContainer);
            addAttributeGjjd(context, originValue, null, attributeContainer,uploadedFacility);
            addAttributeSfHX(context, originValue, null, attributeContainer, uploadedFacility);
            addAttributeSfCzwscl(context, originValue, null, attributeContainer, uploadedFacility);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
            addAttributeFive(context, originValue, null, attributeContainer, uploadedFacility);
        }

    }

    /***
     * 属性四
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     * @param
     */
    private void addAttributeFour(Context context,
                                  Map<String, Object> originValue, String defaultValue, ViewGroup attributelistContainer) {
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        String loginName = new LoginRouter(context, AMDatabase.getInstance()).getUser().getLoginName();

        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_FOUR, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_FOUR, defaultValue);
        }


        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }
        facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, loginName, text);
        facilityOwnerShipUnitView.addTo(attributelistContainer);
        yinjingAttributes.setAttributeFour(text);
    }

    /**
     * 窨井属性一
     *
     * @param context
     * @param originValue          设施图层中的属性，当不是核准时，为null
     * @param defaultSelectedValue 默认选中的值
     * @param container
     */
    private void addAttributeOne(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a158 = dbService.getDictionaryByTypecodeInDB("A158");
        processOrder(a158);

        List<String> allowValues = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a158) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }

        this.yinjingAttributes.setAttributeOneAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_ONE);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                DictionaryItem dictionaryItem = ((DictionaryItem) item);
                yinjingAttributes.setAttributeOne(dictionaryItem.getName());
                if ("接驳井".equals(dictionaryItem.getName()) && mMenPaiView != null) {
                    mMenPaiView.setVisibility(View.VISIBLE);
                } else if (mMenPaiView != null) {
                    mMenPaiView.setVisibility(View.GONE);
                }
                EventBus.getDefault().post(new OnSpinnerChangedEvent(ATTRIBUTE_ONE, dictionaryItem.getName(), dictionaryItem));
            }
        });


        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */

        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
//                    if(!TextUtils.isEmpty(componentOriginValue)&&!"".equals(componentOriginValue)){
                        spinnerTableItem.put(componentOriginValue, item);
//                    }
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeOne(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
//                    if(!TextUtils.isEmpty(componentOriginValue)&& !"".equals(componentOriginValue)){
                        spinnerTableItem.put(componentOriginValue, item);
//                    }
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeOne(defaultSelectedValue);
            }
        } else if (!StringUtil.isEmpty(defaultSelectedValue)) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b && !StringUtil.isEmpty(componentOriginValue)) {
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);
                originAttributeValue.put(ATTRIBUTE_THREE, defaultSelectedValue);
                yinjingAttributes.setAttributeThree(defaultSelectedValue);
            } else if (!b) {
                spinnerTableItem.selectItem(0);
            }
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }


    public static String getComponentOriginValue(Map<String, Object> originValue, String attributeName) {

        if (originValue == null) {
            return null;
        }
        /**
         * 窨井
         */
        if (attributeName.contains(ATTRIBUTE_ONE)) {
            Object o = originValue.get(ATTRIBUTE_ONE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_TWO)) {
            Object o = originValue.get(ATTRIBUTE_TWO_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_THREE)) {
            Object o = originValue.get(ATTRIBUTE_THREE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_FOUR)) {
            Object o = originValue.get(ATTRIBUTE_FOUR_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_FIVE)) {
            Object o = originValue.get(ATTRIBUTE_FIVE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_SFCZWSCL)) {
            Object o = originValue.get(ATTRIBUTE_SFCZWSCL_KEY);
            if (o != null) {
                return o.toString();
            }
        }
        return null;
    }


    public YinjingAttributes getYinjingAttributes() {

        if (facilityOwnerShipUnitView != null) {
            yinjingAttributes.setAttributeFour(facilityOwnerShipUnitView.getText());
        }
        //是否关键节点
        if (gjjdItemChecked && !StringUtil.isEmpty(mItem_jdbh.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setGjjdBh(mItem_jdbh.getText().toString());
        } else{
            yinjingAttributes.setGjjdBh("");
        }
        if (gjjdItemChecked && !StringUtil.isEmpty(mItem_zrr.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setGjjdZrr(mItem_zrr.getText().toString());
        } else{
            yinjingAttributes.setGjjdZrr("");
        }
        if (mYjBh != null && !StringUtil.isEmpty(mYjBh.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setAttributeYjBh(mYjBh.getText().toString());
        } else{
            yinjingAttributes.setAttributeYjBh("");
        }
        if (gjjdItemChecked && !StringUtil.isEmpty(mItem_lxdh.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setLxdh(mItem_lxdh.getText().toString());
        } else{
            yinjingAttributes.setLxdh("");
        }

        if (fiveItemChecked && StringUtil.isEmpty(attributeFiveItem.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setAttributeFive(null);
        } else if (attributeFiveItem != null && fiveItemChecked) {
            yinjingAttributes.setAttributeFive(attributeFiveItem.getText().toString());
        } else {
            yinjingAttributes.setAttributeFive("无");
        }

        if (!ListUtil.isEmpty(mVals)) {
            yinjingAttributes.setMpBeen(mVals);
        }

        //对比属性
        if (yinjingAttributes.getAttributeOne() != null && !yinjingAttributes.getAttributeOne().equals(originAttributeValue.get(ATTRIBUTE_ONE))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeTwo() != null && !yinjingAttributes.getAttributeTwo().equals(originAttributeValue.get(ATTRIBUTE_TWO))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeFive() != null && !yinjingAttributes.getAttributeFive().equals(originAttributeValue.get(ATTRIBUTE_FIVE))) {
            yinjingAttributes.setHasModified(true);
        }else if (yinjingAttributes.getAttributesfCzwscl() != null && !yinjingAttributes.getAttributesfCzwscl().equals(originAttributeValue.get(ATTRIBUTE_SFCZWSCL))) {
            yinjingAttributes.setHasModified(true);
        }

        return yinjingAttributes;

    }

    public YinjingAttributes getOriginalYinjingAttributes() {


        YinjingAttributes yinjingAttributes = new YinjingAttributes();

        String attributeOne = originAttributeValue.get(ATTRIBUTE_ONE);
        if (attributeOne != null) {
            yinjingAttributes.setAttributeOne(attributeOne);
        }else{

        }
        if (!ListUtil.isEmpty(mVals)) {
            yinjingAttributes.setMpBeen(mVals);
        }
        String attributeTwo = originAttributeValue.get(ATTRIBUTE_TWO);
        if (attributeTwo != null) {
            yinjingAttributes.setAttributeTwo(attributeTwo);
        }else{
        }


//        String attributeThree = originAttributeValue.get(ATTRIBUTE_THREE);
//        if (attributeThree != null) {
//            yinjingAttributes.setAttributeThree(attributeThree);
//        }
        yinjingAttributes.setAttributeThree("");
        String attributeFour = originAttributeValue.get(ATTRIBUTE_FOUR);
        if (attributeFour != null) {
            yinjingAttributes.setAttributeFour(attributeFour);
        }

        String attributeFive = originAttributeValue.get(ATTRIBUTE_FIVE);
        if (attributeFive != null) {
            yinjingAttributes.setAttributeFive(attributeFive);
        }
        String attributeSfCzwscl = originAttributeValue.get(ATTRIBUTE_SFCZWSCL);
        if (attributeSfCzwscl != null) {
            yinjingAttributes.setAttributesfCzwscl(attributeSfCzwscl);
        }
        return yinjingAttributes;

    }

    /**
     * 窨井属性一
     *
     * @param context
     * @param mpBeen
     * @param container
     */
    private void addMenPaiView(final Context context, List<DoorNOBean> mpBeen, final Point yjPoint, boolean visibility, ViewGroup container) {
        if (!ListUtil.isEmpty(mpBeen)) {
            mVals.clear();
            mVals.addAll(mpBeen);
            mOldVals.clear();
            mOldVals.addAll(mpBeen);
        }
        mMenPaiView = View.inflate(context, R.layout.view_menpai, null);
        mFlowLayout = (TagFlowLayout) mMenPaiView.findViewById(R.id.id_flowlayout);
        mFlowLayout.setAdapter(new TagAdapter<DoorNOBean>(mVals) {
            @Override
            public View getView(FlowLayout parent, final int position, final DoorNOBean s) {
                final View mView = ((Activity) context).getLayoutInflater().inflate(R.layout.tv_flow,
                        mFlowLayout, false);
                TextView tv = (TextView) mView.findViewById(R.id.flow_tv);
                tv.setText(s.getMph());
                mView.findViewById(R.id.iv_item_nine_photo_flag).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.MessageBox(context, "提示", "确定要删除？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mVals.size() > position) {
                                    mVals.remove(position);
                                    notifyDataChanged();
                                    if (s.getId() != null) {
                                        EventBus.getDefault().post(new OnMenPaiDeleteEvent(s));
                                    }
                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                    }
                });
                return mView;
            }
//            @Override
//            public boolean setSelected(int position, String s) {
//                return s.equals(mVals[position]);
//            }

        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                currentIndex = position;
                DoorNOBean doorNOBean = mVals.get(position);
//                Intent intent = new Intent(context, SelectMenPaiActivity.class);
                Intent intent = new Intent(context, UploadDoorNoMapActivity.class);
                intent.putExtra("yjPoint", yjPoint);
                intent.putExtra("doorbean", (Parcelable) doorNOBean);
                intent.putExtra("isReadOnly", false);
                context.startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFlowLayout.onChanged();
                    }
                }, 200);
                return true;
            }
        });
        mFlowLayout.setLongClickable(true);
        mFlowLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
            }
        });

        btn_add_attached = (Button) mMenPaiView.findViewById(R.id.btn_add_attached);
        btn_add_attached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, SelectMenPaiActivity.class);
                Intent intent = new Intent(context, UploadDoorNoMapActivity.class);
                intent.putExtra("yjPoint", yjPoint);
//                intent.putParcelableArrayListExtra("facilitiesBean", (ArrayList<? extends Parcelable>) facilitiesBeans);
                context.startActivity(intent);
            }
        });
        container.addView(mMenPaiView);
        if (mMenPaiView != null && !visibility) {
            mMenPaiView.setVisibility(View.GONE);
        } else if (mMenPaiView != null) {
            mMenPaiView.setVisibility(View.VISIBLE);
        }
    }

    /***
     * 属性二
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeTwo(final Context context, final Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a163 = dbService.getDictionaryByTypecodeInDB("A163");
        processOrder(a163);
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a163) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yinjingAttributes.setAttributeTwoAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        final SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_TWO);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final Object item) {
                /**
                 * 满足以下条件时弹出对话框提示：
                 * 1. 当初处于核准
                 * 2. 前后选择的选项不一样时
                 */
//                if (originValue != null && yinjingAttributes != null
//                        && yinjingAttributes.getAttributeTwo() != null
//                        && !yinjingAttributes.getAttributeTwo().equals(((DictionaryItem) item).getName())) {
//                    new AlertDialog.Builder(context)
//                            .setMessage("是否确认要修改雨污属性?")
//                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    yinjingAttributes.setAttributeTwo(((DictionaryItem) item).getName());
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    spinnerTableItem.selectItem(yinjingAttributes.getAttributeTwo());
//                                }
//                            })
//                            .show();
//                } else {
                if (yinjingAttributes != null) {
                    yinjingAttributes.setAttributeTwo(((DictionaryItem) item).getName());
                }
//                }
            }
        });

        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_TWO, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
//                    if(!TextUtils.isEmpty(componentOriginValue)&&!"".equals(componentOriginValue)){
                        spinnerTableItem.put(componentOriginValue, item);
//                    }
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeTwo(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
//                    if(!TextUtils.isEmpty(componentOriginValue)&&!"".equals(componentOriginValue)){
                        spinnerTableItem.put(componentOriginValue, item);
//                    }
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeTwo(defaultSelectedValue);
            }
        } else if (!StringUtil.isEmpty(defaultSelectedValue)) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b && !StringUtil.isEmpty(componentOriginValue)) {
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);
                originAttributeValue.put(ATTRIBUTE_THREE, defaultSelectedValue);
                yinjingAttributes.setAttributeThree(defaultSelectedValue);
            } else if (!b) {
                spinnerTableItem.selectItem(0);
            }
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }

    /**
     * 窨井编号
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     */
    private void addYJBH(final Context context, final Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        mYjBh = new TextItemTableItem(context);
        mYjBh.setTextViewName("窨井编号");
        if (!StringUtil.isEmpty(defaultSelectedValue)) {
            //如果originValue为空，但是defaultValue不为空
            mYjBh.setText(defaultSelectedValue);
        } else {
            mYjBh.setText(defaultSelectedValue);
        }
        container.addView(mYjBh);
    }

    public static Map<String, String> getDefaultValue(ModifiedFacility modifiedFacility) {

        Map<String, String> defaultSelectValue = new HashMap<>(5);
        defaultSelectValue.put(ATTRIBUTE_ONE, modifiedFacility.getAttrOne());
        defaultSelectValue.put(ATTRIBUTE_TWO, modifiedFacility.getAttrTwo());
        defaultSelectValue.put(YJ_BH, modifiedFacility.getYjbh());
//        defaultSelectValue.put(ATTRIBUTE_THREE, modifiedFacility.getAttrThree());
        defaultSelectValue.put(ATTRIBUTE_THREE, "");
        defaultSelectValue.put(ATTRIBUTE_FOUR, modifiedFacility.getAttrFour());
        defaultSelectValue.put(ATTRIBUTE_FIVE, modifiedFacility.getAttrFive());
        defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_SFGJJD, modifiedFacility.getSfgjjd());
        if (modifiedFacility.getAttrFive() == null) {
            defaultSelectValue.put(ATTRIBUTE_FIVE, "");
        } else {
            defaultSelectValue.put(ATTRIBUTE_FIVE, modifiedFacility.getAttrFive());
        }
        return defaultSelectValue;
    }

    /***
     * 属性三
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeThree(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a159 = dbService.getDictionaryByTypecodeInDB("A159");
        processOrder(a159);

        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a159) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yinjingAttributes.setAttributeThreeAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_THREE);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                yinjingAttributes.setAttributeThree(((DictionaryItem) item).getName());
            }
        });

        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */

        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_THREE, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeThree(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeThree(defaultSelectedValue);
            }
        } else if (!StringUtil.isEmpty(defaultSelectedValue)) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b && !StringUtil.isEmpty(componentOriginValue)) {
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);
                originAttributeValue.put(ATTRIBUTE_THREE, defaultSelectedValue);
                yinjingAttributes.setAttributeThree(defaultSelectedValue);
            } else if (!b) {
                spinnerTableItem.selectItem(0);
            }
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }

    public static Map<String, Object> getOriginValue(ModifiedFacility modifiedFacility) {

        Map<String, Object> originValue = new HashMap<>(5);
        if (modifiedFacility.getOriginAttrOne() != null) {
            originValue.put(ATTRIBUTE_ONE_KEY, modifiedFacility.getOriginAttrOne());
        } else {
            originValue.put(ATTRIBUTE_ONE_KEY, "");
        }

        if (modifiedFacility.getOriginAttrTwo() != null) {
            originValue.put(ATTRIBUTE_TWO_KEY, modifiedFacility.getOriginAttrTwo());
        } else {
            originValue.put(ATTRIBUTE_TWO_KEY, "");
        }

//        if (modifiedFacility.getOriginAttrThree() != null) {
//            originValue.put(ATTRIBUTE_THREE_KEY, modifiedFacility.getOriginAttrThree());
//        } else {
//        originValue.put(ATTRIBUTE_THREE_KEY, "");
//        }
//        if (modifiedFacility.getOriginAttrFour() != null) {
//            originValue.put(ATTRIBUTE_FOUR_KEY, modifiedFacility.getOriginAttrFour());
//        } else {
//            originValue.put(ATTRIBUTE_FOUR_KEY, "");
//        }
        if (modifiedFacility.getOriginAttrFive() != null) {
            originValue.put(ATTRIBUTE_FIVE_KEY, modifiedFacility.getOriginAttrFive());
        } else {
            originValue.put(ATTRIBUTE_FIVE_KEY, "");
        }
        if (modifiedFacility.getOriginAttrsfCzwscl() != null) {
            originValue.put(ATTRIBUTE_SFCZWSCL, modifiedFacility.getOriginAttrsfCzwscl());
        } else {
            originValue.put(ATTRIBUTE_SFCZWSCL_KEY, "");
        }
        return originValue;
    }

    /**
     * 关键节点
     *
     * @param originMap 设施图层中的属性，当不是核准时，为null
     * @param uploadedFacility
     */
    private void addAttributeGjjd(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup attributelistContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_gjjd, null);
//        final EditText et = (EditText) view.findViewById(R.id.et_1);
        final View ll_container = view.findViewById(R.id.ll_container);
        mItem_jdbh = (TextItemTableItem) view.findViewById(R.id.item_jdbh);
        mItem_jdbh.setRequireTag();
        mItem_zrr = (TextItemTableItem) view.findViewById(R.id.item_zrr);
        mItem_zrr.setRequireTag();
        mItem_lxdh = (PhoneItemTableItem) view.findViewById(R.id.item_lxdh);
        mItem_lxdh.setRequireTag();
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                if(!StringUtil.isEmpty(facility.getGjjdBh())){
                    mItem_jdbh.setText(facility.getGjjdBh());
                }
                if(!StringUtil.isEmpty(facility.getGjjdZrr())){
                    mItem_zrr.setText(facility.getGjjdZrr());
                }
                if(!StringUtil.isEmpty(facility.getGjjdLxdh())){
                    mItem_lxdh.setText(facility.getGjjdLxdh());
                }
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                if(!StringUtil.isEmpty(facility.getGjjdBh())){
                    mItem_jdbh.setText(facility.getGjjdBh());
                }
                if(!StringUtil.isEmpty(facility.getGjjdZrr())){
                    mItem_zrr.setText(facility.getGjjdZrr());
                }
                if(!StringUtil.isEmpty(facility.getGjjdLxdh())){
                    mItem_lxdh.setText(facility.getGjjdLxdh());
                }
            }
        }
//        attributeFiveItem = et;
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        final CheckBox cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gjjdItemChecked = true;
                    yinjingAttributes.setAttributesfGjjd("1");
//                    et.setEnabled(true);
                    ll_container.setVisibility(View.VISIBLE);
                } else {
                    gjjdItemChecked = false;
                    yinjingAttributes.setAttributesfGjjd("0");
//                    et.setEnabled(false);
                    ll_container.setVisibility(View.GONE);
                }
                cb_no.setChecked(!isChecked);
            }
        });
        cb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gjjdItemChecked = false;
//                    et.setEnabled(false);
                    yinjingAttributes.setAttributesfGjjd("0");
                    ll_container.setVisibility(View.GONE);
                } else {
                    gjjdItemChecked = true;
//                    et.setEnabled(true);
                    yinjingAttributes.setAttributesfGjjd("1");
                    ll_container.setVisibility(View.VISIBLE);
                }
                cb_yes.setChecked(!isChecked);
            }
        });
        if (!"1".equals(defaultValue)
                || StringUtil.isEmpty(defaultValue)) {
            cb_no.setChecked(true);
            gjjdItemChecked = false;
            yinjingAttributes.setAttributesfGjjd("0");
            ll_container.setVisibility(View.GONE);
        } else {
//            et.setText(defaultValue);
            cb_yes.setChecked(true);
            gjjdItemChecked = true;
            yinjingAttributes.setAttributesfGjjd("1");
            ll_container.setVisibility(View.VISIBLE);
        }

        attributelistContainer.addView(view);
    }

    /**
     * 属性五
     *
     * @param originMap 设施图层中的属性，当不是核准时，为null
     * @param uploadedFacility
     */
    private void addAttributeFive(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup attributelistContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_five, null);
        final EditText et = (EditText) view.findViewById(R.id.et_1);
        final MultiTakePhotoTableItem takePhotoTableItem = (MultiTakePhotoTableItem) view.findViewById(R.id.take_photo_well);
        takePhotoTableItem.setTitle("添加挂牌照片");
        List<Photo> photos = null;
//        takePhotoTableItem.setPhotoNumShow(false,9);
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                photos = facility.getWellPhotos();
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                photos = facility.getWellPhotos();
            }
            takePhotoTableItem.setSelectedPhotos(photos);
            takePhotoTableItem.setVisibility(View.VISIBLE);
        }
        attributeFiveItem = et;
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        final CheckBox cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE);
                    takePhotoTableItem.setPhotoNumShow(true,9);
                } else {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
                }
                cb_no.setChecked(!isChecked);
            }
        });
        cb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
                } else {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE)
        ;
        takePhotoTableItem.setPhotoNumShow(true,9);
    }
                cb_yes.setChecked(!isChecked);
}
        });
        if ("无".equals(defaultValue)
                || StringUtil.isEmpty(defaultValue)) {
            cb_no.setChecked(true);
        } else {
            et.setText(defaultValue);
            cb_yes.setChecked(true);
        }

        attributelistContainer.addView(view);
    }

    public void onDestroy() {
        if (facilityOwnerShipUnitView != null) {
            facilityOwnerShipUnitView.onDestroy();
            facilityOwnerShipUnitView = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onSelectMenPaiFinished(DoorNOBean doorNOBean) {
        int type = doorNOBean.getType();
        if (type == 0) {
            if (!isContaintDoorNo(doorNOBean)) {
                mVals.add(doorNOBean);
                mFlowLayout.getAdapter().notifyDataChanged();
            }
        } else if (type == 1) {
            mVals.remove(currentIndex);
            if (!isContaintDoorNo(doorNOBean)) {
                doorNOBean = getDoorNoBean(doorNOBean);
                mVals.add(currentIndex, doorNOBean);
            }
            mFlowLayout.getAdapter().notifyDataChanged();
        }
//        if (!isContaintDoorNo(doorNOBean)) {
//            mVals.add(doorNOBean);
//            mFlowLayout.getAdapter().notifyDataChanged();
//        }
    }

    private DoorNOBean getDoorNoBean(DoorNOBean doorNOBean) {
        for (DoorNOBean doorNOBean1 : mOldVals) {
            if (doorNOBean.getMpObjectId().equals(doorNOBean1.getMpObjectId())) {
                return doorNOBean1;
            }
        }
        return doorNOBean;
    }

    private boolean isContaintDoorNo(DoorNOBean doorNOBean) {
        boolean isContaint = false;
        for (DoorNOBean doorNOBean1 : mVals) {
            if (doorNOBean.getMpObjectId()!=null && doorNOBean.getMpObjectId().equals(doorNOBean1.getMpObjectId())) {
                isContaint = true;
            }
        }
        return isContaint;
    }

}
