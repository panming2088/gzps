package com.augurit.am.fw.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;

import com.augurit.am.fw.scy.CryptoUtil;
import com.augurit.am.fw.scy.CustomKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 外置SD卡访问工具类
 * Created by liangsh on 2017-03-07.
 */
public class ExternalSDCardFileUtil {

    /**
     * 外置SD卡根路径Uri，调用本工具类前必须选设置此变量
     * 此变量值获取方法
     Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
     startActivityForResult(intent, CHOOSE_SDCARD_CODE);

     然后在onActivityResult回调中进行以下判断
     Uri treeUri = intent.getData();
     if (!":".equals(treeUri.getPath().substring(treeUri.getPath().length() - 1)) || treeUri.getPath().contains("primary")) {
        ToastUtil.longToast(LoginActivity.this, "请选择正确的外置SD卡！");
     } else {
        saveSDCardUriString(treeUri.toString());  //可保存到本地，下次打开应用时直接生成Uri，而不需要用户再次选外置SD卡，ExternalSDCardFileUtil.rootUri = Uri.parse(uriString);
        ExternalSDCardFileUtil.rootUri = treeUri;
        ToastUtil.shortToast(LoginActivity.this, "外置SD卡设置成功！");
     }
     */
    public static Uri rootUri = null;




    /**
     * 判断路径代表的是否为文件夹。如路径不存在或不是文件夹，则返回false
     * @param context
     * @param path 相对于SD卡的路径，如 aa/bb/cc
     * @return
     */
    public static boolean isDirectory(Context context, String path){
        if(rootUri == null){
            return false;
        }
        DocumentFile rootDocument = DocumentFile.fromTreeUri(context, rootUri);
        DocumentFile document = rootDocument;
        if(path.endsWith("/")){
            path = path.substring(0, path.length()-1);
        }
        String[] dirNames = path.split("/");
        for(String dirName : dirNames){
            DocumentFile dirDocument = document.findFile(dirName);
            if(dirDocument == null){
                return false;
            }
            if(!dirDocument.isDirectory()){
                return false;
            }
            document = dirDocument;
        }
        return true;
    }

