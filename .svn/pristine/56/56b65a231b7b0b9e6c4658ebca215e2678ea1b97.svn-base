package me.nereo.multi_image_selector.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.R;
import me.nereo.multi_image_selector.bean.Folder;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImageFolderAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<Folder> imageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity, List<Folder> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders = new ArrayList<>();

        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders.size() + 1;
    }

    @Override
    public Folder getItem(int position) {
        if (position == 0) return null;
        return imageFolders.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mis_list_item_folder, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Folder folder = getItem(position);
        if (folder == null) {
            holder.name.setText(R.string.mis_folder_all);
            holder.path.setText("/sdcard");
            holder.size.setText(String.format("%d%s",
                    getTotalImageSize(), mActivity.getResources().getString(R.string.mis_photo_unit)));
            folder = imageFolders.get(0);
        } else {
            holder.name.setText(folder.name);
            holder.path.setText(folder.path);
            holder.size.setText(String.format("%d%s",
                    folder.images.size(), mActivity.getResources().getString(R.string.mis_photo_unit)));
        }
//        Glide.with(mActivity)                             //配置上下文
////                .load(Uri.fromFile(new File(f.cover.getFilePath())))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .load(folder.cover.getFilePath())      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .placeholder(R.drawable.mis_default_error)
//                .error(R.drawable.mis_default_error)//加载错误之后的错误图
////                                .override(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)//指定图片的尺寸
//                .diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
//                .centerCrop()
//                .into(holder.cover);
        Picasso.with(mActivity)
                .load(new File(folder.cover.getFilePath()))
                .error(R.drawable.mis_default_error)
                .resizeDimen(R.dimen.mis_folder_cover_size, R.dimen.mis_folder_cover_size)
                .centerCrop()
                .into(holder.cover);
        if (lastSelected == position) {
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.indicator.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (imageFolders != null && imageFolders.size() > 0) {
            for (Folder f : imageFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    private class ViewHolder {
        ImageView cover;
        TextView name;
        TextView path;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            path = (TextView) view.findViewById(R.id.path);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }
    }
}
