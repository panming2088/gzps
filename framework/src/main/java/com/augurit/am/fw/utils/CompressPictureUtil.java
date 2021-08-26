package com.augurit.am.fw.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.augurit.am.fw.scy.CryptoUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ac on 2016-07-26.
 */
public final class CompressPictureUtil {
    public static final boolean useOld = false;
    private static int defaultSize = 14;

    private CompressPictureUtil() {
    }

    public static void startAsyAsyncTaskOrNot(final Context context,
                                              final String filePath,
                                              final OnCompressPictureOverListener onCompressPictureOverListener) {
        // CommonInitDataUtil.initSetData(context);
        try {
            // int fileAvalible = new FileInputStream(new File(filePath))
            // .available();
            // // System.out.println("VVVVVVVVVVVVVv" + fileAvalible);
            // // System.out.println("VVVVVVVVVVVVVv" + filePath);
            // // new FileInputStream(filename)
            // int fileSizeInM = fileAvalible * 100 / 1048576;
            // double fileSize = fileSizeInM / 100.0d;
            if (
                // fileAvalible > 1048576
                    true) {
                // new AlertDialog.Builder(context)
                // .setMessage(
                // "图片大小为" + fileSize + "M"
                // + ",为节省你的流量，建议进行图片压缩，是否要进行图片压缩？")
                // .setPositiveButton("是",
                // new DialogInterface.OnClickListener() {
                // @Override
                // public void onClick(DialogInterface dialog,
                // int which) {
                // finish();
                new CompressPictureAsyncTask(context,
                        filePath, onCompressPictureOverListener).execute();
                // ProblemUpload.this
                // .addPhotoAndUpdateImageAdapter();
                // }
                // })
                // .setNegativeButton("否",
                // new DialogInterface.OnClickListener() {
                // @Override
                // public void onClick(DialogInterface dialog,
                // int which) {
                // // finish();
                // onCompressPictureOverListener
                // .onCompressPictureOver();
                // }
                // }).show();
                // // }
                // Dialog dialog = new
                // AlertDialog.Builder(ProblemUpload.this)
                // // .setTitle("软件更新")
                // .setMessage("图片过大，建议进行图片压缩，是否要进行图片压缩？")// 设置内容
                // .setPositiveButton("是",// 设置确定按钮
                // new DialogInterface.OnClickListener() {
                // @Override
                // public void onClick(
                // DialogInterface dialog,
                // int which) {
                // // finish();
                // }
                // }).setNegativeButton("是", new
                // DialogInterface.OnClickListener() {
                // @Override
                // public void onClick(
                // DialogInterface dialog,
                // int which) {
                // // finish();
                // }).create();// 创建
                // // 显示对话框
                // dialog.show();
            } else {
                onCompressPictureOverListener.onCompressPictureOver(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成缩略图
     *
     * @param context
     * @param srcPath
     * @param onCompressPictureOverListener
     */
    public static void createThumbnail(Context context,
                                       String srcPath,
                                       String thumbnailPath,
                                       boolean showDialog,
                                       OnCompressPictureOverListener onCompressPictureOverListener) {
        new CreateThumbnailAsyncTask(context,
                srcPath, thumbnailPath, showDialog, onCompressPictureOverListener).execute();
    }

    private static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//
            // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//
            // 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        // long size = CommonInitDataUtil.setData != null ?
        // CommonInitDataUtil.setData
        // .getCompressPictureSize() : defaultSize;
        long size = defaultSize;
        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public interface OnCompressPictureOverListener {
        void onCompressPictureOver(String filePath);
    }

    // /**
    // *
    // * 旋转图片
    // */
    // public static Bitmap rotate(Bitmap b, int degrees) {
    //
    // if (degrees != 0 && b != null) {
    // Matrix m = new Matrix();
    // m.setRotate(degrees, (float) b.getWidth() / 2,
    // (float) b.getHeight() / 2);
    // try {
    // Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
    // b.getHeight(), m, true);
    // if (b != b2) {
    // b.recycle(); // 释放bitmap
    // b = b2;
    // }
    // } catch (OutOfMemoryError ex) {
    // // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。
    // }
    //
    // }
    //
    // return b;
    //
    // }

    public static class CompressPictureAsyncTask extends
            AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private Context context;
        private String filePath;
        private OnCompressPictureOverListener onCompressPictureOverListener;

        public CompressPictureAsyncTask(Context context, String filePath,
                                        OnCompressPictureOverListener onCompressPictureOverListener) {
            this.context = context;
            this.filePath = filePath;
            this.onCompressPictureOverListener = onCompressPictureOverListener;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(this.context, "压缩成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            // this.printFileSize(this.filePath);

            //压缩完后进行图片加密
            /*
            if(filePath != null){
                try {
                    byte[] bytes = AMFileUtil.readFileToBytes(filePath);
                    CryptoUtil.encryptFile(bytes,filePath);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            */

            this.onCompressPictureOverListener.onCompressPictureOver(filePath);
        }

        private void printFileSize(String filePath) {
            try {
                // int fileAvalible = new FileInputStream(new File(filePath))
                // .available();
                int fileAvalible = new FileInputStream(new File(filePath))
                        .available();
                int fileSizeInM = fileAvalible * 100 / 1048576;
                double fileSize = fileSizeInM / 100.0d;
                String fileSizeStr = "" + fileSize + "M";
                if (fileSize < 1) {
                    int fileSizeInK = fileAvalible * 100 / 1024;
                    fileSize = fileSizeInK / 100.0d;
                    fileSizeStr = "" + fileSize + "K";
                }
                LogUtil.i("11111111111111now" + fileSizeStr);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch
                // block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch
                // block
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(this.context);
            dialog.setMessage("压缩图片中，请稍候...");
            dialog.setCancelable(false);
            // dialog.set
            // dialog.setButton("取消", new DialogInterface.OnClickListener() {
            // @Override
            // public void onClick(DialogInterface dialog, int whichButton) {
            // dialog.dismiss();
            // }
            // });
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (!CompressPictureUtil.useOld) {

                    int degree = BitmapUtil.getBitmapDegree(this.filePath);
                    // Bitmap bitmap = CompressUtil.getSmallBitmap(filePath);
                    Bitmap bitmap = BitmapUtil.rotateBitmapByDegree(
                            CompressUtil.decodeSampledBitmapFromFile(filePath, DisplayUtil.getWindowWidth(context), DisplayUtil.getWindowHeight(context)), degree);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                    int options = 30;
                    while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
                        options -= 5;//每次都减少5
                        baos.reset();//重置baos即清空baos
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                        if (options <= 10) {
                            //小于等于10时不再压缩
                            break;
                        }
                    }
                    AMFileUtil.saveBytesToFile(baos.toByteArray(), new File(filePath));
                } else {
                    Bitmap rawBitmap = BitmapFactory.decodeFile(this.filePath,
                            null);
                    if (rawBitmap != null) {
                        rawBitmap = comp(rawBitmap);
                        FileOutputStream out = new FileOutputStream(
                                this.filePath);
                        rawBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /*@Override
        protected Void doInBackground(Void... str) {
            try {
                if (!CompressPictureUtil.useOld) {

                    *//**
         * 解决图片自动旋转角度的问题
         *//*
                    int degree = BitmapUtil.getBitmapDegree(this.filePath);
                    // Bitmap bitmap = CompressUtil.getSmallBitmap(filePath);
                    Bitmap bitmap = BitmapUtil.rotateBitmapByDegree(
                            CompressUtil.decodeSampledBitmapFromFile(filePath, DisplayUtil.getWindowWidth(context), DisplayUtil.getWindowHeight(context)), degree);
                    FileOutputStream out = new FileOutputStream(this.filePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                } else {
                    Bitmap rawBitmap = BitmapFactory.decodeFile(this.filePath,
                            null);
                    if (rawBitmap != null) {
                        rawBitmap = comp(rawBitmap);
                        FileOutputStream out = new FileOutputStream(
                                this.filePath);
                        rawBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    }
                }
            } catch (Exception exception) {
            }
            return null;
        }*/


    }

    public static class CreateThumbnailAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String srcPath;
        private String thumbnailPath;
        private boolean showDialog;
        private OnCompressPictureOverListener onCompressPictureOverListener;

        public CreateThumbnailAsyncTask(Context context,
                                        String srcPath,
                                        String thumbnailPath,
                                        boolean showDialog,
                                        OnCompressPictureOverListener onCompressPictureOverListener) {
            this.context = context;
            this.srcPath = srcPath;
            this.thumbnailPath = thumbnailPath;
            this.showDialog = showDialog;
            this.onCompressPictureOverListener = onCompressPictureOverListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(showDialog) {
                dialog = new ProgressDialog(this.context);
                dialog.setMessage("正在生成缩略图，请稍候...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int degree = BitmapUtil.getBitmapDegree(this.srcPath);
                Bitmap bitmap = BitmapUtil.rotateBitmapByDegree(
                        CompressUtil.decodeSampledBitmapFromFile(srcPath, 100, 160), degree);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                int options = 10;
                while (baos.toByteArray().length / 1024 > 10) {  //循环判断如果压缩后图片是否大于10kb,大于继续压缩
                    options -= 2;//每次都减少2
                    baos.reset();//重置baos即清空baos
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                    if (options <= 2) {
                        //小于等于2时不再压缩
                        break;
                    }
                }
                AMFileUtil.saveBytesToFile(baos.toByteArray(), new File(thumbnailPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(showDialog){
                dialog.dismiss();
            }
            this.onCompressPictureOverListener.onCompressPictureOver(thumbnailPath);
        }
    }
}
