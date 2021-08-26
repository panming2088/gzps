package com.augurit.agmobile.photo.identify;

/**
 * 描述：身份证拍照识别出身份证号码
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.photo.identify
 * @createTime 创建时间 ：17/5/22
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/22
 * @modifyMemo 修改备注：
 */

public interface OnCaptureCallback {
    void onCapture(boolean success, String filePath);
}
