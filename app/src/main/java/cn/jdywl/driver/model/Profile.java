package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wuwantao on 15/5/10.
 */
public class Profile {
    @Expose
    private int id;
    @SerializedName("gps_id")
    @Expose
    private int gpsId;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String email;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The gpsId
     */
    public int getGpsId() {
        return gpsId;
    }

    /**
     *
     * @param gpsId
     * The gps_id
     */
    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @param phone
     * The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
