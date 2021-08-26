package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.augurit.agmobile.gzps.BaseActivity;
import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadfacility.model.DrainageEntity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsh on 2018/2/2.
 */

public class SelectDrainageEntityActivity extends BaseActivity {

    public static final int SELECT_DRAINAGE_ENTITY = 0x832;

    private XRecyclerView mRecyclerView;
    private SelectDrainageEntityAdapter mAdapter;
    private List<DrainageEntity> mDrainageEntitys;
    private ArrayList<DrainageEntity> mSelectedDrainageEntitys;

    private int pageSize = 10;
    private int pageNo = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drainage_entity);

        Object o = getIntent().getSerializableExtra("data");
        if(o != null){
            mSelectedDrainageEntitys = (ArrayList<DrainageEntity>) o;
        }

        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", mAdapter.getSelectedDrainageEntitys());
                setResult(RESULT_OK , intent);
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText("排水户列表（可多选）");

        mRecyclerView = (XRecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectDrainageEntityAdapter(this, true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<DrainageEntity>() {
            @Override
            public void onItemClick(View view, int position, DrainageEntity selectedData) {

            }

            @Override
            public void onItemLongClick(View view, int position, DrainageEntity selectedData) {

            }
        });

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
            }
        });
        mockData();
    }


    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("data", mAdapter.getSelectedDrainageEntitys());
        setResult(RESULT_OK , intent);
        finish();
    }

    private void mockData(){
        mDrainageEntitys = new ArrayList<>();
        for(int i=0; i< 20; i++){
            DrainageEntity drainageEntity = new DrainageEntity();
            drainageEntity.setEntry_name("沙县小吃" + (i + 5));
            drainageEntity.setLicense_key("4270字第0100号");
            if(i%2 == 0){
                drainageEntity.setType("一般");
            } else {
                drainageEntity.setType("重点");
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
        mAdapter.notifyDataSetChanged(mDrainageEntitys, mSelectedDrainageEntitys);
    }
}
