package com.augurit.agmobile.mapengine.legend.dao;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.dao
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public class RemoteLegendDao {

    protected Context mContext;

    public RemoteLegendDao(Context context) {

        this.mContext = context;
    }

    /**
     * 从网络获取
     *
     * @return
     */
    public Observable<List<Legend>> getAllLegends() {

        AMNetwork amNetwork = new AMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(ILegendApi.class);

        final ILegendApi legendApi = (ILegendApi) amNetwork.getServiceApi(ILegendApi.class);

        String url = BaseInfoManager.getSupportUrl(mContext) + "resources/legends.json";

        LogUtil.d("请求图例的url是：" + url);

        return legendApi
                .getLegends(url)
                .observeOn(Schedulers.io());
    }

    public Observable<List<LayerLegend>> getAllLegendForLayer(final String baseUrl, final String layerName) {
        AMNetwork amNetwork = new AMNetwork(BaseInfoManager.getBaseServerUrl(mContext));
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(ILegendApi.class);
        final ILegendApi legendApi = (ILegendApi) amNetwork.getServiceApi(ILegendApi.class);

        String layerUrl = baseUrl + "/legend?f=pjson";
        return legendApi
                .getLegend(layerUrl)
                .map(new Func1<ResponseBody, List<LayerLegend>>() {
                    @Override
                    public List<LayerLegend> call(ResponseBody responseBody) {
                        List<LayerLegend> infos = new ArrayList<LayerLegend>();
                        String respone = null;
                        try {
                            respone = responseBody.string();
                            if (TextUtils.isEmpty(respone)) {
                                return new ArrayList<LayerLegend>();
                            }
                            JSONObject jsonArray = new JSONObject(respone);
                            JSONArray jsonObject = jsonArray.getJSONArray("layers");
                            infos = JsonUtil.getObject(jsonObject.toString(), new TypeToken<List<LayerLegend>>() {
                            }.getType());
                            for (LayerLegend layerLegend : infos) {
                                layerLegend.setLayerUrl(baseUrl);
                                layerLegend.setParentName(layerName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return null;
                        }
                        return infos;
                    }
                })
                .observeOn(Schedulers.io());

    }

}
