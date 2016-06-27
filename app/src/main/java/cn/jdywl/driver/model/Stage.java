package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 */
public class Stage implements Serializable {
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
    @SerializedName("position")
    @Expose
    private String position;
    List<BeanService> services;

    public List<BeanService> getServices() {
        return services;
    }

    public String getPosition() {
        return position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public void setOperation_center(String operation_center) {
        this.operation_center = operation_center;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

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
