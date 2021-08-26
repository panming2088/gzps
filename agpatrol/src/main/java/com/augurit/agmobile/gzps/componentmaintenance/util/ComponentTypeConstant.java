package com.augurit.agmobile.gzps.componentmaintenance.util;

/**
 * 部件类型
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.componentmaintenance
 * @createTime 创建时间 ：17/10/14
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/14
 * @modifyMemo 修改备注：
 */

public class ComponentTypeConstant {

    /**
     * 部件类型
     */
    public static final String NEW_ADDED = "2"; //新增部件
    public static final String OLD_COMPONENT = "0" ;//原先就有的部件
    public static final String OLD_COMPONENT2 = "1" ;//修改过的部件

    /**
     * 判断部件类型的key
     */
    public static final String COMPONENT_TYPE_KEY = "FLAG_";
    /**
     * 表示该部件是新增时对应的值
     */
    public static final String NEW_ADDED_COMPONENT_VALUE = "2";
    /**
     * 表示该原先就有的部件时对应的值
     */
    public static final String OLD_COMPONENT_VALUE = "0";
    /**
     * 表示该原先就有的部件时对应的值
     */
    public static final String OLD_COMPONENT_VALUE2 = "1";
}
