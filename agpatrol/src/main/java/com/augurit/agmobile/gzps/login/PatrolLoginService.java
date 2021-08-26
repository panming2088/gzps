package com.augurit.agmobile.gzps.login;

import android.content.Context;

import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.fw.db.AMDatabase;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.login
 * @createTime 创建时间 ：2017-03-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-21
 * @modifyMemo 修改备注：
 */

public class PatrolLoginService extends LoginService{

    public PatrolLoginService(Context context, AMDatabase database) {
        super(context, database);
        mRouter = new PatrolLoginRouter(context,database);
    }


}
