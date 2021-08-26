package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.augurit.am.cmpt.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.HorizontalScrollPhotoView
 * @createTime 创建时间 ：17/3/8
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/8
 * @modifyMemo 修改备注：
 */

public class HorizontalScrollPhotoViewAdapter extends BaseAdapter {

    protected List<Photo> mPList;
    protected LayoutInflater mInflater;
    private HorizontalScrollPhotoView mParentView;
    private Context mContext;
    // ImageView的宽度
    private int mChildWidth;
    // ImageView的高度
    private int mChildHeight;

    public HorizontalScrollPhotoViewAdapter(Context context,
                                            HorizontalScrollPhotoView parentView, List<Photo> pList) {
        // this.mContext = mContext;
        this(context, parentView, pList, 120, 120);
    }

    /**
     * @param context
     * @param parentView  HorizontalScrollPhotoView
     * @param pList
     * @param childWidth  每张照片在 HorizontalScrollPhotoView中的宽度
     * @param childHeight 每张照片在 HorizontalScrollPhotoView中的高度
     */
    public HorizontalScrollPhotoViewAdapter(Context context,
                                            HorizontalScrollPhotoView parentView, List<Photo> pList,
                                            int childWidth, int childHeight) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mPList = pList;
        this.mParentView = parentView;
        this.mChildWidth = childWidth;
        this.mChildHeight = childHeight;
    }

    @Override
    public int getCount() {
        return mPList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HorizontalViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new HorizontalViewHolder();
            convertView = mInflater.inflate(R.layout.checkable_item, parent,
                    false);

            viewHolder.mImgView = (ImageView) convertView
                    .findViewById(R.id.ItemImage);
            ViewGroup.LayoutParams lp = viewHolder.mImgView.getLayoutParams();
            lp.width = mChildWidth;
            lp.height = mChildHeight;
            viewHolder.mImgView.setLayoutParams(lp);
            convertView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HorizontalViewHolder) convertView.getTag();
        }

        if (getCount() != 0) {

            //如果有缩略图，优先加载缩略图
            if (!TextUtils.isEmpty(mPList.get(position).getThumbPath())) {
                Glide.with(mContext).load(mPList.get(position).getThumbPath())
                        .placeholder(R.drawable.common_ic_load_pic)
                        .error(R.drawable.common_ic_load_pic_error1)
                        .into(viewHolder.mImgView);
            } else {
                Glide.with(mContext).load(mPList.get(position).getPhotoPath())
                        .placeholder(R.drawable.common_ic_load_pic)
                        .error(R.drawable.common_ic_load_pic_error1)
                        .into(viewHolder.mImgView);
            }

                /*
                if(mPList.get(position).getPhotoPath().contains("http")) {
                    //网络上的图片
                    Glide.with(mContext).load(mPList.get(position).getPhotoPath())
                            .placeholder(R.drawable.common_ic_load_pic)
                            .error(mContext.getResources().getDrawable(R.drawable.common_ic_load_pic_error))
                            .into(viewHolder.mImgView);
                }else {
                    //本地上的图片(需要解密)
                    String localPath = mPList.get(position).getLocalPath();
                    File encryptFile = new File(localPath);
                    File decryptFile = CryptoUtil.decryptFileToFile(encryptFile);
                    try {
                        Glide.with(mContext).load(decryptFile)
                                .placeholder(R.drawable.common_ic_load_pic)
                                .error(mContext.getResources().getDrawable(R.drawable.common_ic_load_pic_error))
                                .into(viewHolder.mImgView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                */


        }
        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {

        if (mPList == null || mPList.size() == 0) {
            mParentView.setVisibility(View.GONE);
        } else {
            mParentView.setVisibility(View.VISIBLE);
            // isChecked = new boolean[mPList.size()];
            // for (int i = 0; i < mPList.size(); i++) {
            // isChecked[i] = false;
            // }
        }
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<Photo> photos) {
        this.mPList = photos;
        this.notifyDataSetChanged();
    }

    public final class HorizontalViewHolder {

        public ImageView mImgView;
    }
}
