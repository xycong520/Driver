package cn.jdywl.driver.model;

/**
 * Created by wuwantao on 15/5/9.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionItem {

    @Expose
    private int id;
    @Expose
    private int platform;
    @SerializedName("app_version")
    @Expose
    private int versionCode;
    @Expose
    private String versionName;
    @Expose
    private String desc;

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
     * @return The platform
     */
    public int getPlatform() {
        return platform;
    }

    /**
     * @param platform The id
     */
    public void setPlatform(int platform) {
        this.platform = platform;
    }

    /**
     *
     * @return
     * The versionCode
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     *
     * @param versionCode
     * The app_version
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     *
     * @return
     * The versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     *
     * @param versionName
     * The versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     *
     * @return
     * The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @param desc
     * The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}