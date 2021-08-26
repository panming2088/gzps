package com.augurit.agmobile.mapengine.edit.util;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.AttachmentInfo;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;

import java.io.File;
import java.io.InputStream;

/**
 * 附件管理工具类
 * <p>
 * Created by guokunhu on 16/10/27.
 */

public final class AttachmentsUtils {
    private AttachmentsUtils() {
    }

    /**
     * 判断属性图层是否支持附件
     *
     * @param featureLayer
     * @return
     */
    public static boolean isSupportAttachments(ArcGISFeatureLayer featureLayer) {
        return featureLayer.hasAttachments();
    }

    /**
     * 查询某一属性的附件信息,在回调中处理结果
     *
     * @param featureLayer
     * @param objectId
     * @param callback
     */
    public static void queryAttachmentInfos(ArcGISFeatureLayer featureLayer, int objectId, CallbackListener<AttachmentInfo[]> callback) {
        featureLayer.queryAttachmentInfos(objectId, callback);
    }

    /**
     * 上传附件,在回调中处理结果
     *
     * @param featureLayer
     * @param file
     * @param type
     * @param callback
     */
    public static void addAttachment(ArcGISFeatureLayer featureLayer, int objectId, File file, String type, CallbackListener<FeatureEditResult> callback) {
        featureLayer.addAttachment(objectId, file, type, callback);
    }

    /**
     * 上传附件,在回调中处理结果
     *
     * @param featureLayer
     * @param objectId
     * @param file
     * @param callback
     */
    public static void addAttachment(ArcGISFeatureLayer featureLayer, int objectId, File file, CallbackListener<FeatureEditResult> callback) {
        featureLayer.addAttachment(objectId, file, callback);
    }

    /**
     * 获取附件中的内容,记得在获取结果后要调用InputStream.close()来关闭输入流
     *
     * @param featureLayer
     * @param objectId
     * @param attachmentId
     * @return
     */
    public static InputStream retrieveAttachment(ArcGISFeatureLayer featureLayer, int objectId, int attachmentId) {
        try {
            return featureLayer.retrieveAttachment(objectId, attachmentId);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return null;
    }

    /**
     * 删除附件,在回调中处理删除结果
     *
     * @param featureLayer
     * @param obejctId
     * @param attachmentIds
     * @param callback
     */
    public static void deleteAttachments(ArcGISFeatureLayer featureLayer, int obejctId, int[] attachmentIds, CallbackListener<FeatureEditResult[]> callback) {
        featureLayer.deleteAttachments(obejctId, attachmentIds, callback);
    }
}
