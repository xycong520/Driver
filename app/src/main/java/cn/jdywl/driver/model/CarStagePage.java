
package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CarStagePage {

    @Expose
    private int total;
    @SerializedName("per_page")
    @Expose
    private int perPage;
    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("last_page")
    @Expose
    private int lastPage;
    @Expose
    private int from;
    @Expose
    private int to;
    @Expose
    private List<Stage> data = new ArrayList<Stage>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<Stage> getData() {
        return data;
    }

    public void setData(List<Stage> data) {
        this.data = data;
    }

}
