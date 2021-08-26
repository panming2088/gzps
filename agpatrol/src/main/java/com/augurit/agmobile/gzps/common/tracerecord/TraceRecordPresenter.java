package com.augurit.agmobile.gzps.common.tracerecord;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.tracerecord
 * @createTime 创建时间 ：2017-03-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-13
 * @modifyMemo 修改备注：
 */

public class TraceRecordPresenter implements ITraceRecordPresenter {

    protected ITraceRecordView mITraceRecordView;

    public TraceRecordPresenter(ITraceRecordView traceRecordView){
        this.mITraceRecordView = traceRecordView;
    }

    @Override
    public void startTraceRecord() {
        mITraceRecordView.addTraceRecordViewToContainer();
        mITraceRecordView.showTraceRecordView();
    }

    @Override
    public void exitTraceRecord() {

    }
}
