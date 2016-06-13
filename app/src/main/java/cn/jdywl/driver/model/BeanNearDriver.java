
package cn.jdywl.driver.model;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeanNearDriver {

    int id;
    String name;
    String phone;

    List<BeanGPS> gps;

    public BeanNearDriver(JSONObject jsonObject) throws JSONException {
        id = jsonObject.optInt("id");
        name = jsonObject.optString("name");
        phone = jsonObject.optString("phone");
        JSONArray array = jsonObject.getJSONArray("gps");
        gps = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            gps.add(new Gson().fromJson(array.getJSONObject(i).toString(), BeanGPS.class));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BeanGPS> getGps() {
        return gps;
    }

    public void setGps(List<BeanGPS> gps) {
        this.gps = gps;
    }
}
