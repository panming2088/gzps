package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.util.OpinionConstant;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplatePresenter;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.util.RequireState;
import com.augurit.agmobile.patrolcore.common.util.MaxLengthInputFilter;
import com.augurit.agmobile.patrolcore.common.util.StringAdapter;
import com.augurit.am.cmpt.widget.searchview.util.adapter.TextWatcherAdapter;
import com.augurit.am.cmpt.widget.spinner.CustemSpinerAdapter;
import com.augurit.am.cmpt.widget.spinner.SpinerPopWindow;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateView implements IOpinionTemplateView {

    private Context mContext;
    private ViewGroup mContainer;
    private TableItem mTableItem;
    private View mView;
    private IOpinionTemplatePresenter mOpinionTempaltePresenter;

    private TextView tv_title;
    private TextView tv_requiredTag;
    private TextView tv_size;
    private ViewGroup ll_sample_templates;
    private EditText et_search;
    private View ll_search_et;
    private View btn_search;
    private EditText et_content;
    private View btn_save_temp;
    private CustemSpinerAdapter mTemplateAdapter;
    private XRecyclerView searchRecyclerView;
    private StringAdapter mStringAdapter;
    private SpinerPopWindow mSpinerPopWindow;
    private Map<String, OpinionTemplate> mTempItemsMap = new LinkedHashMap<>();
    private ArrayList<String> mTempKeys;
    private int total = 0;

    private int pageNo = 1;
    private int pageSize = 20;

    public OpinionTemplateView(Context context, ViewGroup container, TableItem tableItem) {
        mContext = context;
        mContainer = container;
        mTableItem = tableItem;
        mView = getLayout();
        initData();
        init();
    }

    private void initData() {
        if (!TextUtils.isEmpty(mTableItem.getRow_num()) && !TextUtils.isEmpty(mTableItem.getColum_num())) {
            total = Integer.valueOf(mTableItem.getColum_num()) * Integer.valueOf(mTableItem.getRow_num());
        }
    }

    protected View getLayout() {
        return LayoutInflater.from(mContext).inflate(R.layout.table_text_with_template, null);
    }

    public void init() {
        tv_title = (TextView) mView.findViewById(R.id.tv_);
        tv_requiredTag = (TextView) mView.findViewById(R.id.tv_requiredTag);
        tv_size = (TextView) mView.findViewById(R.id.tv_size);
        tv_size.setText("0/" + total);
        ll_sample_templates = (ViewGroup) mView.findViewById(R.id.ll_sample_templates);
        et_search = (EditText) mView.findViewById(R.id.et_search);
        ll_search_et = mView.findViewById(R.id.ll_search_et);
        View btn_hide_search_et = mView.findViewById(R.id.btn_hide_search_et);
        btn_search = mView.findViewById(R.id.btn_search);
        et_content = (EditText) mView.findViewById(R.id.et_);
        if (total == 0) {
            tv_size.setVisibility(View.GONE);
            mView.findViewById(R.id.tv_mask).setVisibility(View.GONE);
        } else {
            et_content.setFilters(new InputFilter[]{new MaxLengthInputFilter(total,
                    null, et_content, "长度不能超过" + total + "个字").setDismissErrorDelay(1500)});
        }
        et_content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String inputText = s.toString();
                    if (TextUtils.isEmpty(inputText)) {
                        tv_size.setText("0/" + total);
                        return;
                    }
                    tv_size.setText(inputText.getBytes("GB2312").length/2 + "/" + total);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_save_temp = mView.findViewById(R.id.btn_save_temp);
        tv_title.setText(mTableItem.getHtml_name());
        if (mTableItem.getIf_required() != null
                && mTableItem.getIf_required().equals(RequireState.REQUIRE)) {
            tv_requiredTag.setVisibility(View.VISIBLE);
        } else {
            tv_requiredTag.setVisibility(View.GONE);
        }
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchOpinionView();
//                ll_search_et.setVisibility(View.VISIBLE);
//                btn_search.setVisibility(View.GONE);
//                ll_sample_templates.setVisibility(View.GONE);
            }
        });
        btn_hide_search_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_search_et.setVisibility(View.GONE);
                btn_search.setVisibility(View.VISIBLE);
                ll_sample_templates.setVisibility(View.VISIBLE);
            }
        });
        et_search.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    mOpinionTempaltePresenter.setCurrOpinionTemplate(null);
                    return;
                }
                mOpinionTempaltePresenter.searchOpinionTemplateWithKey("" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
            }
        });

        btn_save_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_content.getText().toString())) {
                    showMessage("内容不能为空");
                    return;
                }
                showSaveOpinionDialog();
            }
        });
        if (mTableItem.getValue() != null) {
            et_content.setText(mTableItem.getValue());
        }
        mContainer.addView(mView);

        mTemplateAdapter = new CustemSpinerAdapter(mContext, com.augurit.am.cmpt.R.layout.spinner_item_layout);
        mTemplateAdapter.refreshData(new ArrayList<String>(), 0);

        /*mSpinerPopWindow = new SpinerPopWindow(mContext, com.augurit.am.cmpt.R.layout.spinner_window_layout);
        mSpinerPopWindow.setAdatper(mTemplateAdapter);
        mSpinerPopWindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                String key = mTempKeys.get(pos);
                OpinionTemplate opinionTemplate = mTempItemsMap.get(key);
                mOpinionTempaltePresenter.setCurrOpinionTemplate(opinionTemplate);
                String content = mOpinionTempaltePresenter.handleTemplate(opinionTemplate.getContent());
                et_content.setText(content);
            }
        });
        mSpinerPopWindow.setWidth(et_search.getWidth());*/
    }

    private ArrayList<String> keyList() {
        Set<String> keySet = this.mTempItemsMap.keySet();
        mTempKeys = new ArrayList<>(keySet);
        return mTempKeys;
    }

    public void showOptionalTemplates(List<OpinionTemplate> opinionTemplates) {
        if (ListUtil.isEmpty(opinionTemplates)) {
            return;
        }
        for (OpinionTemplate opinionTemplate : opinionTemplates) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.table_text_with_template_btn, null);
            Button btn = (Button) view.findViewById(R.id.btn);
            btn.setText(opinionTemplate.getName());
            btn.setTag(opinionTemplate);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpinionTemplate opinionTemplate = (OpinionTemplate) v.getTag();
                    mOpinionTempaltePresenter.setCurrOpinionTemplate(opinionTemplate);
                    String content = mOpinionTempaltePresenter.handleTemplate(opinionTemplate.getContent());
                    et_content.setText(content);
                }
            });
            ll_sample_templates.addView(view);
        }
    }

    public void showTemplates(List<OpinionTemplate> opinionTemplates) {
        mTempItemsMap.clear();
        if (ListUtil.isEmpty(opinionTemplates)) {

        } else {
            for (OpinionTemplate opinionTemplate : opinionTemplates) {
                mTempItemsMap.put(opinionTemplate.getName(), opinionTemplate);
            }
        }
        keyList();
        mStringAdapter.notifyDataSetChanged(mTempKeys);
        /*mTemplateAdapter.refreshData(mTempKeys, 0);
        mTemplateAdapter.notifyDataSetChanged();
        mSpinerPopWindow.setWidth(et_search.getWidth());
        if(ListUtil.isEmpty(mTempKeys)){
            mSpinerPopWindow.dismiss();
        } else {
            mSpinerPopWindow.showAsDropDown(et_search);
        }*/
    }

    public void onLoadMore(List<OpinionTemplate> opinionTemplates) {
        if (pageNo == 1) {
            mTempItemsMap.clear();
            mStringAdapter.notifyDataSetChanged(null);
        }
        if (ListUtil.isEmpty(opinionTemplates)) {
            keyList();
            mStringAdapter.loadMore(null);
            searchRecyclerView.setNoMore(true);
            searchRecyclerView.loadMoreComplete();
            return;
        }
        for (OpinionTemplate opinionTemplate : opinionTemplates) {
            mTempItemsMap.put(opinionTemplate.getName(), opinionTemplate);
        }
        keyList();
        mStringAdapter.loadMore(mTempKeys);
        if (opinionTemplates.size() < pageSize) {
            searchRecyclerView.setNoMore(true);
        } else {
            searchRecyclerView.setNoMore(false);
        }
        searchRecyclerView.loadMoreComplete();
    }

    private void showSearchOpinionView() {
        //  final PopupWindow popupWindow = new PopupWindow(mContext);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.opinion_search, null);
        EditText et_search = (EditText) contentView.findViewById(R.id.et_search);
        searchRecyclerView = (XRecyclerView) contentView.findViewById(R.id.recycler_view);
        et_search.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    mOpinionTempaltePresenter.setCurrOpinionTemplate(null);
                    pageNo = 1;
                    pageSize = 20;
                    mOpinionTempaltePresenter.getAllOpinionTemplate(pageNo, pageSize);
                    searchRecyclerView.setLoadingMoreEnabled(true);
                    return;
                }
                searchRecyclerView.setLoadingMoreEnabled(false);
                mOpinionTempaltePresenter.searchOpinionTemplateWithKey("" + s);
            }

        });
        mStringAdapter = new StringAdapter(mContext);
        mStringAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(View view, int position, String selectedData) {
                String key = mTempKeys.get(position);
                OpinionTemplate opinionTemplate = mTempItemsMap.get(key);
                mOpinionTempaltePresenter.setCurrOpinionTemplate(opinionTemplate);
                String content = mOpinionTempaltePresenter.handleTemplate(opinionTemplate.getContent());
                et_content.setText(content);
                //    popupWindow.dismiss();
                bottomSheetDialog.dismiss();
            }

            @Override
            public void onItemLongClick(View view, int position, String selectedData) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchRecyclerView.setLayoutManager(layoutManager);
