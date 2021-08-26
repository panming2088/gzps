package com.augurit.am.fw.log.util;

import android.content.Context;

import com.augurit.am.fw.utils.log.LogUtil;

import java.io.IOException;
import java.util.Scanner;

public final class CrashCodeUtil {
    private CrashCodeUtil() {
    }

    public static String getCrashCode(Context context, String fileName, String className, String methodName, String exceptionType) {
        try {
            Scanner scanner = new Scanner(context.getAssets().open("crashcode.txt"));
            scanner.useDelimiter(System.getProperty("line.separator"));
            //line.separator:获取系统换行符.
            LogUtil.i("代码 文件名 类名 方法名 异常类型");
            while (scanner.hasNext()) {
                parseLine(scanner.next());
            }
            scanner.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private static void parseLine(String line) {
        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter("\\s*,\\s*");  //可以修改useDelimiter参数以读取不同分隔符分隔的内容
        String crashcode = lineScanner.next();
        String fileName = lineScanner.next();
        String className = lineScanner.next();
        String methodName = lineScanner.next();
        String exceptionType = lineScanner.next();
        LogUtil.i(crashcode + " " + fileName + " " + className + " " + methodName + " " + exceptionType);
    }


}
