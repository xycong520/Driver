package cn.jdywl.driver.ui.driver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.OrderStatus;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.carowner.AddOrderFragment;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.common.PayActivity;

public class DDetailActivity extends BaseActivity {

    public static String TAG = LogHelper.makeLogTag(DDetailActivity.class);
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
    @Bind(R.id.tv_totalBill)
    TextView tvTotalBill;
    @Bind(R.id.ll_totalBill)
    LinearLayout llTotalBill;
    @Bind(R.id.stub_zhengban)
    ViewStub stubZhengban;
    @Bind(R.id.stub_sanche)
    ViewStub stubSanche;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.tv_addtionalSrv)
    TextView tvAddtionalSrv;
    @Bind(R.id.ll_insfee)
    LinearLayout llInsfee;

    //只用于整版的View
    private EditText et_bidding_price;
    private TextView tv_expprice;

    //只用于散车的View
    private EditText et_plate;
    private EditText et_driver_phone;

    private OrderItem order;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddetail);
        ButterKnife.bind(this);
        setupToolbar();

        order = getIntent().getParcelableExtra("order");

        //运单信息
        tvOrderno.setText(order.getOrderNo());
        tvRoute.setText(order.getOrigin() + " — " + order.getDestination());
        tvCarinfo.setText(Helper.getCarTypeByid(this, order.getBrand()) + " " + order.getCarNum() + "辆");
        tvSendtime.setText(order.getSendtime());
        tvStatus.setText(OrderStatus.getDesc(order.getStatus()));

        //运价信息
        tvMarketprice.setText(order.getDriverBill() + "元");
        tvFee.setText(order.getDriverDeposit() + "元");
        tvBill.setText(String.format(Locale.CHINA, "%d元", order.getBill()));

        //对司机隐藏的信息
        llInsfee.setVisibility(View.GONE);
        llTotalBill.setVisibility(View.GONE);

        //获取订单状态
        switch (order.getStatus()) {
            case OrderStatus.ORDER_TRADING:
            case OrderStatus.ORDER_ARRIVED:
            case OrderStatus.ORDER_RECEIVER_CONFIRM:
            case OrderStatus.ORDER_RECEIVER_MATCH:
            case OrderStatus.ORDER_RECEIVER_MISMATCH:
            case OrderStatus.ORDER_RECEIVER_PHOTO:
                if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
                    llReceiver.setVisibility(View.GONE);
                }
                break;
            case OrderStatus.ORDER_DRIVERUNPAID:
            case OrderStatus.ORDER_READY:
            case OrderStatus.ORDER_BID:
            case OrderStatus.ORDER_FIRST_BID:
                llReceiver.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                //这几个状态才需要显示输入信息
                if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
                    stubZhengban.setVisibility(View.VISIBLE);
                } else {
                    stubSanche.setVisibility(View.VISIBLE);
                    llBill.setVisibility(View.GONE);
                }
            default:
                break;
        }

        //设置增值服务
        int srv = order.getAddtionalSrv();
        switch (srv) {
            case AddOrderFragment.SRV_CREDIT:
                tvAddtionalSrv.setText("垫款发车");
                break;
            case AddOrderFragment.SRV_REGULATORY:
                tvAddtionalSrv.setText("代收车款");
                break;
            default:
                tvAddtionalSrv.setVisibility(View.INVISIBLE);
                break;
        }

        //接车人信息
        if (llReceiver.getVisibility() == View.VISIBLE) {
            tvReceiver.setText(String.format(Locale.CHINA, "%s:%s", order.getReceiverName(), order.getReceiverPhone()));
        }

        if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
            //整版才显示期望运价和成交运价
            tvExpprice.setText(String.format(Locale.CHINA, "%d元", order.getExpectationPrice()));

            tvStatus.setBackgroundColor(getResources().getColor(R.color.bg_zhengban));
            tvFeeDesc.setText("服务费");
        } else {
            //散车没有期望运价
            llExpprice.setVisibility(View.GONE);

            tvStatus.setBackgroundColor(getResources().getColor(R.color.bg_sanche));
            tvFeeDesc.setText("取车费");
        }

        //整版stub
        if (stubZhengban.getVisibility() == View.VISIBLE) {
            et_bidding_price = (EditText) findViewById(R.id.et_bidding_price);

            btnSubmit.setText("我要竞拍");
            setToolbarTitle("竞拍");
        }

        //散车stub
        if (stubSanche.getVisibility() == View.VISIBLE) {
            et_driver_phone = (EditText) findViewById(R.id.et_driver_phone);
            et_plate = (EditText) findViewById(R.id.et_carmark);

            btnSubmit.setText("我要接单");
            setToolbarTitle("接单");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ddetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 打开司机页面
     *
     * @param view
     */
    public void driverPay(View view) {
        int carNum = order.getCarNum();
        String plate = et_plate.getText().toString();

        if (carNum < ApiConfig.CAR_SIZE) {
            if (plate.length() < 7 || plate.length() > 15) {
                et_plate.setError("车牌号错误");
                return;
            }

            if (!Helper.isPhone(et_driver_phone.getText().toString())) {
                et_driver_phone.setError(getString(R.string.error_invalid_phone));
                return;
            }

            driverSanche();
            return;
        }
        if (et_bidding_price.getText().toString().length() == 0) {
            et_bidding_price.setError("竞拍价格不能为空");
            return;
        }

        int biddingPrice = Integer.valueOf(et_bidding_price.getText().toString());
        int expactionPrice = order.getExpectationPrice();
        int status = order.getStatus();

        if (biddingPrice > expactionPrice) {
            if (biddingPrice > expactionPrice * 1.3) {


                new AlertDialog.Builder(this)
                        .setTitle("竞拍价太高了")
                        .setMessage("高富帅您好，竞拍价不能高出期望运价30%哦")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();

                return;
            }

            if (status == OrderStatus.ORDER_BID) {
                driverBidding();
            } else if (status == OrderStatus.ORDER_FIRST_BID) {
                if (biddingPrice >= Integer.valueOf(order.getBiddingPrice1()) * 0.98) {
                    new AlertDialog.Builder(this)
                            .setTitle("竞价提交失败")
                            .setMessage("您的竞价价格必须至少低于第一个人2%")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    driverZhengban();
                }
            }
        } else {
            driverZhengban();
        }

    }


    /*
    * 散车接单
    */
    void driverSanche() {
        showProgress(true);

        String url = ApiConfig.api_url + ApiConfig.DRIVER_SANCHE_URL + order.getId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("driverRealPhone", et_driver_phone.getText().toString());
        params.put("plateNumber", et_plate.getText().toString());

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.PUT,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                params,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {
                        showProgress(false);
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        //跳转到支付页面
                        Intent it = new Intent(DDetailActivity.this, PayActivity.class);
                        it.putExtra(PayActivity.PAY_ROLE, 1);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("order", response);
                        it.putExtras(bundle);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        Helper.processVolleyErrorMsg(DDetailActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /*
    * 整版接单
    */
    void driverZhengban() {
        showProgress(true);
        String url = ApiConfig.api_url + ApiConfig.DRIVER_ZHENGBAN_URL + order.getId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("biddingPrice", et_bidding_price.getText().toString());

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.PUT,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                params,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {
                        showProgress(false);
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        //跳转到支付页面
                        Intent it = new Intent(DDetailActivity.this, PayActivity.class);
                        it.putExtra(PayActivity.PAY_ROLE, 1);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("order", response);
                        it.putExtras(bundle);

                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        Helper.processVolleyErrorMsg(DDetailActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /*
    * 整版竞价
    */
    void driverBidding() {
        showProgress(true);
        String url = ApiConfig.api_url + ApiConfig.DRIVER_BIDDING_URL + order.getId();

        Map<String, String> params = new HashMap<String, String>();
        params.put("biddingPrice", et_bidding_price.getText().toString());

        // Request a JSON response from the provided URL.
        GsonRequest<OrderItem> myReq = new GsonRequest<OrderItem>(Request.Method.POST,
                //"http://valisendtime.jsontest.com/?json={'key':'value'}",
                url,
                OrderItem.class,
                params,
                new Response.Listener<OrderItem>() {
                    @Override
                    public void onResponse(OrderItem response) {
                        showProgress(false);
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        final OrderItem result = response;

                        //跳转到支付页面

                        new AlertDialog.Builder(DDetailActivity.this)
                                .setTitle("竞价提交成功")
                                .setMessage("请等待系统通知")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                        Intent it = new Intent(DDetailActivity.this, DMainActivity.class);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("order", result);
                                        it.putExtras(bundle);

                                        startActivity(it);
                                    }
                                })
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        Helper.processVolleyErrorMsg(DDetailActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /**
     * Shows the progress UI
     */
    public void showProgress(final boolean show) {
        if (show) {
            dialog = ProgressDialog.show(this, null, "正在提交...");
        } else {
            dialog.dismiss();
        }

    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
