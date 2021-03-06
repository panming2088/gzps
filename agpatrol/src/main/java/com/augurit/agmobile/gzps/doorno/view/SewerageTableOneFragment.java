package com.augurit.agmobile.gzps.doorno.view;


import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.TableSewerageOneData;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.callback.Callback3;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.util.FileBean;
import com.augurit.agmobile.gzps.common.util.SewerageBeanManger;
import com.augurit.agmobile.gzps.common.widget.AgFilePicker;
import com.augurit.agmobile.gzps.doorno.model.DoorNOBean;
import com.augurit.agmobile.gzps.doorno.model.MyApplication;
import com.augurit.agmobile.gzps.doorno.model.PSHAffairDetail;
import com.augurit.agmobile.gzps.doorno.model.PhotoEvent;
import com.augurit.agmobile.gzps.doorno.model.SewerageInfoBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageItemBean;
import com.augurit.agmobile.gzps.doorno.model.SewerageRoomClickItemBean;
import com.augurit.agmobile.gzps.test.PhotoExampleActivity;
import com.augurit.agmobile.gzps.uploadfacility.util.SetMaxLengthUtils;
import com.augurit.agmobile.gzps.uploadfacility.view.doorno.UploadDoorNoMapActivity;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.bottomsheet.AnchorSheetBehavior;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.RadioGroupUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaoyu on 2018/4/4.
 */

public class SewerageTableOneFragment extends MyBaseFragment {
    private AgFilePicker.OnFilePickerClickListener onFilePickerClickListener;
    public onViewCreatedListener viewCreatedListener;
    private TableSewerageOneData tableSewerageOneData;
    private Context mContext;
    private Component mSelComponent;
    private String tv_one;
    private String tv_two;
    private String tv_three;
    private String tv_four;
    private String tv_five;
    private String tv_six;
    private String tv_seven;
    private String tv_eight;
    private String tv_nine;
    private String tv_ten;
    private String tv_water_no;
    private boolean sygypsh;
    private String edtunitname;
    private String psxkFzrq;//????????????????????????
    private String psxkJzrq;//??????????????????????????????
    private PSHAffairDetail.DataBean pshAffairDetailData;
    private boolean EDIT_MODE;
    private PSHAffairDetail pshAffairDetail;
    private ViewGroup approvalOpinionContainer;
    private PopupWindow mCurPopupWindow;
    private PopupWindow mCurPopupWindow2;
    private PopupWindow mCurPopupWindow3;
    private boolean fromMyUpload;//??????????????????(????????????????????????)????????????????????????,?????????EDIT_MODE?????????,?????????false
    private boolean isfromQuryAddressMapFragmnet;
    private boolean fromPSHAffair;//????????????
    private AnchorSheetBehavior mBehavior;
    private ViewGroup map_bottom_sheet;
    private List<Photo> bigImglist, bigImglist1, bigImglist2, bigImglist3, bigImglist4, bigImglist5, bigImglist7;
    private List<Photo> smallImgList, smallImgList1, smallImgList2, smallImgList3, smallImgList4, smallImgList5, smallImgList7;
    private List<FileBean> files, origFiles;
    private boolean isTempStorage;
    private Calendar cal;
    private Long startDate;
    private Long endDate;
    private boolean isAllowSaveLocalDraft;
    private SewerageInfoBean draftBean;
    private boolean isIndustry;

    public static SewerageTableOneFragment newInstance(Bundle bundle) {
        SewerageTableOneFragment sewerageTableOneFragment = new SewerageTableOneFragment();
        sewerageTableOneFragment.setArguments(bundle);
        return sewerageTableOneFragment;
    }

    @Override
    public int initview() {
        return R.layout.table_sewerage_one;
    }

    public void setViewCreatedListener(onViewCreatedListener viewCreatedListener) {
        this.viewCreatedListener = viewCreatedListener;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (viewCreatedListener != null) {
            viewCreatedListener.onViewCreated();
        }

    }

    @Override
    protected void initData() {
        super.initData();

        tableSewerageOneData = getBind();
//        setFocusabledfalse();  //??????????????????

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        files = new ArrayList<>();
        origFiles = new ArrayList<>();
        tableSewerageOneData.setTablesewerageoneonclick(new SewerageTableOneFragmentOnclick());//??????????????????
        tableSewerageOneData.setCheckchangelistener(new MyCheckChangeListener());//??????radiogroup????????????

        pshAffairDetail = (PSHAffairDetail) getArguments().getSerializable("pshAffair");
        fromMyUpload = getArguments().getBoolean("fromMyUpload", false);
        isIndustry = getArguments().getBoolean("isIndustry", false);
        fromPSHAffair = getArguments().getBoolean("fromPSHAffair", false);
        isfromQuryAddressMapFragmnet = getArguments().getBoolean("isfromQuryAddressMapFragmnet", false);
        isTempStorage = getArguments().getBoolean("isTempStorage", false);
        tableSewerageOneData.llState.setVisibility((fromMyUpload || fromPSHAffair) && !isTempStorage ? View.VISIBLE : View.GONE);
        draftBean = (SewerageInfoBean) getArguments().getSerializable("draftBean");
        isAllowSaveLocalDraft = getArguments().getBoolean("isAllowSaveLocalDraft", false);
        tableSewerageOneData.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SewerageDoorMapActivity.class);
                Bundle bundle = new Bundle();
                if (pshAffairDetail != null) {
                    bundle.putSerializable("pshAffair", pshAffairDetail);
                }
                bundle.putBoolean("EDIT_MODE",EDIT_MODE);
                bundle.putBoolean("isIndustry",isIndustry);
                intent.putExtras(bundle);
                startActivityForResult(intent,123);
            }
        });
        /**
         * ??????????????????????????????
         */
        SewerageRoomClickItemBean.UnitListBean roomclickunitListBean = (SewerageRoomClickItemBean.UnitListBean) getArguments().getSerializable("roomclickunitListBeans");
        SewerageItemBean.UnitListBean unitListBeans = (SewerageItemBean.UnitListBean) getArguments().getSerializable("unitListBeans");
//        if ((MyApplication.SEWERAGEITEMBEAN != null || unitListBeans != null) && roomclickunitListBean == null&&MyApplication.refreshListType == 1) {
        if (MyApplication.refreshListType == 1 && MyApplication.SEWERAGEITEMBEAN != null) {
            SewerageBeanManger.getInstance().setArea(MyApplication.SEWERAGEITEMBEAN.getXzq());//??????area?????????????????????
            SewerageBeanManger.getInstance().setVillage(MyApplication.SEWERAGEITEMBEAN.getJwh());//????????????
            SewerageBeanManger.getInstance().setStreet(MyApplication.SEWERAGEITEMBEAN.getJlx()); //??????stree??????
            SewerageBeanManger.getInstance().setJzwcode(MyApplication.SEWERAGEITEMBEAN.getMpwzhm());
            setViewOne(unitListBeans != null ? unitListBeans.getName() : "", MyApplication.SEWERAGEITEMBEAN.getXzj(),
                    null == MyApplication.doorBean ? "" : MyApplication.doorBean.getPSDY_OID(), null == MyApplication.doorBean ? "" : MyApplication.doorBean.getPSDY_NAME(),
                    isfromQuryAddressMapFragmnet ? MyApplication.SEWERAGEITEMBEAN.getJlx() + MyApplication.SEWERAGEITEMBEAN.getMpwzhm() : MyApplication.SEWERAGEITEMBEAN.getMpwzhm(), "",
                    MyApplication.SEWERAGEITEMBEAN.getXzq() + MyApplication.SEWERAGEITEMBEAN.getXzj() + (isfromQuryAddressMapFragmnet ? MyApplication.SEWERAGEITEMBEAN.getJlx() + MyApplication.SEWERAGEITEMBEAN.getMpwzhm() : MyApplication.SEWERAGEITEMBEAN.getMpwzhm()),
                    "", "", unitListBeans != null ? unitListBeans.getLegalRepresent() : "", "");
        }
        if (MyApplication.refreshListType == 2) {
            SewerageBeanManger.getInstance().setJzwcode(MyApplication.SEWERAGEITEMBEAN == null ? "" : MyApplication.SEWERAGEITEMBEAN.getMpwzhm());
            String roomNo = MyApplication.SEWERAGEROOMCLICKITEMBEAN != null && !TextUtils.isEmpty(MyApplication.SEWERAGEROOMCLICKITEMBEAN.getHouse_no()) ? MyApplication.SEWERAGEROOMCLICKITEMBEAN.getHouse_no() : "";
            setViewOne(roomclickunitListBean != null ? roomclickunitListBean.getName() : "", MyApplication.SEWERAGEROOMCLICKITEMBEAN.getXzj(),
                    null == MyApplication.doorBean ? "" : MyApplication.doorBean.getPSDY_OID(), null == MyApplication.doorBean ? "" : MyApplication.doorBean.getPSDY_NAME(),
                    MyApplication.SEWERAGEROOMCLICKITEMBEAN.getMpwzhm(), "",
                    MyApplication.SEWERAGEROOMCLICKITEMBEAN.getXzq() + MyApplication.SEWERAGEROOMCLICKITEMBEAN.getXzj() +
                            MyApplication.SEWERAGEROOMCLICKITEMBEAN.getMpwzhm() + (!TextUtils.isEmpty(MyApplication.SEWERAGEROOMCLICKITEMBEAN.getHouse_no()) ? roomNo : ""),
                    "", "", roomclickunitListBean != null ? roomclickunitListBean.getLegalRepresent() : "", "");
        }


        if (pshAffairDetail != null) {
            //?????????????????????????????????????????????????????????????????????????????????
            setFocusabledfalse();  //??????????????????
            setView(pshAffairDetail);
            tableSewerageOneData.photoItem.setPhotoExampleEnable(false);
            tableSewerageOneData.takephotoSewerageoneOne.setPhotoExampleEnable(false);
            tableSewerageOneData.takephotoSewerageoneThree.setPhotoExampleEnable(false);
            tableSewerageOneData.takephotoSewerageoneFour.setPhotoExampleEnable(false);
            tableSewerageOneData.takephotoSewerageoneFive.setPhotoExampleEnable(false);
            tableSewerageOneData.tvTableSewerageoneSeven.setReadOnly();
            tableSewerageOneData.tvTableSewerageoneFour.setReadOnly();
            tableSewerageOneData.tvTableSewerageoneTwo.setReadOnly();
            tableSewerageOneData.tvTableSewerageoneOne.setReadOnly();
        } else {
            init();//????????????????????????
        }

        setEditInputMaxLength();
        if (isAllowSaveLocalDraft) {
            setEditMode(true);
        }

        /**
         * ??????????????????????????????
         */

