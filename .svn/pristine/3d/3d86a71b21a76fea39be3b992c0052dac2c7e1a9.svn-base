package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.DrainageEntity;
import com.augurit.am.fw.utils.DensityUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.drainageentity
 * @createTime 创建时间 ：2018-02-05
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-02-05
 * @modifyMemo 修改备注：
 */

public class DrainageEntityView {

    public static final int SHOW_DRAINAGE_ENTITYS = 0x632;

    private Context mContext;
    private ViewGroup mContainer;
    private ArrayList<DrainageEntity> mDrainageEntitys;

    public DrainageEntityView(Context context, ViewGroup container){
        this.mContext = context;
        this.mContainer = container;
        mockData();
        init();
    }

    private void init(){
        if(!ListUtil.isEmpty(mDrainageEntitys)){
            for(int i=0; i<mDrainageEntitys.size(); i++){
                if(i >= 9){
                    break;
                }
                DrainageEntity drainageEntity = mDrainageEntitys.get(i);
                TextView textView = new TextView(mContext);
                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = DensityUtil.dp2px(mContext, 8);
                lp.topMargin = DensityUtil.dp2px(mContext, 8);
                lp.rightMargin = DensityUtil.dp2px(mContext, 8);
                lp.bottomMargin = DensityUtil.dp2px(mContext, 8);
                textView.setText(drainageEntity.getEntry_name());
                textView.setLayoutParams(lp);
                textView.setBackgroundResource(R.drawable.btn_2);
                textView.setGravity(Gravity.CENTER);
                mContainer.addView(textView);
            }
            if(mDrainageEntitys.size() > 9){
                TextView textView = new TextView(mContext);
                FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = DensityUtil.dp2px(mContext, 8);
                lp.topMargin = DensityUtil.dp2px(mContext, 8);
                lp.rightMargin = DensityUtil.dp2px(mContext, 8);
                lp.bottomMargin = DensityUtil.dp2px(mContext, 8);
                textView.setText("...");
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.BOTTOM);
                mContainer.addView(textView);
            }
        }
        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DrainageEntityActivity.class);
                intent.putExtra("data", mDrainageEntitys);
                ((Activity) mContext).startActivityForResult(intent, SHOW_DRAINAGE_ENTITYS);
            }
        });
    }

    private void mockData(){
        mDrainageEntitys = new ArrayList<>();
        for(int i=0; i< 12; i++){
            DrainageEntity drainageEntity = new DrainageEntity();
            drainageEntity.setEntry_name("沙县小吃" + i);
            drainageEntity.setLicense_key("4270字第0100号");
            if(i%2 == 0){
                drainageEntity.setType("重点");
            } else {
                drainageEntity.setType("一般");
            }

            List<DrainageEntity.FilesBean> files = new ArrayList<>();
            for(int j=0; j<=3; j++){
                DrainageEntity.FilesBean file = new DrainageEntity.FilesBean();
                file.setThum_path("http://www.zjjnews.cn/d/file/2016-09-06/1473121126365425.jpg");
                file.setAtt_path("http://www.zjjnews.cn/d/file/2016-09-06/1473121126365425.jpg");
                file.setAtt_name("排水许可证");
                files.add(file);
            }
            drainageEntity.setFiles(files);
            mDrainageEntitys.add(drainageEntity);
        }
    }

    public ArrayList<DrainageEntity> getDrainageEntitys() {
        return mDrainageEntitys;
    }

    public void setDrainageEntitys(ArrayList<DrainageEntity> drainageEntitys) {
        this.mDrainageEntitys = drainageEntitys;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SHOW_DRAINAGE_ENTITYS
                || requestCode == SelectDrainageEntityActivity.SELECT_DRAINAGE_ENTITY){
            Object o = data.getSerializableExtra("data");
            if(o != null
                    && (o instanceof DrainageEntity)){
                mDrainageEntitys = (ArrayList<DrainageEntity>) o;
                init();
            }
        }
    }
}
