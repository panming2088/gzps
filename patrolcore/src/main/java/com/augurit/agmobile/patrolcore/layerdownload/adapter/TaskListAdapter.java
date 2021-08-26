package com.augurit.agmobile.patrolcore.layerdownload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.holder.TaskListHolder;
import com.augurit.agmobile.patrolcore.layerdownload.model.LayerDnlTask;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;

/**
 * Created by liangsh on 2016-09-26.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListHolder>{

    private LayoutInflater mInflater;
    private ArrayList<LayerDnlTask> mTasks;

    public TaskListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mTasks = new ArrayList<>();
    }

    public void notifyDataSetChanged(ArrayList<LayerDnlTask> tasks){
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    public void addTask(LayerDnlTask task){
        if(mTasks==null){
            mTasks = new ArrayList<>();
        }
        mTasks.add(0, task);
        notifyDataSetChanged();
    }

    public void updateTask(int taskId, double total, double downed, boolean done, Throwable e){
        LayerDnlTask layerDnlTask = null;
        int position = 0;
        for(; position<mTasks.size(); ++position){
            LayerDnlTask task = mTasks.get(position);
            if(task.getId() == taskId){
                layerDnlTask = task;
                break;
            }
        }
        if(layerDnlTask==null){
            return;
        }
        layerDnlTask.setTotal(total);
        layerDnlTask.setDowned(downed);
        layerDnlTask.setDone(done);
    }

    @Override
    public TaskListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskListHolder(mInflater.inflate(R.layout.dnl_item_dnllist, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskListHolder holder, int position) {
        final LayerDnlTask layerDnlTask = mTasks.get(position);
            String typeStr = layerDnlTask.getType()==0 ? "瓦片：" : "矢量：";
            holder.titleTv.setText(typeStr + layerDnlTask.getLayerName());
            final int p = (int)(layerDnlTask.getDowned()/layerDnlTask.getTotal()*100);
            holder.progressTv.setText(p + "%");
            if(layerDnlTask.isDone()
                    && layerDnlTask.getTotal()!=0
                    && layerDnlTask.getDowned()!=0
                    && layerDnlTask.getTotal()==layerDnlTask.getDowned()){
//                holder.dnlingPb.finishLoad();
            }
    }

    @Override
    public int getItemCount() {
        if(ListUtil.isEmpty(mTasks)){
            return 0;
        }
        return mTasks.size();
    }

    public LayerDnlTask getTaskItem(int position){
        return mTasks.get(position);
    }

    public void removeTaskItem(int position){
        mTasks.remove(position);
    }

}
