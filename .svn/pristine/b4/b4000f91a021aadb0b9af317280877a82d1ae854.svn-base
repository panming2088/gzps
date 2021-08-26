package com.augurit.agmobile.gzps.doorno.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.ActivityManagerUtils;
import com.augurit.agmobile.gzpssb.BaseInfoData;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.bumptech.glide.Glide;

/**
 * Created by xiaoyu on 2018/4/3.
 */

public abstract class BaseDataBindingActivity extends FragmentActivity{
    private String mSearchCompanyName;
    protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerUtils.getInstance().addActivity(this);
        context=this;
        System.out.println("=====ActivityName===========" + getClass().getName() + "\n" +
                "====package===" + getPackageName());

        initBaseTitle();
    }

    ViewDataBinding inflate;
    int titleview = View.VISIBLE;
    BaseInfoData baseInfoData;

    public int getTitleview() {
        return titleview;
    }

    private void initBaseTitle() {
        baseInfoData = DataBindingUtil.setContentView(this, R.layout.titlebar);



        //设置右面的侧滑菜单只能通过编程来打开
        baseInfoData.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);


        baseInfoData.rlTitlebar.setVisibility(getTitleview());
        LayoutInflater from = LayoutInflater.from(this);
        inflate = DataBindingUtil.inflate(from, initview(), baseInfoData.contentParntView, true);
        initdatabinding();
        initData();
        baseInfoData.setBaseInfoActivityonclic(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                  /*  case R.id.ll_back:
                        back();
                        break;*/
                    case R.id.ll_right:
                        openDrawer(null);
                        break;
                }

            }
        });
    }

    public void onTitleRightClick() {

    }

    /**
     * 返回按钮
     */
    public void back(){
      //  if(context instanceof SewerageTableActivity){

     //   }else{
            finish();
      //  }
    }
  /*  private void createDialog(){
        final AlertDialog.Builder mDialog =
                new AlertDialog.Builder(context);
   //     mDialog.setIcon(R.mipmap.ic_alert_yellow);
        mDialog.setTitle("提示");
        mDialog.setMessage("是否放弃本次编辑");
        mDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        mDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        mDialog.show();
    }*/
  /*  public void search(String mSearchCompanyName){

    }*/

    public void setTitleRightImg(int mipmapId) {
        baseInfoData.ivTitlebarRight.setImageResource(mipmapId);
    }

    public void setDataTitle(String titlename) {
        baseInfoData.setTitlename(titlename);
    }

    public void setRightText(String titlerightname) {
        baseInfoData.setTitlerightname(titlerightname);
    }

    public void setRightIsVisiable(int visible) {
        baseInfoData.llRight.setVisibility(visible);
    }

    public <T> void setLeftimg(T img) {
        Glide.with(context).load(img).into(baseInfoData.ivTitlebarLeft);
    }

    public <T> void setRightimg(T img) {
        Glide.with(context).load(img).into(baseInfoData.ivTitlebarRight);
    }

    public void setLeftTextVisible(int visible) {
        baseInfoData.tvTitlebarLeft.setVisibility(visible);
    }

    public <T extends ViewDataBinding> T getBind() {
        return (T) inflate;
    }

    public abstract int initview();

    public abstract void initdatabinding();

    public abstract void initData();


    public void openDrawer(final IDrawerController.OnDrawerOpenListener listener) {
         baseInfoData.drawerLayout.openDrawer(Gravity.RIGHT);
        baseInfoData.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                GravityCompat.END);    //解除锁定
        baseInfoData.drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                if (listener != null) {
                    listener.onOpened(drawerView);
                }
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                //设置右面的侧滑菜单只能通过编程来打开
                baseInfoData.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                        GravityCompat.END);
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtils.getInstance().removeActivity(this);
    }

}
