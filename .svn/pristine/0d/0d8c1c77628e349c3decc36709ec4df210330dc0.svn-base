/*
 * Copyright (C) 2013 litesuits.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.augurit.am.fw.db.liteorm.db;

import android.content.Context;

import com.augurit.am.fw.db.liteorm.db.assit.Checker;
import com.augurit.am.fw.db.liteorm.db.assit.SQLiteHelper;


/**
 * 数据操作配置
 *
 * @author MaTianyu
 *         2013-6-2下午4:36:16
 */
public class DataBaseConfig {
    public static final String DEFAULT_DB_NAME = "liteorm.db";
    public static final String DEFAULT_DB_PASSWORD = "agdata";
    public static final int DEFAULT_DB_VERSION = 1;

    public Context context;
    public boolean debugged = false;
    public String dbName = DEFAULT_DB_NAME;
    public String password = DEFAULT_DB_PASSWORD;
    public int dbVersion = DEFAULT_DB_VERSION;
    public SQLiteHelper.OnUpdateListener onUpdateListener;

    public DataBaseConfig(Context context) {
        this(context, DEFAULT_DB_NAME, DEFAULT_DB_PASSWORD);
    }

    public DataBaseConfig(Context context, String dbName, String password) {
        this(context, dbName, password, false, DEFAULT_DB_VERSION, null);
    }

    public DataBaseConfig(Context context, String dbName, String password, boolean debugged,
                          int dbVersion, SQLiteHelper.OnUpdateListener onUpdateListener) {
        this.context = context.getApplicationContext();
        if (!Checker.isEmpty(dbName)) {
            this.dbName = dbName;
        }
        if (!Checker.isEmpty(password)) {
            this.password = password;
        }
        if (dbVersion > 1) {
            this.dbVersion = dbVersion;
        }
        this.debugged = debugged;
        this.onUpdateListener = onUpdateListener;
    }

    @Override
    public String toString() {
        return "DataBaseConfig [mContext=" + context + ", mDbName=" + dbName + ", mDbVersion="
               + dbVersion + ", mOnUpdateListener=" + onUpdateListener + "]";
    }

}
