package com.augurit.am.fw.scy;

import android.util.Base64;

import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据加、解密工具类
 */
public final class CryptoUtil {
    public static final String entity = "agmobile";

    private CryptoUtil() {
    }

    /*public static byte[] encrypt( byte[] source, Context context) {
        try {
            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);

            // Check for whether the crypto functionality is available
            // This might fail if android does not load libaries correctly.
            if (!crypto.isAvailable()) {
                return null;
            }

            MyOutputStream os = new MyOutputStream();

            // Creates an output stream which encrypts the data as
            // it is written to it and writes it out to the file.
            OutputStream outputStream = crypto.getCipherOutputStream(
                    os,
                    Entity.create(CryptoUtil.entity));

            // Write plaintext to it.
            outputStream.write(source);
            outputStream.close();
            return os.getData();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(byte[] data, Context context){
        try {
            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            MyInputStream is = new MyInputStream(data);

            // Creates an input stream which decrypts the data as
            // it is read from it.
            InputStream inputStream = crypto.getCipherInputStream(
                    is,
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
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/

    /**
     * 字符串加密
     *
     * @param data 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encryptText(String data) {
        Crypto crypto = new Crypto(
                new CustomKeyChain(),
                new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);
        try {
            byte[] cipherText = crypto.encrypt(data.getBytes(), Entity.create(CryptoUtil.entity));

            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public static byte[] encryptText2(String data, Context context){
        Crypto crypto = new Crypto(
                new CustomKeyChain(),
                new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);
        try {
            byte[] cipherText = crypto.encrypt(data.getBytes(), Entity.create(CryptoUtil.entity));
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public static String decryptText(byte []data, Context context){
        Crypto crypto = new Crypto(
                new CustomKeyChain(),
                new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);
        try {
            byte[] cipherText = crypto.decrypt(data, Entity.create(CryptoUtil.entity));
            return new String(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public static byte[] decryptText2(byte []data, Context context){
        Crypto crypto = new Crypto(
                new CustomKeyChain(),
                new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);
        try {
            byte[] cipherText = crypto.decrypt(data, Entity.create(CryptoUtil.entity));
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * 字符串解密
     *
     * @param data 加密后的字符串
     * @return 解密后的字符串
     */
    public static String decryptText(String data) {
        Crypto crypto = new Crypto(
                new CustomKeyChain(),
                new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);
        try {
            byte[] cipherText = crypto.decrypt(Base64.decode(data, Base64.DEFAULT), Entity.create(CryptoUtil.entity));
            return new String(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件加密，把数据加密后存到文件中
     *
     * @param source   待加密的数据
     * @param filename 存放加密数据的文件路径
     */
    public static void encryptFile(byte[] source, String filename) throws Exception {

        //加密前需创建待加密文件
        File file = new File(filename);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
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

        OutputStream fileStream = new FileOutputStream(filename);

        // Creates an output stream which encrypts the data as
        // it is written to it and writes it out to the file.
        OutputStream outputStream = crypto.getCipherOutputStream(
                fileStream,
                Entity.create(CryptoUtil.entity));

        // Write plaintext to it.
        outputStream.write(source);
        outputStream.close();

    }

    /**
     * 文件解密
     *
     * @param filename 待解密的文件的路径
     * @return 解密后的文件内容
     */
    public static String decryptFileToString(String filename) {
        try {
            //解密前需判断待解密文件是否存在
            File file = new File(filename);
            if (!file.exists()) {
                return null;
            }

            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            FileInputStream fileStream = new FileInputStream(filename);

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

    public static File decryptFileToFile(File encryptFile){
        File decryptFile = new File(encryptFile.getAbsolutePath());

        try {
            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            FileInputStream fileStream = new FileInputStream(encryptFile.getAbsolutePath());

            // Creates an input stream which decrypts the data as
            // it is read from it.
            InputStream inputStream = crypto.getCipherInputStream(
                    fileStream,
                    Entity.create(CryptoUtil.entity));

            // Read into a byte array.
            int read;
            byte[] buffer = new byte[1024];

            ByteArrayOutputStream out = new ByteArrayOutputStream(inputStream.available());
            //          OutputStream out = new ByteArrayOutputStream();

            // You must read the entire stream to completion.
            // The verification is done at the end of the stream.
            // Thus not reading till the end of the stream will cause
            // a security bug.
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            inputStream.close();
            byte[] result = out.toByteArray();
            out.close();


            OutputStream output = new FileOutputStream(decryptFile);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(result);
            bufferedOutput.close();

            return decryptFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


      //  return decryptFile;
    }

    /**
     * 文件解密
     *
     * @param filename 待解密的文件的路径
     * @return 解密后的文件内容
     */
    public static byte[] decryptFileToBytes(String filename) {
        try {
            //解密前需判断待解密文件是否存在
            File file = new File(filename);
            if (!file.exists()) {
                return null;
            }


            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            FileInputStream fileStream = new FileInputStream(filename);

            // Creates an input stream which decrypts the data as
            // it is read from it.
            InputStream inputStream = crypto.getCipherInputStream(
                    fileStream,
                    Entity.create(CryptoUtil.entity));

            // Read into a byte array.
            int read;
            byte[] buffer = new byte[1024];

            ByteArrayOutputStream out = new ByteArrayOutputStream(inputStream.available());
            //          OutputStream out = new ByteArrayOutputStream();

            // You must read the entire stream to completion.
            // The verification is done at the end of the stream.
            // Thus not reading till the end of the stream will cause
            // a security bug.
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            inputStream.close();
            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //    /**
    //     * 文件解密
    //     *
    //     * @param filename 待解密的文件的路径
    //     * @return 解密后的文件内容
    //     */
    /*public static byte[] decryptFileToBytes2(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return null;
            }

            // Creates a new Crypto object with default implementations of
            // a key chain as well as native library.
            Crypto crypto = new Crypto(
                    new CustomKeyChain(),
                    new SystemNativeCryptoLibrary(), CryptoConfig.KEY_256);


            // Get the file to which ciphertext has been written.
            FileInputStream fileStream = new FileInputStream(filename);

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

    }*/


    private static class MyInputStream extends InputStream {

        private byte[] data;
        private int nextChar;

        public MyInputStream(byte[] data) {
            this.data = data;
            nextChar = 0;
        }

        @Override
        public int available() throws IOException {
            return super.available();
        }

        @Override
        public void mark(int readlimit) {
            super.mark(readlimit);
        }

        @Override
        public boolean markSupported() {
            return super.markSupported();
        }

        @Override
        public int read(byte[] buffer) throws IOException {
            return super.read(buffer);
        }

        @Override
        public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
            return super.read(buffer, byteOffset, byteCount);
        }

        @Override
        public synchronized void reset() throws IOException {
            super.reset();
        }

        @Override
        public long skip(long byteCount) throws IOException {
            return super.skip(byteCount);
        }

        @Override
        public int read() throws IOException {
            if (nextChar >= data.length) {
                return -1;
            }
            return data[nextChar++];
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


    private static class MyOutputStream extends OutputStream {

        private byte[] data;
        private int nextChar;

        public MyOutputStream() {
            super();
            data = new byte[0];
            nextChar = 0;
        }


        @Override
        public void write(byte[] buffer) throws IOException {
            super.write(buffer);
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);
        }

        @Override
        public void write(int oneByte) throws IOException {
            if (nextChar >= data.length) {
                int size = data.length + 1;
                byte[] temp = new byte[size];
                for (int i = 0; i < data.length; ++i) {
                    temp[i] = data[i];
                }
                data = temp;
            }

            data[nextChar++] = (byte) oneByte;
        }

        private void checkSize() {

        }

        public byte[] getData() {
            return data;
        }

    }

}
