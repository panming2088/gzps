package com.augurit.am.fw.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.view.View;


import com.augurit.am.fw.utils.constant.LibConstant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * Created by ac on 2016-07-26.
 */
public final class BitmapUtil {
    private BitmapUtil() {
    }

    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static Bitmap getBitmap(String bitmapPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(bitmapPath, options);
    }

    public static void recycleBitmapMap(
            Map<? extends Object, SoftReference<Bitmap>> softBitmapMap) {
        if (isEmpty(softBitmapMap)) {
            return;
        }
        for (SoftReference<Bitmap> softBitmap : softBitmapMap.values()) {
            Bitmap bitmap = softBitmap.get();
            if (bitmap == null || bitmap.isRecycled()) {
                continue;
            }
            bitmap.recycle();
        }
        softBitmapMap.clear();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static Bitmap getRightBitmap(String filePath) {
        Bitmap bitmap = null;
        try {
            if (AMFileUtil.getFileSize(filePath, AMFileUtil.FileSizeType.MB) > LibConstant.BITMAPNOTMAXTHANM) {
                return CompressUtil.decodeSampledBitmapFromFile(filePath, 480,
                        800);
            }
            bitmap = BitmapFactory.decodeFile(filePath, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return bitmap;
    }

    // public static Bitmap getSmallBitmap(String filePath) {
    // final BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // BitmapFactory.decodeFile(filePath, options);
    // options.inSampleSize = calculateInSampleSize(options, 480, 800);
    // options.inJustDecodeBounds = false;
    // return BitmapFactory.decodeFile(filePath, options);
    // }


    /**
     * Read the image's Angle of rotation
     *
     * @param path The absolute path to the image
     * @return The Angle of rotation
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            /**
             * Reads the image from the specified directory, and get the EXIF
             * information
             */
            ExifInterface exifInterface = new ExifInterface(path);
            /**
             * Gets the rotation information access to the image
             */
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * Rotate the image according to a specific angle.Uses {@link
     * com.acc.android.util.BitmapUtil.getBitmapDegree(String)} to calculate
     * the Angle of rotation,it will solve the problem that images always roates
     * after taking photo or opening photo after invoking this method.
     *
     * @param bm     The image that need to be rotated
     * @param degree The Angle of rotation
     * @return The image after the rotation
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        /**
         * Generates the rotation matrix according to the Angle of rotation,
         */
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            /**
             * Rotates the original image according to the rotation matrix and
             * get the new image
             */
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

//    /**
    //     * Converts small bitmap into <code>String</code>
    //     *
    //     * @param filePath
    //     * @return
    //     */
    //    public static String smallBitmapToString(String filePath) {
    //        Bitmap bm = CompressUtil
    //                .decodeSampledBitmapFromFile(filePath, 480, 800);
    //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    //        byte[] b = baos.toByteArray();
    //        try {
    //            baos.close();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        return Base64.encodeToString(b, Base64.DEFAULT);
    //    }

    public static String bitmapToString(String filePath) {
        Bitmap bm = getBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

/*
    public static List<AMFile> getACCFiles(Activity activity) {
        try {
            String accFilesString = activity.getIntent().getExtras()
                    .getString(LibConstant.KEY_BUNDLE_ACC_FILE_S);
            Type type = new TypeToken<List<AMFile>>() {
            }.getType();
            // return JsonManager.getInstance().getObject(accFilesString, type);
            return JsonUtil.getObject(accFilesString, type);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
*/

}
