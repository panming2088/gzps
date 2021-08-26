package com.augurit.agmobile.gzps.uploadfacility.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lsh on 2018/2/2.
 */

public class DrainageEntity  implements Serializable{


    /**
     * id : 1
     * administrative :
     * entry_name :
     * license_key :
     * type :
     * state :
     * create_time : 1568465333
     * files : [{"id":1,"att_name":"","att_type":"","att_time":1568465333,"mime":"","att_path":"","thum_path":""}]
     */

    private int id;
    private String administrative;
    private String entry_name;
    private String license_key;
    private String type;
    private String state;
    private long create_time;
    private List<FilesBean> files;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdministrative() {
        return administrative;
    }

    public void setAdministrative(String administrative) {
        this.administrative = administrative;
    }

    public String getEntry_name() {
        return entry_name;
    }

    public void setEntry_name(String entry_name) {
        this.entry_name = entry_name;
    }

    public String getLicense_key() {
        return license_key;
    }

    public void setLicense_key(String license_key) {
        this.license_key = license_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object o){
        if (o == null)
            return false;

        if (this.getClass() != o.getClass())
            return false;
        if(o instanceof DrainageEntity){
            DrainageEntity de = (DrainageEntity) o;
            if(this.entry_name != null
                    && this.entry_name.equals(de.getEntry_name())
                    && this.license_key != null
                    && this.license_key.equals(de.getLicense_key())
                    && this.id != 0
                    && this.id == de.getId()){
                return true;
            }
        }
        return false;
    }

    public static class FilesBean implements Serializable{
        /**
         * id : 1
         * att_name :
         * att_type :
         * att_time : 1568465333
         * mime :
         * att_path :
         * thum_path :
         */

        private int id;
        private String att_name;
        private String att_type;
        private long att_time;
        private String mime;
        private String att_path;
        private String thum_path;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAtt_name() {
            return att_name;
        }

        public void setAtt_name(String att_name) {
            this.att_name = att_name;
        }

        public String getAtt_type() {
            return att_type;
        }

        public void setAtt_type(String att_type) {
            this.att_type = att_type;
        }

        public long getAtt_time() {
            return att_time;
        }

        public void setAtt_time(long att_time) {
            this.att_time = att_time;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }

        public String getAtt_path() {
            return att_path;
        }

        public void setAtt_path(String att_path) {
            this.att_path = att_path;
        }

        public String getThum_path() {
            return thum_path;
        }

        public void setThum_path(String thum_path) {
            this.thum_path = thum_path;
        }
    }
}
