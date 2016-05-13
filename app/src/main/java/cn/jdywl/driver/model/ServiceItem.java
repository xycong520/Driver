package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ServiceItem {
    @Expose
    String name;
    @Expose
    String imgeurl;
    @Expose
    Icon icon;
    public String getName() {
        return name;
    }

    public String getImgeurl() {
        return imgeurl;
    }

    public Icon getIcon() {
        return icon;
    }

    public class Icon{
        @Expose
        int id;
        @Expose
        String url;
        @Expose
        String thumbnail;

        public int getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }
}
