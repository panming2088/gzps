package com.augurit.agmobile.mapengine.bookmark.view.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.agmobile.mapengine.bookmark.service.BookMarkService;
import com.augurit.agmobile.mapengine.bookmark.service.IBookMarkService;
import com.augurit.agmobile.mapengine.bookmark.view.IBookMarkView;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.widget.bottomsheet.BottomSheetBehavior;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认书签Presenter
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.view.presenter
 * @createTime 创建时间 ：2017-01-19 10:51
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:51
 */
public class BookMarkPresenter implements IBookMarkPresenter {

    protected Context mContext;
    protected IBookMarkService mService;
    protected IBookMarkView mView;
    protected ViewGroup mTopBarContainer;
    protected ViewGroup mBookMarkContainer;
    protected Callback1<Void> mBackListener;

    /**
     * 构造方法
     * @param context Context
     * @param database 数据库
     */
    public BookMarkPresenter(Context context, IBookMarkView bookMarkView, AMDatabase database) {
        mContext = context;
        mService = new BookMarkService(database);
        mView = bookMarkView;
        mView.setPresenter(this);
    }

    /**
     * 获取书签列表
     * @param callback 获取数据回调
     */
    @Override
    public void getBookMarks(final Callback2<List<BookMark>> callback) {
        final String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(mContext);
        mService.getBookMarks(new Callback2<List<BookMark>>() {
            @Override
            public void onSuccess(List<BookMark> bookMarks) {
                // 过滤掉非当前专题的书签数据
                List<BookMark> filtered = new ArrayList<>();
                if (bookMarks != null) {
                    for (BookMark bookMark : bookMarks) {
                        if (bookMark.getProjectId() != null
                                && bookMark.getProjectId().equals(currentProjectId)) {
                            filtered.add(bookMark);
                        }
                    }
                }
                callback.onSuccess(filtered);
            }

            @Override
            public void onFail(Exception error) {
                callback.onFail(error);
            }
        });
    }

    /**
     * 上传书签
     * @param bookMark 书签
     * @param callback 上传操作回调
     */
    @Override
    public void uploadBookMark(BookMark bookMark, Callback2<Integer> callback) {
        mService.uploadBookMark(bookMark, callback);
    }

    /**
     * 删除书签
     * @param bookMark 书签
     * @param callback 删除操作回调
     */
    @Override
    public void deleteBookMark(BookMark bookMark, Callback2<Integer> callback) {
        mService.deleteBookMark(bookMark, callback);
    }

    /**
     * 显示书签视图
     * @param topBarContainer 顶栏容器
     * @param bookMarkContainer 书签容器
     * @param loadCallback 加载回调
     */
    @Override
    public void showBookMarks(final ViewGroup topBarContainer, final ViewGroup bookMarkContainer,
                              final Callback2<Void> loadCallback) {
        mTopBarContainer = topBarContainer;
        mBookMarkContainer = bookMarkContainer;
        getBookMarks(new Callback2<List<BookMark>>() {
            @Override
            public void onSuccess(List<BookMark> bookMarks) {
                // 获取书签列表视图
                View bookMarkListView = mView.getBookMarkListView(bookMarks, new IBookMarkView.OnBookMarkClickListener() {
                    @Override
                    public void onBookMarkClicked(BookMark bookMark, int position) {
                        if (!DeviceUtil.isTablet(mContext)) {
                            BottomSheetBehavior.from(bookMarkContainer).setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }
                });
                View btn_back = bookMarkListView.findViewById(R.id.btn_back);  // 平板返回按钮
                if (btn_back != null) {
                    btn_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mBackListener != null) {
                                mBackListener.onCallback(null);
                            }
                        }
                    });
                }
                bookMarkContainer.removeAllViews();
                bookMarkContainer.addView(bookMarkListView);
                bookMarkContainer.setVisibility(View.VISIBLE);

                // 非平板则获取顶栏视图
                if (!DeviceUtil.isTablet(mContext)) {
                    View topBarView = mView.getTopBarView();
                    View top_bar_back = topBarView.findViewById(R.id.btn_back);  // 顶栏返回按钮
                    if (top_bar_back != null) {
                        top_bar_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onClose();
                                if (mBackListener != null) {
                                    mBackListener.onCallback(null);
                                }
                            }
                        });
                    }
                    topBarView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    topBarContainer.removeAllViews();
                    topBarContainer.addView(topBarView);
                    topBarContainer.setVisibility(View.VISIBLE);
                }
                loadCallback.onSuccess(null);
            }

            @Override
            public void onFail(Exception error) {
                loadCallback.onFail(error);
            }
        });
    }

    /**
     * 显示添加书签对话框
     */
    @Override
    public void showDialog() {
        mView.showDialog();
    }

    /**
     * 功能关闭
     */
    @Override
    public void onClose() {
        if (mTopBarContainer != null) {
            mTopBarContainer.removeAllViews();
            mTopBarContainer.setVisibility(View.GONE);
        }
        if (mBookMarkContainer != null) {
            mBookMarkContainer.removeAllViews();
//            mBookMarkContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 设置返回监听
     * @param backListener 返回监听
     */
    @Override
    public void setBackListener(Callback1<Void> backListener) {
        mBackListener = backListener;
    }
}
