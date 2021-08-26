package com.augurit.agmobile.patrolcore.common;

import com.esri.core.geometry.Envelope;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.home
 * @createTime 创建时间 ：17/4/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/4/21
 * @modifyMemo 修改备注：
 */

public class MapLocateEvent {
    private Envelope mEvelope;

    public MapLocateEvent(Envelope mEvelope) {
        this.mEvelope = mEvelope;
    }

    public Envelope getEvelope() {
        return mEvelope;
    }
}
