package com.augurit.am.fw.utils;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

/**
 * 文件io操作类
 * Created by lb on 2016-07-28.
 */
public final class AMFileUtil {
    private AMFileUtil() {
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @param fileSizeType 文件大小类型（B, KB, MB, GB）
     * @return
     */
    public static double getFileSize(String filePath, FileSizeType fileSizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            blockSize = getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, fileSizeType);
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @param fileSizeType 文件大小类型（B, KB, MB, GB）
     * @return
     */
    public static double getFileSize(File file, FileSizeType fileSizeType) {
        long blockSize = 0;
        try {
            blockSize = getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, fileSizeType);
    }

    /**
     * 获取文件大小，并用String来表示
     *
     * @param filePath
     * @param fileSizeType
     * @return
     */
    public static String getFileSizeString(String filePath,
                                           FileSizeType fileSizeType) {
        return getFileSize(filePath, fileSizeType)
                + getFileSizeString(fileSizeType);
    }

    /**
     * 通过文件路径获取文件大小
     *
     * @param filePath
     * @return
     */
    public static String getAutoFileSizeString(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            blockSize = getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize);
    }

    /**
     * 通过文件获取文件大小
     *
     * @param file 文件对象
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.isDirectory()) {
            File flist[] = file.listFiles();
            for (int i = 0; i < flist.length; i++) {
                //                size +=FileUtils.sizeOf(file);
                size += getFileSize(file);
            }
        } else {
            if (file.exists()) {
                size += FileUtils.sizeOf(file);
            }
        }
        return size;
    }

    /**
     * 格式化文件大小
     *
     * @param fileSize
     * @return
     */
    private static String formatFileSize(long fileSize) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = decimalFormat.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = decimalFormat.format((double) fileSize / 1024)
                    + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = decimalFormat.format((double) fileSize / 1048576)
                    + "MB";
        } else {
            fileSizeString = decimalFormat
                    .format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    // private static long getDirFileSize(File file) throws Exception {
    // long size = 0;
    // File flist[] = file.listFiles();
    // for (int i = 0; i < flist.length; i++) {
    // if (flist[i].isDirectory()) {
    // size = size + getDirFileSize(flist[i]);
    // } else {
    // size = size + getFileSize(flist[i]);
    // }
    // }
    // return size;
    // }

    /**
     * 文件大小类型
     *
     * @param fileSizeType
     * @return
     */
    private static String getFileSizeString(FileSizeType fileSizeType) {
        switch (fileSizeType) {
            case B:
                return "B";
            case KB:
                return "KB";
            case MB:
                return "MB";
            case GB:
                return "GB";
            default:
                return "";
        }
    }

    /**
     * 将文件大小转成（B, KB, MB, GB）类型
     *
     * @param fileSize
     * @param fileSizeType
     * @return
     */
    private static double formatFileSize(long fileSize,
                                         FileSizeType fileSizeType) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (fileSizeType) {
            case B:
                fileSizeLong = Double.valueOf(decimalFormat
                        .format((double) fileSize));
                break;
            case KB:
                fileSizeLong = Double.valueOf(decimalFormat
                        .format((double) fileSize / 1024));
                break;
            case MB:
                fileSizeLong = Double.valueOf(decimalFormat
                        .format((double) fileSize / 1048576));
                break;
            case GB:
                fileSizeLong = Double.valueOf(decimalFormat
                        .format((double) fileSize / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 读取Asset文件内容
     *
     * @param context
     * @param fileName
     * @param charset  编码类型
     * @return
     * @throws IOException
     */
    public static String readStringFromAsset(Context context, String fileName, Charset charset)
            throws IOException {
        BufferedSource bufferedSource = null;
        InputStream in = null;
        try {
            in = context.getResources().getAssets().open(fileName);
            bufferedSource = Okio.buffer(Okio.source(in));
            return bufferedSource.readString(charset);
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 读取raw文件夹下面的文件
     *
     * @param context
     * @param rawId
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readStringFromRaw(Context context, int rawId, Charset charset)
            throws IOException {
        BufferedSource bufferedSource = null;
        InputStream in = null;
        try {
            in = context.getResources().openRawResource(rawId);
            bufferedSource = Okio.buffer(Okio.source(in));
            return bufferedSource.readString(charset);
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 读取文件字节数组
     *
     * @param file 文件对象
     * @return
     * @throws Exception
     */
    public static byte[] readFileToBytes(File file) throws Exception {
        Buffer chunk = null;
        BufferedSource bufferedSource = null;
        try {
            Long length = AMFileUtil.getFileSize(file);
            bufferedSource = Okio.buffer(Okio.source(file));
            chunk = new Buffer();
            bufferedSource.readFully(chunk, length);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());

        } finally {
            if (chunk != null) {
                chunk.close();
            }
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return chunk.readByteArray();
    }

    /**
     * 通过文件路径获取字节数组
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static byte[] readFileToBytes(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("文件不存在或者文件路径指向的是文件夹");
        }
        return readFileToBytes(file);
    }

    /**
     * 读取字符串（utf-8）
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readUtf8(File file) throws IOException {
        String strUtf8 = null;
        BufferedSource bufferedSource = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(file));
            strUtf8 = bufferedSource.readUtf8();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return strUtf8;
    }

    /**
     * 以charset编码的形式读取一个String
     *
     * @param file
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readStringToFile(File file, Charset charset) throws IOException {
        String strUtf8 = null;
        BufferedSource bufferedSource = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(file));
            strUtf8 = bufferedSource.readString(charset);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return strUtf8;
    }

    /**
     * 获取文件的InputStream
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static InputStream readFileToInputStream(File file) throws IOException {
        InputStream inputStream = null;
        BufferedSource bufferedSource = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(file));
            inputStream = bufferedSource.inputStream();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return inputStream;
    }

    /**
     * 通过文件路径获取InputStream
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static InputStream readFileToInputStream(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("文件不存在或者文件路径指向的是文件夹");
        }
        return readFileToInputStream(file);
    }

    /**
     * 通过文件路径获取ByteString
     *
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static ByteString readFileToByteString(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("文件不存在或者文件路径指向的是文件夹");
        }
        return readByteString(file);
    }

    /**
     * 读取字符串
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static ByteString readByteString(File file) throws IOException {
        ByteString byteString = null;
        BufferedSource bufferedSource = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(file));
            byteString = bufferedSource.readByteString();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return byteString;
    }

    /**
     * 读取字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static ByteString readByteString(InputStream inputStream) throws IOException {
        ByteString byteString = null;
        BufferedSource bufferedSource = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(inputStream));
            byteString = bufferedSource.readByteString();
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSource != null) {
                bufferedSource.close();
            }
        }
        return byteString;
    }

    /**
     * 将字节数组保存成本地文件
     *
     * @param sources
     * @param targetFile
     * @throws IOException
     */
    public static void saveBytesToFile(byte[] sources, File targetFile) throws IOException {
        BufferedSink bufferedSink = null;
        try {
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            bufferedSink = Okio.buffer(Okio.sink(targetFile));
            bufferedSink.write(sources);
            bufferedSink.flush();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close();
            }
        }
    }

    /**
     * 将InputStream写入到文件
     *
     * @param inputStream
     * @param targetFile
     * @throws IOException
     */
    public static void saveInputStreamToFile(InputStream inputStream, File targetFile) throws IOException {
        BufferedSink bufferedSink = null;
        BufferedSource bufferedSource = null;
        try {
            //判断文件是否存在
            AMFileOpUtil.isFileExists(targetFile);
            ByteString byteString = readByteString(inputStream);
            bufferedSink = Okio.buffer(Okio.sink(targetFile));
            bufferedSink.write(byteString);
            bufferedSink.flush();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close();
            }
        }
    }

    /**
     * 将String以charset编码形式写入文件
     *
     * @param string
     * @param charset    编码格式
     * @param targetFile
     * @throws IOException
     */
    public static void saveStringToFile(String string, Charset charset, File targetFile) throws IOException {
        BufferedSink bufferedSink = null;
        try {
            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs(); // 若文件夹也不存在则需创建文件夹，否则下一步无法创建文件
                targetFile.createNewFile();
            }
            bufferedSink = Okio.buffer(Okio.sink(targetFile));
            bufferedSink.writeString(string, charset);
            bufferedSink.flush();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close();
            }
        }
    }

    /**
     * 将ByteString保存成文件
     *
     * @param byteString
     * @param targetFile
     * @throws IOException
     */
    public static void saveByteStringToFile(ByteString byteString, File targetFile) throws IOException {
        BufferedSink bufferedSink = null;
        try {
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            bufferedSink = Okio.buffer(Okio.sink(targetFile));
            bufferedSink.write(byteString);
            bufferedSink.flush();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close();
            }
        }
    }

    public static boolean deleteProjectFiles() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AGMobile/projects";
            return deleteFile(new File(path));
        }
        return false;
    }

    public static boolean deleteLayerFiles() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AGMobile/layers";
            return deleteFile(new File(path));
        }
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            for (File childFile : childFiles) {
                deleteFile(childFile);
            }
            return file.delete();
        }
        return false;
    }

    public enum FileSizeType {
        B, KB, MB, GB
    }

}
