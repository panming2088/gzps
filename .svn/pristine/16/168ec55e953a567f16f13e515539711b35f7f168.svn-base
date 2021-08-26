package com.augurit.agmobile.mapengine.map;


import com.augurit.agmobile.mapengine.map.Map;
import com.augurit.agmobile.mapengine.map.OnMapClickListener;
import com.augurit.agmobile.mapengine.map.OnMapDoubleClickListener;
import com.augurit.agmobile.mapengine.map.OnMapLongClickListener;
import com.augurit.agmobile.mapengine.map.OnMapMoveListener;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.action
 * @createTime 创建时间 ：2017-02-13
 * @modifyBy 修改人 ： xuciluan
 * @modifyTime 修改时间 ：2017-02-21 将IMapActionMapLogic从defaultview下移动到mapengine中，并且取消继承自IAction
 */
public interface IMapActionMapLogic extends OnMapClickListener, OnMapDoubleClickListener, OnMapLongClickListener,
                                    OnMapMoveListener{
    Map getMap();
    //xcl  2017-02-21 添加以下方法
    void onCreate();

    void onDestroy();
}
