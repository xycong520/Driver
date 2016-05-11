package cn.jdywl.driver.config;

/**
 * Created by wuwantao on 15-1-23.
 */
public class ApiConfig {

//    public static final String api_url = "http://115.29.227.98:8080/";
    //public static final String api_url = "http://192.168.1.102/api/public/";

    //windows调试机
    public static final String api_url = "http://120.25.147.20/api/public/";
    //public static final String api_url = "http://120.25.147.20/release/public/";

    public static final String WEB_HOME = "http://115.29.227.98/";

    //common URLs
    public static final String DEBUG_PARA = "?XDEBUG_SESSION_START=PHPSTORM";
    public static final String LOCATION_URL = "user/location" + DEBUG_PARA;
    public static final String APPVERSION_URL = "user/appversion" + DEBUG_PARA;
    public static final String BD_PUSH_URL = "bdmsg/channel" + DEBUG_PARA;
    public static final String VERSION_URL = "version"+DEBUG_PARA;
    public static final String HELPTEL_URL = "helptels"+DEBUG_PARA;
    public static final String ORIGIN_URL = "price/origins"+DEBUG_PARA;
    public static final String RECEIVER_URL = "orders/receiver"+DEBUG_PARA;

    public static final String CAROWNER_TYPE = "carowner";
    public static final String LOGIN_URL = "users/login"+DEBUG_PARA;
    public static final String LOGINCODE_URL = "logincode"+DEBUG_PARA;
    public static final String REGISTER_URL = "users"+DEBUG_PARA;

    public static final String PAY_FAIL_URL = "alipayerror/store"+DEBUG_PARA;

    public static final String DESTINATION_URL = "price/destinations"+DEBUG_PARA;

    public static final String STATS = "orders/stats"+DEBUG_PARA;

    public static final String C_CANCEL = "orders/cancel/"; //货主取消订单(也有可能是超时时主动取消)

    public static final String ROUTE_PRICE_URL = "price/route" + DEBUG_PARA; //根据route获取价格表

    /*********** 微信支付 *************/
    //统一下单API URL
    public static final String WXORDER_URL = "wxpay/unifiedorder"+DEBUG_PARA;

    //carowner URLs
    public static final String CAROWNER_ORDER_URL = "orders/carowner"+DEBUG_PARA;
    public static final String CTODOS_ORDER_URL = "orders/ctodos" + DEBUG_PARA;
    public static final String MARKET_ORDER_URL = "orders/market"+DEBUG_PARA;
    public static final String SUBMIT_ORDER_URL = "orders/store" + DEBUG_PARA;
    public static final String CAROWNER_PAY_URL = "orders/carownerPay/";
    public static final String CAROWNER_HISTORY_URL = "orders/carownerHistory"+DEBUG_PARA;
    public static final String CAROWNER_PROFILE_URL = "carowners/profile"+DEBUG_PARA;
    public static final String CAROWNER_UPDATE_PROFILE_URL = "carowners/update"+DEBUG_PARA;
    public static final String PRICE_QUERY = "price/calPrice" + DEBUG_PARA;
    public static final String C_TRANSPORTING = "orders/ctransporting"+DEBUG_PARA;
    public static final String CREDITCOMPANY_URL = "creditcompany" + DEBUG_PARA;
    public static final String CARPHOTO_URL = "orders/carPhotos/";
    public static final String RECEIVER_CONFIRM_URL = "orders/receiverConfirm/";
    public static final String CREDIT_PREPAY_URL = "orders/creditPrepay/";
    public static final String INSURANCE_STATUS_URL = "insurance/status/";

    //driver URLs
    public static final String DRIVER_ORDER_URL = "orders/driver"+DEBUG_PARA;
    public static final String DRIVER_PAY_URL = "orders/driverPay/";
    public static final String DRIVER_SANCHE_URL = "orders/driverSanche/";
    public static final String DRIVER_ZHENGBAN_URL = "orders/driverZhengban/";
    public static final String DRIVER_BIDDING_URL = "orders/bidding/";
    public static final String DRIVER_HISTORY_URL = "orders/driverHistory"+DEBUG_PARA;
    public static final String DRIVER_PROFILE_URL = "drivers/profile"+DEBUG_PARA;
    public static final String DRIVER_UPDATE_PROFILE_URL = "drivers/update"+DEBUG_PARA;

    //stage
    public static final String STAGE_PROVINCES_URL = "stations/provinces"+DEBUG_PARA;
    public static final String STAGE_CITY_URL = "stations/cities"+DEBUG_PARA;
    public static final String STAGE_URL = "stations"+DEBUG_PARA;
    public static final String STAGE_NEARBY_URL = "stations/nearby"+DEBUG_PARA;
    public static final String STAGE_OFMASTER_URL = "stations/ofmaster"+DEBUG_PARA;
    public static String STAGE_POSOTION_URL = "stations/position"+DEBUG_PARA;
    public static final String STAGE_PRICE_URL = "express/price" +DEBUG_PARA;
    public static final String STAGE_EXPRESS_URL = "express" +DEBUG_PARA;
    public static final String STAGE_ROUTE_URL = "express/route" +DEBUG_PARA;
    public static final String STAGE_CAROWNER_HISTRY_URL = "express/carownerHistory" +DEBUG_PARA;
    public static final String STAGE_SDRIVER_URL = "express/sdriver" +DEBUG_PARA;
    public static final String STAGE_SDRIVER_HISTORY_URL = "express/sdriverHistory" +DEBUG_PARA;
    public static final String STAGE_ORIGINS_URL = "express/origins" +DEBUG_PARA;
    public static final String STAGE_DESTINATIONS_URL = "express/destinations" +DEBUG_PARA;

    //mobile网页
    public static final String WEB_ADDTIONAL_SERVICE = "mobile/service";
    public static final String WEB_SERVICE_AGREEMENT = "mobile/agreement";
    public static final String WEB_CREDIT_COMPANY = "creditcompany/";

    //判断整板车和散车的临界值
    public static final int CAR_SIZE = 20;

    //每页加载的数目
    public static final int PAGE_SIZE = 50;
}
