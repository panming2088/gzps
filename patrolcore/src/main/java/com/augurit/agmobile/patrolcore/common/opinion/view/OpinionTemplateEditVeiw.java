package com.augurit.agmobile.patrolcore.common.opinion.view;

import android.content.Context;
import android.graphics.Path;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.util.OpinionConstant;
import com.augurit.agmobile.patrolcore.common.opinion.view.presenter.IOpinionTemplateEditPresenter;
import com.augurit.am.fw.utils.view.ToastUtil;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.view
 * @createTime 创建时间 ：2017-07-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-18
 * @modifyMemo 修改备注：
 */

public class OpinionTemplateEditVeiw implements IOpinionTemplateEditView{

    private Context mContext;
    private ViewGroup mContainer;
    private View mView;
    private IOpinionTemplateEditPresenter mPresenter;
    private OpinionTemplate mOpinionTemplate;

    public OpinionTemplateEditVeiw(Context context, ViewGroup container, OpinionTemplate opinionTemplate){
        this.mContext = context;
        this.mContainer = container;
        mOpinionTemplate = opinionTemplate;
        mView = getLayout();
        init();
    }

    private View getLayout(){
        return LayoutInflater.from(mContext).inflate(R.layout.opinion_template_edit_view, null);
    }

    private void init(){
        final EditText nameET = (EditText) mView.findViewById(R.id.name);
        final EditText contentET = (EditText) mView.findViewById(R.id.content);
        final RadioGroup rg_auth = (RadioGroup) mView.findViewById(R.id.rg_auth_type);
        nameET.setText(mOpinionTemplate.getName());
        contentET.setText(mOpinionTemplate.getContent());
        if(mOpinionTemplate.getAuthorization().equals(OpinionConstant.PRIVATE)){
            rg_auth.check(R.id.rb_auth_type_pri);
        } else if(mOpinionTemplate.getAuthorization().equals(OpinionConstant.PUBLIC)){
            rg_auth.check(R.id.rb_auth_type_pub);
        }
        View ok = mView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String content = contentET.getText().toString();
                if(TextUtils.isEmpty(name)){
                    showMessage("名称不能为空");
                    return;
                }
                if(TextUtils.isEmpty(content)){
                    showMessage("内容不能为空");
                    return;
                }
                String auth = OpinionConstant.PRIVATE;
                int authId = rg_auth.getCheckedRadioButtonId();
                if (authId == R.id.rb_auth_type_pri) {
                    auth = OpinionConstant.PRIVATE;
                } else if (authId == R.id.rb_auth_type_pub) {
                    auth = OpinionConstant.PUBLIC;
                }
                mOpinionTemplate.setName(name);
                mOpinionTemplate.setContent(content);
                mOpinionTemplate.setAuthorization(auth);
                mPresenter.saveOpinionTemplate(mOpinionTemplate);
            }
        });
        mContainer.addView(mView);
    }

    @Override
    public void setPresenter(IOpinionTemplateEditPresenter presenter) {
        mPresenter = presenter;
    }

    public void showMessage(String msg){
        ToastUtil.shortToast(mContext, msg);
    }
}
