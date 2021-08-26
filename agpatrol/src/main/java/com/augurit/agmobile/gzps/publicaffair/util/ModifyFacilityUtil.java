package com.augurit.agmobile.gzps.publicaffair.util;

import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.publicaffair.model.ModifyFacilityDetail;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 设施纠错工具
 * Created by xcl on 2017/11/17.
 */

public class ModifyFacilityUtil {

    /**
     *
     * @param modifyFacilityDetail
     * @return
     */
    public static ModifiedFacility getModifiedFacility(ModifyFacilityDetail modifyFacilityDetail){

        List<ServerAttachment.ServerAttachmentDataBean> rows = modifyFacilityDetail.getRows();
        if(!ListUtil.isEmpty(rows)){
            List<Photo> photos = new ArrayList<>();
            for (ServerAttachment.ServerAttachmentDataBean dataBean : rows){
                Photo photo = new Photo();
                photo.setId(Long.valueOf(dataBean.getId()));
                photo.setPhotoPath(dataBean.getAttPath());
                photo.setThumbPath(dataBean.getThumPath());
                photo.setPhotoName(dataBean.getAttName());
                photos.add(photo);
            }
            modifyFacilityDetail.getData().setPhotos(photos);
        }

        return modifyFacilityDetail.getData();
    }
}
