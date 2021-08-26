package com.augurit.am.cmpt.widget.filepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.augurit.am.fw.utils.log.LogUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dimorinny on 24.10.15.
 */
public final class FileUtils {
    private FileUtils() {
    }

    public static List<File> getFileListByDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);

        if (files == null) {
            return new ArrayList<>();
        }

        List<File> result = Arrays.asList(files);
        Collections.sort(result, new FileComparator());
        return result;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }



    public static String getFilenameFromUrlWithSuffix(String url) {
        int start = url.lastIndexOf("/") + 1;
        String filename = url.substring(start);
        return filename;
    }

    public static String getFilenameFromUrl(String url) {
        int start = url.lastIndexOf("/") + 1;
        int point = url.lastIndexOf(".");
        String filename = url.substring(start, point);
        return filename;
    }

    public static String getFilenameWithoutSubffix(String file){
        boolean isPath = file.contains(File.separator);
        int point = file.lastIndexOf(".");
        String filename = file;
        if(isPath){
            filename = file.substring(file.lastIndexOf(File.separator)+1);
        }
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public static String getFileSuffix(String file) {
        int point = file.lastIndexOf(".");
        return file.substring(++point);
    }

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.#");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }

    /**
     * 适配android7.0的文件打开
     * @param intent
     */
    public static void setFlags(Intent intent) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }

    public static Uri getUri(String UrlStr, Context context) {
        File file = new File(UrlStr);
        Uri uri;
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            uri = FileProvider.getUriForFile(context, context.getPackageName()+".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 找文件后缀
     * @param fileName
     * @return
     */
    public static String getMimeTypeByFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
    }

    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
}
