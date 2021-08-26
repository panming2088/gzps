package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.augurit.agmobile.mapengine.common.utils.BundleUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-04-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-04-14
 * @modifyMemo 修改备注：
 */
public class XORDecryptTileCacheReader extends TileCacheReader {

    public XORDecryptTileCacheReader(String LayersPath) throws Exception{
        super(LayersPath);
    }

    @Override
    InputStream getInputStream(InputStream inputStream){
        byte[] bytes = BundleUtil.getBytesByInputStream(inputStream, 100*1024);
        return new ByteArrayInputStream(encrypt(bytes));
    }

    @Override
    byte[] getByte(byte[] bytes){
        return encrypt(bytes);
    }

    /**
     * 异或加密解密都是这个方法
     * @param data
     * @return
     */
    public byte[] encrypt(byte[] data){
        if (data == null) {
            return null;
        }
        int len = data.length;
        int key = 0x17;
        for (int i = 0; i < len; i++) {
            data[i] ^= key;
        }
        return data;
    }

}
