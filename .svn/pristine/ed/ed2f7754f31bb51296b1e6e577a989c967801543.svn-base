package com.augurit.agmobile.gzps.uploadfacility.util;

import com.augurit.agmobile.gzps.common.model.ServerAttachment;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

/**
 * Created by xucil on 2018/1/4.
 */

public class ServerAttachmentToPhotoUtil {

    public static Photo getPhoto(ServerAttachment.ServerAttachmentDataBean serverAttachment){
        Photo photo = new Photo();
        photo.setId(Long.valueOf(serverAttachment.getId()));
        photo.setPhotoPath(serverAttachment.getAttPath());
        photo.setThumbPath(serverAttachment.getThumPath());
        photo.setPhotoName(serverAttachment.getAttName());
        return photo;
    }
}
