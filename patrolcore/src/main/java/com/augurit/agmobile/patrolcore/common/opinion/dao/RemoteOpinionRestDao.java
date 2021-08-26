package com.augurit.agmobile.patrolcore.common.opinion.dao;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.agmobile.patrolcore.common.opinion.model.TemplateResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.net.AMNetwork;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.dao
 * @createTime 创建时间 ：2017-07-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-07
 * @modifyMemo 修改备注：
 */

public class RemoteOpinionRestDao {

    private AMNetwork amNetwork;
    private OpinionApi mOpinionApi;

    public RemoteOpinionRestDao(Context context){
        String serverUrl = BaseInfoManager.getBaseServerUrl(context);
        amNetwork = new AMNetwork(serverUrl);
        amNetwork.addLogPrint();
        amNetwork.addRxjavaConverterFactory();
        amNetwork.build();
        amNetwork.registerApi(OpinionApi.class);
        mOpinionApi = (OpinionApi) amNetwork.getServiceApi(OpinionApi.class);
    }

    public Observable<String> saveOpinionTemplate(String id,
                                                  String name,
                                                  String content,
                                                  String projectId,
                                                  String link,
                                                  String userId,
                                                  String authorization){
        return mOpinionApi.saveOpinionTemplate(id, name, content, projectId, link, userId, authorization)
                .map(new Func1<Response<ResponseBody>, String>() {
                    @Override
                    public String call(Response<ResponseBody> response) {
                        try {
                            String body = response.body().string();
                            return body;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                    }
                });
    }

    public Observable<String> deleteOpinionTemplate(String id){
        return mOpinionApi.deleteOpinionTemplate(id)
                .map(new Func1<Response<ResponseBody>, String>() {
                    @Override
                    public String call(Response<ResponseBody> response) {
                        try {
                            String body = response.body().string();
                            return body;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                    }
                });
    }

    public Observable<List<OpinionTemplate>> getOpinionTemplates(String userId,
                                                                String name,
                                                                 String projectId,
                                                                String link,
                                                                int pageNo,
                                                                int pageSize){
        return mOpinionApi.getOpinionTemplates(userId, name, projectId, link, pageNo, pageSize)
                .map(new Func1<TemplateResult, List<OpinionTemplate>>() {
                    @Override
                    public List<OpinionTemplate> call(TemplateResult templateResult) {
                        if(templateResult == null){
                            return null;
                        }
                        return templateResult.getRows();
                    }
                });
    }
}
