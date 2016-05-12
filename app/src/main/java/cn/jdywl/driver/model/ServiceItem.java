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

    public String getName() {
        return name;
    }

    public String getImgeurl() {
        return imgeurl;
    }
}
