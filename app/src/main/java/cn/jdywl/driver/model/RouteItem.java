
package cn.jdywl.driver.model;

import com.google.gson.annotations.SerializedName;

public class RouteItem {


    /**
     * id : 19831
     * origin : 南京
     * destination : 武汉
     * car_price : 1000
     * suv_price : 1100
     * bigsuv_price : 1200
     * platformfee : 700
     * addtionalSrv : 1
     * note : 无
     */

    @SerializedName("id")
    private int id;
    @SerializedName("origin")
    private String origin;
    @SerializedName("destination")
    private String destination;
    @SerializedName("car_price")
    private int carPrice;
    @SerializedName("suv_price")
    private int suvPrice;
    @SerializedName("bigsuv_price")
    private int bigsuvPrice;
    @SerializedName("platformfee")
    private int platformfee;
    @SerializedName("addtionalSrv")
    private int addtionalSrv;
    @SerializedName("note")
    private String note;
    @SerializedName("express_price")
    private int express_price;

    public int getExpress_price() {
        return express_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(int carPrice) {
        this.carPrice = carPrice;
    }

    public int getSuvPrice() {
        return suvPrice;
    }

    public void setSuvPrice(int suvPrice) {
        this.suvPrice = suvPrice;
    }

    public int getBigsuvPrice() {
        return bigsuvPrice;
    }

    public void setBigsuvPrice(int bigsuvPrice) {
        this.bigsuvPrice = bigsuvPrice;
    }

    public int getPlatformfee() {
        return platformfee;
    }

    public void setPlatformfee(int platformfee) {
        this.platformfee = platformfee;
    }

    public int getAddtionalSrv() {
        return addtionalSrv;
    }

    public void setAddtionalSrv(int addtionalSrv) {
        this.addtionalSrv = addtionalSrv;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
