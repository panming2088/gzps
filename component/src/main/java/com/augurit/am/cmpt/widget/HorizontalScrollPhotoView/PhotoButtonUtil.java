package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.SdkUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.view.DialogUtil;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.HorizontalScrollPhotoView
 * @createTime 创建时间 ：17/3/9
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/9
 * @modifyMemo 修改备注：
 */

public class PhotoButtonUtil {
    public static final int RESULT_CAPTURE_PHOTO = 3;// 图片返回刷新
    public static final int RESULT_OPEN_PHOTO = 4;

    public static PhotoExtra registTakePhotoButton(Activity activity,
                                                   String photoPath) {
        PhotoExtra photoExtra = getPhotoExtra(activity, photoPath, "take_photo");
        if (photoExtra == null) {
            return null;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoExtra.getBitmapDataUri());
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        activity.startActivityForResult(intent,
                RESULT_CAPTURE_PHOTO);
        return photoExtra;
    }

    public static PhotoExtra registOpenPhotoButton(Activity activity,
                                                   String photoPath) {
        PhotoExtra photoExtra = getPhotoExtra(activity, photoPath, "open_photo");
        if (photoExtra == null) {
            return null;
        }
        Intent intent_open = new Intent(Intent.ACTION_GET_CONTENT);
        intent_open.setType("image/*");
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        // intent.putExtra("outputFormat", "JPEG");
        activity.startActivityForResult(intent_open,
               RESULT_OPEN_PHOTO);
        return photoExtra;
    }


    private static PhotoExtra getPhotoExtra(Activity activity,
                                            String photoPath, String title) {
        if (!HSPVFileUtil.isSdCardExit()) {
            DialogUtil.MessageBox(activity,
                    ResourceUtil.getString(activity, title),
                    ResourceUtil.getString(activity, "sdcard_error"));
            return null;
        }

        boolean temp = HSPVFileUtil.isFolderExists(photoPath);
        if (!HSPVFileUtil.isFolderExists(photoPath)) {
            return null;
        }
        Date photoTime = new Date();
        String dateString = TimeUtil
                .getStringTimeYYMMDDSS(photoTime);
        String filename = dateString + "_img.jpg";

        String mFilePath = photoPath + "/" + filename;
        File mImageFile = new File(mFilePath);

        Uri bitmapDataUri ;
        //Android 7.0采用了StrictMode API政策,私有目录被限制访问
        //判断Android版本是否是Android7.0以上
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            bitmapDataUri= FileProvider.getUriForFile(activity,activity.getPackageName()+".FileProvider",mImageFile);
        }else{
            bitmapDataUri=Uri.fromFile(mImageFile);
        }
       // Uri bitmapDataUri = Uri.fromFile(mImageFile);
        return new PhotoExtra(filename, mFilePath, photoTime, bitmapDataUri);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void openPhotoCopy(Context context, Intent data,
                                     String mFilePath) {
        if (data == null) {
            return;
        }
        Uri contentUri = data.getData();
        int column_index = -1;
        String img_path = "";
        Cursor cursor;
        /**
         * Build.VERSION_CODES.KITKAT(19)后有两种Uri
         * content://com.android.providers.media.documents/document/image:3951
         content://media/external/images/media/3951
         */
        if (SdkUtil.isHighSdkVerson(Build.VERSION_CODES.KITKAT) && DocumentsContract.isDocumentUri(context, contentUri)) {
            String wholeID = DocumentsContract.getDocumentId(contentUri);
            String imageId = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                    sel, new String[]{imageId}, null);
            column_index = cursor.getColumnIndex(column[0]);

        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
            if(cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
        }
        if (cursor != null && cursor.moveToFirst()) {
            img_path = cursor.getString(column_index);
        }else {
            Uri selectedImage = data.getData();
            img_path = selectedImage.getPath();
        }

        try {
            if (!TextUtils.isEmpty(img_path)) {

                copyFile(img_path, mFilePath);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void copyFile(String srcFilePath, String destFilePath)
            throws IOException {
        File srcFile = new File(srcFilePath);

        //加密图片
        /*
        try {
            byte[] bytes = AMFileUtil.readFileToBytes(srcFile);
            CryptoUtil.encryptFile(bytes,srcFilePath);

        }catch (Exception e){
            e.printStackTrace();
        }
        */

        File destFile = new File(destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (SdkUtil.isHighSdkVerson(Build.VERSION_CODES.KITKAT) && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static PhotoExtra registOpenPhotoButton2(Activity activity,
                                                    String photoPath) {
        PhotoExtra photoExtra = getPhotoExtra(activity, photoPath, "open_photo");
        if (photoExtra == null) {
            return null;
        }
        photoExtra.setFilePath(photoExtra.getFilePath().substring(0,photoExtra.getFilePath().lastIndexOf("/")+1));
        return photoExtra;
    }
}
