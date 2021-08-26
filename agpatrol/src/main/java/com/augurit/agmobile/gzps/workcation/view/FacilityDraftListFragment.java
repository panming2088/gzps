package com.augurit.agmobile.gzps.workcation.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.model.DoorNOBean;
import com.augurit.agmobile.gzps.uploadevent.util.PhotoUploadType;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.reeditfacility.ReEditUploadFacilityActivity;
import com.augurit.agmobile.mapengine.common.base.BaseRecyclerAdapter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by liangsh on 2017/11/21.
 */

public class FacilityDraftListFragment extends Fragment {
    private RecyclerView rv_upload_draft;
    private UploadDraftListAdapter uploadDraftAdapter;
    private TextView tv_draft_empty;
    private Context mContext;
    private List<UploadedFacility> mNWUploadedFacility = new ArrayList<>();
    private int currentPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_draft_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_upload_draft = (RecyclerView) view.findViewById(R.id.rv_event_draft);
        tv_draft_empty = (TextView) view.findViewById(R.id.tv_draft_empty);
        tv_draft_empty.setText("数据上报的本地草稿为空");
        uploadDraftAdapter = new UploadDraftListAdapter(mContext,mNWUploadedFacility);
        rv_upload_draft.setLayoutManager(new LinearLayoutManager(mContext));
        rv_upload_draft.setAdapter(uploadDraftAdapter);
        uploadDraftAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                currentPosition = position;
                UploadedFacility nwUploadedFacility = mNWUploadedFacility.get(position);
                Intent intent = new Intent(mContext, ReEditUploadFacilityActivity.class);
                intent.putExtra("data", (Parcelable) nwUploadedFacility);
                intent.putExtra("isDraft", true);
                startActivityForResult(intent, 123);
            }
        });
        uploadDraftAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position, long id) {
                DialogUtil.MessageBox(mContext, null, "确定删除草稿吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UploadedFacility nwUploadedFacility = mNWUploadedFacility.get(position);
                        AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", PhotoUploadType.UPLOAD_FOR_FACILITY + nwUploadedFacility.getDbid() + "");
                        AMDatabase.getInstance().delete(nwUploadedFacility);
                        initData();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return false;
            }
        });
        initData();
    }

    private void initData(){
        mNWUploadedFacility = AMDatabase.getInstance().getQueryByWhere(
                UploadedFacility.class,"markPersonId", BaseInfoManager.getUserId(mContext));
        for(UploadedFacility uploadedFacility:mNWUploadedFacility){
            List<Photo> photos = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", PhotoUploadType.UPLOAD_FOR_FACILITY + uploadedFacility.getDbid() + "");
            List<Photo> photoList = new ArrayList<>();
            List<Photo> thumPhotoList = new ArrayList<>();
            for(Photo photo:photos){
                if(photo.getType() == 1){
                    photoList.add(photo);
                }else if(photo.getType() == 2){
                    thumPhotoList.add(photo);
                }
            }
            if(!ListUtil.isEmpty(photos)){
                uploadedFacility.setPhotos(photoList);
                uploadedFacility.setThumbnailPhotos(thumPhotoList);
            }
            List<DoorNOBean> doorNOBeans = AMDatabase.getInstance().getQueryByWhere(DoorNOBean.class, "mph_id", PhotoUploadType.MPH_FOR_FACILITY + uploadedFacility.getDbid() + "");
            if(!ListUtil.isEmpty(doorNOBeans)){
                uploadedFacility.setMpBeen(doorNOBeans);
            }
        }
        Collections.sort(mNWUploadedFacility, new Comparator<UploadedFacility>() {
            @Override
            public int compare(UploadedFacility o1, UploadedFacility o2) {
                if(o1.getTime() > o2.getTime()){
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        uploadDraftAdapter.addData(mNWUploadedFacility);
        uploadDraftAdapter.notifyDataSetChanged();
        if(ListUtil.isEmpty(mNWUploadedFacility)){
            tv_draft_empty.setVisibility(View.VISIBLE);
            rv_upload_draft.setVisibility(View.GONE);
        } else {
            tv_draft_empty.setVisibility(View.GONE);
            rv_upload_draft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 123:
                    UploadedFacility nwUploadedFacility = mNWUploadedFacility.get(currentPosition);
                    AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", PhotoUploadType.UPLOAD_FOR_FACILITY + nwUploadedFacility.getDbid() + "");
                    AMDatabase.getInstance().delete(nwUploadedFacility);
                    initData();
                    break;
            }
        }
        if(requestCode == 123
                && resultCode == 123){
            initData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }
}
