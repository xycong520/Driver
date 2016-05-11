package cn.jdywl.driver.model;

import com.google.gson.annotations.Expose;

/**
 * Created by xycong on 2016/5/8.
 */
public class StageOrderItem {
    @Expose
    int id;
    @Expose
    int carowner_id;
    @Expose
    int sdriver_id;
    @Expose
    int shipping_id;
    @Expose
    String order_no;
    @Expose
    int status;
    @Expose
    String origin;
    @Expose
    String destination;
    @Expose
    String sendtime;
    @Expose
    int ifIns;
    @Expose
    int car_price;
    @Expose
    int vin_car_price;
    @Expose
    int cypher;
    @Expose
    int receiver_cypher;
    @Expose
    int charge;
    @Expose
    int insurance;
    @Expose
    int actual_insurance;
    @Expose
    double from_longitude;
    @Expose
    double from_latitude;
    @Expose
    double to_latitude;
    @Expose
    double to_longitude;
    @Expose
    String from_address;
    @Expose
    String receiver_name;
    @Expose
    String to_address;
    @Expose
    String receiver_phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarowner_id() {
        return carowner_id;
    }

    public void setCarowner_id(int carowner_id) {
        this.carowner_id = carowner_id;
    }

    public int getSdriver_id() {
        return sdriver_id;
    }

    public void setSdriver_id(int sdriver_id) {
        this.sdriver_id = sdriver_id;
    }

    public int getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(int shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public int getIfIns() {
        return ifIns;
    }

    public void setIfIns(int ifIns) {
        this.ifIns = ifIns;
    }

    public int getCar_price() {
        return car_price;
    }

    public void setCar_price(int car_price) {
        this.car_price = car_price;
    }

    public int getVin_car_price() {
        return vin_car_price;
    }

    public void setVin_car_price(int vin_car_price) {
        this.vin_car_price = vin_car_price;
    }

    public int getCypher() {
        return cypher;
    }

    public void setCypher(int cypher) {
        this.cypher = cypher;
    }

    public int getReceiver_cypher() {
        return receiver_cypher;
    }

    public void setReceiver_cypher(int receiver_cypher) {
        this.receiver_cypher = receiver_cypher;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getActual_insurance() {
        return actual_insurance;
    }

    public void setActual_insurance(int actual_insurance) {
        this.actual_insurance = actual_insurance;
    }

    public double getFrom_longitude() {
        return from_longitude;
    }

    public void setFrom_longitude(double from_longitude) {
        this.from_longitude = from_longitude;
    }

    public double getFrom_latitude() {
        return from_latitude;
    }

    public void setFrom_latitude(double from_latitude) {
        this.from_latitude = from_latitude;
    }

    public double getTo_latitude() {
        return to_latitude;
    }

    public void setTo_latitude(double to_latitude) {
        this.to_latitude = to_latitude;
    }

    public double getTo_longitude() {
        return to_longitude;
    }

    public void setTo_longitude(double to_longitude) {
        this.to_longitude = to_longitude;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }
}
