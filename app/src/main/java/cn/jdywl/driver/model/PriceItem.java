package cn.jdywl.driver.model;

/**
 * Created by wuwantao on 15-1-24.
 */


import com.google.gson.annotations.SerializedName;

public class PriceItem {


    /**
     * totalBill : 1
     * marketPrice : 1
     * deposit : 1
     * driverDeposit: 1
     * insurance : 1
     * srvFee: 1
     */

    @SerializedName("totalBill")
    private int totalBill;
    @SerializedName("marketPrice")
    private int marketPrice;
    @SerializedName("deposit")
    private int deposit;
    @SerializedName("driverDeposit")
    private int driverDeposit;
    @SerializedName("insurance")
    private int insurance;
    @SerializedName("charge")
    private float charge;
    @SerializedName("srvFee")
    private int srvFee;

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public void setDriverDeposit(int driverDeposit) {
        this.driverDeposit = driverDeposit;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public void setSrvFee(int srvFee) {
        this.srvFee = srvFee;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public int getDeposit() {
        return deposit;
    }

    public int getDriverDeposit() {
        return driverDeposit;
    }

    public int getMarketPrice() {
        return marketPrice;
    }

    public int getInsurance() {
        return insurance;
    }

    public int getSrvFee() {
        return srvFee;
    }
}