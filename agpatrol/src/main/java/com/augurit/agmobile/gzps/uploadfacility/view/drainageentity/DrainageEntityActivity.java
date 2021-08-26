package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.DrainageEntity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsh on 2018/2/2.
 */

public class DrainageEntityActivity extends BaseActivity {

    private TextView btn_add;
    private View ll_msg;
    private XRecyclerView mRecyclerView;
    private DrainageEntityAdapter mAdapter;
    private boolean mSelectMode = false;    //列表是否处于选择模式
    private ArrayList<DrainageEntity> mDrainageEntitys;

    private int pageSize = 10;
    private int pageNo = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drainage_entity_list);

        Object data = getIntent().getSerializableExtra("data");
        if(data != null){
            mDrainageEntitys = (ArrayList<DrainageEntity>) data;
        }

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", mDrainageEntitys);
                setResult(RESULT_OK, intent);
                finish();
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText("已选排水户");

        ll_msg = findViewById(R.id.ll_msg);
        btn_add = (TextView) findViewById(R.id.btn_add_drainage_entity);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectMode){
                    List<DrainageEntity> selected = mAdapter.getSelelecedList();
                    if(ListUtil.isEmpty(selected)){
                        ToastUtil.shortToast(DrainageEntityActivity.this, "未选中任何项");
                        return;
                    }
                    DialogUtil.MessageBox(DrainageEntityActivity.this, "提示", "确定删除所选项吗？",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectMode = false;
                                    mAdapter.setSelectMode(false);
                                    btn_add.setText("添加");
                                    List<DrainageEntity> selected = mAdapter.getSelelecedList();
                                    for(DrainageEntity drainageEntity : selected){
                                        mDrainageEntitys.remove(drainageEntity);
                                    }
                                    mAdapter.notifyDataSetChanged(mDrainageEntitys);
                                }
                            }, null);

                } else {
                    Intent intent = new Intent(DrainageEntityActivity.this, SelectDrainageEntityActivity.class);
                    intent.putExtra("data", mDrainageEntitys);
                    startActivityForResult(intent, SelectDrainageEntityActivity.SELECT_DRAINAGE_ENTITY);
                }
            }
        });

        mRecyclerView = (XRecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DrainageEntityAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<DrainageEntity>() {
            @Override
            public void onItemClick(View view, int position, DrainageEntity selectedData) {

            }

            @Override
            public void onItemLongClick(View view, int position, DrainageEntity selectedData) {
                mSelectMode = true;
                btn_add.setText("删除");
            }
        });

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int aa = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                //达到这个条件就说明滑到了顶部
                if(mRecyclerView.getChildAt(0).getY()==0f&&aa==1){
                    ll_msg.setVisibility(View.VISIBLE);
                } else {
                    ll_msg.setVisibility(View.GONE);
                }
            }
        });*/
        mAdapter.notifyDataSetChanged(mDrainageEntitys);
    }


    public void onBackPressed(){
        if(mSelectMode){
            mSelectMode = false;
            mAdapter.setSelectMode(false);
            btn_add.setText("添加");
        } else {
            Intent intent = new Intent();
            intent.putExtra("data", mDrainageEntitys);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SelectDrainageEntityActivity.SELECT_DRAINAGE_ENTITY){
            Object o = data.getSerializableExtra("data");
            if(o != null
                    && (o instanceof DrainageEntity)){
                mDrainageEntitys = (ArrayList<DrainageEntity>) o;
                mAdapter.notifyDataSetChanged(mDrainageEntitys);
            }
        }
    }
}
