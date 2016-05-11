package cn.jdywl.driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wuwantao on 15/10/23.
 */
public class StatsItem {

    /**
     * sumcars : 1
     * sumorders : 1
     */

    @SerializedName("sumcars")
    private int sumcars;
    @SerializedName("sumorders")
    private int sumorders;

    public void setSumcars(int sumcars) {
        this.sumcars = sumcars;
    }

    public void setSumorders(int sumorders) {
        this.sumorders = sumorders;
    }

    public int getSumcars() {
        return sumcars;
    }

    public int getSumorders() {
        return sumorders;
    }
}
