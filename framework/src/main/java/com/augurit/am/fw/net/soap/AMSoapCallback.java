package com.augurit.am.fw.net.soap;

import org.ksoap2.serialization.SoapSerializationEnvelope;

/**
 * soap网络请求结果回调接口
 * Created by GuoKunhu on 2016-07-09.
 */
public interface AMSoapCallback {
    /**
     * 请求成功时回调处理
     * @param envelope
     */
    void onSuccess(SoapSerializationEnvelope envelope);

    /**
     * 请求失败时回调处理
     * @param envelope
     */
    void onFail(SoapSerializationEnvelope envelope);
}
