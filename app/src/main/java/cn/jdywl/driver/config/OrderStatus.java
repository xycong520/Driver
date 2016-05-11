package cn.jdywl.driver.config;

/**
 * Created by wuwantao on 15/5/1.
 */
public class OrderStatus {
    //这三个状态下不发布到运输市场
    public final static int ORDER_INVALID = 0; //无效状态
    public final static int ORDER_UNPAID = 1;  //待支付
    public final static int ORDER_FETCH = 2;   //提车验车
    public final static int ORDER_CONFIRM = 3;   //新增状态，验车提车后进入订单确认状态，确认之后才进入运输市场
    public final static int ORDER_CREDIT_AUDITING = 4;   //融资审核状态，融资订单车主支付后进入改状态
    public final static int ORDER_CREDIT_PREPAY = 5; //融资审核通过，车主需要支付预付款
    public final static int ORDER_CREDIT_REFUSE = 6;   //融资审核失败

    //这几个状态需要发布到运输市场
    public final static int ORDER_READY = 20;    //待运输
    public final static int ORDER_BID = 21;      //待竞价
    public final static int ORDER_FIRST_BID = 22;   //第一次竞价

    //这之后的状态不发布到运输市场,司机在我的承运里可以看到
    public final static int ORDER_DRIVERUNPAID = 50;  //待司机支付，一个小时内司机不支付，回退到待运输状态
    public final static int ORDER_TRADING = 51;       //运输中
    public final static int ORDER_ARRIVED = 52;   //到达目的地
    public final static int ORDER_RECEIVER_CONFIRM = 53;   //收车人确认
    public final static int ORDER_RECEIVER_MATCH = 54;  //收车人匹配
    public final static int ORDER_RECEIVER_MISMATCH = 55;  //收车人错误
    public final static int ORDER_RECEIVER_PHOTO = 56;//收车人照片已上传

    //这个两个状态供司机查看自己的竞价记录
    public final static int ORDER_BID_OK = 70;
    public final static int ORDER_BID_FAIL = 71;

    //这几个状态查询历史订单
    public final static int ORDER_CREDIT_REFUSE_CLOSE = 99;    //订单取消，暂时不开放此功能
    public final static int ORDER_CANCEL = 100;    //订单取消，暂时不开放此功能
    public final static int ORDER_CLOSE = 101;     //订单关闭，指的是非正常关闭
    public final static int ORDER_FINISH = 102;    //订单完成，这里是正常完成后关闭


    public static String getDesc(int status) {
        switch (status) {
            case ORDER_INVALID:
                return "未知状态";

            case ORDER_UNPAID:
                return "待支付";
            case ORDER_FETCH:
            case ORDER_CONFIRM:
                return "验车提车";
            case ORDER_CREDIT_AUDITING:
                return "融资审核中";
            case ORDER_CREDIT_PREPAY:
                return "支付首付款";
            case ORDER_CREDIT_REFUSE:
                return "融资不通过";

            case ORDER_READY:
                return "待运输";
            case ORDER_BID:
                return "待竞价";
            case ORDER_FIRST_BID:
                return "待第二次竞价";
            case ORDER_DRIVERUNPAID:
                return "待司机支付";
            case ORDER_TRADING:
                return "正在运输";
            case ORDER_ARRIVED:
            case ORDER_RECEIVER_PHOTO:
                return "抵达目的地";

            case ORDER_RECEIVER_CONFIRM:
                return "收车人确认";
            case ORDER_RECEIVER_MATCH:
                return "收车人正确";
            case ORDER_RECEIVER_MISMATCH:
                return "收车人错误";

            case ORDER_BID_OK:
                return "竞价成功";
            case ORDER_BID_FAIL:
                return "竞价失败";

            case ORDER_CREDIT_REFUSE_CLOSE:
                return "融资失败";
            case ORDER_CANCEL:
                return "订单取消";
            case ORDER_CLOSE:
                return "订单关闭";
            case ORDER_FINISH:
                return "订单完成";

            default:
                return "未知状态";
        }
    }
}
