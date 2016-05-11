
package cn.jdywl.driver.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityPage {

    @SerializedName("prices")
    @Expose
    private List<CityItem> cityItems = new ArrayList<CityItem>();

    /**
     * 
     * @return
     *     The cityItems
     */
    public List<CityItem> getCityItems() {
        return cityItems;
    }

    /**
     * 
     * @param cityItems
     *     The cityItems
     */
    public void setCityItems(List<CityItem> cityItems) {
        this.cityItems = cityItems;
    }

}
