/**
 * ACCAndroid - ACC Android Development Platform
 * Copyright (c) 2014, AfirSraftGarrier, afirsraftgarrier@qq.com
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;


import com.augurit.am.fw.utils.IntentUtil;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.constant.BaseBundleKeyConstant;
import com.augurit.am.fw.utils.view.DialogUtil;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ImageUtil {

    /**
     * 照片点击事件
     *
     * @param context
     * @param list
     * @param position
     */
    public static void photoViewClick(Context context, List<Photo> list,
                                      int position, Class viewPhotoActivitySubClass) {
        Date time = list.get(position).getPhotoTime();
        if (time == null) {
            String name = list.get(position).getPhotoName();
            Date timeFromName = new Date();
            if (!TextUtils.isEmpty(name)) {
                String[] timestr = name.split("_");
                if (timestr != null && timestr.length != 0) {
                    try {
                        timeFromName = TimeUtil.getDateTimeYMDM(timestr[0]);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            list.get(position).setPhotoTime(timeFromName);

        }
        ImageUtil.showImage(context, list.get(position), viewPhotoActivitySubClass);
    }

    public static void showImage(Context context, Photo photo, Class viewPhotoActivitySubClass) {

        try {
            IntentUtil.startIntent(context, viewPhotoActivitySubClass,
                    BaseBundleKeyConstant.INTENT_SERIALIZABLE, photo);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    // public static void showImage(Context context, String imagePath) {
    // Intent intent = new Intent();
    // Bundle bundle = new Bundle();
    // bundle.putString("FilePath", imagePath);
    // intent.putExtras(bundle);
    // intent.setClass(context, MyphotoActivity.class);
    // context.startActivity(intent);
    // }

    // public static void showMyImage(Context context, String smallPath,
    // String imagePath, String url) {
    // Intent intent = new Intent();
    // Bundle bundle = new Bundle();
    // bundle.putString("SmallPath", smallPath);
    // bundle.putString("FilePath", imagePath);
    // bundle.putString("Url", url);
    // intent.putExtras(bundle);
    // intent.setClass(context, MyphotoActivity.class);
    // context.startActivity(intent);
    // }

    public static void deleteImage(final Context context,
                                   final String imagePath,
                                   final OnDeletedPhotoListener onDeletedPhotoListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog
                .setTitle("删除图片")
                .setMessage("确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // if (gallery.getSelectedItem() != null) {
                            // int id = gallery.getSelectedItemPosition();
                            // ACCPhoto photo = pList.get(id);
                            // if (photo.getDbID() != null) {
                            // PhotoDao photoDao = new PhotoDao(context);
                            // photoDao.DeletePhoto(photo);
                            // }
                            // pList.remove(id);
                            // edtPhotoAddr.setText("");
                            // edtPhotoInfo.setText("");
                            // imageAdapter.notifyDataSetChanged();
                            // Utils.MessageBox(context, "删除图片", "删除成功！");
                            // } else {
                            // Utils.MessageBox(context, "删除图片", "错误：没有选择图片！");
                            // }
                            if (imagePath != null) {
                                File file = new File(imagePath);
                                file.delete();
                            }
                            onDeletedPhotoListener.onDeletedPhoto();
                        } catch (Exception e) {
                            // TODO: handle exception
                            DialogUtil
                                    .MessageBox(context, "删除图片", "删除失败！");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setCancelable(false).create().show();
    }

    public interface OnDeletedPhotoListener {
        void onDeletedPhoto();
    }
}