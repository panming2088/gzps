package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectMenPaiActivity;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.core.geometry.Point;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 显示原有属性和纠错后属性的自定义视图；
 *
 * Created by xcl on 2017/11/13.
 */

public class ReadOnlyMenPaiView {

    private Context mContext;
    private View root;

    private View mMenPaiView;
    private TagFlowLayout mFlowLayout;
    private List<DoorNOBean> mVals = new ArrayList<>();
    private Button btn_add_attached;
    private int currentIndex = -1;
    private Point mPoint;

    public ReadOnlyMenPaiView(Context context, List<DoorNOBean> mpBeen,Point point){
        this.mContext = context;
        this.mPoint = point;
        initView(mpBeen);
    }


    private void initView(final List<DoorNOBean> mpBeen) {
        if(!ListUtil.isEmpty(mpBeen)){
            mVals.clear();
            mVals.addAll(mpBeen);
        }
        mMenPaiView = View.inflate(mContext, R.layout.view_menpai, null);
        mFlowLayout = (TagFlowLayout) mMenPaiView.findViewById(R.id.id_flowlayout);
        mFlowLayout.setAdapter(new TagAdapter<DoorNOBean>(mVals) {
            @Override
            public View getView(FlowLayout parent, final int position, DoorNOBean s) {
                final View mView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.tv_flow,
                        mFlowLayout, false);
                TextView tv = (TextView) mView.findViewById(R.id.flow_tv);
                tv.setText(s.getMph());
                mView.findViewById(R.id.iv_item_nine_photo_flag).setVisibility(View.GONE);
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
                Intent intent = new Intent(mContext, SelectMenPaiActivity.class);
                intent.putExtra("doorbean", (Parcelable) doorNOBean);
                intent.putExtra("yjPoint", mPoint);
                intent.putExtra("isReadOnly",true);
                mContext.startActivity(intent);
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
                Intent intent = new Intent(mContext, SelectMenPaiActivity.class);
//                intent.putParcelableArrayListExtra("facilitiesBean", (ArrayList<? extends Parcelable>) facilitiesBeans);
                mContext.startActivity(intent);
            }
        });
        btn_add_attached.setVisibility(View.GONE);
        mMenPaiView.findViewById(R.id.tv_requiredTag1).setVisibility(View.GONE);
    }


    public void addTo(ViewGroup viewGroup){
        viewGroup.addView(mMenPaiView);
    }
}
