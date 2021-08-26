package com.augurit.am.fw.net.soap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;


import com.augurit.am.fw.net.util.SharedPreferencesUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Soap请求类
 * Created by GuoKunhu on 2016-07-09.
 */
public class AMSoapRequest {

    private Context context;

    //WebService调用完整URL
    private String url;

    //web服务的命名空间（从相关WSDL文档中可以查看命名空间）
    private String  nameSpace;

    //只支持Bundle或者Map
    private Object paramObject;

    //返回类型
    private Object classOrTypeObject;

    //网络请求结果回调接口
    private AMSoapCallback callback;

    //编码格式
    public  String ENCODE = "utf-8";

    //连接超时时间
    protected  int TIME_OUT = 5 * 1000;
    public  String PRE_SET_COOKIE = "PRE_SET_COOKIE";
    public String DEFAULT_SET_COOKIE = "";
    public String methodName;
    protected SharedPreferencesUtil mSharedPreferencesUtil;

    private static final String WSDL ="?wsdl";

    private static final String COOKIE = "cookie";

    private static final String HEADER_COOKIE_KEY ="set-cookie";







    /**
     * 构造函数
     * @param context
     * @param url  WebService调用完整URL
     * @param paramObject 参数集
     * @param nameSpace web服务的命名空间
     * @param classOrTypeObject 请求数据的类型
     * @param methodName 请求服务的方法名称
     */
    public AMSoapRequest(Context context,String url, Object paramObject, String nameSpace,Object classOrTypeObject,String methodName ) {
        this.classOrTypeObject = classOrTypeObject;
        this.nameSpace = nameSpace;
        this.paramObject = paramObject;
        this.url = url;
        this.context = context;
        this.methodName = methodName;
        this.initSharedPreferencesUtil(context);
    }

    /**
     * soap网络请求执行函数
     * @param callback 请求结果回调接口
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public void execute(AMSoapCallback callback) throws IOException {

        if(url==null || nameSpace==null  ){
            throw new RuntimeException("soap request params should not be empty");
        }

        /* 一、基本参数设置*/
    //    String[] urlArray = url.split("\\/");
        // 调用的方法名称
      //  String methodName = urlArray[urlArray.length - 1];
        //指明WSDL文档URL
     //   String endPoint = url.endsWith("WSDL") ? url : url + "WSDL"; //?????? WSDL
        String endPoint = url;

        LogUtil.i("Begin a soap request--->>>>>>>>>>");

        /*二、指定命名空间与调用方法名*/

        // 实例化SoapObject 对象，指定webService的命名空间，以及调用方法名称
        SoapObject soapObject = new SoapObject(nameSpace, methodName);

        /*三、传递参数设置*/
        if (paramObject != null) {
            soapObject = encodeSoapParam(soapObject, paramObject);
        }

        /*四、生成调用WebService方法的SOAP请求信息,并指定SOAP的版本*/

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;


        /*五、（可选）设置是否调用的是dotNet开发的WebService*/

        //        envelope.dotNet = true;
        // 等价于envelope.bodyOut = soapObject;
        //        envelope.setOutputSoapObject(soapObject);

        /*六、创建Http对象，调用WebService方法*/
        HttpTransportSE transport = new HttpTransportSE(endPoint, TIME_OUT);
        //        transport.debug = true;
        try {
            String sessionStr = this.getCookieFromSharePreference(context);
            List headerList = new ArrayList();
            if (!TextUtils.isEmpty(sessionStr)) {
                HeaderProperty headerPropertyObj = new HeaderProperty("cookie", sessionStr);//????? COOKIE
                headerList.add(headerPropertyObj);
            }
            //调用WebService方法
            headerList = transport.call(nameSpace + methodName, envelope, headerList);
            this.setCookieFromHeader(context, headerList);
        } catch (XmlPullParserException e) {
            LogUtil.i("End a request with exception below---XXXXXXXXXX");
            e.printStackTrace();
        }

          /*七、解析返回结果*/

        if (envelope != null) {  // 获取返回的数据
            if (envelope.bodyIn instanceof SoapFault) {
                String str = ((SoapFault) envelope.bodyIn).faultstring;
                LogUtil.i(this + "End a request with exception below---XXXXXXXXXX");
                LogUtil.i(this +"SoapFault:"+ str);
                if(this.callback != null){
                    callback.onFail(envelope);
                }

            } else {
                if (envelope.getResponse() != null && !TextUtils.isEmpty(envelope.getResponse().toString())) {
                    LogUtil.i(this+ "End a request successfully---VVVVVVVVVV");
                    LogUtil.i(this+"response:"+envelope.getResponse().toString());
                    if(callback != null){
                        callback.onSuccess(envelope);
                    }
                    /*
                    if (String.class.equals(classOrTypeObject)) {
                        return (T) envelope.getResponse().toString();
                    }
                    return getObject(envelope.getResponse().toString(), (Type) classOrTypeObject);
                    */
                }
            }
        }
    }

    /**
     * soap参数编码
     * @param soapObject
     * @param paramObject
     * @return
     * @throws UnsupportedEncodingException
     */
    private SoapObject encodeSoapParam(SoapObject soapObject, Object paramObject) throws UnsupportedEncodingException {
        if (paramObject != null) {
            if (paramObject instanceof Bundle) {
                Bundle bundle = (Bundle) paramObject;
                if (bundle.size() == 0) {
                    return soapObject;
                }
                for (String key : bundle.keySet()) {
                    soapObject.addProperty(key, URLEncoder.encode(bundle.getString(key), ENCODE));
                }

            } else if (paramObject instanceof Map) {
                HashMap<String, String> map = (HashMap<String, String>) paramObject;
                if (map.size() == 0) {
                    return soapObject;
                }
                for (String key : map.keySet()) {
                    soapObject.addProperty(key, URLEncoder.encode(map.get(key).toString(), ENCODE));
                }
            } else {
                throw new IllegalArgumentException("paramObject only can be Bundle or Map");
            }
        }
        return soapObject;
    }


    //初始化SharedPreferencesUtil
    protected void initSharedPreferencesUtil(Context context) {
        this.mSharedPreferencesUtil = new SharedPreferencesUtil(context);
    }

    //从SharedPreferences获取保存的Cookie
    protected String getCookieFromSharePreference(Context context) {
        if (mSharedPreferencesUtil == null) {
            this.initSharedPreferencesUtil(context);
        }
        return mSharedPreferencesUtil.getString(PRE_SET_COOKIE, DEFAULT_SET_COOKIE);
    }

    //将Cookie保存到SharedPreferences
    protected void setCookieToSharePreference(Context context, String sessionStr) {
        if (mSharedPreferencesUtil == null) {
            this.initSharedPreferencesUtil(context);
        }
        mSharedPreferencesUtil.setString(PRE_SET_COOKIE, sessionStr);
        mSharedPreferencesUtil.refresh();
    }

    //从响应头获取Cookie
    protected void setCookieFromHeader(Context context, List headerList) {
        String sessionStr = this.getCookieFromSharePreference(context);
        if (TextUtils.isEmpty(sessionStr)) {
            for (Object header : headerList) {
                HeaderProperty headerProperty = (HeaderProperty) header;
                if (headerProperty == null) {
                    continue;
                }
                String headerKey = headerProperty.getKey();
                if (TextUtils.isEmpty(headerKey)) {
                    continue;
                }
                if ("header_cookie_key".equals(headerKey.toLowerCase())) { //??????HEADER_COOKIE_KEY
                    this.setCookieToSharePreference(context, headerProperty.getValue());
                    break;
                }
            }
        }
    }
}
