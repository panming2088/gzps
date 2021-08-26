/**
 * ACCJava - ACC Java Development Platform
 * Copyright (c) 2014, AfirSraftGarrier, afirsraftgarrier@qq.com
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.augurit.am.fw.utils;

import android.accounts.NetworkErrorException;

import java.net.SocketTimeoutException;
/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：17/9/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/26
 * @modifyMemo 修改备注：
 * @version 1.0
 */

public final class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static IllegalArgumentException getIllegalArgumentException(
            String exceptionString) {
        return new IllegalArgumentException(exceptionString);
    }


    /**
     * 获取数据失败时的返回的提示信息
     * @param dataName
     * @param e
     * @return
     */
    public static String getErrorMessage(String dataName, Exception e) {

        if (e instanceof SocketTimeoutException) {
            return "连接服务器超时";
        } else if (e instanceof NetworkErrorException) {
            return "网络连接失败";
        } else {
            return "获取" + dataName + "数据失败,原因是：" + e.getLocalizedMessage();
        }
    }

    /**
     * 获取数据失败时的返回的提示信息
     * @param e
     * @return
     */
    public static String getErrorMessage(Exception e) {
        return getErrorMessage("", e);
    }
}