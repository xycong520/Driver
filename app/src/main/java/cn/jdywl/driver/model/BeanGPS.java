package cn.jdywl.driver.model;

/**
 * Created by xycong on 2016/6/13.
 */
public class BeanGPS {
    int id;
    int user_id;
    double latitude;
    double longitude;
    String position;
    String gathertime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGathertime() {
        return gathertime;
    }

    public void setGathertime(String gathertime) {
        this.gathertime = gathertime;
    }
}