//        searchRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        searchRecyclerView.setAdapter(mStringAdapter);
        searchRecyclerView.setPullRefreshEnabled(false);
        searchRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
//                mOpinionTempaltePresenter.searchOpinionTemplate("");
                pageNo++;
                mOpinionTempaltePresenter.getAllOpinionTemplate(pageNo, pageSize);
            }
        });

        /*
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        popupWindow.showAtLocation(mView.getRootView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        */
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.show();
        pageNo = 1;
        pageSize = 20;
        mOpinionTempaltePresenter.getAllOpinionTemplate(pageNo, pageSize);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Activity activity = (Activity) mContext;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    private void showSaveOpinionDialog() {
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_save_opinion_template, null);
        final EditText nameET = (EditText) dialogView.findViewById(R.id.name);
        final EditText contentET = (EditText) dialogView.findViewById(R.id.content);
        final RadioGroup rg_auth = (RadioGroup) dialogView.findViewById(R.id.rg_auth_type);
        contentET.setText(et_content.getText().toString());
        View cancel = dialogView.findViewById(R.id.cancel);
        View ok = dialogView.findViewById(R.id.ok);
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("保存为模板");
        dialog.setContentView(dialogView);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.dialog_bg);  // 设置背景
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String content = contentET.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showMessage("名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(content)) {
                    showMessage("内容不能为空");
                    return;
                }
                String auth = "";
                int authId = rg_auth.getCheckedRadioButtonId();
                if (authId == R.id.rb_auth_type_pri) {
                    auth = OpinionConstant.PRIVATE;
                } else if (authId == R.id.rb_auth_type_pub) {
                    auth = OpinionConstant.PUBLIC;
                }
                mOpinionTempaltePresenter.saveOpinionTemplate(name, content, auth);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showMessage(String msg) {
        ToastUtil.shortToast(mContext, msg);
    }

    public void setPresenter(IOpinionTemplatePresenter opinionTemplatePresenter) {
        mOpinionTempaltePresenter = opinionTemplatePresenter;
    }

    public View getView() {
        return mView;
    }
}
