package com.augurit.agmobile.gzps.uploadfacility.view.drainageentity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.cache.img.amimageloader.AMImageLoader;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * Created by lsh on 2018/2/2.
 */

public class DrainageEntityFileAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> mFiles;
    private OnRecyclerItemClickListener<Photo> mOnRecyclerItemClickListener;

    public DrainageEntityFileAdapter(Context context){
        mContext = context;
    }

    public void notifyDataSetChanged(List<Photo> files){
        this.mFiles = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(ListUtil.isEmpty(mFiles)) {
            return 0;
        }
        return mFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_drainage_file, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        final Photo photo = mFiles.get(position);
        AMImageLoader.loadStringRes(iv, photo.getThumbPath());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRecyclerItemClickListener != null){
                    mOnRecyclerItemClickListener.onItemClick(v, position, photo);
                }
            }
        });
        return view;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<Photo> onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }
}
