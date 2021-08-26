package com.augurit.agmobile.mapengine.bookmark.service;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.agmobile.mapengine.bookmark.router.BookMarkRouter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.db.AMDatabase;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 默认书签Service
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.service
 * @createTime 创建时间 ：2017-01-19 10:48
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:48
 */
public class BookMarkService implements IBookMarkService {

    private BookMarkRouter mRouter;

    public BookMarkService(AMDatabase database) {
        mRouter = new BookMarkRouter(database);
    }

    /**
     * 获取书签列表
     * @param callback 获取数据回调
     */
    @Override
    public void getBookMarks(final Callback2<List<BookMark>> callback) {
        Observable.create(new Observable.OnSubscribe<List<BookMark>>() {
            @Override
            public void call(Subscriber<? super List<BookMark>> subscriber) {
                try {
                    List<BookMark> bookMarks = mRouter.getBookMarks();
                    subscriber.onNext(bookMarks);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<List<BookMark>>() {
              @Override
              public void onCompleted() {
              }

              @Override
              public void onError(Throwable e) {
                  callback.onFail(new Exception(e));
              }

              @Override
              public void onNext(List<BookMark> bookMarks) {
                  callback.onSuccess(bookMarks);
              }
          });
    }

    /**
     * 上传书签
     * @param bookMark 书签
     * @param callback 上传操作回调
     */
    @Override
    public void uploadBookMark(final BookMark bookMark, final Callback2<Integer> callback) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int id = mRouter.uploadBookMark(bookMark);
                    subscriber.onNext(id);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                callback.onFail(new Exception(e));
            }

            @Override
            public void onNext(Integer integer) {
                callback.onSuccess(integer);
            }
        });
    }

    /**
     * 删除书签
     * @param bookMark 书签
     * @param callback 删除操作回调
     */
    @Override
    public void deleteBookMark(final BookMark bookMark, final Callback2<Integer> callback) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    mRouter.deleteBookMark(bookMark);
                    subscriber.onNext(1);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (callback != null) {
                    callback.onFail(new Exception(e));
                }
            }

            @Override
            public void onNext(Integer integer) {
                if (callback != null) {
                    callback.onSuccess(integer);
                }
            }
        });
    }
}
