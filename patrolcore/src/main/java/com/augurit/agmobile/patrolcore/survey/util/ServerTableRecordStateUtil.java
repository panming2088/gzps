package com.augurit.agmobile.patrolcore.survey.util;

import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.survey.constant.ServerTableRecordConstant;
import com.augurit.am.fw.utils.StringUtil;

/**
 * 判断记录核实状态工具类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.util
 * @createTime 创建时间 ：17/9/16
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/16
 * @modifyMemo 修改备注：
 */

public class ServerTableRecordStateUtil {

    /**
     * 获取当境外人口审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getJingWaiRenkouUploadSuccessState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getJingWaiRenkouNewAddedState();
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[0] = '1'; //将第一个标志位变为1
            return StringUtil.byte2HexStr(bytes);
        }

        return getJingWaiRenkouNewAddedState();
    }

    /**
     * 获取当境外人口删除完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getJingWaiRenkouDeletedCheckedState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getJingWaiRenkouDeletedCheckedState();
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[0] = '2'; //将第一个标志位变为2,
            return StringUtil.byte2HexStr(bytes);
        }

        return getJingWaiRenkouDeletedCheckedState();
    }

    /**
     * 获取当境外人口审核新增时提交给服务端的状态
     *
     * @return
     */
    public static String getJingWaiRenkouNewAddedState() {
        return "10000";
    }

    /**
     * 获取当境外人口审核删除时提交给服务端的状态
     *
     * @return
     */
    public static String getJingWaiRenkouDeletedCheckedState() {
        return "20000";
    }


    /**
     * 获取基本人口信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getRenkouBasicInfoUploadSuccessState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getRenkouBasicInfoNewAddedCheckedState();
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[1] ='1'; //将第一个标志位变为1
            return StringUtil.byte2HexStr(bytes);
        }
        return getRenkouBasicInfoNewAddedCheckedState();

    }

    /**
     * 获取基本人口信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getRenkouBasicInfoNewAddedCheckedState() {

        return "01000";
    }

    /**
     * 获取基本人口信息审核删除时提交给服务端的状态
     *
     * @return
     */
    public static String getRenkouBasicInfoDeletedCheckedState() {

        return "02000";
    }


    /**
     * 获取流动人口信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getLiuDongInfoCheckedState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getLiuDongInfoCheckedState();
        }


        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[2] = '1'; //将第一个标志位变为1
            return StringUtil.byte2HexStr(bytes);
        }
        return getLiuDongInfoCheckedState();

    }

    /**
     * 获取流动人口信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getLiuDongInfoDeletedCheckedState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getLiuDongInfoDeletedCheckedState();
        }


        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[2] = '2'; //将第一个标志位变为2
            return StringUtil.byte2HexStr(bytes);
        }
        return getLiuDongInfoDeletedCheckedState();

    }

    /**
     * 获取流动人口信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getLiuDongInfoCheckedState() {
        return "00100";
    }

    /**
     * 获取流动人口信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getLiuDongInfoDeletedCheckedState() {
        return "00200";
    }


    /**
     * 获取学生信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getXueshengInfoCheckedState(String originDataState) {


        if (TextUtils.isEmpty(originDataState)) {
            return getXueshengInfoCheckedState();
        }


        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[3] = '1'; //将第一个标志位变为1
            return StringUtil.byte2HexStr(bytes);
        }

        return getXueshengInfoCheckedState();
    }

    /**
     * 获取学生信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getXueshengInfoDeletedCheckedState(String originDataState) {


        if (TextUtils.isEmpty(originDataState)) {
            return getXueshengInfoDeletedCheckedState();
        }


        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[3] = '2'; //将第一个标志位变为2
            return StringUtil.byte2HexStr(bytes);
        }

        return getXueshengInfoDeletedCheckedState();
    }

    /**
     * 获取学生信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getXueshengInfoCheckedState() {

        return "00010";
    }

    /**
     * 获取学生信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getXueshengInfoDeletedCheckedState() {

        return "00020";
    }

    /**
     * 获取从业人员信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getCongyeInfoCheckedState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getCongyeInfoCheckedState();
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[4] = '1'; //将第一个标志位变为1
            return StringUtil.byte2HexStr(bytes);
        }

        return getCongyeInfoCheckedState();
    }

    /**
     * 获取从业人员信息审核完成时提交给服务端的状态
     *
     * @param originDataState
     * @return
     */
    public static String getCongyeInfoDeletedCheckedState(String originDataState) {

        if (TextUtils.isEmpty(originDataState)) {
            return getCongyeInfoDeletedCheckedState();
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5) {
            bytes[4] = '2'; //将第一个标志位变为2
            return StringUtil.byte2HexStr(bytes);
        }

        return getCongyeInfoDeletedCheckedState();
    }

