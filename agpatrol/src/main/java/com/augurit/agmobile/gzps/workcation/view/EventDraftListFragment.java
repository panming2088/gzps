package com.augurit.agmobile.gzps.workcation.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.uploadevent.model.ProblemUploadBean;
import com.augurit.agmobile.gzps.uploadevent.view.eventdraft.EventDraftDetailActivity;
import com.augurit.agmobile.gzps.uploadevent.view.eventdraft.FeedBackEventDraftDetailActivity;
import com.augurit.agmobile.gzps.uploadevent.view.eventdraft.JournalEventDraftDetailActivity;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.DialogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liangsh on 2017/11/21.
 */

public class EventDraftListFragment extends Fragment {

    private RecyclerView rv_event_draft;
    private EventDraftAdapter eventDraftAdapter;
    private TextView tv_draft_empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_draft_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_event_draft = (RecyclerView) view.findViewById(R.id.rv_event_draft);
        tv_draft_empty = (TextView) view.findViewById(R.id.tv_draft_empty);
        eventDraftAdapter = new EventDraftAdapter(getContext());
        rv_event_draft.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_event_draft.setAdapter(eventDraftAdapter);
        eventDraftAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<ProblemUploadBean>() {
            @Override
            public void onItemClick(View view, int position, ProblemUploadBean selectedData) {
                String sfjb = selectedData.getSfjb();
                if("1".equals(sfjb)){
                    Intent intent = new Intent(getContext(), FeedBackEventDraftDetailActivity.class);
                    intent.putExtra("problemUploadBean", selectedData);
                    List<Photo> photos = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", selectedData.getDbid() + "");
                    intent.putExtra("photos", new ArrayList<Photo>(photos));
                    startActivityForResult(intent, 123);
                }else if("2".equals(sfjb)){
                    Intent intent = new Intent(getContext(), JournalEventDraftDetailActivity.class);
                    intent.putExtra("problemUploadBean", selectedData);
                    List<Photo> photos = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", selectedData.getDbid() + "");
                    intent.putExtra("photos", new ArrayList<Photo>(photos));
                    startActivityForResult(intent, 123);
                }else{
                    Intent intent = new Intent(getContext(), EventDraftDetailActivity.class);
                    intent.putExtra("problemUploadBean", selectedData);
                    List<Photo> photos = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", selectedData.getDbid() + "");
                    intent.putExtra("photos", new ArrayList<Photo>(photos));
                    startActivityForResult(intent, 123);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, final ProblemUploadBean selectedData) {
                DialogUtil.MessageBox(getContext(), null, "确定删除草稿吗", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AMDatabase.getInstance().deleteWhere(Photo.class, "problem_id", selectedData.getDbid() + "");
                        AMDatabase.getInstance().delete(selectedData);
                        initData();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        initData();
    }

    private void initData(){
        List<ProblemUploadBean> problemUploadBeanList = AMDatabase.getInstance().getQueryByWhere(
                ProblemUploadBean.class,"SBR", BaseInfoManager.getUserName(getActivity()));

        Collections.sort(problemUploadBeanList, new Comparator<ProblemUploadBean>() {
            @Override
            public int compare(ProblemUploadBean o1, ProblemUploadBean o2) {
                if(o1.getTime() > o2.getTime()){
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        eventDraftAdapter.notifyDataSetChanged(problemUploadBeanList);
        if(ListUtil.isEmpty(problemUploadBeanList)){
            tv_draft_empty.setVisibility(View.VISIBLE);
            rv_event_draft.setVisibility(View.GONE);
        } else {
            tv_draft_empty.setVisibility(View.GONE);
            rv_event_draft.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123
                && resultCode == 123){
            initData();
        }
    }
}
