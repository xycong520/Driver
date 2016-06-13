
package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class StageOrderPage {

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
    private List<StageOrderItem> data = new ArrayList<StageOrderItem>();

    /**
     * @return The total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    public StageOrderPage withTotal(int total) {
        this.total = total;
        return this;
    }

    /**
     * @return The perPage
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     * @param perPage The per_page
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public StageOrderPage withPerPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    /**
     * @return The currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage The current_page
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public StageOrderPage withCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    /**
     * @return The lastPage
     */
    public int getLastPage() {
        return lastPage;
    }

    /**
     * @param lastPage The last_page
     */
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public StageOrderPage withLastPage(int lastPage) {
        this.lastPage = lastPage;
        return this;
    }

    /**
     * @return The from
     */
    public int getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(int from) {
        this.from = from;
    }

    public StageOrderPage withFrom(int from) {
        this.from = from;
        return this;
    }

    /**
     * @return The to
     */
    public int getTo() {
        return to;
    }

    /**
     * @param to The to
     */
    public void setTo(int to) {
        this.to = to;
    }

    public StageOrderPage withTo(int to) {
        this.to = to;
        return this;
    }

    /**
     * @return The data
     */
    public List<StageOrderItem> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<StageOrderItem> data) {
        this.data = data;
    }

    public StageOrderPage withData(List<StageOrderItem> data) {
        this.data = data;
        return this;
    }

}
