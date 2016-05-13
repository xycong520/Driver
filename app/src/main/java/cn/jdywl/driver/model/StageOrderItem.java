package cn.jdywl.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by xycong on 2016/5/8.
 */
public class StageOrderItem implements Parcelable {

    @Expose
    int id;
    @SerializedName("carowner_id")
    @Expose
    int carowner_id;
    @SerializedName("sdriver_id")
    @Expose
    int sdriver_id;
    @SerializedName("shipping_id")
    @Expose
    int shipping_id;
    @SerializedName("order_no")
    @Expose
    String order_no;
    @SerializedName("status")
    @Expose
    int status;
    @SerializedName("origin")
    @Expose
    String origin;
    @SerializedName("destination")
    @Expose
    String destination;
    @SerializedName("sendtime")
    @Expose
    String sendtime;
    @SerializedName("ifIns")
    @Expose
    int ifIns;
    @SerializedName("car_price")
    @Expose
    int car_price;
    @SerializedName("vin_car_price")
    @Expose
    int vin_car_price;
    @SerializedName("cypher")
    @Expose
    int cypher;
    @SerializedName("receiver_cypher")
    @Expose
    int receiver_cypher;
    @SerializedName("charge")
    @Expose
    int charge;
    @SerializedName("insurance")
    @Expose
    int insurance;
    @SerializedName("actual_insurance")
    @Expose
    int actual_insurance;
    @SerializedName("from_longitude")
    @Expose
    double from_longitude;
    @SerializedName("from_latitude")
    @Expose
    double from_latitude;
    @SerializedName("to_latitude")
    @Expose
    double to_latitude;
    @SerializedName("to_longitude")
    @Expose
    double to_longitude;
    @SerializedName("from_address")
    @Expose
    String from_address;
    @SerializedName("receiver_name")
    @Expose
    String receiver_name;
    @SerializedName("to_address")
    @Expose
    String to_address;
    @SerializedName("receiver_phone")
    @Expose
    String receiver_phone;

    protected StageOrderItem(Parcel in) {
        id = in.readInt();
        carowner_id = in.readInt();
        sdriver_id = in.readInt();
        shipping_id = in.readInt();
        order_no = in.readString();
        status = in.readInt();
        origin = in.readString();
        destination = in.readString();
        sendtime = in.readString();
        ifIns = in.readInt();
        car_price = in.readInt();
        vin_car_price = in.readInt();
        cypher = in.readInt();
        receiver_cypher = in.readInt();
        charge = in.readInt();
        insurance = in.readInt();
        actual_insurance = in.readInt();
        from_longitude = in.readDouble();
        from_latitude = in.readDouble();
        to_latitude = in.readDouble();
        to_longitude = in.readDouble();
        from_address = in.readString();
        receiver_name = in.readString();
        to_address = in.readString();
        receiver_phone = in.readString();
    }

    public static final Creator<StageOrderItem> CREATOR = new Creator<StageOrderItem>() {
        @Override
        public StageOrderItem createFromParcel(Parcel in) {
            return new StageOrderItem(in);
        }

        @Override
        public StageOrderItem[] newArray(int size) {
            return new StageOrderItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(carowner_id);
        dest.writeInt(sdriver_id);
        dest.writeInt(shipping_id);
        dest.writeString(order_no);
        dest.writeInt(status);
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeString(sendtime);
        dest.writeInt(ifIns);
        dest.writeInt(car_price);
        dest.writeInt(vin_car_price);
        dest.writeInt(cypher);
        dest.writeInt(receiver_cypher);
        dest.writeInt(charge);
        dest.writeInt(insurance);
        dest.writeInt(actual_insurance);
        dest.writeDouble(from_longitude);
        dest.writeDouble(from_latitude);
        dest.writeDouble(to_latitude);
        dest.writeDouble(to_longitude);
        dest.writeString(from_address);
        dest.writeString(receiver_name);
        dest.writeString(to_address);
        dest.writeString(receiver_phone);
    }
}