//        if ((MyApplication.SEWERAGEROOMCLICKITEMBEAN != null || roomclickunitListBean != null) && unitListBeans == null&&MyApplication.refreshListType == 2) {
        setFileListener();
    }

    private void setFileListener() {
        onFilePickerClickListener = new AgFilePicker.OnFilePickerClickListener() {
            @Override
            public void onDeleteButtonClick(int position, FileBean data) {
                if (data == null) return;
                if (!isAllowSaveLocalDraft) {
                    //????????????id????????????????????????????????????????????????????????????????????????????????????id
                    SewerageBeanManger.getInstance().setDelImg(data.getId() == 0 ? "" : data.getId() + "");
                }
                if (files.contains(data)) {
                    files.remove(data);
                    if (isAllowSaveLocalDraft) {
                        AMDatabase.getInstance().delete(data);
                    }
                } else {
                    for (FileBean fileBean : files) {
                        if (fileBean.getId() == data.getId()) {
                            files.remove(fileBean);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onItemClick(View view, int position, FileBean data) {
                if (TextUtils.isEmpty(data.getPath())) {
                    ToastUtil.shortToast(mContext, "?????????????????????");
                } else if (data.getPath().indexOf("http://") != -1) {
                    Intent intent = getPdfFileIntent2(data.getPath());
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        final ComponentName componentName = intent.resolveActivity(mContext.getPackageManager());
                        // ??????Log   ComponentName???????????????
                        startActivity(Intent.createChooser(intent, "??????????????????"));
                    } else {
                        ToastUtil.shortToast(mContext, "?????????????????????");
                    }
                } else {
                    startActivity(getPdfFileIntent(data.getPath()));
                }
            }

            @Override
            public void onAddFile(List<FileBean> datas) {
                files.clear();
                files.addAll(datas);
            }
        };

        tableSewerageOneData.filePicker.setOnFilePickerListener(onFilePickerClickListener);
    }

    private void setEditInputMaxLength() {
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneOne.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneSix.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_150);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneSeven.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_40);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneEight.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_18 >> 1);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneNine.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_40);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvTableSewerageoneTen.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_18 >> 1);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvSewerageOneCode1.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvSewerageOneCode2.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_50);
        SetMaxLengthUtils.setMaxLength(tableSewerageOneData.tvSewerageOneCode3.getEt_right(), SetMaxLengthUtils.MAX_LENGTH_50);
    }

    private void init() {
        initEditText();
        setNoticeIcon();
    }

    private void initEditText() {

        tableSewerageOneData.tvTableSewerageoneFive.setInputTypeAboutNum();
        tableSewerageOneData.tvTableSewerageoneEight.setInputTypeAboutNum();
        tableSewerageOneData.tvTableSewerageoneTen.setInputTypeAboutNum();

        tableSewerageOneData.photoItem.setPhotoNumShow(true, 9);
        tableSewerageOneData.takephotoSewerageoneOne.setPhotoNumShow(true, 3);
        tableSewerageOneData.takephotoSewerageoneTwo.setPhotoNumShow(true, 3);
        tableSewerageOneData.takephotoSewerageoneThree.setPhotoNumShow(true, 3);
        tableSewerageOneData.takephotoSewerageoneFour.setPhotoNumShow(true, 3);
        tableSewerageOneData.takephotoSewerageoneFive.setPhotoNumShow(true, 3);
        tableSewerageOneData.takephotoSewerageoneSeven.setPhotoNumShow(true, 3);
        tableSewerageOneData.filePicker.setMaxLimit(3);
//        tableSewerageOneData.filePicker.showHint(true);
//        tableSewerageOneData.filePicker.setHint("?????????");
//        tableSewerageOneData.filePicker.setErrorText("pdf??????");
//        tableSewerageOneData.filePicker.showRequireTag(true);
//        tableSewerageOneData.filePicker.setTextViewName("????????????");????????????
        tableSewerageOneData.filePicker.setTextViewContentTextAndColor("????????????", R.color.black);

        tableSewerageOneData.tvTableSewerageoneFour.setReadOnly();
//        tableSewerageOneData.tvTableSewerageoneSix.setEditable(false);
        //   tableSewerageOneData.tvTableSewerageoneFour.setEditable(true);
        if (!EDIT_MODE) {
            tableSewerageOneData.tvTableSewerageoneOne.setEditable(true);
            tableSewerageOneData.tvTableSewerageoneTwo.setReadOnly();
        } else {
            tableSewerageOneData.tvTableSewerageoneOne.setEditable(true);
            tableSewerageOneData.tvTableSewerageoneTwo.setReadOnly();
//          tableSewerageOneData.filePicker.setHint("?????????");
//          tableSewerageOneData.filePicker.showRequireTag(true);
            tableSewerageOneData.filePicker.setErrorText("?????????pdf??????");
            tableSewerageOneData.filePicker.setTextViewName("????????????");
            tableSewerageOneData.filePicker.setTextViewContentTextAndColor("????????????", R.color.black);

        }


        //??????????????????
        tableSewerageOneData.photoItem.setPhotoExampleEnable(true);
        tableSewerageOneData.takephotoSewerageoneOne.setPhotoExampleEnable(true);
        tableSewerageOneData.takephotoSewerageoneThree.setPhotoExampleEnable(true);
        tableSewerageOneData.takephotoSewerageoneFour.setPhotoExampleEnable(true);
        initEvent();


//        ((RadioButton) tableSewerageOneData.rgTableSewerageoneOne.getChildAt(0)).setChecked(true);//?????????????????????
//        ((RadioButton) tableSewerageOneData.rgTableSewerageoneTwo.getChildAt(0)).setChecked(true);//?????????????????????
//        ((RadioButton) tableSewerageOneData.rgTableSewerageoneThree.getChildAt(0)).setChecked(true);//?????????????????????
//        ((RadioButton) tableSewerageOneData.rgTableSewerageoneFour.getChildAt(0)).setChecked(true);//?????????????????????

    }

    private void initEvent() {
        tableSewerageOneData.photoItem.getmExample().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PhotoExampleActivity.class);
                getContext().startActivity(intent);
            }
        });
        tableSewerageOneData.takephotoSewerageoneOne.getmExample().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurPopupWindow = showTipPopupWindow(tableSewerageOneData.takephotoSewerageoneOne.getmExample(), getContext().getString(R.string.sew_tab1_photo_exam), null);
            }
        });
        tableSewerageOneData.takephotoSewerageoneThree.getmExample().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurPopupWindow2 = showTipPopupWindow(tableSewerageOneData.takephotoSewerageoneThree.getmExample(), getContext().getString(R.string.sew_tab1_photo_exam2), null);

            }
        });
        tableSewerageOneData.takephotoSewerageoneFour.getmExample().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurPopupWindow3 = showTipPopupWindow(tableSewerageOneData.takephotoSewerageoneFour.getmExample(), getContext().getString(R.string.sew_tab1_photo_exam3), null);

            }
        });
        tableSewerageOneData.llTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurPopupWindow != null) {
                    mCurPopupWindow.dismiss();
                }
                if (mCurPopupWindow2 != null) {
                    mCurPopupWindow2.dismiss();
                }
                if (mCurPopupWindow3 != null) {
                    mCurPopupWindow3.dismiss();
                }
            }
        });

        cal = Calendar.getInstance();
        tableSewerageOneData.btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate != null) {
                    cal.setTimeInMillis(startDate);
                }
                showDatePickerDialog(tableSewerageOneData.btnStartTime, cal, START_DATE);
            }
        });
        tableSewerageOneData.btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endDate != null) {
                    cal.setTimeInMillis(endDate);
                }
                showDatePickerDialog(tableSewerageOneData.btnEndTime, cal, END_DATE);
            }
        });
    }

    private static final int START_DATE = 1;
    private static final int END_DATE = 2;

    public void showDatePickerDialog(final Button btn, Calendar calendar, final int type) {
        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        display(btn, year, monthOfYear, dayOfMonth);
                        if (type == START_DATE) {
                            startDate = new Date(year - 1900, monthOfYear, dayOfMonth).getTime();
                        } else {
                            endDate = new Date(year - 1900, monthOfYear, dayOfMonth + 1).getTime();
                        }
                    }
                }
                // ??????????????????
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * ????????????
     */
    public void display(Button dateDisplay, int year,
                        int monthOfYear, int dayOfMonth) {
        dateDisplay.setText(new StringBuffer().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth));
    }


    /**
     * ???????????????????????????popuWindow????????????MultiSelectFlexLayout????????????
     *
     * @param anchorView      ??????popuwindo??????????????????View
     * @param hint            popuwindow ????????????????????????popuwindow ??????????????????.9??????????????????????????????TextView,???????????????????????????????????????
     * @param onClickListener popuwindo??????????????????
     * @return ????????????popuWindo
     */
    public PopupWindow showTipPopupWindow(final View anchorView, String hint, final View.OnClickListener onClickListener) {

        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.popuw_content_top_arrow_layout, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        TextView textView = (TextView) contentView.findViewById(R.id.tip_text);
        textView.setText(hint);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // ???????????????????????????
                autoAdjustArrowPos(popupWindow, contentView, anchorView);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        // ???????????????PopupWindow????????????????????????????????????????????????????????????????????????????????????Back????????????dismiss??????
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // setOutsideTouchable????????????????????????setTouchable(true)???setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // ?????????true?????????PopupWindow???????????? ???????????????????????????
        popupWindow.setTouchable(true);

        // true?????????????????????????????? PopupWindow
        // ???????????????true???setOutsideTouchable???setTouchable??????????????????????????????????????????????????????????????????????????????
        // false???PopupWindow??????????????????
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // ??????????????????????????????
            }
        });
        // ????????????showAsDropDown?????????????????????????????????????????????anchorView???????????????
        // ???????????????PopupWindow?????????????????????????????????wrap_content
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }

    private void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
        View upArrow = contentView.findViewById(R.id.up_arrow);
        View downArrow = contentView.findViewById(R.id.down_arrow);

        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
        upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

        RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
        upArrowParams.leftMargin = arrowLeftMargin;
        RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
        downArrowParams.leftMargin = arrowLeftMargin;
    }

    private void setListener() {
        if (EDIT_MODE && pshAffairDetail != null) {
            init();
            bigImglist = new ArrayList<>();
            bigImglist1 = new ArrayList<>();
            bigImglist2 = new ArrayList<>();
            bigImglist3 = new ArrayList<>();
            bigImglist4 = new ArrayList<>();
            bigImglist5 = new ArrayList<>();
            bigImglist7 = new ArrayList<>();
            smallImgList = new ArrayList<>();
            smallImgList1 = new ArrayList<>();
            smallImgList2 = new ArrayList<>();
            smallImgList3 = new ArrayList<>();
            smallImgList4 = new ArrayList<>();
            smallImgList5 = new ArrayList<>();
            smallImgList7 = new ArrayList<>();
            if (draftBean != null) {
                bigImglist.addAll(draftBean.getPhotos0());
                smallImgList.addAll(draftBean.getThumbnailPhotos0());

                bigImglist1.addAll(draftBean.getPhotos1());
                smallImgList1.addAll(draftBean.getThumbnailPhotos1());

                bigImglist2.addAll(draftBean.getPhotos2());
                smallImgList2.addAll(draftBean.getThumbnailPhotos2());

                bigImglist3.addAll(draftBean.getPhotos3());
                smallImgList3.addAll(draftBean.getThumbnailPhotos3());

                bigImglist.addAll(draftBean.getPhotos0());
                smallImgList.addAll(draftBean.getThumbnailPhotos0());

                bigImglist4.addAll(draftBean.getPhotos4());
                smallImgList4.addAll(draftBean.getThumbnailPhotos4());

                bigImglist5.addAll(draftBean.getPhotos5());
                smallImgList5.addAll(draftBean.getThumbnailPhotos5());

                bigImglist7.addAll(draftBean.getPhotos7());
                smallImgList7.addAll(draftBean.getThumbnailPhotos7());

                files.addAll(draftBean.getFiles());
            }

            tableSewerageOneData.photoItem.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList.contains(photo2)) {
                        smallImgList.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist.contains(photo)) {
                        bigImglist.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });

            tableSewerageOneData.takephotoSewerageoneOne.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList1.contains(photo2)) {
                        smallImgList1.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist1.contains(photo)) {
                        bigImglist1.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });
            tableSewerageOneData.takephotoSewerageoneTwo.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList2.contains(photo2)) {
                        smallImgList2.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist2.contains(photo)) {
                        bigImglist2.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });
            tableSewerageOneData.takephotoSewerageoneThree.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList3.contains(photo2)) {
                        smallImgList3.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist3.contains(photo)) {
                        bigImglist3.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });
            tableSewerageOneData.takephotoSewerageoneFour.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList4.contains(photo2)) {
                        smallImgList4.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist4.contains(photo)) {
                        bigImglist4.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });

            tableSewerageOneData.takephotoSewerageoneFive.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList5.contains(photo2)) {
                        smallImgList5.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist5.contains(photo)) {
                        bigImglist5.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });

            tableSewerageOneData.takephotoSewerageoneSeven.setOnDeletePhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    SewerageBeanManger.getInstance().setDelImg(photo.getId() == 0 ? "" : photo.getId() + "");
                    if (photo2 != null && smallImgList7.contains(photo2)) {
                        smallImgList7.remove(photo2);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                    if (bigImglist7.contains(photo)) {
                        bigImglist7.remove(photo);
                        if (draftBean != null && isAllowSaveLocalDraft) {
                            AMDatabase.getInstance().delete(photo);
                        }
                    }
                }
            });


            tableSewerageOneData.filePicker.setEnable(EDIT_MODE);
            tableSewerageOneData.filePicker.setReadOnly(!EDIT_MODE);
            if (EDIT_MODE) {
                if (tableSewerageOneData.rbTableSewerageoneFiveYes.isChecked()) {
                    tableSewerageOneData.filePicker.setVisibility(View.VISIBLE);
                    tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.VISIBLE);
                }
            }
            tableSewerageOneData.photoItem.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist.add(photo);
                    smallImgList.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneOne.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist1.add(photo);
                    smallImgList1.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneTwo.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist2.add(photo);
                    smallImgList2.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneThree.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist3.add(photo);
                    smallImgList3.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneFour.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist4.add(photo);
                    smallImgList4.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneFive.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist5.add(photo);
                    smallImgList5.add(photo2);
                }
            });
            tableSewerageOneData.takephotoSewerageoneSeven.setOnAddPhotoListener(new Callback3<Photo, Photo>() {
                @Override
                public void onCallback(Photo photo, Photo photo2) {
                    bigImglist7.add(photo);
                    smallImgList7.add(photo2);
                }
            });
        }

    }

    //??????????????????
    public Intent getPdfFileIntent(String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        i.setDataAndType(uri, "application/pdf");
        return i;
    }

    //????????????
    public Intent getPdfFileIntent2(String path) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(path));
        // ?????????????????????intent.resolveActivity()?????????????????????Intent???Activity??????????????????
        // ???????????? : Name of the component implementing an activity that can display the intent
        return intent;
    }

    private void setViewOne(String tvone, String tvtwo, String edtfq, String tvthree, String tvfour, String tvfive, String tvsix, String tvseven, String tveight, String tvnine, String tvTen) {
        tableSewerageOneData.tvTableSewerageoneOne.setText(tvone);
        tableSewerageOneData.tvTableSewerageoneTwo.setText(tvtwo);
        tableSewerageOneData.tvTableSewerageoneThree.setText(tvthree);
        tableSewerageOneData.tvTableSewerageoneFour.setText(tvfour);
        tableSewerageOneData.tvTableSewerageoneFive.setText(tvfive);
        tableSewerageOneData.tvTableSewerageoneSix.setText(tvsix);
        tableSewerageOneData.tvTableSewerageoneSeven.setText(tvseven);
        tableSewerageOneData.tvTableSewerageoneEight.setText(tveight);
        tableSewerageOneData.tvTableSewerageoneNine.setText(tvnine);
        tableSewerageOneData.tvTableSewerageoneTen.setText(tvTen);
        tableSewerageOneData.edtUnitName.setText(edtfq);


    }

    /**
     * ??????????????????????????????
     */
    public void getString() {
        tv_one = tableSewerageOneData.tvTableSewerageoneOne.getText().toString();
        tv_two = tableSewerageOneData.tvTableSewerageoneTwo.getText().toString();
        tv_three = tableSewerageOneData.tvTableSewerageoneThree.getText().toString();
        tv_four = tableSewerageOneData.tvTableSewerageoneFour.getText().toString();
        tv_five = tableSewerageOneData.tvTableSewerageoneFive.getText().toString();
        tv_six = tableSewerageOneData.tvTableSewerageoneSix.getText().toString();
        tv_seven = tableSewerageOneData.tvTableSewerageoneSeven.getText().toString();
        tv_eight = tableSewerageOneData.tvTableSewerageoneEight.getText().toString();
        tv_nine = tableSewerageOneData.tvTableSewerageoneNine.getText().toString();
        tv_ten = tableSewerageOneData.tvTableSewerageoneTen.getText().toString();
        tv_water_no = tableSewerageOneData.tvTableSewerageoneWaterno.getText().toString();
        edtunitname = tableSewerageOneData.edtUnitName.getText().toString();
        sygypsh = tableSewerageOneData.cbSygypsh.isChecked() ? true : false;
        psxkFzrq = dateToStamp(tableSewerageOneData.btnStartTime.getText().toString());
        psxkJzrq = dateToStamp(tableSewerageOneData.btnEndTime.getText().toString());
    }


    /*
     * ???????????????????????????
     */
    public String dateToStamp(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(s);
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            getString();
//            CheckString();
            getTab1AllParams();
            hideBoottomSheet();
        } else {
            System.out.println("????????????");
        }
    }

    public void getTab1AllParams() {
        if (!EDIT_MODE || isAllowSaveLocalDraft) {
            SewerageBeanManger.getInstance().setPhotos0(tableSewerageOneData.photoItem.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos1(tableSewerageOneData.takephotoSewerageoneOne.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos2(tableSewerageOneData.takephotoSewerageoneTwo.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos3(tableSewerageOneData.takephotoSewerageoneThree.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos4(tableSewerageOneData.takephotoSewerageoneFour.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos5(tableSewerageOneData.takephotoSewerageoneFive.getSelectedPhotos());
            SewerageBeanManger.getInstance().setPhotos7(tableSewerageOneData.takephotoSewerageoneSeven.getSelectedPhotos());
            SewerageBeanManger.getInstance().setFiles(tableSewerageOneData.filePicker.getSelectFiles());

            SewerageBeanManger.getInstance().setThumbnailPhotos0(tableSewerageOneData.photoItem.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos1(tableSewerageOneData.takephotoSewerageoneOne.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos2(tableSewerageOneData.takephotoSewerageoneTwo.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos3(tableSewerageOneData.takephotoSewerageoneThree.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos4(tableSewerageOneData.takephotoSewerageoneFour.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos5(tableSewerageOneData.takephotoSewerageoneFive.getThumbnailPhotos());
            SewerageBeanManger.getInstance().setThumbnailPhotos7(tableSewerageOneData.takephotoSewerageoneSeven.getThumbnailPhotos());

        } else {
            SewerageBeanManger.getInstance().setPhotos0(bigImglist);
            SewerageBeanManger.getInstance().setPhotos1(bigImglist1);
            SewerageBeanManger.getInstance().setPhotos2(bigImglist2);
            SewerageBeanManger.getInstance().setPhotos3(bigImglist3);
            SewerageBeanManger.getInstance().setPhotos4(bigImglist4);
            SewerageBeanManger.getInstance().setPhotos5(bigImglist5);
            SewerageBeanManger.getInstance().setPhotos7(bigImglist7);

            SewerageBeanManger.getInstance().setThumbnailPhotos0(smallImgList);
            SewerageBeanManger.getInstance().setThumbnailPhotos1(smallImgList1);
            SewerageBeanManger.getInstance().setThumbnailPhotos2(smallImgList2);
            SewerageBeanManger.getInstance().setThumbnailPhotos3(smallImgList3);
            SewerageBeanManger.getInstance().setThumbnailPhotos4(smallImgList4);
            SewerageBeanManger.getInstance().setThumbnailPhotos5(smallImgList5);
            SewerageBeanManger.getInstance().setThumbnailPhotos7(smallImgList7);
            ArrayList<FileBean> tempFiles = new ArrayList<>();
            tempFiles.addAll(files);
            for (FileBean fileBean : origFiles) {
                if (files.contains(fileBean)) {
                    tempFiles.remove(fileBean);
                }
            }
            SewerageBeanManger.getInstance().setFiles(tempFiles);
        }

        SewerageBeanManger.getInstance().setName(tv_one);//???????????????
        SewerageBeanManger.getInstance().setTown(tv_two);//????????????
        SewerageBeanManger.getInstance().setPsdy(edtunitname);//??????????????????
        SewerageBeanManger.getInstance().setFqname(tv_three);//????????????
        SewerageBeanManger.getInstance().setMph(tv_four);//?????????
        SewerageBeanManger.getInstance().setVolume(tv_five);//????????????
        SewerageBeanManger.getInstance().setAddr(tv_six);//????????????
        SewerageBeanManger.getInstance().setOwner(tv_seven);//?????????
        SewerageBeanManger.getInstance().setOwnerTele(tv_eight);//?????????????????????
        SewerageBeanManger.getInstance().setOperator(tv_nine);//?????????
        SewerageBeanManger.getInstance().setOperatorTele(tv_ten);//?????????????????????
        SewerageBeanManger.getInstance().setPsxkFzrq(psxkFzrq);//????????????????????????
        SewerageBeanManger.getInstance().setPsxkJzrq(psxkJzrq);//??????????????????
        SewerageBeanManger.getInstance().setWaterNo(tv_water_no);//?????????
        SewerageBeanManger.getInstance().setSfgypsh(sygypsh);//????????????????????????


        if (!StringUtil.isEmpty(psxkFzrq) && !StringUtil.isEmpty(psxkJzrq) && Long.valueOf(psxkFzrq) < Long.valueOf(psxkJzrq)) {
            SewerageBeanManger.getInstance().setIsRight("1");//??????????????????
        } else {
            SewerageBeanManger.getInstance().setIsRight("0");
        }

        if (tableSewerageOneData.rbTableSewerageoneOneYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert1("1");
        }
    /*    if (tableSewerageOneData.rbTableSewerageoneOneNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert1("0");
        }
        if (tableSewerageOneData.rbTableSewerageoneTwoYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert2("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneTwoNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert2("0");
        }

        if (tableSewerageOneData.rbTableSewerageoneThreeYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert3("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneThreeNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert3("0");
        }

        if (tableSewerageOneData.rbTableSewerageoneFourYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert4("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneFourNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert4("0");
        }*/
        if (tableSewerageOneData.rbTableSewerageoneOneNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert1("0");
            SewerageBeanManger.getInstance().setPhotos1(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos1(null);
            SewerageBeanManger.getInstance().setCert1Code("");
        }
        if (tableSewerageOneData.rbTableSewerageoneTwoYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert2("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneTwoNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert2("0");
            SewerageBeanManger.getInstance().setPhotos2(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos2(null);
        }

        if (tableSewerageOneData.rbTableSewerageoneThreeYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert3("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneThreeNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert3("0");
            SewerageBeanManger.getInstance().setPhotos3(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos3(null);
        }

        if (tableSewerageOneData.rbTableSewerageoneSevenYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert7("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneSevenNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert7("0");
            SewerageBeanManger.getInstance().setPhotos7(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos7(null);
        }


        if (tableSewerageOneData.rbTableSewerageoneFourYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert4("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneFourNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert4("0");
            SewerageBeanManger.getInstance().setPhotos4(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos4(null);
        }

        if (tableSewerageOneData.rbTableSewerageoneFiveYes.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert5("1");
        }
        if (tableSewerageOneData.rbTableSewerageoneFiveNo.isChecked()) {
            SewerageBeanManger.getInstance().setHasCert5("0");
            SewerageBeanManger.getInstance().setPhotos5(null);
            SewerageBeanManger.getInstance().setThumbnailPhotos5(null);
            SewerageBeanManger.getInstance().setFiles(null);
        }

        //SewerageBeanManger.getInstance().setHasCert1(tableSewerageOneData.rbTableSewerageoneOneYes.isChecked() ? "1" : "0");
        SewerageBeanManger.getInstance().setCert1Code(tableSewerageOneData.rbTableSewerageoneOneYes.isChecked() ? tableSewerageOneData.tvSewerageOneCode1.getText() : "");

        // SewerageBeanManger.getInstance().setHasCert2(tableSewerageOneData.rbTableSewerageoneTwoYes.isChecked() ? "1" : "0");

        //   SewerageBeanManger.getInstance().setHasCert3(tableSewerageOneData.rbTableSewerageoneThreeYes.isChecked() ? "1" : "0");
        SewerageBeanManger.getInstance().setCert3Code(tableSewerageOneData.rbTableSewerageoneThreeYes.isChecked() ? tableSewerageOneData.tvSewerageOneCode2.getText() : "");

        //  SewerageBeanManger.getInstance().setHasCert4(tableSewerageOneData.rbTableSewerageoneFourYes.isChecked() ? "1" : "0");
        SewerageBeanManger.getInstance().setCert4Code(tableSewerageOneData.rbTableSewerageoneFourYes.isChecked() ? tableSewerageOneData.tvSewerageOneCode3.getText() : "");

        SewerageBeanManger.getInstance().setDescription(tableSewerageOneData.tfTableUploadIndicate.getText());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        EventBus.getDefault().register(this);
    }

    private void setView(PSHAffairDetail pshAffairDetail) {
        pshAffairDetailData = pshAffairDetail.getData();

        tableSewerageOneData.llUploadInfo.setVisibility(View.VISIBLE);
        if (isAllowSaveLocalDraft) {
            tableSewerageOneData.llUploadInfo.setVisibility(View.GONE);
        }
        tableSewerageOneData.tvTableUploadEditTime.setVisibility(View.VISIBLE);
        if (pshAffairDetailData != null) {
            tableSewerageOneData.btnMap.setVisibility(View.VISIBLE);
        } else {
            tableSewerageOneData.btnMap.setVisibility(View.GONE);
        }
        //??????????????????
        if (null == pshAffairDetailData) {
            tableSewerageOneData.photoItem.setVisibility(View.GONE);
//            tableSewerageOneData.llState.setVisibility(View.GONE);
            return;
        }
        tableSewerageOneData.tvSewerageOneCode1.setText(pshAffairDetailData.getCert1Code());
        tableSewerageOneData.tvSewerageOneCode2.setText(pshAffairDetailData.getCert3Code());
        tableSewerageOneData.tvSewerageOneCode3.setText(pshAffairDetailData.getCert4Code());


        if (!isAllowSaveLocalDraft) {
            List<PSHAffairDetail.DataBean.SewerageUserAttachmentBean> sewerageUserAttachment = pshAffairDetailData.getSewerageUserAttachment();
            List<PSHAffairDetail.DataBean.HasCert1AttachmentBean> hasCert1Attachment = pshAffairDetailData.getHasCert1Attachment();
            List<PSHAffairDetail.DataBean.HasCert2AttachmentBean> hasCert2Attachment = pshAffairDetailData.getHasCert2Attachment();
            List<PSHAffairDetail.DataBean.HasCert3AttachmentBean> hasCert3Attachment = pshAffairDetailData.getHasCert3Attachment();
            List<PSHAffairDetail.DataBean.HasCert4AttachmentBean> hasCert4Attachment = pshAffairDetailData.getHasCert4Attachment();
            List<PSHAffairDetail.DataBean.HasCert5AttachmentBean> hasCert5Attachment = pshAffairDetailData.getHasCert5Attachment();
            List<PSHAffairDetail.DataBean.HasCert6AttachmentBean> hasCert6Attachment = pshAffairDetailData.getHasCert6Attachment();
            List<PSHAffairDetail.DataBean.HasCert7AttachmentBean> hasCert7Attachment = pshAffairDetailData.getHasCert7Attachment();

            if (!ListUtil.isEmpty(sewerageUserAttachment)) {
                List<Photo> photoList = new ArrayList<>();
                for (PSHAffairDetail.DataBean.SewerageUserAttachmentBean attachmentBean : sewerageUserAttachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList.add(photo);
                }
                tableSewerageOneData.photoItem.setSelectedPhotos(photoList);
            }


            if (!ListUtil.isEmpty(hasCert1Attachment)) {
                List<Photo> photoList1 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert1AttachmentBean attachmentBean : hasCert1Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList1.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneOne.setSelectedPhotos(photoList1);
            }
            if (!ListUtil.isEmpty(hasCert2Attachment)) {
                List<Photo> photoList2 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert2AttachmentBean attachmentBean : hasCert2Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList2.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneTwo.setSelectedPhotos(photoList2);
            }
            if (!ListUtil.isEmpty(hasCert3Attachment)) {
                List<Photo> photoList3 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert3AttachmentBean attachmentBean : hasCert3Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList3.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneThree.setSelectedPhotos(photoList3);
            }
            if (!ListUtil.isEmpty(hasCert4Attachment)) {
                List<Photo> photoList4 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert4AttachmentBean attachmentBean : hasCert4Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList4.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneFour.setSelectedPhotos(photoList4);
            }

            if (!ListUtil.isEmpty(hasCert5Attachment)) {
                List<Photo> photoList5 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert5AttachmentBean attachmentBean : hasCert5Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList5.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneFive.setSelectedPhotos(photoList5);
            }

            if (!ListUtil.isEmpty(hasCert6Attachment)) {
                ArrayList<FileBean> fileBeans = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert6AttachmentBean attachmentBean : hasCert6Attachment) {
                    FileBean fileBean = new FileBean();
                    fileBean.setId(attachmentBean.getId());
                    fileBean.setName(attachmentBean.getAttName());
                    fileBean.setPath(attachmentBean.getAttPath());
                    fileBean.setMimeType(attachmentBean.getAttType());
                    fileBean.setSewerageUserId(attachmentBean.getSewerageUserId());
                    fileBeans.add(fileBean);
                }
                tableSewerageOneData.filePicker.setValue(fileBeans);
                files.addAll(fileBeans);
                origFiles.addAll(fileBeans);
            }

            if (!ListUtil.isEmpty(hasCert7Attachment)) {
                List<Photo> photoList7 = new ArrayList<>();
                for (PSHAffairDetail.DataBean.HasCert7AttachmentBean attachmentBean : hasCert7Attachment) {
                    Photo photo = new Photo();
                    photo.setId(attachmentBean.getId());
                    photo.setHasBeenUp(1);
                    photo.setPhotoPath(attachmentBean.getAttPath());
                    photo.setPhotoName(attachmentBean.getAttName());
                    photo.setThumbPath(attachmentBean.getThumPath());
                    photo.setField1("photo");
                    photoList7.add(photo);
                }
                tableSewerageOneData.takephotoSewerageoneSeven.setSelectedPhotos(photoList7);
            }


            if ("0".equals(pshAffairDetailData.getState())) {
                tableSewerageOneData.tvApproveState.setText("????????????");
                tableSewerageOneData.tvApproveState.setTextColor(Color.parseColor("#b1afab"));
            } else if ("2".equals(pshAffairDetailData.getState())) {
                tableSewerageOneData.tvApproveState.setText("?????????");
                tableSewerageOneData.tvApproveState.setTextColor(Color.parseColor("#3EA500"));
            } else if ("3".equals(pshAffairDetailData.getState())) {
                tableSewerageOneData.tvApproveState.setText("????????????");
                tableSewerageOneData.tvApproveState.setTextColor(Color.parseColor("#FFFFC248"));
            } else {
                tableSewerageOneData.tvApproveState.setText("?????????");
                tableSewerageOneData.tvApproveState.setTextColor(Color.RED);
            }

            map_bottom_sheet = tableSewerageOneData.mapBottomSheet;
            mBehavior = AnchorSheetBehavior.from(map_bottom_sheet);
            mBehavior.setAnchorHeight(DensityUtil.dp2px(mContext, 230));
            approvalOpinionContainer = (ViewGroup) tableSewerageOneData.mapBottomSheet.findViewById(R.id.ll_approval_opinion_container);
            tableSewerageOneData.approveDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tableSewerageOneData.svTab1.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        hideBoottomSheet();
                    }
                });
            }
        } else {//?????????????????????????????????????????????
            tableSewerageOneData.photoItem.setSelectedPhotos(draftBean.getPhotos0());
            tableSewerageOneData.photoItem.setSelThumbPhotos(draftBean.getThumbnailPhotos0());
            tableSewerageOneData.takephotoSewerageoneOne.setSelectedPhotos(draftBean.getPhotos1());
            tableSewerageOneData.takephotoSewerageoneOne.setSelThumbPhotos(draftBean.getThumbnailPhotos1());
            tableSewerageOneData.takephotoSewerageoneTwo.setSelectedPhotos(draftBean.getPhotos2());
            tableSewerageOneData.takephotoSewerageoneTwo.setSelThumbPhotos(draftBean.getThumbnailPhotos2());
            tableSewerageOneData.takephotoSewerageoneThree.setSelectedPhotos(draftBean.getPhotos3());
            tableSewerageOneData.takephotoSewerageoneThree.setSelThumbPhotos(draftBean.getThumbnailPhotos3());
            tableSewerageOneData.takephotoSewerageoneFour.setSelectedPhotos(draftBean.getPhotos4());
            tableSewerageOneData.takephotoSewerageoneFour.setSelThumbPhotos(draftBean.getThumbnailPhotos4());
            tableSewerageOneData.takephotoSewerageoneFive.setSelectedPhotos(draftBean.getPhotos5());
            tableSewerageOneData.takephotoSewerageoneFive.setSelThumbPhotos(draftBean.getThumbnailPhotos5());
            tableSewerageOneData.takephotoSewerageoneSeven.setSelectedPhotos(draftBean.getPhotos7());
            tableSewerageOneData.takephotoSewerageoneSeven.setSelThumbPhotos(draftBean.getThumbnailPhotos7());

            tableSewerageOneData.filePicker.setValue(draftBean.getFiles());
        }


        tableSewerageOneData.tvTableSewerageoneOne.setText(pshAffairDetailData.getName());
        tableSewerageOneData.tvTableSewerageoneTwo.setText(pshAffairDetailData.getTown());
        tableSewerageOneData.tvTableSewerageoneThree.setText(pshAffairDetailData.getPsdy());
        tableSewerageOneData.tvTableSewerageoneFour.setText(pshAffairDetailData.getMph());
        tableSewerageOneData.tvTableSewerageoneFive.setText(pshAffairDetailData.getVolume());
        tableSewerageOneData.tvTableSewerageoneSix.setText(pshAffairDetailData.getAddr());
        tableSewerageOneData.tvTableSewerageoneSeven.setText(pshAffairDetailData.getOwner());
        tableSewerageOneData.tvTableSewerageoneEight.setText(pshAffairDetailData.getOwnerTele());
        tableSewerageOneData.tvTableSewerageoneNine.setText(pshAffairDetailData.getOperator());
        tableSewerageOneData.tvTableSewerageoneTen.setText(pshAffairDetailData.getOperatorTele());
        tableSewerageOneData.tvTableSewerageoneWaterno.setText(pshAffairDetailData.getWaterNo());
        tableSewerageOneData.cbSygypsh.setChecked(pshAffairDetailData.isSfgypsh());

        if ("???????????????".equals(pshAffairDetailData.getPsxkLx())) {
            tableSewerageOneData.rgTableSewerageoneThreeOne.check(tableSewerageOneData.rbTableSewerageoneThreeZd.getId());
            SewerageBeanManger.getInstance().setPsxkLx("???????????????");//???????????????
        } else if ("???????????????".equals(pshAffairDetailData.getPsxkLx())) {
            tableSewerageOneData.rgTableSewerageoneThreeOne.check(tableSewerageOneData.rbTableSewerageoneThreeYb.getId());
            SewerageBeanManger.getInstance().setPsxkLx("???????????????");//???????????????
        }
        if (!TextUtils.isEmpty(pshAffairDetailData.getPsxkFzrq()) && !"0".equals(pshAffairDetailData.getPsxkFzrq())) {
            Date date = new Date(Long.parseLong(pshAffairDetailData.getPsxkFzrq()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date);
            tableSewerageOneData.btnStartTime.setText(format);
        }

        if (!TextUtils.isEmpty(pshAffairDetailData.getPsxkJzrq()) && !"0".equals(pshAffairDetailData.getPsxkJzrq())) {
            Date date = new Date(Long.parseLong(pshAffairDetailData.getPsxkJzrq()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date);
            tableSewerageOneData.btnEndTime.setText(format);
        }
        tableSewerageOneData.tfTableUploadIndicate.setText(pshAffairDetailData.getDescription());
        tableSewerageOneData.tvTableUploadName.setText(pshAffairDetailData.getMarkPerson());
        tableSewerageOneData.tvTableUploadOrg.setText(pshAffairDetailData.getParentOrgName());
        tableSewerageOneData.tvTableUploadTime.setText(TimeUtil.getStringTimeMdHmChines(new Date(pshAffairDetailData.getMarkTime())));
        try {
            tableSewerageOneData.tvTableUploadEditTime.setText(TextUtils.isEmpty(pshAffairDetailData.getUpdateTime()) ? "" : TimeUtil.getStringTimeMdHmChines(new Date(Long.parseLong(pshAffairDetailData.getUpdateTime()))));
        } catch (Exception e) {
        }


   /*     tableSewerageOneData.rgTableSewerageoneOne.check("1".equals(pshAffairDetailData.getHasCert1()) ? tableSewerageOneData.rbTableSewerageoneOneYes.getId() :
                tableSewerageOneData.rbTableSewerageoneOneNo.getId()
        );*/
        if ("1".equals(pshAffairDetailData.getHasCert1())) {
            tableSewerageOneData.rgTableSewerageoneOne.check(tableSewerageOneData.rbTableSewerageoneOneYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert1())) {
            tableSewerageOneData.rgTableSewerageoneOne.check(tableSewerageOneData.rbTableSewerageoneOneNo.getId());
        }
     /*   if(TextUtils.isEmpty(pshAffairDetailData.getHasCert1())){
            tableSewerageOneData.rgTableSewerageoneOne.clearCheck();
        }*/
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneOne, tableSewerageOneData.rgTableSewerageoneOne.getCheckedRadioButtonId());

      /*  tableSewerageOneData.rgTableSewerageoneTwo.check("1".equals(pshAffairDetailData.getHasCert2()) ? tableSewerageOneData.rbTableSewerageoneTwoYes.getId() :
                tableSewerageOneData.rbTableSewerageoneTwoNo.getId()
        );
        if(TextUtils.isEmpty(pshAffairDetailData.getHasCert2())){
            tableSewerageOneData.rgTableSewerageoneTwo.clearCheck();
        }*/
        if ("1".equals(pshAffairDetailData.getHasCert2())) {
            tableSewerageOneData.rgTableSewerageoneTwo.check(tableSewerageOneData.rbTableSewerageoneTwoYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert2())) {
            tableSewerageOneData.rgTableSewerageoneTwo.check(tableSewerageOneData.rbTableSewerageoneTwoNo.getId());
        }
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneTwo, tableSewerageOneData.rgTableSewerageoneTwo.getCheckedRadioButtonId());

      /*  tableSewerageOneData.rgTableSewerageoneThree.check("1".equals(pshAffairDetailData.getHasCert3()) ? tableSewerageOneData.rbTableSewerageoneThreeYes.getId() :
                tableSewerageOneData.rbTableSewerageoneThreeNo.getId()
        );
        if(TextUtils.isEmpty(pshAffairDetailData.getHasCert3())){
            tableSewerageOneData.rgTableSewerageoneThree.clearCheck();
        }*/
        if ("1".equals(pshAffairDetailData.getHasCert3())) {
            tableSewerageOneData.rgTableSewerageoneThree.check(tableSewerageOneData.rbTableSewerageoneThreeYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert3())) {
            tableSewerageOneData.rgTableSewerageoneThree.check(tableSewerageOneData.rbTableSewerageoneThreeNo.getId());
        }
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneThree, tableSewerageOneData.rgTableSewerageoneThree.getCheckedRadioButtonId());

        /*tableSewerageOneData.rgTableSewerageoneFour.check("1".equals(pshAffairDetailData.getHasCert4()) ? tableSewerageOneData.rbTableSewerageoneFourYes.getId() :
                tableSewerageOneData.rbTableSewerageoneFourNo.getId()
        );
        if(TextUtils.isEmpty(pshAffairDetailData.getHasCert4())){
            tableSewerageOneData.rgTableSewerageoneFour.clearCheck();
        }*/
        if ("1".equals(pshAffairDetailData.getHasCert4())) {
            tableSewerageOneData.rgTableSewerageoneFour.check(tableSewerageOneData.rbTableSewerageoneFourYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert4())) {
            tableSewerageOneData.rgTableSewerageoneFour.check(tableSewerageOneData.rbTableSewerageoneFourNo.getId());
        }
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneFour, tableSewerageOneData.rgTableSewerageoneFour.getCheckedRadioButtonId());

        if ("1".equals(pshAffairDetailData.getHasCert5())) {
            tableSewerageOneData.rgTableSewerageoneFive.check(tableSewerageOneData.rbTableSewerageoneFiveYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert5())) {
            tableSewerageOneData.rgTableSewerageoneFive.check(tableSewerageOneData.rbTableSewerageoneFiveNo.getId());
        }
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneFive, tableSewerageOneData.rgTableSewerageoneFive.getCheckedRadioButtonId());

        if ("1".equals(pshAffairDetailData.getHasCert7())) {
            tableSewerageOneData.rgTableSewerageoneSeven.check(tableSewerageOneData.rbTableSewerageoneSevenYes.getId());
        } else if ("0".equals(pshAffairDetailData.getHasCert7())) {
            tableSewerageOneData.rgTableSewerageoneSeven.check(tableSewerageOneData.rbTableSewerageoneSevenNo.getId());
        }
        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneSeven, tableSewerageOneData.rgTableSewerageoneSeven.getCheckedRadioButtonId());

    }

    private void showBottomSheet() {
//        approvalOpinionContainer.removeAllViews();
        showBottomSheet(pshAffairDetail);
//        ((SewerageTableActivity)getActivity()).showBottomSheet(pshAffairDetail);
//        UploadDoorNoApprovalViewManager viewManager = new UploadDoorNoApprovalViewManager(mContext,pshAffairDetail);
//        DialogUtil.MessageBox(mContext,"????????????",viewManager.getView(), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });

    }

    private void showBottomSheet(PSHAffairDetail pshAffairDetail) {
        tableSewerageOneData.mapBottomSheet.setVisibility(View.VISIBLE);
        if (approvalOpinionContainer.getChildCount() == 0) {
            DoorNoOpinionViewManager viewManager = new DoorNoOpinionViewManager(mContext, pshAffairDetail.getData().getId());
            viewManager.addTo(approvalOpinionContainer);
            mBehavior.setState(com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
        } else if (mBehavior.getState() == com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (mBehavior.getState() == com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void hideBoottomSheet() {
        if (tableSewerageOneData.mapBottomSheet != null) {
            tableSewerageOneData.mapBottomSheet.setVisibility(View.GONE);
        }
    }

    public void hideLlstate() {
        tableSewerageOneData.llState.setVisibility(View.GONE);
    }

    /**
     * ??????????????????????????????
     */

    private void setNoticeIcon() {

        tableSewerageOneData.photoItem.setRequired(true);
        tableSewerageOneData.tvTableSewerageoneOne.setRequireTag();
        tableSewerageOneData.tvTableSewerageoneTwo.setRequireTag();
        tableSewerageOneData.tvTableSewerageoneThree.setRequireTag();
        tableSewerageOneData.tvTableSewerageoneFour.setRequireTag();

        // tableSewerageOneData.tvTableSewerageoneFive.setRequireTag();

    /*    tableSewerageOneData.tvNotice.setVisibility(View.VISIBLE);
        tableSewerageOneData.tvNotice2.setVisibility(View.VISIBLE);
        tableSewerageOneData.tvNotice3.setVisibility(View.VISIBLE);
        tableSewerageOneData.tvNotice4.setVisibility(View.VISIBLE);*/

        tableSewerageOneData.tvTableSewerageoneSix.setRequireTag();
        tableSewerageOneData.tvSewerageOneCode1.setRequireTag();
        tableSewerageOneData.tvSewerageOneCode2.setRequireTag();
        tableSewerageOneData.tvSewerageOneCode3.setRequireTag();
        tableSewerageOneData.tvTableUploadName.setRequireTag();
        tableSewerageOneData.tvTableUploadOrg.setRequireTag();
        tableSewerageOneData.tvTableUploadTime.setRequireTag();

        tableSewerageOneData.tfTableUploadIndicate.setTvSizeVisible(1);
        tableSewerageOneData.tfTableUploadIndicate.setMaxLength(200);
        tableSewerageOneData.tvTableUploadEditTime.setRequireTag();
        tableSewerageOneData.takephotoSewerageoneOne.setRequired(true);
        tableSewerageOneData.takephotoSewerageoneTwo.setRequired(true);
        tableSewerageOneData.takephotoSewerageoneThree.setRequired(true);
        tableSewerageOneData.takephotoSewerageoneFour.setRequired(true);
        tableSewerageOneData.takephotoSewerageoneFive.setRequired(true);
        tableSewerageOneData.takephotoSewerageoneSeven.setRequired(true);
    }

    /**
     * ??????????????????
     */
    private void setFocusabledfalse() {

        setListener();

        tableSewerageOneData.tvTableUploadName.setReadOnly();
        tableSewerageOneData.tvTableUploadOrg.setReadOnly();
        tableSewerageOneData.tvTableUploadTime.setReadOnly();
        tableSewerageOneData.tvTableUploadEditTime.setReadOnly();

        /**
         * ??????edittext????????????
         */
        tableSewerageOneData.tfTableUploadIndicate.setEnableEdit(EDIT_MODE);
        // tableSewerageOneData.tvTableSewerageoneOne.setEditable(EDIT_MODE);
        tableSewerageOneData.cbSygypsh.setEnabled(EDIT_MODE);
        // tableSewerageOneData.tvTableSewerageoneTwo.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneThree.setEditable(EDIT_MODE);
        //  tableSewerageOneData.tvTableSewerageoneFour.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneFive.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneSix.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneSeven.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneEight.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneNine.setEditable(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneTen.setEditable(EDIT_MODE);
        tableSewerageOneData.edtUnitName.setEnabled(EDIT_MODE);
        tableSewerageOneData.tvTableSewerageoneWaterno.setEditable(EDIT_MODE);

        tableSewerageOneData.btnStartTime.setEnabled(EDIT_MODE);
        tableSewerageOneData.btnEndTime.setEnabled(EDIT_MODE);

        tableSewerageOneData.tvSewerageOneCode1.setEditable(EDIT_MODE);
        tableSewerageOneData.tvSewerageOneCode2.setEditable(EDIT_MODE);
        tableSewerageOneData.tvSewerageOneCode3.setEditable(EDIT_MODE);
        if (isIndustry) {
            tableSewerageOneData.tvTableSewerageoneOne.setReadOnly();
        }
//        tableSewerageOneData.tvTableSewerageoneOne.setText("?????????");//????????????

        /**
         * ???????????????????????????????????????
         */

        if (EDIT_MODE) {
            tableSewerageOneData.tvNotice5.setVisibility(View.VISIBLE);
            tableSewerageOneData.tvNotice3One.setVisibility(View.VISIBLE);
            tableSewerageOneData.tvNotice6.setVisibility(View.VISIBLE);
            tableSewerageOneData.photoItem.setEditable();
            tableSewerageOneData.takephotoSewerageoneOne.setEditable();
            tableSewerageOneData.takephotoSewerageoneTwo.setEditable();
            tableSewerageOneData.takephotoSewerageoneThree.setEditable();
            tableSewerageOneData.takephotoSewerageoneFour.setEditable();
            tableSewerageOneData.takephotoSewerageoneFive.setEditable();
            tableSewerageOneData.takephotoSewerageoneSeven.setEditable();
            tableSewerageOneData.filePicker.setEnable(true);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneOne);//??????radiogroup???radiobutton?????????
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneTwo);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneThree);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneFour);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneFive);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneSeven);
            RadioGroupUtil.enableRadioGroup(tableSewerageOneData.rgTableSewerageoneThreeOne);
        } else {
            tableSewerageOneData.tvNotice5.setVisibility(View.GONE);
            tableSewerageOneData.tvNotice6.setVisibility(View.GONE);
            tableSewerageOneData.tvNotice3One.setVisibility(View.GONE);
            tableSewerageOneData.photoItem.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneOne.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneTwo.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneThree.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneFour.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneFive.setReadOnly();
            tableSewerageOneData.takephotoSewerageoneSeven.setReadOnly();
            tableSewerageOneData.filePicker.setReadOnly(true);

            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneOne);//??????radiogroup???radiobutton????????????
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneTwo);
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneThree);
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneFour);
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneFive);
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneSeven);
            RadioGroupUtil.disableRadioGroup(tableSewerageOneData.rgTableSewerageoneThreeOne);
        }

        tableSewerageOneData.photoItem.setImageIsShow(tableSewerageOneData.photoItem.getSelectedPhotos().size() >= 9 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneOne.setImageIsShow(tableSewerageOneData.takephotoSewerageoneOne.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneTwo.setImageIsShow(tableSewerageOneData.takephotoSewerageoneTwo.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneThree.setImageIsShow(tableSewerageOneData.takephotoSewerageoneThree.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneFour.setImageIsShow(tableSewerageOneData.takephotoSewerageoneFour.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneFive.setImageIsShow(tableSewerageOneData.takephotoSewerageoneFive.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.takephotoSewerageoneSeven.setImageIsShow(tableSewerageOneData.takephotoSewerageoneFive.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);
        tableSewerageOneData.filePicker.setMaxLimit(3);
        //        tableSewerageOneData.filePicker.setMaxLimit(tableSewerageOneData.takephotoSewerageoneFive.getSelectedPhotos().size() >= 3 ? false : EDIT_MODE);

//        tableSewerageOneData.photoItem.setEnabled(EDIT_MODE);
//        tableSewerageOneData.takephotoSewerageoneOne.setEnabled(EDIT_MODE);
//        tableSewerageOneData.takephotoSewerageoneTwo.setEnabled(EDIT_MODE);
//        tableSewerageOneData.takephotoSewerageoneThree.setEnabled(EDIT_MODE);
//        tableSewerageOneData.takephotoSewerageoneFour.setEnabled(EDIT_MODE);


//        /**
//         * /**
//         * ??????????????????radiobutton????????????????????????????????????,????????????????????????????????????,radiogroup???radiobutton????????????
//         */
//        tableSewerageOneData.rgTableSewerageoneOne.check(tableSewerageOneData.rbTableSewerageoneOneYes.getId());
//        selectDefaultCheck(tableSewerageOneData.rgTableSewerageoneOne, tableSewerageOneData.rgTableSewerageoneOne.getCheckedRadioButtonId());
    }

    private void selectDefaultCheck(RadioGroup radioGroup, int radiobuttonId) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
            if (rb.isChecked()) {
                if (radioGroup == tableSewerageOneData.rgTableSewerageoneOne) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_one_no:
                            tableSewerageOneData.llTakephotoSewerageoneOne.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_one_yes:
                            tableSewerageOneData.llTakephotoSewerageoneOne.setVisibility(View.VISIBLE);
                            if (pshAffairDetailData != null) {
                                tableSewerageOneData.tvSewerageOneCode1.setText(pshAffairDetailData.getCert1Code());
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert1Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert1AttachmentBean attachmentBean : pshAffairDetailData.getHasCert1Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneOne.setSelectedPhotos(photoList);
                                }
                            }
                            break;
                    }

                } else if (radioGroup == tableSewerageOneData.rgTableSewerageoneTwo) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_two_no:
                            tableSewerageOneData.takephotoSewerageoneTwo.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_two_yes:
                            tableSewerageOneData.takephotoSewerageoneTwo.setVisibility(View.VISIBLE);
                            if (pshAffairDetailData != null) {
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert2Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert2AttachmentBean attachmentBean : pshAffairDetailData.getHasCert2Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneTwo.setSelectedPhotos(photoList);
                                }
                            }
                            break;
                    }

                } else if (radioGroup == tableSewerageOneData.rgTableSewerageoneThree) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_three_no:
                            tableSewerageOneData.llTakephotoSewerageoneThree.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_three_yes:
                            tableSewerageOneData.llTakephotoSewerageoneThree.setVisibility(View.VISIBLE);
                            if (pshAffairDetailData != null) {
                                tableSewerageOneData.tvSewerageOneCode2.setText(pshAffairDetailData.getCert3Code());
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert3Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert3AttachmentBean attachmentBean : pshAffairDetailData.getHasCert3Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneThree.setSelectedPhotos(photoList);
                                }
                            }
                            break;
                    }

                } else if (radioGroup == tableSewerageOneData.rgTableSewerageoneFour) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_four_no:
                            tableSewerageOneData.llTakephotoSewerageoneFour.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_four_yes:
                            tableSewerageOneData.llTakephotoSewerageoneFour.setVisibility(View.VISIBLE);
                            if (pshAffairDetailData != null) {
                                tableSewerageOneData.tvSewerageOneCode3.setText(pshAffairDetailData.getCert4Code());
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert4Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert4AttachmentBean attachmentBean : pshAffairDetailData.getHasCert4Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneFour.setSelectedPhotos(photoList);
                                }
                            }
                            break;
                    }
                } else if (radioGroup == tableSewerageOneData.rgTableSewerageoneFive) {
                    //?????????
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_five_no:
                            tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.GONE);
                            tableSewerageOneData.filePicker.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_five_yes:

                            if (isAllowSaveLocalDraft) {
                                tableSewerageOneData.filePicker.setEnable(true);
                                tableSewerageOneData.filePicker.setReadOnly(false);
                            } else {
                                tableSewerageOneData.filePicker.setEnable(false);
                                tableSewerageOneData.filePicker.setReadOnly(true);
                            }

                            if (pshAffairDetailData != null) {
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert5Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert5AttachmentBean attachmentBean : pshAffairDetailData.getHasCert5Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.VISIBLE);
                                    tableSewerageOneData.takephotoSewerageoneFive.setSelectedPhotos(photoList);
                                } else {
                                    tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.GONE);
                                }

                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert6Attachment())) {
                                    List<FileBean> fileBeans = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert6AttachmentBean attachmentBean : pshAffairDetailData.getHasCert6Attachment()) {
                                        FileBean fileBean = new FileBean();
                                        fileBean.setName(attachmentBean.getAttName());
                                        fileBean.setPath(attachmentBean.getAttPath());
                                        fileBean.setMimeType(attachmentBean.getAttType());
                                        fileBean.setId(attachmentBean.getId());
                                        fileBeans.add(fileBean);
                                    }
                                    tableSewerageOneData.filePicker.setVisibility(View.VISIBLE);
                                    tableSewerageOneData.filePicker.setValue(fileBeans);
                                } else {
                                    tableSewerageOneData.filePicker.setVisibility(View.GONE);
                                }
                            }
                            break;
                    }
                } else if (radioGroup == tableSewerageOneData.rgTableSewerageoneSeven) {
                    switch (radiobuttonId) {
                        case R.id.rb_table_sewerageone_seven_no:
                            tableSewerageOneData.takephotoSewerageoneSeven.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_seven_yes:
                            tableSewerageOneData.takephotoSewerageoneSeven.setVisibility(View.VISIBLE);
                            if (pshAffairDetailData != null) {
                                if (!ListUtil.isEmpty(pshAffairDetailData.getHasCert7Attachment())) {
                                    List<Photo> photoList = new ArrayList<>();
                                    for (PSHAffairDetail.DataBean.HasCert7AttachmentBean attachmentBean : pshAffairDetailData.getHasCert7Attachment()) {
                                        Photo photo = new Photo();
                                        photo.setId(attachmentBean.getId());
                                        photo.setHasBeenUp(1);
                                        photo.setPhotoName(attachmentBean.getAttName());
                                        photo.setPhotoPath(attachmentBean.getAttPath());
                                        photo.setThumbPath(attachmentBean.getThumPath());
                                        photo.setField1("photo");
                                        photoList.add(photo);
                                    }
                                    tableSewerageOneData.takephotoSewerageoneSeven.setSelectedPhotos(photoList);
                                }
                            }
                            break;
                    }

                }
                break;
            }
        }
    }

    public void setEditMode(boolean isEdit) {
        //??????????????????
        if (isEdit != EDIT_MODE) {
            EDIT_MODE = isEdit;
            setFocusabledfalse();
        }
    }

    public MultiTakePhotoTableItem getPhotoItem() {
        return tableSewerageOneData.photoItem;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneOne() {
        return tableSewerageOneData.takephotoSewerageoneOne;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneTwo() {
        return tableSewerageOneData.takephotoSewerageoneTwo;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneThree() {
        return tableSewerageOneData.takephotoSewerageoneThree;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneFour() {
        return tableSewerageOneData.takephotoSewerageoneFour;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneFive() {
        return tableSewerageOneData.takephotoSewerageoneFive;
    }

    public MultiTakePhotoTableItem getTakephotoSewerageoneSeven() {
        return tableSewerageOneData.takephotoSewerageoneSeven;
    }

    public AgFilePicker getAgFilePicker() {
        return tableSewerageOneData.filePicker;
    }

    @Subscribe
    public void onEventMainThread(PhotoEvent event) {
        Intent intent = event.getData();
        int requestCode = event.getRequestCode();
        int resultCode = event.getResultCode();
        if (tableSewerageOneData.photoItem != null) {
            tableSewerageOneData.photoItem.onActivityResult(requestCode, resultCode, intent);
        }
        if (tableSewerageOneData.takephotoSewerageoneOne != null) {
            tableSewerageOneData.takephotoSewerageoneOne.onActivityResult(requestCode, resultCode, intent);
        }
        if (tableSewerageOneData.takephotoSewerageoneTwo != null) {
            tableSewerageOneData.takephotoSewerageoneTwo.onActivityResult(requestCode, resultCode, intent);
        }
        if (tableSewerageOneData.takephotoSewerageoneThree != null) {
            tableSewerageOneData.takephotoSewerageoneThree.onActivityResult(requestCode, resultCode, intent);
        }

        if (tableSewerageOneData.takephotoSewerageoneFour != null) {
            tableSewerageOneData.takephotoSewerageoneFour.onActivityResult(requestCode, resultCode, intent);
        }
        if (tableSewerageOneData.takephotoSewerageoneFive != null) {
            tableSewerageOneData.takephotoSewerageoneFive.onActivityResult(requestCode, resultCode, intent);
        }
        if (tableSewerageOneData.takephotoSewerageoneSeven != null) {
            tableSewerageOneData.takephotoSewerageoneSeven.onActivityResult(requestCode, resultCode, intent);
        }

        if (tableSewerageOneData.filePicker != null) {
            tableSewerageOneData.filePicker.onActivityResult(requestCode, resultCode, intent);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param selectComponentFinishEvent
     */
    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent2
                                                       selectComponentFinishEvent) {
        Component component = selectComponentFinishEvent.getFindResult();
        mSelComponent = component;
        String name = String.valueOf(component.getGraphic().getAttributes().get(component.getDisplayFieldName()));
        tableSewerageOneData.edtUnitName.setText(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//?????????EventBus
        }
    }

    public interface onViewCreatedListener {
        void onViewCreated();
    }

    /*
     * ??????radiogroup????????????
     */
    private class MyCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (radioGroup.getId()) {
                case R.id.rg_table_sewerageone_one:
                    switch (i) {
                        case R.id.rb_table_sewerageone_one_no:
                            tableSewerageOneData.llTakephotoSewerageoneOne.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_one_yes:
                            tableSewerageOneData.llTakephotoSewerageoneOne.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case R.id.rg_table_sewerageone_two:
                    switch (i) {
                        case R.id.rb_table_sewerageone_two_no:
                            tableSewerageOneData.takephotoSewerageoneTwo.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_two_yes:
                            tableSewerageOneData.takephotoSewerageoneTwo.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case R.id.rg_table_sewerageone_three:
                    switch (i) {
                        case R.id.rb_table_sewerageone_three_no:
                            tableSewerageOneData.llTakephotoSewerageoneThree.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_three_yes:
                            tableSewerageOneData.llTakephotoSewerageoneThree.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case R.id.rg_table_sewerageone_four:
                    switch (i) {
                        case R.id.rb_table_sewerageone_four_no:
                            tableSewerageOneData.llTakephotoSewerageoneFour.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_four_yes:
                            tableSewerageOneData.llTakephotoSewerageoneFour.setVisibility(View.VISIBLE);
                            break;
                    }
                case R.id.rg_table_sewerageone_five:
                    switch (i) {
                        case R.id.rb_table_sewerageone_five_no:
                            tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.GONE);
                            tableSewerageOneData.filePicker.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_five_yes:
                            tableSewerageOneData.takephotoSewerageoneFive.setVisibility(View.VISIBLE);
                            tableSewerageOneData.filePicker.setVisibility(View.VISIBLE);
                            tableSewerageOneData.filePicker.setEnable(true);
                            tableSewerageOneData.filePicker.setReadOnly(false);
//                            tableSewerageOneData.filePicker.setHint("?????????");
                            //      tableSewerageOneData.filePicker.showRequireTag(true);
                            tableSewerageOneData.filePicker.setErrorText("?????????pdf??????");
                            tableSewerageOneData.filePicker.setTextViewName("????????????");
                            tableSewerageOneData.filePicker.setTextViewContentTextAndColor("????????????", R.color.black);

                            break;
                    }

                case R.id.rg_table_sewerageone_seven:
                    switch (i) {
                        case R.id.rb_table_sewerageone_seven_no:
                            tableSewerageOneData.takephotoSewerageoneSeven.setVisibility(View.GONE);
                            break;
                        case R.id.rb_table_sewerageone_seven_yes:
                            tableSewerageOneData.takephotoSewerageoneSeven.setVisibility(View.VISIBLE);
                            break;
                    }
                    break;
                case R.id.rg_table_sewerageone_three_one:
                    switch (i) {
                        case R.id.rb_table_sewerageone_three_zd:
                            SewerageBeanManger.getInstance().setPsxkLx("???????????????");//???????????????
                            break;
                        case R.id.rb_table_sewerageone_three_yb:
                            SewerageBeanManger.getInstance().setPsxkLx("???????????????");//???????????????
                            break;
                    }
                    break;

            }

        }
    }

    private class SewerageTableOneFragmentOnclick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_select_location:
                    Intent intent = new Intent(mContext, SewerageSelectComponentActivity.class);
                    if (mSelComponent != null) {
                        intent.putExtra("geometry", mSelComponent.getGraphic().getGeometry());
                    }
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String isExist = data.getStringExtra("isExist");
            String HouseIdFlag = data.getStringExtra("HouseIdFlag");
            String HouseId = data.getStringExtra("HouseId");
            PSHAffairDetail pshAffairDetail = (PSHAffairDetail) data.getSerializableExtra("pshAffairDetail");
            DoorNOBean mCurrentDoorNOBean = (DoorNOBean) data.getSerializableExtra("doorBean");
            String UnitId = data.getStringExtra("UnitId");
            SewerageBeanManger.getInstance().setVillage(MyApplication.SEWERAGEITEMBEAN.getJwh());//????????????
            SewerageBeanManger.getInstance().setStreet(MyApplication.SEWERAGEITEMBEAN.getJlx());//?????????
            SewerageBeanManger.getInstance().setJzwcode(MyApplication.SEWERAGEITEMBEAN.getMpwzhm());//?????????????????????

            tableSewerageOneData.tvTableSewerageoneTwo.setText(MyApplication.SEWERAGEITEMBEAN.getXzj());//????????????
            tableSewerageOneData.tvTableSewerageoneFour.setText(MyApplication.SEWERAGEITEMBEAN.getMpwzhm());//?????????
            //????????????
            tableSewerageOneData.tvTableSewerageoneSix.setText(MyApplication.SEWERAGEITEMBEAN.getXzq() + MyApplication.SEWERAGEITEMBEAN.getXzj() + MyApplication.SEWERAGEITEMBEAN.getJlx() + MyApplication.SEWERAGEITEMBEAN.getMpwzhm());
        }
    }
}
