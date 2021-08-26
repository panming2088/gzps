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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.UrlUtil;
import com.augurit.am.fw.utils.constant.PathConstant;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class HSPVFileUtil {
    // MIME_MapTable是所有文件的后缀名所对应的MIME类型的一个String数组
    private static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
    private static String PATH_ROOT;
    private static String TAKE_PHOTO_PATH;
    private static String PHOTO_PATH;
    private static String THUMBNAIL_PATH;
    private static String VIDEO_PATH;
    private static String INSTALL_PATH;

    public static final String ENCODE = "utf-8";

    // public static String SECOND_PATH_ROOT;
    private static String RECORD_PATH;

    /**
     * Judges whether sdcard is mounted.
     *
     * @return
     * @throws IllegalArgumentException if sdcard is not mounted.
     */
    public static boolean isSdCardExit() {

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            throw new IllegalArgumentException(
                    "Root path is empty.Please check whether the insert SD card！");
        }
        return sdCardExist;
    }

    /**
     * Sets the root path of the application.This method has to invoke once in
     * running application.
     *
     * @param customPath Allows to customize the root path of each application.If it is
     *                   {@code null},the default path is "/ACC/COMMON".
     * @return
     */
    public static String getPathRoot(String customPath) {
        if (PATH_ROOT == null) {
            if (isSdCardExit()) {
                File sdDir = Environment.getExternalStorageDirectory();
                PATH_ROOT = sdDir.toString();
                customPath = customPath != null ? customPath : (PathConstant.DEFAULT_PATH);
                PATH_ROOT = UrlUtil.buildURL(customPath, PATH_ROOT);
                if (!isFolderExists(PATH_ROOT)) {
                    PATH_ROOT = "";
                }
            } else {
                PATH_ROOT = "";
            }
        }

        return PATH_ROOT;
    }

    /**
     * Gets the file path of a specific file name.This file is in the root path.
     *
     * @param fileName
     * @return
     * @throws IllegalArgumentException if PATH_ROOT is empty or {@code null}.
     */
    public static String getFilePath(String fileName) {
        if (TextUtils.isEmpty(PATH_ROOT)) {
            PATH_ROOT = Environment.getExternalStorageDirectory().getPath();
            if (TextUtils.isEmpty(PATH_ROOT)) {
                throw new IllegalArgumentException(
                        "Root path is empty.Please check whether the insert SD card and invoke getPathRoot(customPath) again");
            } else {
                return UrlUtil.buildURL(fileName, PATH_ROOT);
            }
        }
        return UrlUtil.buildURL(fileName, PATH_ROOT);
    }

    /**
     * Gets the <code>File</code> of a specific file name.This <code>File</code>
     * is in the root path.
     *
     * @param filePath
     * @return
     */
    public static File getFile(String filePath) {
        HSPVFileUtil.isFileExists(filePath);
        return new File(filePath);
    }

    public static String getTimestampImageFilePath(String prefixString) {
        return getFilePath((prefixString != null ? prefixString + "_" : "")
                + System.currentTimeMillis() + ".jpg");
    }

    /**
     * Reads the contents of a file into a byte array. The file is always
     * closed.
     *
     * @param fileName the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     * @throws IOException in case of an I/O error
     */
    public static byte[] readFileToBytes(String fileName) throws IOException {
        String filePath = getFilePath(fileName);
        File file = new File(filePath);
        return FileUtils.readFileToByteArray(file);

    }

    /**
     * Reads the contents of a file line by line to a StringBuffer. The file is
     * always closed.
     *
     * @param file
     * @return
     */
    public static String getStringFromFile(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            List<String> contents = FileUtils.readLines(file, ENCODE);
            for (String lineTXT : contents) {
                sb.append(lineTXT.toString().trim());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Reads the contents of a file into a String . The file is always closed.
     *
     * @param filePath
     * @return
     */
    public static String readFileToString(String filePath) {
        File file = new File(filePath);
        try {
            return FileUtils.readFileToString(file, ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file
     * @param string
     * @return
     */
    public static boolean saveStringToFile(File file, String string) {
        try {
            FileUtils.writeStringToFile(file, string, ENCODE);
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a Bitmap to a file creating the file if it does not exist.
     *
     * @param filePath
     * @param bitmap
     */
    public static void saveBitmapFile(String filePath, Bitmap bitmap) {
        File file = new File(filePath);
        if (isFileExists(filePath)) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                // Bitmap.CompressFormat.PNG can save the transparent background
                // of map
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Judges a folder whether exists,if not it will create include its parent
     * folders.
     *
     * @param folderPath
     * @return true means exists or has been created;false means not exists or
     * fail to create folder .
     * @throws NullPointerException if folderPath is {@code null}.
     */
    public static boolean isFolderExists(String folderPath) {
        if (TextUtils.isEmpty(folderPath)) {
            throw new NullPointerException(
                    "Parameter 'folderPath' is null or empty");
        }
        if (isSdCardExit()) {
            File file = new File(folderPath);
            if (file.exists()) {
                return true;
            } else {
                return (file.mkdirs() || file.isDirectory());
            }
        }
        return false;
    }

    /**
     * Judges a file whether exists,if not it will create include its folder
     * path. folders.
     *
     * @param filePath
     * @return true if the file has been created or it already exists.
     * @throws NullPointerException if filePath is {@code null}.
     */
    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new NullPointerException(
                    "Parameter 'filepath' is null or empty");
        }
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        } else {
            String fileName = filePath;
            String fileDirName = fileName.substring(0,
                    fileName.lastIndexOf('/'));
            if (isFolderExists(fileDirName)) {
                try {
                    return (file.createNewFile() || file.isFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return false;
    }

    /**
     * Gets a file's name from its file path.Filepath never be {@code null}.
     *
     * @param filepath
     * @return
     * @throws NullPointerException     if filepath is {@code null}.
     * @throws IllegalArgumentException if filePath does not contain any '/'.
     */
    public static String getFileRealName(String filepath) {
        if (TextUtils.isEmpty(filepath)) {
            throw new NullPointerException(
                    "Parameter 'filepath' is null or empty");
        }
        String[] nameStrings = filepath.split(File.separator);
        if (ListUtil.isEmpty(nameStrings)) {
            throw new IllegalArgumentException(
                    "Parameter 'filepath' does not contain any '/'");
        }
        return nameStrings[nameStrings.length - 1];
    }

    /**
     * Deletes a file. If file is a directory, delete it and all
     * sub-directories.
     *
     * @param strFilePath
     * @return
     * @throws IllegalArgumentException if strFilePath is null or empty.
     * @throws IOException              in case deletion is unsuccessful
     */
    public static boolean deleteFile(String strFilePath) {
        if (TextUtils.isEmpty(strFilePath)) {
            throw new NullPointerException(
                    "Parameter 'strFilePath' is null or empty");
        }
        boolean result = false;
        File file = new File(strFilePath);
        try {
            FileUtils.forceDelete(file);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    /**
     * Copies a file to a new location preserving the file date.
     *
     * @param srcFilePath
     * @param destFilePath
     * @throws IOException
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs during copying
     */
    public static void copyFile(String srcFilePath, String destFilePath)
            throws IOException {

        File srcFile = new File(srcFilePath);
        File destFile = new File(destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * Reads the contents of a file in assets.
     *
     * @param context
     * @param fileName
     * @return
     */
    /*
    public static String getStringFromAsset(Context context, String fileName)
            throws IOException {
        InputStream in = null;
        try {
            in = context.getResources().getAssets().open(fileName);
            return IOUtils.toString(in, Charsets.toCharset(ENCODE));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    */
    /**
     * Reads the contents of a file in raw.
     * @return
     * @throws IOException
     */
    /*
    public static String getStringFromRaw(Context context, int rawId)
            throws IOException {

        InputStream in = null;
        try {
            in = context.getResources().openRawResource(rawId);
            return IOUtils.toString(in, Charsets.toCharset(ENCODE));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    */

    public static String getTakePathPhoto() {
        if (TAKE_PHOTO_PATH == null) {
            TAKE_PHOTO_PATH = getFilePath(PathConstant.DEFAULT_TAKE_PHOTO_PATH);
        }
        return TAKE_PHOTO_PATH;
    }

    public static String getPathPhoto() {
        if (PHOTO_PATH == null) {
            PHOTO_PATH = getFilePath(PathConstant.DEFAULT_SELECT_PHOTO_PATH);
        }
        try {
            File photoFile = new File(PHOTO_PATH);
            if (!photoFile.exists()) {
                photoFile.mkdirs();
            }
            String noMediaFile = PHOTO_PATH + "/.nomedia";
            File file = new File(noMediaFile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PHOTO_PATH;
    }

    public static String getPathThumbnail() {
        if (THUMBNAIL_PATH == null) {
            THUMBNAIL_PATH = getFilePath(PathConstant.DEFAULT_THUMBNAIL_PATH);
        }
        try {
            File thumbnalFile = new File(THUMBNAIL_PATH);
            if (!thumbnalFile.exists()) {
                thumbnalFile.mkdirs();
            }
            String noMediaFile = THUMBNAIL_PATH + "/.nomedia";
            File file = new File(noMediaFile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return THUMBNAIL_PATH;
    }

    public static String getPathVideo() {
        if (VIDEO_PATH == null) {
            VIDEO_PATH = getFilePath(PathConstant.DEFAULT_VIDEO_PATH);
        }
        return VIDEO_PATH;
    }

    public static String getPathRecord() {
        if (RECORD_PATH == null) {
            RECORD_PATH = getFilePath(PathConstant.DEFAULT_RECORD_PATH);
        }
        return RECORD_PATH;
    }

    public static String getPathInstall() {
        if (INSTALL_PATH == null) {
            INSTALL_PATH = getFilePath(PathConstant.DEFAULT_INSTALL_PATH);
        }
        return INSTALL_PATH;
    }

    public static File getPhotoFile(String fileName) {
        String photoFolderPath = getPathPhoto();
        String photoFullPath = photoFolderPath + File.separator + fileName;
        return new File(photoFullPath);
    }

    public static File getVideoFile(String fileName) {
        String videoFolderPath = getPathPhoto();
        String videoFullPath = videoFolderPath + File.separator + fileName;
        return new File(videoFullPath);
    }

    public static File geRecordFile(String fileName) {
        String recordFolderPath = getPathRecord();
        String recordFullPath = recordFolderPath + File.separator + fileName;
        return new File(recordFullPath);
    }

    public static File geInstallFile(String fileName) {
        String installFolderPath = getPathInstall();
        String installFullPath = installFolderPath + File.separator + fileName;
        return new File(installFullPath);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 打开文件
     *
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent);
    }
}