package com.augurit.agmobile.mapengine.common.utils;

import com.augurit.am.fw.utils.log.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 用于读取Bundle格式的瓦片
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：2017-03-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-20
 * @modifyMemo 修改备注：
 */
public class BundleUtil {

    /**
     * 生成Bundle文件名
     *
     * @param llevel 地图缩放级别
     * @param rrow   行
     * @param ccol   列
     * @return
     */
    public static String getBundleBase(String LayersDir, int llevel, int ccol, int rrow) {
        String level = Integer.toString(llevel);
        int levelLength = level.length();
        if (levelLength == 1) {
            level = "0" + level;
        }
        level = "L" + level;

        int rowGroup = 128 * (rrow / 128);
        String row = Integer.toHexString(rowGroup);
        int rowLength = row.length();
        if (rowLength < 4) {
            for (int i = 0; i < 4 - rowLength; i++) {
                row = "0" + row;
            }
        }
        row = "R" + row;

        int columnGroup = 128 * (ccol / 128);
        String column = Integer.toHexString(columnGroup);
        int columnLength = column.length();
        if (columnLength < 4) {
            for (int i = 0; i < 4 - columnLength; i++) {
                column = "0" + column;
            }
        }
        column = "C" + column;

        return LayersDir + "/_alllayers/" + level + "/" + row + column;
    }


