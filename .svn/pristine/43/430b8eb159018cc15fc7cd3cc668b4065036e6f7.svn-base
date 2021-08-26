package com.augurit.agmobile.mapengine.layerimport.util;


import com.augurit.agmobile.mapengine.layerimport.model.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.activation.MimetypesFileTypeMap;

/**
 * Created by liangsh on 2016-12-19.
 */
public final class ShapeFileScanUtil {
    private ShapeFileScanUtil() {
    }

    /**
     * 扫描目录中的shape文件，必须包含.shp、.shx、.dbf、.prj四个文件
     *
     * @param scanDir shape文件目录
     */
    public static ArrayList<Document> scanShapeFile(String scanDir) {
        ArrayList<Document> documents = new ArrayList<Document>();
        File scanDirF = new File(scanDir);
        if (!scanDirF.exists()) {
            return documents;
        }
        File[] files = scanDirF.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (!filename.endsWith(".shp")) {
                    return false;
                }
                String fn = filename.substring(0, filename.lastIndexOf("."));
                File prj = new File(dir.getPath() + "/" + fn + ".prj");
                File shx = new File(dir.getPath() + "/" + fn + ".shx");
                File dbf = new File(dir.getPath() + "/" + fn + ".dbf");
                return prj.exists()
                        && shx.exists()
                        && dbf.exists();
            }
        });

        for (int i = 0; i < files.length; ++i) {
            File f = files[i];
            Document doc = new Document();
            doc.setId(i);
            doc.setMimeType(new MimetypesFileTypeMap().getContentType(f));
            doc.setPath(f.getAbsolutePath());
            doc.setTitle(f.getName());
            long fileSize = 0;
            try {
                fileSize = new FileInputStream(f).available();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.setSize("" + fileSize);
            documents.add(doc);
        }
        return documents;
    }
}
