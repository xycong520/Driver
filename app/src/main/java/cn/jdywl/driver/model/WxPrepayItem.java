package cn.jdywl.driver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wuwantao on 15/7/6.
 */
public class WxPrepayItem {

    /**
     * err_code_des : SUCCESS
     * prepay_id : wx201507062222134dfc3e04060504966669
     * return_msg : OK
     * appid : wx5482596dd4036e17
     * order_id : O10
     * nonce_str : iJEShIsHBQqD8UWS
     * device_info : android-18626468980
     * trade_type : APP
     * result_code : SUCCESS
     * sign : 448C6DA929C8A54DC63B2FC3EFDB6182
     * timestamp : 1436192533
     * id : 21
     * mch_id : 1236486302
     * err_code : NOAUTH
     * package : Sign=WXPay
     * return_code : SUCCESS
     */
    private String err_code_des;
    private String prepay_id;
    private String return_msg;
    private String appid;
    private String order_id;
    private String nonce_str;
    private String device_info;
    private String trade_type;
    private String result_code;
    private String sign;
    private String timestamp;
    private int id;
    private String mch_id;
    private String err_code;
    //package为保留关键字
    @SerializedName("package")
    private String packag;
    private String return_code;

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public void setPackag(String packag) {
        this.packag = packag;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public String getAppid() {
        return appid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getDevice_info() {
        return device_info;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getSign() {
        return sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getErr_code() {
        return err_code;
    }

    public String getPackag() {
        return packag;
    }

    public String getReturn_code() {
        return return_code;
    }
}
