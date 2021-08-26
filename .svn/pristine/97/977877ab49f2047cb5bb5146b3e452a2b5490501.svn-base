package com.augurit.am.fw.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ac on 2016-07-26.
 */
public final class StreamUtil {
    private StreamUtil() {
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }


    public static String read(InputStream inputStream, String encode) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream, encode), 1024 * 4);
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader
                    .readLine()) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void write(DataOutputStream dataOutputStream, String string) {
        FileInputStream fileInputStream = null;
        try {
            dataOutputStream.writeUTF(string);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
