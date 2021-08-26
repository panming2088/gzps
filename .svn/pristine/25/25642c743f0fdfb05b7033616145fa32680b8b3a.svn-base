package com.augurit.am.fw.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ac on 2016-07-26.
 */
public final class CompressUtil {
    // The default sample than
    private static final int DEFAULT_SAMPLE_SIZE = 1;

    private CompressUtil() {
    }

    /**
     * Calculate the scale value of the picture
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int caclulateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        if (options == null)
            return DEFAULT_SAMPLE_SIZE;
        int width = options.outWidth;
        int height = options.outHeight;
        int sampleSize = DEFAULT_SAMPLE_SIZE;
        if (width > reqWidth || height > reqHeight) {
            int widthRatio = (int) (width / (float) reqWidth);
            int heightRatio = (int) (height / (float) reqHeight);

            sampleSize = (widthRatio > heightRatio) ? heightRatio : widthRatio;
        }
        return sampleSize;
    }

    /**
     * Compresses the image from <code>Resources</code> according to the
     * sampling than
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opts);
        opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, opts);
    }

    /**
     * Compresses the image from the specified directory according to the
     * sampling than
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName,
                                                     int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opts);
        opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, opts);
    }

    /**
     * Compresses the image from the specified byte according to the sampling
     * than
     *
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data,
                                                          int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = caclulateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    /**
     * Compresses the image from the specified InputStream according to the
     * sampling than
     *
     * @param in
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromStream(InputStream in,
                                                       int reqWidth, int reqHeight) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] data = null;
        try {
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                bout.write(buf, 0, len);
            }
            data = bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodeSampledBitmapFromByteArray(data, reqWidth, reqHeight);
    }
}
