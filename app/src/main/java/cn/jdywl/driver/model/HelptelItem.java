
package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelptelItem {

    @Expose
    private int id;
    @Expose
    private String province;
    @Expose
    private String city;
    @SerializedName("help_tel1")
    @Expose
    private String helpTel1;
    @SerializedName("help_tel2")
    @Expose
    private String helpTel2;
    @SerializedName("trailer_tel")
    @Expose
    private String trailerTel;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The helpTel1
     */
    public String getHelpTel1() {
        return helpTel1;
    }

    /**
     * @param helpTel1 The help_tel1
     */
    public void setHelpTel1(String helpTel1) {
        this.helpTel1 = helpTel1;
    }

    /**
     * @return The helpTel2
     */
    public String getHelpTel2() {
        return helpTel2;
    }

    /**
     * @param helpTel2 The help_tel2
     */
    public void setHelpTel2(String helpTel2) {
        this.helpTel2 = helpTel2;
    }

    /**
     * @return The trailerTel
     */
    public String getTrailerTel() {
        return trailerTel;
    }

    /**
     * @param trailerTel The trailer_tel
     */
    public void setTrailerTel(String trailerTel) {
        this.trailerTel = trailerTel;
    }

}
