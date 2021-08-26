package com.augurit.agmobile.mapengine.layerdownload.dao;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.layerdownload.model.Tile;
import com.augurit.am.fw.net.AMNetwork;
import com.augurit.am.fw.net.api.CommonApi;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.tasks.tilecache.ExportTileCacheTask;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 描述：瓦片图层下载
 *
 * @author 创建人 ：xiejiexin
 * @version 1.1
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.dao
 * @createTime 创建时间 ：2017-02-14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-27
 * @modifyMemo 修改备注：下载图片从直接用okhttp改为用retrofit，以避免打开socket过多面导致的下载终止的bug
 */
public class TiledLayerDownloadDao {

    private AMNetwork amNetwork;
    private CommonApi commonApi;

    public TiledLayerDownloadDao() {
        this.amNetwork = new AMNetwork("http://www.augurit.com/");
        this.amNetwork.build();
        commonApi = (CommonApi) amNetwork.getServiceApi(CommonApi.class);
    }

    public MapServiceInfo fetchMapServiceInfo(String serviceUrl) throws Exception {
        ExportTileCacheTask task = new ExportTileCacheTask(serviceUrl, null);
        return task.fetchMapServiceInfo();
    }

    public int downloadTile(String serviceUrl,
                            Tile tile,
                            ITileCacheHelper cacheHelper) throws IOException {
        // 构造存储路径
        //        String cachePath = pathHelper.buildOffLineCachePath(tile.getLevel(), tile.getCol(), tile.getRow());
        // 读取缓存文件
        byte[] bytes = cacheHelper.getOfflineCacheFile(tile.getLevel(), tile.getCol(), tile.getRow());
        if (bytes == null) {  // 本地无缓存则进行下载
            String tileUrl = serviceUrl + "/tile/" + tile.getLevel() + "/" + tile.getRow() + "/" + tile.getCol();
            /*OkHttpClient client = new OkHttpClient.Builder().build();
            Request request = new Request.Builder().url(tileUrl).build();
            Response response = client.newCall(request).execute();
            if(response!=null
                    && response.body() != null
                    && response.isSuccessful()){
                bytes = response.body().bytes();
            }*/
            Call<ResponseBody> call = commonApi.get(tileUrl);
            try {
                retrofit2.Response<ResponseBody> response = call.execute();
                bytes = response.body().bytes();
            } catch (Exception e) {
                bytes = null;
            }
            if (bytes != null) {
                cacheHelper.addOfflineCacheFile(bytes, tile.getLevel(), tile.getCol(), tile.getRow());
                //TODO
                bytes = new byte[0];
                bytes = null;
                return 1;
            }
            return 0;
        }
        return 1;
    }
}
