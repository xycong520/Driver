package cn.jdywl.driver.ui.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.MainActivity;
import cn.jdywl.driver.R;
import cn.jdywl.driver.alipay.PayResult;
import cn.jdywl.driver.alipay.SignUtils;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.OrderStatus;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.model.WxPrepayItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.carowner.AddOrderFragment;
import cn.jdywl.driver.ui.carowner.CMainActivity;
import cn.jdywl.driver.ui.driver.DMainActivity;
import cn.jdywl.driver.wxapi.WXPayEntryActivity;

public class PayActivity extends BaseActivity {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(PayActivity.class);

    public static final String PAY_ROLE = "PayRole";

    /**
     * 支付宝支付
     */
    //商户PID
    public static final String PARTNER = "2088512029200003";
    //商户收款账号
    public static final String SELLER = "shj731108@163.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAL9lE6Oh4L/oGkp7iJCjrjj2lBLXfnm2J71PBSb52ul47mHUU9pqD1Ud8H46xKrRb+1kb2jpdcX85oDYn6EtBgEuL4oJ7OpBhcrnAG+NBxp0slQgcg1W/InYgQpgalVsfu8VYdXqV+vS5gKHfeqAlODG7X1s50CZhRRmBd4p3qFhAgMBAAECgYA6em+dt4AsIoal60i2qMw6q1xixWTnWziZJuO0HPM+Eq0DRxS3z2AP6a3pY5tjdcp1BhLkQzCw5ayoAYgEfvUuL8qdWPkL+g3MrXbVe7JABk0Cr5etXrBxaM+kdo4O3N8vpDFRdbhLWNHQzTXhqVGOBy/nVNS1IJMy8OZ+IXl6AQJBAOW7RoBL2ZhiekavMuTOguGvBxE+cTXy3GnkFaLUhMEp487111t2J3UlzmSOzYujLshhHvpWsP2Ekh1VAdqblWkCQQDVR5rwVghWKxqvNHWX3DAqN3MmY2mFzxJC7WF7/kXxnyMqLlnnbnyn1ffOKbt/Lo8BinHR1eFC6hC0KnsLWtU5AkEA2tq4ZUzdNODMID6Tu4pJAXtevjzAWWbOOErDmeXfuq2PiwCFc2pyWJX1s7KfemGxFdAAVPj1j+8Vy7/KDlqd0QJBAJUsJhRfOCl8sIdGu9Dhcta0PdflxFKQyIcDHSHqcOVUlvXTwZH7VzjUkSQjUD7eQ6uiN4W6gHma2OAKrlD80RECQQCEvM5vx6w4OlHJ/6nbUghcGuU6mWRv3p4faYI0gAehGEhO0dc54Vc0N1HzU6xVbDt0X5JBPVy2yvcB+bgsARkX";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    /**
     * 微信支付
     */
    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    Map<String, String> resultunifiedorder;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_timer)
    TextView tvTimer;
    @Bind(R.id.rl_timer)
    RelativeLayout rlTimer;
    @Bind(R.id.tv_orderno)
    TextView tvOrderno;
    @Bind(R.id.ll_orderno)
    LinearLayout llOrderno;
    @Bind(R.id.tv_route)
    TextView tvRoute;
    @Bind(R.id.ll_route)
    LinearLayout llRoute;
    @Bind(R.id.tv_carinfo)
    TextView tvCarinfo;
    @Bind(R.id.ll_carinfo)
    LinearLayout llCarinfo;
    @Bind(R.id.tv_sendtime)
    TextView tvSendtime;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.ll_sendtime)
    LinearLayout llSendtime;
    @Bind(R.id.tv_receiver)
    TextView tvReceiver;
    @Bind(R.id.ll_receiver)
    LinearLayout llReceiver;
    @Bind(R.id.marketprice)
    TextView marketprice;
    @Bind(R.id.tv_marketprice)
    TextView tvMarketprice;
    @Bind(R.id.ll_marketprice)
    LinearLayout llMarketprice;
    @Bind(R.id.tv_bill_desc)
    TextView tvBillDesc;
    @Bind(R.id.tv_bill)
    TextView tvBill;
    @Bind(R.id.ll_bill)
    LinearLayout llBill;
    @Bind(R.id.expprice)
    TextView expprice;
    @Bind(R.id.tv_expprice)
    TextView tvExpprice;
    @Bind(R.id.ll_expprice)
    LinearLayout llExpprice;
    @Bind(R.id.tv_fee_desc)
    TextView tvFeeDesc;
    @Bind(R.id.tv_fee)
    TextView tvFee;
    @Bind(R.id.ll_fee)
    LinearLayout llFee;
    @Bind(R.id.tv_totalBill_desc)
    TextView tvTotalBillDesc;
    @Bind(R.id.tv_totalBill)
    TextView tvTotalBill;
    @Bind(R.id.ll_totalBill)
    LinearLayout llTotalBill;
    @Bind(R.id.tv_pay)
    TextView tvPay;
    @Bind(R.id.iv_alipay)
    ImageView ivAlipay;
    @Bind(R.id.tv_alipay_title)
    TextView tvAlipayTitle;
    @Bind(R.id.tv_alipay_desc)
    TextView tvAlipayDesc;
    @Bind(R.id.ib_alipay)
    ImageButton ibAlipay;
    @Bind(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @Bind(R.id.iv_wxpay)
    ImageView ivWxpay;
    @Bind(R.id.tv_wxpay_title)
    TextView tvWxpayTitle;
    @Bind(R.id.tv_wxpay_desc)
    TextView tvWxpayDesc;
    @Bind(R.id.ib_wxpay)
    ImageButton ibWxpay;
    @Bind(R.id.rl_wxpay)
    RelativeLayout rlWxpay;
    @Bind(R.id.tv_pay_desc)
    TextView tvPayDesc;
    @Bind(R.id.tv_payBill)
    TextView tvPayBill;
    @Bind(R.id.submit_button)
    Button submitButton;
    @Bind(R.id.ll_submit)
    RelativeLayout llSubmit;
    @Bind(R.id.tv_insfee)
    TextView tvInsfee;
    @Bind(R.id.ll_insfee)
    LinearLayout llInsfee;
    @Bind(R.id.tv_addtionalSrv)
    TextView tvAddtionalSrv;

    private OrderItem order;
    int payRole = 0;
    int payType = 0;  //支付类型，0：支付宝支付, 1:微信支付，2：银联支付

    boolean bPayExpire = false;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        setupToolbar();

        //初始化微信支付
        req = new PayReq();
        msgApi.registerApp(Constants.APP_ID);

        //获取订单和支付角色
        Intent it = getIntent();
        order = it.getParcelableExtra("order");
        payRole = it.getIntExtra(PAY_ROLE, 0);
        ibAlipay.setSelected(true); //默认选择支付宝支付

        //隐藏相关视图
        llReceiver.setVisibility(View.GONE);
        llExpprice.setVisibility(View.GONE);
        tvStatus.setVisibility(View.INVISIBLE);

        //获取订单状态
        tvOrderno.setText(order.getOrderNo());
        tvRoute.setText(order.getOrigin() + " — " + order.getDestination());
        tvCarinfo.setText(Helper.getCarTypeByid(this, order.getBrand()) + " " + order.getCarNum() + "辆");
        tvSendtime.setText(order.getSendtime());

        tvMarketprice.setText(order.getDriverBill() + "元");
        tvBill.setText(order.getBill() + "元");
        tvTotalBill.setText(order.getMarketPrice() + "元");

        if (payRole == 1) //司机支付
        {
            setToolbarTitle("司机支付");

            //司机不现实保险和总价
            llInsfee.setVisibility(View.GONE);
            tvPayDesc.setVisibility(View.GONE);
            llTotalBill.setVisibility(View.GONE);

            tvFee.setText(String.format(Locale.CHINA, "%d元", order.getDriverDeposit()));

            if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
                tvFeeDesc.setText(getString(R.string.zhengban_dfee_desc));
                tvPayBill.setText(String.format(Locale.CHINA, "预付服务费: %d元", order.getDriverDeposit()));
            } else {
                llMarketprice.setVisibility(View.GONE);

                tvFeeDesc.setText(getString(R.string.sanche_dfee_desc));
                tvPayBill.setText(String.format(Locale.CHINA, "预付取车费: %d元", order.getDriverDeposit()));
            }

        } else {  //货主支付
            setToolbarTitle("货主支付");

            tvFee.setText(String.format(Locale.CHINA, "%d元", order.getDeposit() - order.getInsurance()));
            tvInsfee.setText(String.format(Locale.CHINA, "%d元", order.getInsurance()));

            if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
                tvFeeDesc.setText(getString(R.string.zhengban_cfee_desc));
                tvPayBill.setText(String.format(Locale.CHINA, "预付保险和服务费: %d元", order.getDeposit()));
                tvPayDesc.setText(getString(R.string.zhengban_cpay_desc));
            } else {
                llBill.setVisibility(View.GONE);

                tvFeeDesc.setText(getString(R.string.sanche_cfee_desc));
                tvPayBill.setText(String.format(Locale.CHINA, "预付保险和服务费: %d元", order.getDeposit()));
                tvPayDesc.setText(getString(R.string.sanche_cpay_desc));
            }
        }

        //设置增值服务
        int srv = order.getAddtionalSrv();
        switch (srv) {
            case AddOrderFragment.SRV_CREDIT:
                tvAddtionalSrv.setText("垫资发运");
                break;
            case AddOrderFragment.SRV_REGULATORY:
                tvAddtionalSrv.setText("代收车款");
                break;
            default:
                tvAddtionalSrv.setVisibility(View.INVISIBLE);
                break;
        }

        long now = System.currentTimeMillis();
        long expire = order.getExpireTimeStamp() * 1000;  //转换为毫秒

        timer = new CountDownTimer(expire - now, 1000) {

            public void onTick(long millisUntilFinished) {

                setTimerText(millisUntilFinished / 1000);
            }

            public void onFinish() {
                setTimerText(0);
            }
        }.start();

    }

    void setTimerText(long timeleft) {
        if (timeleft <= 0) {
            tvTimer.setText(Html.fromHtml(String.format(getString(R.string.timer_desc), "已超时")));

            /*
            UIAlertView *messageAlert = [[UIAlertView alloc]
            initWithTitle:@"无法继续支付" message:@"已经超过支付时间，订单无法支付" delegate:nil cancelButtonTitle:@"好" otherButtonTitles:nil];
            [messageAlert show];

            [self backToMainViewConroller];
            */
            if (order.getStatus() == OrderStatus.ORDER_UNPAID) {
                cancelOrder();
            }

            //超时后改变显示字段
            submitButton.setText("返回");
            bPayExpire = true;

        } else {
            long hours = timeleft / 60 / 60;
            long minutes = (timeleft - (hours * 3600)) / 60;
            long seconds = timeleft % 60;

            String html = String.format(getString(R.string.timer_desc), String.format("%d小时%2d分%2d秒", hours, minutes, seconds));

            if (hours <= 0) {
                html = String.format(getString(R.string.timer_desc), String.format("%2d分%2d秒", minutes, seconds));
            }

            tvTimer.setText(Html.fromHtml(html));

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            //点击拨打客服热线
            case R.id.action_hotline:
                final boolean telAvailable = Helper.isIntentAvailable(this, Intent.ACTION_DIAL);
                if (telAvailable) {
                    Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006501387"));
                    startActivity(it);
                } else {
                    Toast.makeText(this, "未安装电话应用，无法拨打电话", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /**
     * 支付方式选择
     *
     * @param view
     */
    public void onPaySelected(View view) {
        if (view.getId() == R.id.ib_alipay) {

            if (!ibAlipay.isSelected()) {
                ibAlipay.setSelected(true);
                ibWxpay.setSelected(false);
                payType = 0;
            }
        } else if (view.getId() == R.id.ib_wxpay) {
            if (!ibWxpay.isSelected()) {
                ibWxpay.setSelected(true);
                ibAlipay.setSelected(false);
                payType = 1;
            }
        }
    }

    /**
     * 根据支付方式发起支付
     *
     * @param view
     */
    public void onOrderPay(View view) {
        if (bPayExpire) //司机支付超时，直接返回
        {
            Intent it;
            switch (payRole) {
                case 0:  //货主支付
                    it = new Intent(this, CMainActivity.class);
                    break;
                case 1: //司机支付
                    it = new Intent(this, DMainActivity.class);
                    break;
                default:  //其它
                    it = new Intent(this, MainActivity.class);
                    break;
            }

            startActivity(it);
        } else { //开始支付
            if (payType == 0) {  //支付宝支付
                sendAliPay();
            } else if (payType == 1) { //微信支付
                sendWxPay();
            }
        }
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        if (payRole == 1) //司机支付
                        {
                            driverPaySuccess();
                        } else {
                            carownerPaySuccess();
                        }


                        Toast.makeText(PayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

    };


    /*
    * 提交车单
    */
    void carownerPaySuccess() {

        String url = ApiConfig.api_url + ApiConfig.CAROWNER_PAY_URL + order.getId();

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.PUT,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                null,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        //跳转到支付页面
                        Intent it = new Intent(PayActivity.this, WXPayEntryActivity.class);
                        it.putExtra(PAY_ROLE, payRole);
                        it.putExtra(WXPayEntryActivity.PAY_RESULT, 0);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.processVolleyErrorMsg(PayActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /*
    * 提交车单
    */
    void driverPaySuccess() {

        String url = ApiConfig.api_url + ApiConfig.DRIVER_PAY_URL + order.getId();

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.PUT,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                null,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        //跳转到支付页面
                        Intent it = new Intent(PayActivity.this, WXPayEntryActivity.class);
                        it.putExtra(PAY_ROLE, payRole);
                        it.putExtra(WXPayEntryActivity.PAY_RESULT, 0);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.processVolleyErrorMsg(PayActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    /*
    * 提交车单
    */
    void cancelOrder() {

        String url = ApiConfig.api_url + ApiConfig.C_CANCEL + order.getId() + ApiConfig.DEBUG_PARA;

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.POST,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                null,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        order = response;
                        Toast.makeText(PayActivity.this, "已超过支付时间，订单自动取消", Toast.LENGTH_SHORT).show();

                        //跳转到支付页面
                        Intent it = new Intent(PayActivity.this, WXPayEntryActivity.class);
                        it.putExtra(PAY_ROLE, payRole);
                        it.putExtra(WXPayEntryActivity.PAY_RESULT, -2);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.processVolleyErrorMsg(PayActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /**
     * 发起支付宝支付
     */
    public void sendAliPay() {
        // 订单
        String subject = tvRoute.getText() + " " + tvCarinfo.getText().toString();
        String body = tvCarinfo.getText().toString();

        String orderPrice;
        if (payRole == 1) //司机支付
        {
            orderPrice = String.valueOf(order.getDriverDeposit());
        } else {  //货主支付
            orderPrice = String.valueOf(order.getDeposit());
        }
        //TODO:修改为实际的额度
        orderPrice = "0.01";

        String orderInfo = getOrderInfo(subject, body, orderPrice);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(PayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";


        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        if (payRole == 1) {  //司机
            // 商户网站唯一订单号
            orderInfo += "&out_trade_no=" + "\"" + "D-" + order.getOrderNo() + "-" + order.getPayNum() + "\"";
            /*
             * 司机的支付超时时间为45分钟
             */
            orderInfo += "&it_b_pay=\"45m\"";
        } else { //货主
            // 商户网站唯一订单号
            orderInfo += "&out_trade_no=" + "\"" + "C-" + order.getOrderNo() + "-" + order.getPayNum() + "\"";

            /*
             * 货主的支付超时时间为1天
             */
            orderInfo += "&it_b_pay=\"1d\"";
        }

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ApiConfig.api_url + "orders/alipayNotify"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * 发起微信支付
     */
    public void sendWxPay() {
        final ProgressDialog dialog;

        String orderName = tvRoute.getText() + " " + tvCarinfo.getText().toString();
        String orderNO;
        int orderPrice;
        if (payRole == 1) { //司机支付
            orderNO = "D-" + order.getOrderNo() + "-" + order.getPayNum();
            orderPrice = order.getDriverDeposit() * 100;
        } else { //货主支付
            orderNO = "C-" + order.getOrderNo() + "-" + order.getPayNum();
            orderPrice = order.getDeposit() * 100;
        }
        //TODO:发布时修改为真实价格
        orderPrice = 1;

        String url = ApiConfig.api_url + ApiConfig.WXORDER_URL;

        dialog = ProgressDialog.show(this, null, "正在提交微信支付...");

        //设置回调URL
        String NOTIFY_URL = ApiConfig.api_url + "wxpay/wxpayNotify";
        String DEVICE_INFO = "Android-" + AppConfig.phone;

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", orderNO);
        params.put("body", orderName);
        params.put("total_fee", orderPrice + "");
        params.put("trade_type", "APP");
        params.put("notify_url", NOTIFY_URL);
        params.put("device_info", DEVICE_INFO);

        //传递order id
        params.put("order_id", String.format("%d", order.getId()));

        GsonRequest<WxPrepayItem> myReq = new GsonRequest<WxPrepayItem>(Request.Method.POST,
                url,
                WxPrepayItem.class,
                params,
                new Response.Listener<WxPrepayItem>() {
                    @Override
                    public void onResponse(WxPrepayItem response) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        if (response.getReturn_code().equals("SUCCESS") && response.getResult_code().equals("SUCCESS")) {

                            //给req赋值
                            req.appId = response.getAppid();
                            req.partnerId = response.getMch_id();
                            req.prepayId = response.getPrepay_id();
                            req.nonceStr = response.getNonce_str();
                            req.sign = response.getSign();
                            req.packageValue = response.getPackag();
                            req.timeStamp = response.getTimestamp();

                            sendPayReq();
                            /*
                            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                            builder.setTitle("支付失败").setNegativeButton("确定",null).create().show();
                            */

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                            builder.setTitle("微信支付失败").setNegativeButton("确定", null).create().show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        Helper.processVolleyErrorMsg(PayActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);

    }

    private boolean sendPayReq() {


        return msgApi.sendReq(req);
    }

}
