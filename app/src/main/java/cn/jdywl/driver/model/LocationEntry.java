package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2015/3/22.
 */
public class LocationEntry {
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @Expose
    private String longitude;
    @Expose
    private String latitude;
    @Expose
    private String position;
    @Expose
    private String gathertime;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getGathertime() {
        return gathertime;
    }

    public String getPosition() {
        return position;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setGathertime(String gathertime) {
        this.gathertime = gathertime;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
