package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/6.
 */
public class CarStage {
    @Expose
    private int id;
    @SerializedName("master")
    @Expose
    private String master;
    @SerializedName("operation_center")
    @Expose
    private String operation_center;
    @SerializedName("station")
    @Expose
    private String station;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("longitude")
    @Expose
    private float longitude;
    @SerializedName("latitude")
    @Expose
    private float latitude;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("province")
    @Expose
    private String province;

    public int getId() {
        return id;
    }

    public String getOperation_center() {
        return operation_center;
    }

    public String getMaster() {
        return master;
    }

    public String getStation() {
        return station;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }
}
