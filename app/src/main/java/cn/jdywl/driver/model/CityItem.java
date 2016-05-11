
package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;


public class CityItem {

    @Expose
    private String origin;
    @Expose
    private String destination;
    @Expose
    private String letter;
    @Expose
    private String abb;
    @Expose
    private String pinyin;

    /**
     * 
     * @return
     *     The origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 
     * @param origin
     *     The origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     *
     * @return
     *     The destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     *
     * @param destination
     *     The origin
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * 
     * @return
     *     The letter
     */
    public String getLetter() {
        return letter;
    }

    /**
     * 
     * @param letter
     *     The letter
     */
    public void setLetter(String letter) {
        this.letter = letter;
    }

    /**
     * 
     * @return
     *     The abb
     */
    public String getAbb() {
        return abb;
    }

    /**
     * 
     * @param abb
     *     The abb
     */
    public void setAbb(String abb) {
        this.abb = abb;
    }

    /**
     * 
     * @return
     *     The pinyin
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * 
     * @param pinyin
     *     The pinyin
     */
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

}
