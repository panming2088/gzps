package com.augurit.agmobile.mapengine.route.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.route.service
 * @createTime 创建时间 ：17/1/16
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/16
 */

public class RouteService implements IRouteService {

    private Context mContext;

    //路网服务地址
    private String mRouteTaskUrl;

    //网络路径分析任务
    RouteTask mRouteTask = null;

    //路径分析结果
    RouteResult mResults = null;

    private ProgressDialog progressDialog;

    private OnRouteListener mListener;

    private MapView mapView;

    private SpatialReference mapSR;

    public Handler mHandler;
    public static int ROUTE_RESULT = 0;
    public static int ROUTE_RESULT_FAIL = 1;

    public RouteService(Context context, String routeTaskUrl, MapView mapView) {
        this.mContext = context;
        this.mRouteTaskUrl = routeTaskUrl;
        this.mapView = mapView;

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("路径搜索中...");

        //初始化RouteTask
        try {
            mRouteTask = RouteTask.createOnlineRouteTask(mRouteTaskUrl, null);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                //回到主线程处理结果
                if (message.what == ROUTE_RESULT) {
                    mListener.onSuccess(mResults);
                    progressDialog.dismiss();
                } else if (message.what == ROUTE_RESULT_FAIL) {
                    mListener.onFail("查询失败");
                    progressDialog.dismiss();
                }

                return false;
            }
        });
    }

    //要分析的路径停靠点,注意点是有顺序的
    @Override
    public void startPointsRoute(final List<Point> points, OnRouteListener listener) {
        if (points.size() < 2) {
            Toast.makeText(mContext, "必须至少选择两个停靠点", Toast.LENGTH_LONG).show();
            return;
        }

        this.mListener = listener;
        progressDialog.show();

        //网络分析不能再主线程中进行
        new Thread() {
            public void run() {
                //准备参数
                RouteParameters rp = null;
                try {
                    rp = mRouteTask
                            .retrieveDefaultRouteTaskParameters();

                } catch (Exception e) {
                    e.getLocalizedMessage();
                }

                NAFeaturesAsFeature naferture = new NAFeaturesAsFeature();
                //设置查询停靠点，至少要两个
                for (Point p : points) {
                    StopGraphic sg = new StopGraphic(p);
                    naferture.addFeature(sg);
                }
                rp.setStops(naferture);
                //设置查询输入的坐标系跟底图一样
                mapSR = mapView.getSpatialReference();
                rp.setOutSpatialReference(mapSR);

                    /*
                    注:关于交通方式的选择,需要服务中增加相应的网络数据集中的属性来区别开来
                     */

                // rp.setImpedanceAttributeName("Time");
                //        RouteTask rt = new RoutingTask(
                //                "http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Network/USA/NAServer/Route",
                //                null);
                try {
                    //执行操作
                    RouteResult rr = mRouteTask.solve(rp);
                    mResults = rr;
                    mHandler.sendEmptyMessage(ROUTE_RESULT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    mHandler.sendEmptyMessage(ROUTE_RESULT_FAIL);
                    Toast.makeText(mContext, "查询不到路径!", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

            }
        }.start();
    }
}