    /**
     * 获取从业人员信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getCongyeInfoCheckedState() {
        return "00001";
    }

    /**
     * 获取从业人员信息审核完成时提交给服务端的状态（新增状态下）
     *
     * @return
     */
    public static String getCongyeInfoDeletedCheckedState() {
        return "00002";
    }


    /**
     * 判断人口基本信息是否已经核查
     *
     * @param originDataState 状态
     * @return
     */
    public static String judgeBasicInfoCheckedState(String originDataState){

        if (TextUtils.isEmpty(originDataState)) {
            return ServerTableRecordConstant.UNCHECK;
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5 && (bytes[1] == 49 || bytes[1] == 1)) {
           return ServerTableRecordConstant.CHECKED;
        }

        if (bytes.length == 5 && bytes[1] == '2') {
            return ServerTableRecordConstant.DELETED;
        }

        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        return ServerTableRecordConstant.UNCHECK;
    }

    /**
     * 判断流动人口信息是否已经核查
     *
     * @param originDataState 状态
     * @return
     */
    public static  String judgeLiuDongRenKouInfoCheckState(String originDataState){

        if (TextUtils.isEmpty(originDataState)) {
            return ServerTableRecordConstant.UNCHECK;
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5 && (bytes[2] == 49 ||bytes[2] == 1)) {
           return ServerTableRecordConstant.CHECKED;
        }

        if (bytes.length == 5 && bytes[2] == '2') {
            return ServerTableRecordConstant.DELETED;
        }

        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        return ServerTableRecordConstant.UNCHECK;
    }

    /**
     * 判断境外人口信息是否已经核查
     *
     * @param originDataState 状态
     * @return
     */
    public static  String judgeJingwaiRenKouInfoCheckState(String originDataState){

        if (TextUtils.isEmpty(originDataState)) {
            return ServerTableRecordConstant.UNCHECK;
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5 && (bytes[0] == 49 || bytes[0] == 1)) {
           return ServerTableRecordConstant.CHECKED;
        }

        if (bytes.length == 5 && bytes[0] == '2') {
            return ServerTableRecordConstant.DELETED;
        }

        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        return ServerTableRecordConstant.UNCHECK;
    }

    /**
     * 判断学生信息是否已经核查
     *
     * @param originDataState 状态
     * @return
     */
    public static String judgeXueShengInfoCheckState(String originDataState){

        if (TextUtils.isEmpty(originDataState)) {
            return ServerTableRecordConstant.UNCHECK;
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5 && (bytes[3] == 49 || bytes[3] == 1)) {
           return ServerTableRecordConstant.CHECKED;
        }

        if (bytes.length == 5 && bytes[3] =='2') {
            return ServerTableRecordConstant.DELETED;
        }


        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        return ServerTableRecordConstant.UNCHECK;
    }

    /**
     * 判断从业人员信息是否已经核查
     *
     * @param originDataState 状态
     * @return
     */
    public static  String judgeCongYeInfoCheckState(String originDataState){

        if (TextUtils.isEmpty(originDataState)) {
            return ServerTableRecordConstant.UNCHECK;
        }

        byte[] bytes = StringUtil.hexStr2Bytes(originDataState);

        if (bytes.length == 5 && (bytes[4] == 49 || bytes[4] == 1)) {
           return ServerTableRecordConstant.CHECKED;
        }

        if (bytes.length == 5 && bytes[4] == '2') {
            return ServerTableRecordConstant.DELETED;
        }

        if (ServerTableRecordConstant.UNUPLOAD.equals(originDataState)){
            return ServerTableRecordConstant.UNUPLOAD;
        }

        return ServerTableRecordConstant.UNCHECK;
    }
}