    /**
     * 读取Bundle里的瓦片数据
     *
     * @param LayersDir 瓦片目录的Layers目录，如/sdcard/HD02_TDLYGH_MZJSXM/v101/Layers
     * @param mLevel    级别
     * @param mCol      列
     * @param mRow      行
     * @return
     */
    public static byte[] getTile(String LayersDir, int mLevel, int mCol, int mRow) {
        int size = 128;
        byte[] result = null;
        RandomAccessFile isBundlx = null;
        RandomAccessFile isBundle = null;
        try {
            String level = Integer.toString(mLevel);
            int levelLength = level.length();
            if (levelLength == 1) {
                level = "0" + level;
            }
            level = "L" + level;

            int rowGroup = 128 * (mRow / 128);
            String row = Integer.toHexString(rowGroup);
            int rowLength = row.length();
            if (rowLength < 4) {
                for (int i = 0; i < 4 - rowLength; i++) {
                    row = "0" + row;
                }
            }
            row = "R" + row;

            int columnGroup = 128 * (mCol / 128);
            String column = Integer.toHexString(columnGroup);
            int columnLength = column.length();
            if (columnLength < 4) {
                for (int i = 0; i < 4 - columnLength; i++) {
                    column = "0" + column;
                }
            }
            column = "C" + column;

            String bundleBase = LayersDir + "/_alllayers/" + level + "/" + row + column;
            LogUtil.e("bundle", bundleBase);
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";

            int index = size * (mCol - columnGroup) + (mRow - rowGroup);
            //行列号是整个范围内的，在某个文件中需要先减去前面文件所占有的行列号（都是128的整数）这样就得到在文件中的真是行列号
            isBundlx = new RandomAccessFile(bundlxFileName, "r");
            isBundlx.skipBytes(16 + 5 * index);
            byte[] buffer = new byte[5];
            isBundlx.read(buffer, 0, 5);
            long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                    * 256 + (long) (buffer[2] & 0xff) * 65536
                    + (long) (buffer[3] & 0xff) * 16777216
                    + (long) (buffer[4] & 0xff) * 4294967296L;

            isBundle = new RandomAccessFile(bundleFileName, "r");
            String offsetStr = String.valueOf(offset);
            int offsetInt = Integer.valueOf(offsetStr);
            isBundle.skipBytes(offsetInt);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes, 0, 4);
            int length = (lengthBytes[0] & 0xff)
                    + (lengthBytes[1] & 0xff) * 256
                    + (lengthBytes[2] & 0xff) * 65536
                    + (lengthBytes[3] & 0xff) * 16777216;
            result = new byte[length];
            isBundle.read(result, 0, length);
            /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int bytesRead = 0;
            if(length > 0){
                bytesRead = isBundle.read(result, 0, length);
                if(bytesRead > 0){
                    bos.write(result, 0, bytesRead);
                }
            }
            byte[] tiles = bos.toByteArray();
            result = tiles;*/
        } catch (Exception ex) {
            return null;
        } finally {
            if (isBundle != null) {
                try {
                    isBundle.close();
                    isBundlx.close();
                } catch (Exception e) {

                }
            }
        }
        return result;
    }


    public static byte[] getTile2(String LayersDir, int level, int col, int row) throws Exception {
        byte[] result = null;
        try {
            String bundlesDir = LayersDir + "/_alllayers";

            String l = "0" + level;
            int lLength = l.length();
            if (lLength > 2) {
                l = l.substring(lLength - 2);
            }
            l = "L" + l;

            int rGroup = 128 * (row / 128);
            String r = "000" + Integer.toHexString(rGroup);
            int rLength = r.length();
            if (rLength > 4) {
                r = r.substring(rLength - 4);
            }
            r = "R" + r;

            int cGroup = 128 * (col / 128);
            String c = "000" + Integer.toHexString(cGroup);
            int cLength = c.length();
            if (cLength > 4) {
                c = c.substring(cLength - 4);
            }
            c = "C" + c;

            String bundleBase = String
                    .format("%s/%s/%s%s", bundlesDir, l, r, c);
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";

            int index = 128 * (col - cGroup) + (row - rGroup);
            FileInputStream isBundlx = new FileInputStream(bundlxFileName);
            isBundlx.skip(16 + 5 * index);
            byte[] buffer = new byte[5];
            isBundlx.read(buffer);
            long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                    * 256 + (long) (buffer[2] & 0xff) * 65536
                    + (long) (buffer[3] & 0xff) * 16777216
                    + (long) (buffer[4] & 0xff) * 4294967296L;

            FileInputStream isBundle = new FileInputStream(bundleFileName);
            isBundle.skip(offset);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes);
            int length = (lengthBytes[0] & 0xff)
                    + (lengthBytes[1] & 0xff) * 256
                    + (lengthBytes[2] & 0xff) * 65536
                    + (lengthBytes[3] & 0xff) * 16777216;
            result = new byte[length];
            isBundle.read(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }


    @Deprecated
    public static byte[] getTile3(String LayersDir, int level, int col, int row) throws Exception {
        byte[] result = null;
        try {
            String bundlesDir = LayersDir + "/_alllayers";

            String l = "0" + level;
            int lLength = l.length();
            if (lLength > 2) {
                l = l.substring(lLength - 2);
            }
            l = "L" + l;

            int rGroup = 128 * (row / 128);
            String r = "000" + Integer.toHexString(rGroup);
            int rLength = r.length();
            if (rLength > 4) {
                r = r.substring(rLength - 4);
            }
            r = "R" + r;

            int cGroup = 128 * (col / 128);
            String c = "000" + Integer.toHexString(cGroup);
            int cLength = c.length();
            if (cLength > 4) {
                c = c.substring(cLength - 4);
            }
            c = "C" + c;

            String bundleBase = String
                    .format("%s/%s/%s%s", bundlesDir, l, r, c);
            String bundlxFileName = bundleBase + ".bundlx";
            String bundleFileName = bundleBase + ".bundle";


            FileInputStream isBundlx = new FileInputStream(bundlxFileName);
            FileInputStream isBundle = new FileInputStream(bundleFileName);
            byte[] bundlxBytes = getBytesByInputStream(isBundlx, 100*1024);
            byte[] bundleBytes = getBytesByInputStream(isBundle, 100*1024);
            result = getImageByteByBundle(level, row, col, bundleBytes, bundlxBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }


    /**
     * 在压缩格式图片中获取到某张图片的字节信息(从网上获取的截取方式)
     * @param level 级别
     * @param row 行
     * @param col 列
     * @return
     */
    public static byte[] getImageByteByBundle(int level,int row,int col,byte[] isBundle,byte[] isBundlx){
        int rGroup = 128 * (row / 128);
        int cGroup = 128 * (col / 128);
        int index = 16 + 5 * (128 * (col - cGroup) + (row - rGroup));

//		byte[] isBundlx = byteMap.get("BUNDLX");
//		byte[] isBundle = byteMap.get("BUNDLE");
        if(isBundlx == null || isBundle == null){
            return new byte[0];
        }

        byte[] buffer = new byte[5];

        try {
            System.arraycopy(isBundlx, index, buffer, 0, buffer.length);
        } catch (Exception e) {
            return new byte[0];
        }

        long offset = (long) (buffer[0] & 0xff) + (long) (buffer[1] & 0xff)
                * 256 + (long) (buffer[2] & 0xff) * 65536
                + (long) (buffer[3] & 0xff) * 16777216
                + (long) (buffer[4] & 0xff) * 4294967296L;

        byte[] lengthBytes = new byte[4];

        System.arraycopy(isBundle, (int)offset, lengthBytes, 0, lengthBytes.length);

        int length = (lengthBytes[0] & 0xff)
                + (lengthBytes[1] & 0xff) * 256
                + (lengthBytes[2] & 0xff) * 65536
                + (lengthBytes[3] & 0xff) * 16777216;

        byte[] newBytes = new byte[length];

        System.arraycopy(isBundle, (int)offset + 4, newBytes, 0, newBytes.length);
        return newBytes;
    }

    /**
     * 获取某输入流中的字节数组
     * @param is 输入流
     * @param readMaxNum 每次读取字节数
     * @return
     */
    public static byte[] getBytesByInputStream(InputStream is,Integer readMaxNum){
        int readNum = readMaxNum == null ? 1024 : readMaxNum;
        byte c[] = null;
        try {
            byte b[] = new byte[readNum];
            int bytesRead = is.read(b);
            while(bytesRead != -1){
                if(c == null){
                    c = new byte[bytesRead];
                    System.arraycopy(b, 0, c, 0, bytesRead);
                }else{
                    byte d[] = c;
                    c = new byte[d.length + bytesRead];
                    System.arraycopy(d, 0, c, 0, d.length);
                    System.arraycopy(b, 0, c, d.length, bytesRead);
                }
                bytesRead = is.read(b,0,b.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public static byte[] getTile10X3(String LayersDir, int mLevel, int mColumn, int mRow) {
        byte[] tiles = null;
        try {
            String bundlesDir = LayersDir + "/_alllayers";
            String level = Integer.toString(mLevel);
            int levelLength = level.length();
            if (levelLength == 1) {
                level = "0" + level;
            }
            level = "L" + level;

            int rowGroup = 128 * (mRow / 128);
            String row = Integer.toHexString(rowGroup);
            int rowLength = row.length();
            if (rowLength < 4) {
                for (int i = 0; i < 4 - rowLength; i++) {
                    row = "0" + row;
                }
            }
            row = "R" + row;

            int columnGroup = 128 * (mColumn / 128);
            String column = Integer.toHexString(columnGroup);
            int columnLength = column.length();
            if (columnLength < 4) {
                for (int i = 0; i < 4 - columnLength; i++) {
                    column = "0" + column;
                }
            }
            column = "C" + column;

            String bundleName = String.format("%s/%s/%s%s", bundlesDir, level, row, column) + ".bundle";

            int index = 128 * (mRow - rowGroup) + (mColumn - columnGroup);

            RandomAccessFile isBundle = new RandomAccessFile(bundleName, "r");
            isBundle.skipBytes(64 + 8 * index);

            //获取位置索引并计算切片位置偏移量
            byte[] indexBytes = new byte[4];
            isBundle.read(indexBytes, 0, 4);
            long offset = (long) (indexBytes[0] & 0xff) + (long) (indexBytes[1] & 0xff) * 256 + (long) (indexBytes[2] & 0xff) * 65536
                    + (long) (indexBytes[3] & 0xff) * 16777216;

            //获取切片长度索引并计算切片长度
            long startOffset = offset - 4;
            isBundle.seek(startOffset);
            byte[] lengthBytes = new byte[4];
            isBundle.read(lengthBytes, 0, 4);
            int length = (lengthBytes[0] & 0xff) + (lengthBytes[1] & 0xff) * 256 + (lengthBytes[2] & 0xff) * 65536
                    + (lengthBytes[3] & 0xff) * 16777216;

            //根据切片位置和切片长度获取切片
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] tileBytes = new byte[length];
            int bytesRead = 0;
            if (length > 0) {
                bytesRead = isBundle.read(tileBytes, 0, tileBytes.length);
                if (bytesRead > 0) {
                    bos.write(tileBytes, 0, bytesRead);
                }
            }
            tiles = bos.toByteArray();
        } catch (Exception e) {

        }
        return tiles;

    }


}
