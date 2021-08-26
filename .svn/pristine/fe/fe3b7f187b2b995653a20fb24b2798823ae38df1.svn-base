package com.augurit.agmobile.patrolcore.localhistory.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.patrolcore.common.table.dao.TableDataManager;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTableItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.cmpt.widget.multichoicelistview.MultiChoiceAdapter;
import com.bumptech.glide.Glide;
import com.augurit.agmobile.patrolcore.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Key on 2016/5/4.
 *
 * @author Key
 */
public class UnCommitProblemAdapter extends MultiChoiceAdapter<UnCommitProblemAdapter.UnCommitProblemViewHolder> {

    private List<LocalTable> mData;
    private Context context;

    public UnCommitProblemAdapter(List<LocalTable> data,Context context) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public UnCommitProblemAdapter.UnCommitProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_problems_listview_item, parent, false);
        return new UnCommitProblemAdapter.UnCommitProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UnCommitProblemAdapter.UnCommitProblemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof UnCommitProblemAdapter.UnCommitProblemViewHolder) {
            UnCommitProblemAdapter.UnCommitProblemViewHolder viewHolder = holder;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String timeStr = format.format(new Date(mData.get(position).getTime()));
            viewHolder.date.setText(timeStr);

            String title = getTitle(mData.get(position).getList());
            viewHolder.title.setText(title);

            String location = getPositon(mData.get(position).getList());
            viewHolder.addr.setText(location);

            Photo photo = getDefaultPhoto(mData.get(position));
            if(photo != null){
                Glide.with(context).load(photo.getPhotoPath())
                        .into(viewHolder.iv);
            }

        }
        if (isSelectable()) {
            if (holder.checkbox.isChecked() != isItemCheck(position)) {
                holder.checkbox.setOnCheckedChangeListener(null);
                holder.checkbox.setChecked(isItemCheck(position));
            }
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setOnCheckedChangeListener(new CheckBoxCheckedChangeListener(position));
        }  else {
            holder.checkbox.setVisibility(View.GONE);
        }
    }

    public class UnCommitProblemViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView title;
        TextView date;
        TextView addr;
        TextView status;
        public CheckBox checkbox;

        public UnCommitProblemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.search_result_item_iv);
            title = (TextView) itemView.findViewById(R.id.search_item_title);
            date = (TextView) itemView.findViewById(R.id.search_item_date);
            addr = (TextView) itemView.findViewById(R.id.search_item_addr);
            status = (TextView) itemView.findViewById(R.id.search_item_status);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    private class CheckBoxCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        private int mPosition;

        public CheckBoxCheckedChangeListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setItemCheck(mPosition, isChecked);
        }
    }

    public String getTitle(List<LocalTableItem> list){
        String title ="";
        for(LocalTableItem item : list){
            if(item.getField1().equals("DISEASE_TYPES")){
                if(item.getValue() != null){
                    title = item.getValue();
                    break;
                }
            }
        }
        return  title;
    }

    public String getPositon(List<LocalTableItem> list){
        String positon = "";
        for(LocalTableItem item : list){
            if(item.getField1().equals("POISION")){
                if(item.getValue() != null){
                    positon = item.getValue();
                    break;
                }
            }
        }
        return positon;
    }

    public Photo getDefaultPhoto(LocalTable localTable){
        String photoKey = localTable.getKey();
        TableDataManager tableDataManager = new TableDataManager(context);
        List<Photo> photos = tableDataManager.getPhotoFormDB(photoKey);
        if(photos != null && photos.size() > 0){
            return photos.get(0);
        }

        return null;

    }

}
