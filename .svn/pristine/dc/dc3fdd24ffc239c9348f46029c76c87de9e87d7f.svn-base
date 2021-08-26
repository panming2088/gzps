package com.augurit.agmobile.patrolcore.baiduapi;

import android.content.Context;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectlocation.model.BaiduGeocodeResult;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 百度地图API
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.baiduapi
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class BaiduApiService {

    private Context mContext;

    private static final String BAIDU_BASE_URL = "http://api.map.baidu.com/geocoder/v2/";

    //private static final String AK = "fwlhLVLWA9hyPRbqPaiGudGwqzrPF5k1";

    //private static final String MCODE ="E0:E8:DC:FC:7F:93:A1:B1:C5:34:6F:74:9E:D6:54:65:70:AA:0F:A3;com.augurit.agmobile.gzps";

    //private static final String AK1 = "HHeKkfwGdlLoICEg5j4LEioCGkXlO7Ma";
   // private static final String AK2 = "enqKzqGFFBwlnrbRjzW8lX6Ksg0v6Nhq";
    /**
     * 企业账号申请的Key
     */
    private static final String AK3 = "Pery3TI7E0M0gHz25LGG1VMTy7W47g4C";

    private String[] aks = {AK3};
    private static final String MCODE1 = "3A:F4:A4:3E:81:9F:17:A7:4C:69:F4:44:8C:A5:46:06:B7:13:C5:86;com.augurit.agmobile.agpatrol";
    private static final String MCODE2 = "E0:E8:DC:FC:7F:93:A1:B1:C5:34:6F:74:9E:D6:54:65:70:AA:0F:A3;com.augurit.agmobile.gzps";
    private AMNetwork mAMNetwork;
    private BaiduApi baiduApiService;
    private String mode;

    public BaiduApiService(Context context) {
        this.mContext = context;
    }

    public Observable<BaiduGeocodeResult> parseLocation(LatLng latLng) {
        initAMNetwork();
        String ak = AK3;
        mode = MCODE2;
// String ak = aks[new Random().nextInt(aks.length)];
//        if (ak.equals(AK1)) {
//            mode = MCODE1;
//        } else {
//            mode = MCODE2;
//        }
        return baiduApiService.requestAddress(latLng.getLatitude() + "," + latLng.getLongitude(),
                ak, mode)
                .map(new Func1<ResponseBody, BaiduGeocodeResult>() {
                    @Override
                    public BaiduGeocodeResult call(ResponseBody responseBody) {
                        String result = null;
                        try {
                            result = responseBody.string();
                            String finalResultStr = result.replace("renderReverse&&renderReverse(", "");
                            String substring = finalResultStr.substring(0, finalResultStr.length() - 1);
                            //LogUtil.d("百度接口返回的数据是：" + substring);
                            return JsonUtil.getObject(substring, BaiduGeocodeResult.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .onErrorReturn(new Func1<Throwable, BaiduGeocodeResult>() {
                    @Override
                    public BaiduGeocodeResult call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    //    public BaiduGeocodeResult parseLocation(LatLng latLng) throws IOException {
    //
    //        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&coordtype=wgs84ll&location="+
    //                latLng.getLatitude()+","+
    //                latLng.getLongitude()+"&output=json&pois=1&ak=HHeKkfwGdlLoICEg5j4LEioCGkXlO7Ma&" +
    //                "mcode=3A:F4:A4:3E:81:9F:17:A7:4C:69:F4:44:8C:A5:46:06:B7:13:C5:86;com.augurit.agmobile.agpatrol";
    //
    //        OkHttpClient client = new OkHttpClient();
    //        Request request = new Request.Builder().
    //                url(url).
    //                build();
    //        okhttp3.Response response = client.newCall(request).execute();
    //        ResponseBody body = response.body();
    //        if (body != null){
    //            String result = body.string();
    //            String finalResultStr = result.replace("renderReverse&&renderReverse(", "");
    //            String substring = finalResultStr.substring(0, finalResultStr.length() - 1);
    //            LogUtil.d("百度接口返回的数据是："+ substring);
    //            return JsonUtil.getObject(substring, BaiduGeocodeResult.class);
    //        }
    //        return null;
    //    }

    private void initAMNetwork() {

        if (mAMNetwork == null) {

            mAMNetwork = new AMNetwork(BAIDU_BASE_URL);
            mAMNetwork.setConnectTime(10000);
            mAMNetwork.addRxjavaConverterFactory();
            mAMNetwork.addLogPrint();
            mAMNetwork.build();
            mAMNetwork.registerApi(BaiduApi.class);
            baiduApiService = (BaiduApi) mAMNetwork.getServiceApi(BaiduApi.class);
        }
    }
}
