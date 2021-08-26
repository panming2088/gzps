package com.augurit.agmobile.patrolcore.common.opinion.dao;

import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.common.opinion.model.OpinionTemplate;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.db.AMWhereBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.dao
 * @createTime 创建时间 ：2017-07-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-07
 * @modifyMemo 修改备注：
 */

public class LocalDatabaseOpinionDao {


    public void saveOpinionTemplate(OpinionTemplate opinionTemplate) {
        AMDatabase.getInstance().save(opinionTemplate);
    }

    public void saveOpinionTemplate(List<OpinionTemplate> opinionTemplates) {
        AMDatabase.getInstance().saveAll(opinionTemplates);
    }

    public void deleteOpinionTemplate(String id) {
        AMDatabase.getInstance().deleteWhere(OpinionTemplate.class, "id", id);
    }

    public List<OpinionTemplate> getOpinionTemplates(final String userId,
                                                     final String name,
                                                     final String projectId,
                                                     final String link) {
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(OpinionTemplate.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }
        if (!TextUtils.isEmpty(name)) {
            whereBuilder.and("(name like '%" + name + "%')");
        }
        if (!TextUtils.isEmpty(projectId)) {
            whereBuilder.and("projectId=?", projectId);
        }
        if (!TextUtils.isEmpty(link)) {
            whereBuilder.and("link=?", link);
        }

        return AMDatabase.getInstance().query(
                new AMQueryBuilder<OpinionTemplate>(OpinionTemplate.class)
                        .where(whereBuilder));
                /*Collections.sort(opinionTemplates, new Comparator<OpinionTemplate>() {
                    @Override
                    public int compare(OpinionTemplate o1, OpinionTemplate o2) {
                        if(o1.getId()>o2.getId()){
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });*/


    }

    public List<OpinionTemplate> getOpinionTemplates(final String userId,
                                                     final String name,
                                                     final String projectId,
                                                     final String link,
                                                     final int start,
                                                     final int length) {
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(OpinionTemplate.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }
        if (!TextUtils.isEmpty(name)) {
            whereBuilder.and("(name like '%" + name + "%')");
        }
        if (!TextUtils.isEmpty(projectId)) {
            whereBuilder.and("projectId=?", projectId);
        }
        if (!TextUtils.isEmpty(link)) {
            whereBuilder.and("link=?", link);
        }


        return AMDatabase.getInstance().query(
                new AMQueryBuilder<OpinionTemplate>(OpinionTemplate.class)
                        .where(whereBuilder)
                        .limit(start, length));
//                                        .appendOrderDescBy("id"));
                /*Collections.sort(opinionTemplates, new Comparator<OpinionTemplate>() {
                    @Override
                    public int compare(OpinionTemplate o1, OpinionTemplate o2) {
                        if(o1.getId()>o2.getId()){
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });*/


    }

    public List<OpinionTemplate> queryAll(){
        return AMDatabase.getInstance().getQueryAll(OpinionTemplate.class);
    }

    public List<OpinionTemplate> getNotUploadOpinions(final String userId) {
        AMWhereBuilder whereBuilder = AMWhereBuilder.create(OpinionTemplate.class);
        whereBuilder.where("1=1");
        if (!TextUtils.isEmpty(userId)) {
            whereBuilder.and("userId=?", userId);
        }

        whereBuilder.and("isUpload=?", false);

        return AMDatabase.getInstance().query(
                new AMQueryBuilder<OpinionTemplate>(OpinionTemplate.class)
                        .where(whereBuilder));
    }
}
