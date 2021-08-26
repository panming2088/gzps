package com.augurit.agmobile.gzps.statistic.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnInitLayerUrlFinishEvent;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentTypeConstant;
import com.augurit.agmobile.gzps.statistic.model.StatisticResult2;
import com.augurit.agmobile.gzps.statistic.service.StatisticService2;
import com.augurit.agmobile.gzps.common.widget.HorizontalBarEChart;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;

/**
 * Created by liangsh on 2017/11/6.
 */

public class ComponentStatsFragment extends Fragment {

    private AMSpinner mSpinnerArea1;
    private AMSpinner mSpinnerArea2;
    private HorizontalBarEChart mBarChart;
    private StatisticService2 mStatisticService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_componentstats, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mStatisticService = new StatisticService2();
        mSpinnerArea1 = (AMSpinner) view.findViewById(R.id.stats_spinner_area1);
        mSpinnerArea2 = (AMSpinner) view.findViewById(R.id.stats_spinner_area2);
        mBarChart = (HorizontalBarEChart) view.findViewById(R.id.stats_barchart);


        mSpinnerArea1.addItems("全市", "全市");
        mSpinnerArea1.setVisibility(View.GONE);
        /*for(String district : GzpsConstant.districts){
            mSpinnerArea1.addItems(district, district);
        }*/


        mSpinnerArea1.setOnItemClickListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                String dis = item.toString();
                Observable.zip(mStatisticService.statisticByDistrict(ComponentTypeConstant.NEW_ADDED_COMPONENT_VALUE),
                        mStatisticService.statisticByDistrict(ComponentTypeConstant.OLD_COMPONENT_VALUE2),
                        new Func2<Map<String,List<StatisticResult2>>, Map<String,List<StatisticResult2>>, List<Map<String,List<StatisticResult2>>>>() {
                            @Override
                            public List<Map<String,List<StatisticResult2>>> call(Map<String,List<StatisticResult2>> articleDetailBean,
                                                                                 Map<String,List<StatisticResult2>> commentBean) {
                                List<Map<String,List<StatisticResult2>>> list = new ArrayList<>();
                                list.add(articleDetailBean);
                                list.add(commentBean);
                                return list;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Map<String,List<StatisticResult2>>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onNext(List<Map<String,List<StatisticResult2>>> list) {
                                Map<String, List<StatisticResult2>> newComponentMap = list.get(0);
                                Map<String, List<StatisticResult2>> oldComponentMap = list.get(1);
                                String[] xVals = new String[newComponentMap.size()];
                                double[] yVals1 = new double[newComponentMap.size()];
                                double[] yVals2 = new double[newComponentMap.size()];
                                int i = 0;
                                Set<String> keySet = newComponentMap.keySet();
                                for(String key : keySet){
                                    xVals[i] = key;
                                    yVals1[i] = newComponentMap.get(key).size();
                                    yVals2[i] = oldComponentMap.get(key).size();
                                    i++;
                                }

                                mBarChart.setData(xVals, yVals2, yVals1);
                            }
                        });
                /*mStatisticService.statisticByDistrict(ComponentTypeConstant.NEW_ADDED_COMPONENT_VALUE)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, List<StatisticResult2>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onNext(Map<String, List<StatisticResult2>> stringListMap) {
                                String[] xVals = new String[stringListMap.size()];
                                double[] yVals = new double[stringListMap.size()];
                                int i = 0;
                                Set<String> keySet = stringListMap.keySet();
                                for(String key : keySet){
                                    xVals[i] = key;
                                    yVals[i] = stringListMap.get(key).size();
                                    i++;
                                }
                                mBarChart.setData(xVals, yVals);

                            }
                        });

                mStatisticService.statisticByDistrict(ComponentTypeConstant.OLD_COMPONENT_VALUE2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, List<StatisticResult2>>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }

                            @Override
                            public void onNext(Map<String, List<StatisticResult2>> stringListMap) {

                                String[] xVals = new String[stringListMap.size()];
                                double[] yVals = new double[stringListMap.size()];
                                int i = 0;
                                Set<String> keySet = stringListMap.keySet();
                                for(String key : keySet){
                                    xVals[i] = key;
                                    yVals[i] = stringListMap.get(key).size();
                                    i++;
                                }
                                mBarChart2.setData(xVals, yVals);
                            }
                        });*/
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitLayerUrlFinish(OnInitLayerUrlFinishEvent onInitLayerUrlFinishEvent){
//        mSpinnerArea1.selectItem(0);
    }
}
