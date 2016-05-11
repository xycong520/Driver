
package cn.jdywl.driver.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItem implements Parcelable {

    @Expose
    private int id;
    @SerializedName("carowner_id")
    @Expose
    private int carownerId;
    @SerializedName("driver_id")
    @Expose
    private int driverId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @Expose
    private int bill;
    @SerializedName("market_price")
    @Expose
    private int marketPrice;
    @SerializedName("expectation_price")
    @Expose
    private int expectationPrice;
    @Expose
    private int deposit;
    @Expose
    private int driverDeposit;
    @Expose
    private int driverBill;
    @Expose
    private int noNeedIns;
    @Expose
    private int status;
    @SerializedName("pay_status")
    @Expose
    private int payStatus;
    @Expose
    private int payNum;
    @Expose
    private long expireTimeStamp;
    @Expose
    private String origin;
    @Expose
    private String destination;
    @Expose
    private String sendtime;
    @Expose
    private int oldCar;
    @Expose
    private int brand;
    @SerializedName("car_num")
    @Expose
    private int carNum;
    @SerializedName("suv_num")
    @Expose
    private int suvNum;
    @SerializedName("bigsuv_num")
    @Expose
    private int bigsuvNum;
    @Expose
    private int totalCarPrice;
    @Expose
    private String province;
    @Expose
    private String city;
    @Expose
    private String district;
    @Expose
    private String street;
    @Expose
    private String location;
    @SerializedName("carowner_name")
    @Expose
    private String carownerName;
    @SerializedName("carowner_phone")
    @Expose
    private String carownerPhone;
    @Expose
    private String cypher;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_phone")
    @Expose
    private String driverPhone;
    @Expose
    private String driverRealPhone;
    @Expose
    private String plateNumber;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("receiver_cypher")
    @Expose
    private String receiverCypher;
    @Expose
    private String vin1;
    @Expose
    private String vin2;
    @Expose
    private String vin3;
    @Expose
    private String vin4;
    @Expose
    private String vin5;
    @Expose
    private String vin6;
    @Expose
    private String vin7;
    @Expose
    private String vin8;
    @Expose
    private String vin9;
    @Expose
    private String biddingPhone1;
    @Expose
    private String biddingPrice1;
    @Expose
    private String biddingPhone2;
    @Expose
    private String biddingPrice2;
    @Expose
    private String model;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    /**
     * shipping_id : 0
     * source : 0
     * insurance : 50
     * payRole : 0
     * addtionalSrv : 2
     * incTotalPrice : 0
     * creditCompany_id : 1
     * quota : 14000
     * creditMsg : 测试
     * receiverPhoto_id : 108
     * accountingType :
     * accountingBill : 0
     * payment :
     * note :
     * credit_company : {"id":1,"company":"北京搬运工汽车科技有限公司","contacter":"郑海涛","phone":"13241101109","bank":"中国建设银行北京天通苑支行","accountName":"北京搬运工汽车科技有限公司","accountNo":"11001029700059000644","introduce":"北京搬运工汽车科技有限公司，注册资本金1000万元人民币，是一家p2p金融与汽车供应链相结合的创新型公司。主营汽车金融与汽车科技、技术开发、投资管理等业务。本公司于2015年获得\u201c火球理财\u201d创始人孟庆彪的大规模投资，资金雄厚。截止到目前，公司拥有各类专业员工103名，其中融资类员工30名，专户管理的汽车物流垫款资金规模达1亿元。公司总部坐落于北京市昌平区汽车交易市场，办公面积800余平方米，并在上海、广东、重庆、天津等地设有分公司。秉承诚信至上，服务第一的经营理念，为各经销商、汽贸公司提供垫款发车、运单质押借款、车辆质押贷款等多项优质金融服务。 ","interest":"利息：10万以下:70元/天;\n10万－15万:100元/天；\n15万－20万:140元/天；\n20万－25万:170元/天；\n25万－30万:210元/天；\n30万－40万:280元/天；\n40万－50万:350元/天；\n50万－70万:490元/天；\n70万－100万:700元/天。"}
     * receiver_photo : {"url":"http://192.168.1.102/laravel/public/images/9/yihu.jpg","thumbnail":"http://192.168.1.102/laravel/public/images/9/thumbnail_yihu.jpg"}
     */

    @SerializedName("shipping_id")
    private int shippingId;
    @SerializedName("source")
    private int source;
    @SerializedName("insurance")
    private int insurance;
    @SerializedName("payRole")
    private int payRole;
    @SerializedName("addtionalSrv")
    private int addtionalSrv;
    @SerializedName("incTotalPrice")
    private int incTotalPrice;
    @SerializedName("creditCompany_id")
    private int creditCompanyId;
    @SerializedName("quota")
    private int quota;
    @SerializedName("creditMsg")
    private String creditMsg;
    @SerializedName("receiverPhoto_id")
    private int receiverPhotoId;
    @SerializedName("accountingType")
    private String accountingType;
    @SerializedName("accountingBill")
    private int accountingBill;
    @SerializedName("payment")
    private String payment;
    @SerializedName("note")
    private String note;
    /**
     * id : 1
     * company : 北京搬运工汽车科技有限公司
     * contacter : 郑海涛
     * phone : 13241101109
     * bank : 中国建设银行北京天通苑支行
     * accountName : 北京搬运工汽车科技有限公司
     * accountNo : 11001029700059000644
     * introduce : 北京搬运工汽车科技有限公司，注册资本金1000万元人民币，是一家p2p金融与汽车供应链相结合的创新型公司。主营汽车金融与汽车科技、技术开发、投资管理等业务。本公司于2015年获得“火球理财”创始人孟庆彪的大规模投资，资金雄厚。截止到目前，公司拥有各类专业员工103名，其中融资类员工30名，专户管理的汽车物流垫款资金规模达1亿元。公司总部坐落于北京市昌平区汽车交易市场，办公面积800余平方米，并在上海、广东、重庆、天津等地设有分公司。秉承诚信至上，服务第一的经营理念，为各经销商、汽贸公司提供垫款发车、运单质押借款、车辆质押贷款等多项优质金融服务。
     * interest : 利息：10万以下:70元/天;
     10万－15万:100元/天；
     15万－20万:140元/天；
     20万－25万:170元/天；
     25万－30万:210元/天；
     30万－40万:280元/天；
     40万－50万:350元/天；
     50万－70万:490元/天；
     70万－100万:700元/天。
     */

    @SerializedName("credit_company")
    private CreditCompanyItem creditCompany;
    /**
     * url : http://192.168.1.102/laravel/public/images/9/yihu.jpg
     * thumbnail : http://192.168.1.102/laravel/public/images/9/thumbnail_yihu.jpg
     */

    @SerializedName("receiver_photo")
    private ReceiverPhotoEntity receiverPhoto;

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    public OrderItem withId(int id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The carownerId
     */
    public int getCarownerId() {
        return carownerId;
    }

    /**
     * 
     * @param carownerId
     *     The carowner_id
     */
    public void setCarownerId(int carownerId) {
        this.carownerId = carownerId;
    }

    public OrderItem withCarownerId(int carownerId) {
        this.carownerId = carownerId;
        return this;
    }

    /**
     * 
     * @return
     *     The driverId
     */
    public int getDriverId() {
        return driverId;
    }

    /**
     * 
     * @param driverId
     *     The driver_id
     */
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public OrderItem withDriverId(int driverId) {
        this.driverId = driverId;
        return this;
    }

    /**
     * 
     * @return
     *     The orderNo
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 
     * @param orderNo
     *     The order_no
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderItem withOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    /**
     * 
     * @return
     *     The bill
     */
    public int getBill() {
        return bill;
    }

    /**
     * 
     * @param bill
     *     The bill
     */
    public void setBill(int bill) {
        this.bill = bill;
    }

    public OrderItem withBill(int bill) {
        this.bill = bill;
        return this;
    }

    /**
     * 
     * @return
     *     The marketPrice
     */
    public int getMarketPrice() {
        return marketPrice;
    }

    /**
     * 
     * @param marketPrice
     *     The market_price
     */
    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }

    public OrderItem withMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
        return this;
    }

    /**
     * 
     * @return
     *     The expectationPrice
     */
    public int getExpectationPrice() {
        return expectationPrice;
    }

    /**
     * 
     * @param expectationPrice
     *     The expectation_price
     */
    public void setExpectationPrice(int expectationPrice) {
        this.expectationPrice = expectationPrice;
    }

    public OrderItem withExpectationPrice(int expectationPrice) {
        this.expectationPrice = expectationPrice;
        return this;
    }

    /**
     * 
     * @return
     *     The deposit
     */
    public int getDeposit() {
        return deposit;
    }

    /**
     * 
     * @param deposit
     *     The deposit
     */
    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public OrderItem withDeposit(int deposit) {
        this.deposit = deposit;
        return this;
    }

    /**
     * 
     * @return
     *     The driverDeposit
     */
    public int getDriverDeposit() {
        return driverDeposit;
    }

    /**
     * 
     * @param driverDeposit
     *     The driverDeposit
     */
    public void setDriverDeposit(int driverDeposit) {
        this.driverDeposit = driverDeposit;
    }

    public OrderItem withDriverDeposit(int driverDeposit) {
        this.driverDeposit = driverDeposit;
        return this;
    }

    /**
     * 
     * @return
     *     The driverBill
     */
    public int getDriverBill() {
        return driverBill;
    }

    /**
     * 
     * @param driverBill
     *     The driverBill
     */
    public void setDriverBill(int driverBill) {
        this.driverBill = driverBill;
    }

    public OrderItem withDriverBill(int driverBill) {
        this.driverBill = driverBill;
        return this;
    }

    /**
     *
     * @return
     *     The noNeedIns
     */
    public int getNoNeedIns() {
        return noNeedIns;
    }

    /**
     *
     * @param noNeedIns
     *     The noNeedIns
     */
    public void setNoNeedIns(int noNeedIns) {
        this.noNeedIns = noNeedIns;
    }

    public OrderItem withNoNeedIns(int noNeedIns) {
        this.noNeedIns = noNeedIns;
        return this;
    }

    /**
     * 
     * @return
     *     The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public OrderItem withStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * @return
     *     The payStatus
     */
    public int getPayStatus() {
        return payStatus;
    }

    /**
     * 
     * @param payStatus
     *     The pay_status
     */
    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public OrderItem withPayStatus(int payStatus) {
        this.payStatus = payStatus;
        return this;
    }


    /**
     *
     * @return
     *     The payNum
     */
    public int getPayNum() {
        return payNum;
    }

    /**
     *
     * @param payNum
     *     The payNum
     */
    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    public OrderItem withPayNum(int payNum) {
        this.payNum = payNum;
        return this;
    }

    /**
     *
     * @return
     *     The expireTimeStamp
     */
    public long getExpireTimeStamp() {
        return expireTimeStamp;
    }

    /**
     *
     * @param expireTimeStamp
     *     The expireTimeStamp
     */
    public void setExpireTimeStamp(long expireTimeStamp) {
        this.expireTimeStamp = expireTimeStamp;
    }

    public OrderItem withExpireTimeStamp(long expireTimeStamp) {
        this.expireTimeStamp = expireTimeStamp;
        return this;
    }

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

    public OrderItem withOrigin(String origin) {
        this.origin = origin;
        return this;
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
     *     The destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public OrderItem withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    /**
     * 
     * @return
     *     The sendtime
     */
    public String getSendtime() {
        return sendtime;
    }

    /**
     * 
     * @param sendtime
     *     The sendtime
     */
    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public OrderItem withSendtime(String sendtime) {
        this.sendtime = sendtime;
        return this;
    }

    /**
     *
     * @return
     *     The oldCar
     */
    public int getOldCar() {
        return oldCar;
    }

    /**
     * @param oldCar The oldCar
     */
    public void setOldCar(int oldCar) {
        this.oldCar = oldCar;
    }

    public OrderItem withOldCar(int oldCar) {
        this.oldCar = oldCar;
        return this;
    }

    /**
     * 
     * @return
     *     The brand
     */
    public int getBrand() {
        return brand;
    }

    /**
     * 
     * @param brand
     *     The brand
     */
    public void setBrand(int brand) {
        this.brand = brand;
    }

    public OrderItem withBrand(int brand) {
        this.brand = brand;
        return this;
    }

    /**
     * 
     * @return
     *     The carNum
     */
    public int getCarNum() {
        return carNum;
    }

    /**
     * 
     * @param carNum
     *     The car_num
     */
    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public OrderItem withCarNum(int carNum) {
        this.carNum = carNum;
        return this;
    }

    /**
     * 
     * @return
     *     The suvNum
     */
    public int getSuvNum() {
        return suvNum;
    }

    /**
     * 
     * @param suvNum
     *     The suv_num
     */
    public void setSuvNum(int suvNum) {
        this.suvNum = suvNum;
    }

    public OrderItem withSuvNum(int suvNum) {
        this.suvNum = suvNum;
        return this;
    }

    /**
     *
     * @return
     *     The bigsuvNum
     */
    public int getBigsuvNum() {
        return bigsuvNum;
    }

    /**
     *
     * @param bigsuvNum
     *     The bigsuv_num
     */
    public void setBigsuvNum(int bigsuvNum) {
        this.bigsuvNum = bigsuvNum;
    }

    public OrderItem withBigsuvNum(int bigsuvNum) {
        this.bigsuvNum = bigsuvNum;
        return this;
    }

    /**
     *
     * @return
     *     The totalCarPrice
     */
    public int getTotalCarPrice() {
        return totalCarPrice;
    }

    /**
     *
     * @param totalCarPrice
     *     The totalCarPrice
     */
    public void setTotalCarPrice(int totalCarPrice) {
        this.totalCarPrice = totalCarPrice;
    }

    public OrderItem withTotalCarPrice(int totalCarPrice) {
        this.totalCarPrice = totalCarPrice;
        return this;
    }

    /**
     * 
     * @return
     *     The province
     */
    public String getProvince() {
        return province;
    }

    /**
     * 
     * @param province
     *     The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    public OrderItem withProvince(String province) {
        this.province = province;
        return this;
    }

    /**
     * 
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    public OrderItem withCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * 
     * @return
     *     The district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * 
     * @param district
     *     The district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    public OrderItem withDistrict(String district) {
        this.district = district;
        return this;
    }

    /**
     * 
     * @return
     *     The street
     */
    public String getStreet() {
        return street;
    }

    /**
     * 
     * @param street
     *     The street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    public OrderItem withStreet(String street) {
        this.street = street;
        return this;
    }

    /**
     * 在途位置
     * @return
     *     The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     *     The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public OrderItem withLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * 
     * @return
     *     The carownerName
     */
    public String getCarownerName() {
        return carownerName;
    }

    /**
     * 
     * @param carownerName
     *     The carowner_name
     */
    public void setCarownerName(String carownerName) {
        this.carownerName = carownerName;
    }

    public OrderItem withCarownerName(String carownerName) {
        this.carownerName = carownerName;
        return this;
    }

    /**
     * 
     * @return
     *     The carownerPhone
     */
    public String getCarownerPhone() {
        return carownerPhone;
    }

    /**
     * 
     * @param carownerPhone
     *     The carowner_phone
     */
    public void setCarownerPhone(String carownerPhone) {
        this.carownerPhone = carownerPhone;
    }

    public OrderItem withCarownerPhone(String carownerPhone) {
        this.carownerPhone = carownerPhone;
        return this;
    }

    /**
     * 
     * @return
     *     The cypher
     */
    public String getCypher() {
        return cypher;
    }

    /**
     * 
     * @param cypher
     *     The cypher
     */
    public void setCypher(String cypher) {
        this.cypher = cypher;
    }

    public OrderItem withCypher(String cypher) {
        this.cypher = cypher;
        return this;
    }

    /**
     * 
     * @return
     *     The driverName
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * 
     * @param driverName
     *     The driver_name
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public OrderItem withDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    /**
     * 
     * @return
     *     The driverPhone
     */
    public String getDriverPhone() {
        return driverPhone;
    }

    /**
     * 
     * @param driverPhone
     *     The driver_phone
     */
    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public OrderItem withDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
        return this;
    }

    /**
     * 
     * @return
     *     The driverRealPhone
     */
    public String getDriverRealPhone() {
        return driverRealPhone;
    }

    /**
     * 
     * @param driverRealPhone
     *     The driverRealPhone
     */
    public void setDriverRealPhone(String driverRealPhone) {
        this.driverRealPhone = driverRealPhone;
    }

    public OrderItem withDriverRealPhone(String driverRealPhone) {
        this.driverRealPhone = driverRealPhone;
        return this;
    }

    /**
     * 
     * @return
     *     The plateNumber
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * 
     * @param plateNumber
     *     The plateNumber
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public OrderItem withPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
        return this;
    }

    /**
     * 
     * @return
     *     The receiverName
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * 
     * @param receiverName
     *     The receiver_name
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public OrderItem withReceiverName(String receiverName) {
        this.receiverName = receiverName;
        return this;
    }

    /**
     * 
     * @return
     *     The receiverPhone
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * 
     * @param receiverPhone
     *     The receiver_phone
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public OrderItem withReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
        return this;
    }

    /**
     * 
     * @return
     *     The receiverCypher
     */
    public String getReceiverCypher() {
        return receiverCypher;
    }

    /**
     * 
     * @param receiverCypher
     *     The receiver_cypher
     */
    public void setReceiverCypher(String receiverCypher) {
        this.receiverCypher = receiverCypher;
    }

    public OrderItem withReceiverCypher(String receiverCypher) {
        this.receiverCypher = receiverCypher;
        return this;
    }

    /**
     * 
     * @return
     *     The vin1
     */
    public String getVin1() {
        return vin1;
    }

    /**
     * 
     * @param vin1
     *     The vin1
     */
    public void setVin1(String vin1) {
        this.vin1 = vin1;
    }

    public OrderItem withVin1(String vin1) {
        this.vin1 = vin1;
        return this;
    }

    /**
     * 
     * @return
     *     The vin2
     */
    public String getVin2() {
        return vin2;
    }

    /**
     * 
     * @param vin2
     *     The vin2
     */
    public void setVin2(String vin2) {
        this.vin2 = vin2;
    }

    public OrderItem withVin2(String vin2) {
        this.vin2 = vin2;
        return this;
    }

    /**
     * 
     * @return
     *     The vin3
     */
    public String getVin3() {
        return vin3;
    }

    /**
     * 
     * @param vin3
     *     The vin3
     */
    public void setVin3(String vin3) {
        this.vin3 = vin3;
    }

    public OrderItem withVin3(String vin3) {
        this.vin3 = vin3;
        return this;
    }

    /**
     * 
     * @return
     *     The vin4
     */
    public String getVin4() {
        return vin4;
    }

    /**
     * 
     * @param vin4
     *     The vin4
     */
    public void setVin4(String vin4) {
        this.vin4 = vin4;
    }

    public OrderItem withVin4(String vin4) {
        this.vin4 = vin4;
        return this;
    }

    /**
     * 
     * @return
     *     The vin5
     */
    public String getVin5() {
        return vin5;
    }

    /**
     * 
     * @param vin5
     *     The vin5
     */
    public void setVin5(String vin5) {
        this.vin5 = vin5;
    }

    public OrderItem withVin5(String vin5) {
        this.vin5 = vin5;
        return this;
    }

    /**
     * 
     * @return
     *     The vin6
     */
    public String getVin6() {
        return vin6;
    }

    /**
     * 
     * @param vin6
     *     The vin6
     */
    public void setVin6(String vin6) {
        this.vin6 = vin6;
    }

    public OrderItem withVin6(String vin6) {
        this.vin6 = vin6;
        return this;
    }

    /**
     * 
     * @return
     *     The vin7
     */
    public String getVin7() {
        return vin7;
    }

    /**
     * 
     * @param vin7
     *     The vin7
     */
    public void setVin7(String vin7) {
        this.vin7 = vin7;
    }

    public OrderItem withVin7(String vin7) {
        this.vin7 = vin7;
        return this;
    }

    /**
     * 
     * @return
     *     The vin8
     */
    public String getVin8() {
        return vin8;
    }

    /**
     * 
     * @param vin8
     *     The vin8
     */
    public void setVin8(String vin8) {
        this.vin8 = vin8;
    }

    public OrderItem withVin8(String vin8) {
        this.vin8 = vin8;
        return this;
    }

    /**
     * 
     * @return
     *     The vin9
     */
    public String getVin9() {
        return vin9;
    }

    /**
     * 
     * @param vin9
     *     The vin9
     */
    public void setVin9(String vin9) {
        this.vin9 = vin9;
    }

    public OrderItem withVin9(String vin9) {
        this.vin9 = vin9;
        return this;
    }

    /**
     * 
     * @return
     *     The biddingPhone1
     */
    public String getBiddingPhone1() {
        return biddingPhone1;
    }

    /**
     * 
     * @param biddingPhone1
     *     The biddingPhone1
     */
    public void setBiddingPhone1(String biddingPhone1) {
        this.biddingPhone1 = biddingPhone1;
    }

    public OrderItem withBiddingPhone1(String biddingPhone1) {
        this.biddingPhone1 = biddingPhone1;
        return this;
    }

    /**
     * 
     * @return
     *     The biddingPrice1
     */
    public String getBiddingPrice1() {
        return biddingPrice1;
    }

    /**
     * 
     * @param biddingPrice1
     *     The biddingPrice1
     */
    public void setBiddingPrice1(String biddingPrice1) {
        this.biddingPrice1 = biddingPrice1;
    }

    public OrderItem withBiddingPrice1(String biddingPrice1) {
        this.biddingPrice1 = biddingPrice1;
        return this;
    }

    /**
     * 
     * @return
     *     The biddingPhone2
     */
    public String getBiddingPhone2() {
        return biddingPhone2;
    }

    /**
     * 
     * @param biddingPhone2
     *     The biddingPhone2
     */
    public void setBiddingPhone2(String biddingPhone2) {
        this.biddingPhone2 = biddingPhone2;
    }

    public OrderItem withBiddingPhone2(String biddingPhone2) {
        this.biddingPhone2 = biddingPhone2;
        return this;
    }

    /**
     * 
     * @return
     *     The biddingPrice2
     */
    public String getBiddingPrice2() {
        return biddingPrice2;
    }

    /**
     * 
     * @param biddingPrice2
     *     The biddingPrice2
     */
    public void setBiddingPrice2(String biddingPrice2) {
        this.biddingPrice2 = biddingPrice2;
    }

    public OrderItem withBiddingPrice2(String biddingPrice2) {
        this.biddingPrice2 = biddingPrice2;
        return this;
    }

    /**
     * 
     * @return
     *     The model
     */
    public String getModel() {
        return model;
    }

    /**
     * 
     * @param model
     *     The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    public OrderItem withModel(String model) {
        this.model = model;
        return this;
    }

    /**
     * 
     * @return
     *     The deletedAt
     */
    public String getDeletedAt() {
        return deletedAt;
    }

    /**
     * 
     * @param deletedAt
     *     The deleted_at
     */
    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public OrderItem withDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public OrderItem withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * 
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OrderItem withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public OrderItem() {
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getPayRole() {
        return payRole;
    }

    public void setPayRole(int payRole) {
        this.payRole = payRole;
    }

    public int getAddtionalSrv() {
        return addtionalSrv;
    }

    public void setAddtionalSrv(int addtionalSrv) {
        this.addtionalSrv = addtionalSrv;
    }

    public int getIncTotalPrice() {
        return incTotalPrice;
    }

    public void setIncTotalPrice(int incTotalPrice) {
        this.incTotalPrice = incTotalPrice;
    }

    public int getCreditCompanyId() {
        return creditCompanyId;
    }

    public void setCreditCompanyId(int creditCompanyId) {
        this.creditCompanyId = creditCompanyId;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getCreditMsg() {
        return creditMsg;
    }

    public void setCreditMsg(String creditMsg) {
        this.creditMsg = creditMsg;
    }

    public int getReceiverPhotoId() {
        return receiverPhotoId;
    }

    public void setReceiverPhotoId(int receiverPhotoId) {
        this.receiverPhotoId = receiverPhotoId;
    }

    public String getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(String accountingType) {
        this.accountingType = accountingType;
    }

    public int getAccountingBill() {
        return accountingBill;
    }

    public void setAccountingBill(int accountingBill) {
        this.accountingBill = accountingBill;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CreditCompanyItem getCreditCompany() {
        return creditCompany;
    }

    public void setCreditCompany(CreditCompanyItem creditCompany) {
        this.creditCompany = creditCompany;
    }

    public ReceiverPhotoEntity getReceiverPhoto() {
        return receiverPhoto;
    }

    public void setReceiverPhoto(ReceiverPhotoEntity receiverPhoto) {
        this.receiverPhoto = receiverPhoto;
    }

    public static class ReceiverPhotoEntity implements Parcelable {
        @SerializedName("url")
        private String url;
        @SerializedName("thumbnail")
        private String thumbnail;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeString(this.thumbnail);
        }

        public ReceiverPhotoEntity() {
        }

        protected ReceiverPhotoEntity(Parcel in) {
            this.url = in.readString();
            this.thumbnail = in.readString();
        }

        public static final Creator<ReceiverPhotoEntity> CREATOR = new Creator<ReceiverPhotoEntity>() {
            @Override
            public ReceiverPhotoEntity createFromParcel(Parcel source) {
                return new ReceiverPhotoEntity(source);
            }

            @Override
            public ReceiverPhotoEntity[] newArray(int size) {
                return new ReceiverPhotoEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.carownerId);
        dest.writeInt(this.driverId);
        dest.writeString(this.orderNo);
        dest.writeInt(this.bill);
        dest.writeInt(this.marketPrice);
        dest.writeInt(this.expectationPrice);
        dest.writeInt(this.deposit);
        dest.writeInt(this.driverDeposit);
        dest.writeInt(this.driverBill);
        dest.writeInt(this.noNeedIns);
        dest.writeInt(this.status);
        dest.writeInt(this.payStatus);
        dest.writeInt(this.payNum);
        dest.writeLong(this.expireTimeStamp);
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeString(this.sendtime);
        dest.writeInt(this.oldCar);
        dest.writeInt(this.brand);
        dest.writeInt(this.carNum);
        dest.writeInt(this.suvNum);
        dest.writeInt(this.bigsuvNum);
        dest.writeInt(this.totalCarPrice);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.street);
        dest.writeString(this.location);
        dest.writeString(this.carownerName);
        dest.writeString(this.carownerPhone);
        dest.writeString(this.cypher);
        dest.writeString(this.driverName);
        dest.writeString(this.driverPhone);
        dest.writeString(this.driverRealPhone);
        dest.writeString(this.plateNumber);
        dest.writeString(this.receiverName);
        dest.writeString(this.receiverPhone);
        dest.writeString(this.receiverCypher);
        dest.writeString(this.vin1);
        dest.writeString(this.vin2);
        dest.writeString(this.vin3);
        dest.writeString(this.vin4);
        dest.writeString(this.vin5);
        dest.writeString(this.vin6);
        dest.writeString(this.vin7);
        dest.writeString(this.vin8);
        dest.writeString(this.vin9);
        dest.writeString(this.biddingPhone1);
        dest.writeString(this.biddingPrice1);
        dest.writeString(this.biddingPhone2);
        dest.writeString(this.biddingPrice2);
        dest.writeString(this.model);
        dest.writeString(this.deletedAt);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeInt(this.shippingId);
        dest.writeInt(this.source);
        dest.writeInt(this.insurance);
        dest.writeInt(this.payRole);
        dest.writeInt(this.addtionalSrv);
        dest.writeInt(this.incTotalPrice);
        dest.writeInt(this.creditCompanyId);
        dest.writeInt(this.quota);
        dest.writeString(this.creditMsg);
        dest.writeInt(this.receiverPhotoId);
        dest.writeString(this.accountingType);
        dest.writeInt(this.accountingBill);
        dest.writeString(this.payment);
        dest.writeString(this.note);
        dest.writeParcelable(this.creditCompany, flags);
        dest.writeParcelable(this.receiverPhoto, flags);
    }

    protected OrderItem(Parcel in) {
        this.id = in.readInt();
        this.carownerId = in.readInt();
        this.driverId = in.readInt();
        this.orderNo = in.readString();
        this.bill = in.readInt();
        this.marketPrice = in.readInt();
        this.expectationPrice = in.readInt();
        this.deposit = in.readInt();
        this.driverDeposit = in.readInt();
        this.driverBill = in.readInt();
        this.noNeedIns = in.readInt();
        this.status = in.readInt();
        this.payStatus = in.readInt();
        this.payNum = in.readInt();
        this.expireTimeStamp = in.readLong();
        this.origin = in.readString();
        this.destination = in.readString();
        this.sendtime = in.readString();
        this.oldCar = in.readInt();
        this.brand = in.readInt();
        this.carNum = in.readInt();
        this.suvNum = in.readInt();
        this.bigsuvNum = in.readInt();
        this.totalCarPrice = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.street = in.readString();
        this.location = in.readString();
        this.carownerName = in.readString();
        this.carownerPhone = in.readString();
        this.cypher = in.readString();
        this.driverName = in.readString();
        this.driverPhone = in.readString();
        this.driverRealPhone = in.readString();
        this.plateNumber = in.readString();
        this.receiverName = in.readString();
        this.receiverPhone = in.readString();
        this.receiverCypher = in.readString();
        this.vin1 = in.readString();
        this.vin2 = in.readString();
        this.vin3 = in.readString();
        this.vin4 = in.readString();
        this.vin5 = in.readString();
        this.vin6 = in.readString();
        this.vin7 = in.readString();
        this.vin8 = in.readString();
        this.vin9 = in.readString();
        this.biddingPhone1 = in.readString();
        this.biddingPrice1 = in.readString();
        this.biddingPhone2 = in.readString();
        this.biddingPrice2 = in.readString();
        this.model = in.readString();
        this.deletedAt = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.shippingId = in.readInt();
        this.source = in.readInt();
        this.insurance = in.readInt();
        this.payRole = in.readInt();
        this.addtionalSrv = in.readInt();
        this.incTotalPrice = in.readInt();
        this.creditCompanyId = in.readInt();
        this.quota = in.readInt();
        this.creditMsg = in.readString();
        this.receiverPhotoId = in.readInt();
        this.accountingType = in.readString();
        this.accountingBill = in.readInt();
        this.payment = in.readString();
        this.note = in.readString();
        this.creditCompany = in.readParcelable(CreditCompanyItem.class.getClassLoader());
        this.receiverPhoto = in.readParcelable(ReceiverPhotoEntity.class.getClassLoader());
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