    /**
     * 判断路径代表的是否为文件夹。如路径不存在或不是文件夹，则返回false
     * @param context
     * @param filePath 相对于SD卡的路径，如 aa/bb/cc
     * @return
     */
    public static boolean isFile(Context context, String filePath){
        if(rootUri == null){
            return false;
        }
        if(filePath.endsWith("/")){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        DocumentFile rootDocument = DocumentFile.fromTreeUri(context, rootUri);
        DocumentFile document = rootDocument;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        String[] dirNames = dirPath.split("/");
        for(String dirName : dirNames){
            DocumentFile dirDocument = document.findFile(dirName);
            if(dirDocument == null){
                return false;
            }
            document = dirDocument;
        }
        DocumentFile file = document.findFile(fileName);
        return !(file == null || !file.isFile());
    }

    /**
     * 创建文件夹
     * @param context 上下文
     * @param path 相对于SD卡的路径，如 aa/bb/cc
     * @return
     */
    public static DocumentFile mkdirs(Context context, String path){
        if(rootUri == null){
            return null;
        }
        DocumentFile rootDocument = DocumentFile.fromTreeUri(context, rootUri);
        DocumentFile document = rootDocument;
        if(path.endsWith("/")){
            path = path.substring(0, path.length()-1);
        }
        String[] dirNames = path.split("/");
        for(String dirName : dirNames){
            DocumentFile dirDocument = document.findFile(dirName);
            if(dirDocument == null){
                dirDocument = document.createDirectory(dirName);
            }
            document = dirDocument;
        }
        return document;
    }

    /**
     * 创建文件
     * @param context
     * @param filePath 相对于SD卡的路径，如 aa/bb/cc/dd.txt
     * @return
     */
    public static DocumentFile createNewFile(Context context, String filePath){
        if(rootUri == null){
            return null;
        }
        if(filePath.endsWith("/")){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        DocumentFile dirDocument = mkdirs(context, dirPath);
        DocumentFile file = dirDocument.findFile(fileName);
        if(file != null){
            return file;
        }
        return dirDocument.createFile("application/octet-stream", fileName);
    }


    /**
     * 删除文件或文件夹
     * @param context
     * @param filePath 相对于SD卡的路径，如 aa/bb/cc/dd.txt
     * @return
     */
    public static boolean deleteFile(Context context, String filePath){
        if(rootUri == null){
            return false;
        }
        if(filePath.endsWith("/")){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        DocumentFile rootDocument = DocumentFile.fromTreeUri(context, rootUri);
        DocumentFile document = rootDocument;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        String[] dirNames = dirPath.split("/");
        for(String dirName : dirNames){
            DocumentFile dirDocument = document.findFile(dirName);
            if(dirDocument == null){
                return true;
            }
            document = dirDocument;
        }
        DocumentFile file = document.findFile(fileName);
        if(file == null){
            return true;
        }
        return file.delete();
    }

    /**
     * @param context
     * @param filePath 相对路径
     * @param bytes
     * @return
     */
    public static boolean writeBytes(Context context, String filePath, byte[] bytes){
        if(filePath.endsWith("/")){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        DocumentFile document = createNewFile(context, filePath);
        if(document == null){
            return false;
        }
        try {
            OutputStream outputStream = context.getContentResolver().openOutputStream(document.getUri());
            if (outputStream != null) {
                outputStream.write(bytes, 0, bytes.length);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param context
     * @param filePath 相对路径
     */
    public static byte[] readBytes(Context context, String filePath){
        if(rootUri == null){
            return null;
        }
        if(filePath.endsWith("/")){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        DocumentFile rootDocument = DocumentFile.fromTreeUri(context, rootUri);
        DocumentFile document = rootDocument;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        String[] dirNames = dirPath.split("/");
        for(String dirName : dirNames){
            DocumentFile dirDocument = document.findFile(dirName);
            if(dirDocument == null){
                return null;
            }
            document = dirDocument;
        }
        DocumentFile file = document.findFile(fileName);
        if(file == null){
            return null;
        }
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(file.getUri());
            int count = inputStream.available();
            byte[] b = new byte[count];
            inputStream.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件加密，把数据加密后存到文件中
     *
     * @param source   待加密的数据
     * @param filePath 存放加密数据的文件路径
     */
    public static void encryptFile(Context context, byte[] source, String filePath){
        if(rootUri == null){
            return;
        }
        try {
            //加密前需创建待加密文件
            DocumentFile file = createNewFile(context, filePath);
            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);

            // Check for whether the crypto functionality is available
            // This might fail if android does not load libaries correctly.
            if (!crypto.isAvailable()) {
                return;
            }

            OutputStream fileStream = context.getContentResolver().openOutputStream(file.getUri());

            // Creates an output stream which encrypts the data as
            // it is written to it and writes it out to the file.
            OutputStream outputStream = crypto.getCipherOutputStream(
                    fileStream,
                    Entity.create(CryptoUtil.entity));

            // Write plaintext to it.
            outputStream.write(source);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件解密
     *
     * @param filePath 待解密的文件的路径
     * @return 解密后的文件内容
     */
    public static String decryptFileToString(Context context, String filePath){
        try {
            //解密前需判断待解密文件是否存在
            if(!isFile(context, filePath)){
                return null;
            }
            DocumentFile file = createNewFile(context, filePath);

            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            InputStream fileStream = context.getContentResolver().openInputStream(file.getUri());

            // Creates an input stream which decrypts the data as
            // it is read from it.
            InputStream inputStream = crypto.getCipherInputStream(
                    fileStream,
                    Entity.create(CryptoUtil.entity));

            // Read into a byte array.
            int read;
            byte[] buffer = new byte[1024];

            OutputStream out = new ByteArrayOutputStream(inputStream.available());
            //          OutputStream out = new ByteArrayOutputStream();

            // You must read the entire stream to completion.
            // The verification is done at the end of the stream.
            // Thus not reading till the end of the stream will cause
            // a security bug.
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            inputStream.close();
            String result = out.toString();
            out.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件解密
     *
     * @param filePath 待解密的文件的路径
     * @return 解密后的文件内容
     */
    public static byte[] decryptFileToBytes(Context context,String filePath) {
        try {
            //解密前需判断待解密文件是否存在
            if(!isFile(context, filePath)){
                return null;
            }
            DocumentFile file = createNewFile(context, filePath);

            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            InputStream fileStream = context.getContentResolver().openInputStream(file.getUri());

            // Creates an input stream which decrypts the data as
            // it is read from it.
            InputStream inputStream = crypto.getCipherInputStream(
                    fileStream,
                    Entity.create(CryptoUtil.entity));

            // Read into a byte array.
            byte[] buffer = new byte[inputStream.available()];

            // You must read the entire stream to completion.
            // The verification is done at the end of the stream.
            // Thus not reading till the end of the stream will cause
            // a security bug.
            if ((inputStream.read(buffer)) == -1) {
                buffer = null;
            }
            inputStream.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
