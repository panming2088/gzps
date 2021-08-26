package com.augurit.am.fw.utils;

import android.os.Environment;
import android.text.TextUtils;


import com.augurit.am.fw.utils.constant.PathConstant;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okio.ByteString;

/**
 * 文件操作类
 * sd卡的判断和文件的读写
 * <p/>
 * Created by lb on 2016-07-31.
 */
public final class AMFileOpUtil {
    private AMFileOpUtil() {
    }

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

    private static String PATH_ROOT ; //根目录
    private static String PHOTO_PATH;//相片路径
    private static String VIDEO_PATH; //视频路径
    private static String INSTALL_PATH; //安装路径
    private static String RECORD_PATH; //音频路径

    ////初始化PATH_ROOT
    static{
        if (PATH_ROOT == null) {
            if (isSdCardExist()) {
                File sdDir = Environment.getExternalStorageDirectory();
                PATH_ROOT = sdDir.toString();
                PATH_ROOT = UrlUtil.buildURL(PathConstant.DEFAULT_PATH,PATH_ROOT);
                if (!isFolderExists(PATH_ROOT)) {
                    PATH_ROOT = "";
                }
            } else {
                PATH_ROOT = "";
            }
        }
    }
    /**
     * 判断sd卡是否存在
     *
     * @return
     * @throws IllegalArgumentException 抛出异常，sd卡没安装
     */
    public static boolean isSdCardExist() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            throw new IllegalArgumentException(
                    "Root path is empty.Please check whether the insert SD card！");
        }
        return sdCardExist;
    }


    /**
     * 创建文件
     *
     * @param file 文件对象
     * @throws IOException
     */
    public static void createFile(File file) throws IOException {
        if (!file.exists() && !file.isDirectory()) {
            file.createNewFile();
        }
    }


    /**
     * 创建文件夹
     *
     * @param file 文件对象
     */
    public static void createDirectory(File file) {
        makeIfNotExistFileDir(file);
    }

    /**
     * 创建文件夹
     * @param dirName 文件夹路径
     */
    public static void createDirectory(String dirName) {
        makeIfNotExistDir(dirName);
    }

    /**
     * 创建文件夹
     * @param dirName
     * @return
     */
    private static File makeIfNotExistDir(String dirName) {
        final File file = new File(dirName);
        if (!file.exists()) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * 创建文件对象的文件夹
     * @param file
     */
    private static void makeIfNotExistFileDir(File file) {
        try {
            String fileName = file.getAbsolutePath();
            String fileDirName = fileName.substring(0,
                    fileName.lastIndexOf('/'));
            File dirFile = new File(fileDirName);
            if (!dirFile.exists()) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    dirFile.mkdirs();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在，否则创建文件
     *
     * @param filePath
     * @return
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
     * 判断文件是否存在，否则创建文件
     *
     * @param file
     * @return
     */
    public static boolean isFileExists(File file) {
        if (file.exists()) {
            return true;
        } else {
            try {
                return (file.createNewFile() || file.isFile());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 判断文件夹是否存在，否则创建文件夹
     *
     * @param folderPath
     * @return
     */
    public static boolean isFolderExists(String folderPath) {
        if (TextUtils.isEmpty(folderPath)) {
            throw new NullPointerException(
                    "Parameter 'folderPath' is null or empty");
        }
        if (isSdCardExist()) {
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
     * 获取文件完整路径
     *
     * @param file 文件对象
     * @return
     * @throws IOException
     */
    public static String getFilePath(File file) throws Exception {
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }
        return file.getAbsolutePath();
    }

    /**
     * 通过文件名获取文件路径
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName) {
        if (TextUtils.isEmpty(PATH_ROOT)) {
            throw new IllegalArgumentException(
                    "Root path is empty.Please check whether the insert SD card and invoke getPathRoot(customPath) again");
        }
        return UrlUtil.buildURL(fileName, PATH_ROOT);
    }

    /**
     * 获取文件名
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }
        return file.getName();
    }

    /**
     * 通过文件路径获取文件名称
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        filePath = filePath.trim();
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 删除文件，如果是文件夹，就把文件下面所有文件删除
     *
     * @param strFilePath
     * @return
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
     * 复制文件，源文件复制到目标文件
     *
     * @param srcFilePath
     * @param destFilePath
     * @throws IOException
     */
    public static void copyFile(String srcFilePath, String destFilePath)
            throws IOException {
        File srcFile = new File(srcFilePath);
        File destFile = new File(destFilePath);
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * 将文件拷贝到指定文件夹下面
     *
     * @param srcFile
     * @param destDir
     * @throws IOException
     */
    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        FileUtils.copyFileToDirectory(srcFile, destDir);
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
     * 获取根目录
     * @return
     */
    public static String getPathRoot(){
        return PATH_ROOT;
    }

    /**
     * 获取照片文件路径
     * @return
     */
    public static String getPathPhoto() {
        if (PHOTO_PATH == null) {
            PHOTO_PATH = getFilePath(PathConstant.DEFAULT_SELECT_PHOTO_PATH);
        }
        return PHOTO_PATH;
    }

    /**
     * 获取照片视频路径
     * @return
     */
    public static String getPathVideo() {
        if (VIDEO_PATH == null) {
            VIDEO_PATH = getFilePath(PathConstant.DEFAULT_VIDEO_PATH);
        }
        return VIDEO_PATH;
    }

    /**
     * 获取音频文件路径
     * @return
     */
    public static String getPathRecord() {
        if (RECORD_PATH == null) {
            RECORD_PATH = getFilePath(PathConstant.DEFAULT_RECORD_PATH);
        }
        return RECORD_PATH;
    }

    /**
     * 获取安装文件路径
     * @return
     */
    public static String getPathInstall() {
        if (INSTALL_PATH == null) {
            INSTALL_PATH = getFilePath(PathConstant.DEFAULT_INSTALL_PATH);
        }
        return INSTALL_PATH;
    }

    /**
     * 通过文件名获取相片文件对象
     * @param fileName
     * @return
     */
    public static File getPhotoFile(String fileName) {
        String photoFolderPath = getPathPhoto();
        String photoFullPath = photoFolderPath + File.separator + fileName;
        return new File(photoFullPath);
    }

    /**
     * 通过文件名获取视频文件对象
     * @param fileName
     * @return
     */
    public static File getVideoFile(String fileName) {
        String videoFolderPath = getPathPhoto();
        String videoFullPath = videoFolderPath + File.separator + fileName;
        return new File(videoFullPath);
    }

    /**
     * 通过文件名获取音频文件对象
     * @param fileName
     * @return
     */
    public static File geRecordFile(String fileName) {
        String recordFolderPath = getPathRecord();
        String recordFullPath = recordFolderPath + File.separator + fileName;
        return new File(recordFullPath);
    }

    /**
     * 通过文件名获取安装文件对象
     * @param fileName
     * @return
     */
    public static File geInstallFile(String fileName) {
        String installFolderPath = getPathInstall();
        String installFullPath = installFolderPath + File.separator + fileName;
        return new File(installFullPath);
    }


    /**
     * 将ByteString转成base64Url
     *
     * @param byteString
     * @return
     */
    public static String base64Url(ByteString byteString) {
        return byteString.base64Url();
    }

    /**
     * 将ByteString转成base64
     *
     * @param byteString
     * @return
     */
    public static String base64(ByteString byteString) {
        return byteString.base64();
    }

    /**
     * 将ByteString转成utf8
     *
     * @param byteString
     * @return
     */
    public static String utf8(ByteString byteString) {
        return byteString.utf8();
    }

    /**
     * 将ByteString转成sha1
     *
     * @param byteString
     * @return
     */
    public static ByteString sha1(ByteString byteString) {
        return byteString.sha1();
    }

    /**
     * 将ByteString转成sha256
     *
     * @param byteString
     * @return
     */
    public static ByteString sha256(ByteString byteString) {
        return byteString.sha256();
    }

    /**
     * 将base64字符串转成ByteString
     *
     * @param base64
     * @return
     */
    public static ByteString decodeBase64(String base64) {
        return ByteString.decodeBase64(base64);
    }

    /**
     * 将Hex字符串转成ByteString
     *
     * @param hex
     * @return
     */
    public static ByteString decodeHex(String hex) {
        return ByteString.decodeHex(hex);
    }

    /**
     * 将Utf8编码的字符串转成ByteString
     *
     * @param s
     * @return
     */
    public static ByteString encodeUtf8(String s) {
        return ByteString.encodeUtf8(s);
    }


}
