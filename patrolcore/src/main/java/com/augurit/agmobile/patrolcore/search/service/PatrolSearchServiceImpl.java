package com.augurit.agmobile.patrolcore.search.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.search.dao.SearchDao;
import com.augurit.agmobile.patrolcore.search.model.BriefSearchResult;
import com.augurit.agmobile.patrolcore.search.model.CompleteUploadInfo;
import com.augurit.agmobile.patrolcore.search.model.FilterCondition;
import com.augurit.agmobile.patrolcore.search.model.GetPhotosResult;
import com.augurit.agmobile.patrolcore.search.model.SearchResult;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.search.service
 * @createTime 创建时间 ：2017-03-16
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-16
 * @modifyMemo 修改备注：
 */
public class PatrolSearchServiceImpl implements IPatrolSearchService {

    public static final String GET_IMAGE_PREFIX = "img/";

    protected SearchDao mSearchDao;
    protected Context mContext;

    public PatrolSearchServiceImpl(Context context, SearchDao searchDao) {
        mSearchDao = searchDao;
        mContext = context;
    }

    public PatrolSearchServiceImpl(Context context) {
        mSearchDao = new SearchDao(context);
        mContext = context;
    }

    @Override
    public Observable<List<SearchResult>> getHistory(int index, int loadNum) {
       /* return new FakePatrolSearchService(mContext).getHistory(index,loadNum);*/
        return mSearchDao.getHistories(index, loadNum)
                .map(new Func1<BriefSearchResult, List<SearchResult>>() {
                    @Override
                    public List<SearchResult> call(BriefSearchResult tempSearchResult) {
                        List<SearchResult> searchResults = new ArrayList<SearchResult>();
                        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
                        //进行实体类转换
                        List<List<BriefSearchResult.BriefRow>> rows = tempSearchResult.getRows();
                        for (List<BriefSearchResult.BriefRow> rowList : rows) {
                            SearchResult searchResult = new SearchResult();
                            for (BriefSearchResult.BriefRow singleRow : rowList) {
                                String value = singleRow.getValue();
                                switch (singleRow.getField1()) {
                                    case "report_time": //上报时间
                                        String[] split = value.split("\\.");
                                        searchResult.setDate(split[0]);
                                        // searchResult.setDate(singleRow.getValue());
                                        break;
                                    case "state":
                                        if ("".equals(value)) {
                                            searchResult.setStatus("待处置");
                                        } else {
                                            searchResult.setStatus(value);
                                        }
                                        break;
                                    case "photo":
                                        if (value.equals("")) {
                                            searchResult.setPic("");
                                        } else {
                                            //得到完整的图片路径
                                            //xcl 2017-08-14 接口已改了，直接返回url
                                           // String completePath = getImageUrl(value);
                                            String completePath = value;
                                            searchResult.setPic(completePath);
                                            // LogUtil.d("获取上报图片","上报图片的url是："+ completePath);
                                        }
                                        break;
                                    case "patrol_code": //工单编号
                                        String patrolId = value;
                                        if (patrolId != null && !patrolId.equals("")) {
                                            // searchResult.setName(patrolId);
                                            searchResult.setPatrolId(patrolId);
                                        } else {
                                            searchResult.setPatrolId("patrolId为空！！！");
                                        }
                                        break;
                                    case "patrol_id": //上报表主键
                                        String id = value;
                                        if (id != null && !id.equals("")) {
                                            searchResult.setId(id);
                                        }
                                        break;
                                    case "disease_types":
                                        // searchResult.setName(PatrolUtil.getDiseaseNameByDiseaseId(mContext, value));
                                        searchResult.setName(singleRow.getValue());
                                        break;
                                    case "poision":
                                        searchResult.setAddress(value);
                                        break;
                                }
                            }
                            searchResult.setTotal(tempSearchResult.getTotal());
                            searchResults.add(searchResult);
                        }
                        return searchResults;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<SearchResult>> getHistory(int index, int loadNum, Map<String, String> params) {
        return null;    // TODO xjx 先实现impl2
    }

    @NonNull
    protected String getImageUrl(String value) {

        String baseServerUrl = BaseInfoManager.getBaseServerUrl(mContext);
       // String imageUrl = baseServerUrl + GET_IMAGE_PREFIX + value; //+ ".jpg";
        baseServerUrl = "http://39.108.72.145:8081/"; //todo 写死URL
        String imageUrl = baseServerUrl + GET_IMAGE_PREFIX + value; //+ ".jpg";

        LogUtil.d("获取上报图片", "上报图片的url是：" + imageUrl);
        return imageUrl;
    }

    @Override
    public Observable<List<FilterCondition>> getFilterCondition() {
        return new FakePatrolSearchService(mContext, mSearchDao).getFilterCondition();
    }

    @Override
    public Observable<List<TableItem>> getKeywordFileds(String url, String projectId) {
        return null;    // TODO xjx 先实现impl2
    }

    @Override
    public Observable<List<TableItem>> getFilterConditions(String url) {
        return null;    // TODO xjx 先实现impl2
    }

    @Override
    public Observable<List<Photo>> getPhotos(final String patrolId) {
        return mSearchDao.getPhotos(patrolId)
                .map(new Func1<GetPhotosResult, List<Photo>>() {
                    @Override
                    public List<Photo> call(GetPhotosResult getPhotosResult) {
                        List<Photo> photos = new ArrayList<Photo>();
                        List<GetPhotosResult.NetPhoto> result = getPhotosResult.getResult();
                        for (GetPhotosResult.NetPhoto netPhoto : result) {
                            List<GetPhotosResult.NetPhoto.PhotoInfo> photoInfos = netPhoto.getContent();
                            for (GetPhotosResult.NetPhoto.PhotoInfo photoInfo : photoInfos) {
                                Photo photo = new Photo();
                                //通过path得到后缀
                                int i = photoInfo.getPath().lastIndexOf(".");
                                String picType = photoInfo.getPath().substring(i);
                                //得到完整的图片路径
                                String name = photoInfo.getName();
                                //xcl 2017-08-14 接口改变，现在name里面直接返回的就是完整的图片url
                                String imageUrl = getImageUrl(name);
                                photo.setPhotoPath(photoInfo.getPath());//xcl 2017-10-12 修改成自己拼接
                                //photo.setPhotoPath(name);
                                photo.setField1(photoInfo.getType());   // 新加字段
                                photos.add(photo);
                            }
                        }
                        return photos;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public List<TableItem> convert(SearchResult searchResult) {
        return null;
    }

    @Override
    public Observable<CompleteUploadInfo> getCompleteUploadInfoBySearchResult(SearchResult searchResult) {
        return Observable.zip(
                mSearchDao.getCompleteUploadInfo(searchResult.getId()),
                getPhotos(searchResult.getPatrolId()), new Func2<List<List<TableItem>>, List<Photo>, CompleteUploadInfo>() {
                    @Override
                    public CompleteUploadInfo call(List<List<TableItem>> lists, List<Photo> photos) {
                        CompleteUploadInfo completeUploadInfo = new CompleteUploadInfo();
                        completeUploadInfo.setPhotos(photos);
                        completeUploadInfo.setTableItems(lists.get(0));
                        return completeUploadInfo;
                    }
                }).subscribeOn(Schedulers.newThread());
    }

}
