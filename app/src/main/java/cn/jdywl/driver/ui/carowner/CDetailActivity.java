package cn.jdywl.driver.ui.carowner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
import cn.jdywl.driver.model.CreditCompanyItem;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.common.InsuranceActivity;
import cn.jdywl.driver.ui.common.MapActivity;
import cn.jdywl.driver.ui.common.NetworkImageActivity;
import cn.jdywl.driver.ui.common.PayActivity;
import cn.jdywl.driver.ui.common.VinActivity;

public class CDetailActivity extends BaseActivity {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(CDetailActivity.class);


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
    @Bind(R.id.tv_insfee)
    TextView tvInsfee;
    @Bind(R.id.ll_insfee)
    LinearLayout llInsfee;
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
    @Bind(R.id.stub_location)
    ViewStub stubLocation;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.tv_addtionalSrv)
    TextView tvAddtionalSrv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.stub_credit)
    ViewStub stubCredit;
    @Bind(R.id.stub_carphoto)
    ViewStub stubCarphoto;
    @Bind(R.id.stub_receiver_photo)
    ViewStub stubReceiverPhoto;

    private OrderItem order;

    private TextView tvLocation;
    private TextView tvInsurance;
    private LinearLayout llInsurance;
    private LinearLayout llLocation;
    private TextView tvPdfDesc;

    //融资监管STUB相关View
    private TextView tvQuota;
    private TextView tvCompany;
    private TextView tvCreditMgr;
    private TextView tvCreditBank;
    private LinearLayout llCreditBank;
    private TextView tvCreditName;
    private LinearLayout llCreditName;
    private TextView tvCreditNo;
    private LinearLayout llCreditNo;
    private LinearLayout llPrepay;
    private EditText etPrepay;

    //接车人照片stub
    private NetworkImageView ivReceiverPhoto;
    private RadioGroup rgReceiverConfirm;
    private LinearLayout llReceiverConfirm;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdetail);
        ButterKnife.bind(this);
        setupToolbar();

        //获取订单信息
        order = getIntent().getParcelableExtra("order");

        //获取订单状态
        int status = order.getStatus();

        tvOrderno.setText(order.getOrderNo());
        tvRoute.setText(order.getOrigin() + " — " + order.getDestination());
        tvCarinfo.setText(Helper.getCarTypeByid(this, order.getBrand()) + " " + order.getCarNum() + "辆");
        tvSendtime.setText(order.getSendtime());
        tvStatus.setText(OrderStatus.getDesc(status));
        tvReceiver.setText(String.format(Locale.CHINA, "%s:%s", order.getReceiverName(), order.getReceiverPhone()));

        tvMarketprice.setText(String.format(Locale.CHINA, "%d元", order.getDriverBill()));
        tvBill.setText(String.format(Locale.CHINA, "%d元", order.getBill()));
        tvFee.setText(String.format(Locale.CHINA, "%d元", order.getDeposit() - order.getInsurance()));
        tvTotalBill.setText(String.format(Locale.CHINA, "%d元", order.getMarketPrice()));
        tvExpprice.setText(String.format(Locale.CHINA, "%d元", order.getExpectationPrice()));
        tvInsfee.setText(String.format(Locale.CHINA, "%d元", order.getInsurance()));

        switch (status) {
            case OrderStatus.ORDER_READY:
                stubCarphoto.setVisibility(View.VISIBLE);
                break;
            case OrderStatus.ORDER_TRADING:
            case OrderStatus.ORDER_ARRIVED:
                stubCarphoto.setVisibility(View.VISIBLE);
                stubLocation.setVisibility(View.VISIBLE);
                break;
            case OrderStatus.ORDER_RECEIVER_CONFIRM:
                //收车人确认
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("确认");
            case OrderStatus.ORDER_RECEIVER_MATCH:
            case OrderStatus.ORDER_RECEIVER_MISMATCH:
                stubCarphoto.setVisibility(View.VISIBLE);
                stubLocation.setVisibility(View.VISIBLE);
                //代收车款和垫款发车才显示接车人照片
                if (order.getAddtionalSrv() > AddOrderFragment.SRV_NONE) {
                    stubReceiverPhoto.setVisibility(View.VISIBLE);
                }
                break;
            case OrderStatus.ORDER_CREDIT_PREPAY:
                btnSubmit.setVisibility(View.VISIBLE);
                break;
            case OrderStatus.ORDER_UNPAID:
                btnSubmit.setVisibility(View.VISIBLE);
                if (order.getCarNum() >= ApiConfig.CAR_SIZE) //显示整版stub
                {
                    //整版支付
                    btnSubmit.setText(getString(R.string.zhengban_cpay_btn));
                } else {//散车支付
                    btnSubmit.setText(getString(R.string.sanche_cpay_btn));
                }
                break;
            default:
                break;
        }

        //整版和散车界面调整
        if (order.getCarNum() >= ApiConfig.CAR_SIZE) //显示整版stub
        {
            //整版隐藏收车人和保险
            llReceiver.setVisibility(View.GONE);

            tvFeeDesc.setText(R.string.zhengban_cfee_desc);
        } else {
            //散车隐藏成交运价和期望运价
            llBill.setVisibility(View.GONE);
            llExpprice.setVisibility(View.GONE);

            tvFeeDesc.setText(R.string.sanche_cfee_desc);
        }

        //融资相关信息
        if (order.getAddtionalSrv() == AddOrderFragment.SRV_CREDIT) {
            //设置增值服务标签
            tvAddtionalSrv.setText("垫款发车");

            stubCredit.setVisibility(View.VISIBLE); //显示STUB

            tvQuota = (TextView) findViewById(R.id.tv_quota);
            tvCompany = (TextView) findViewById(R.id.tv_company);
            tvCreditMgr = (TextView) findViewById(R.id.tv_creditMgr);

            CreditCompanyItem company = order.getCreditCompany();
            tvQuota.setText(String.format(Locale.CHINA, "%d元", order.getQuota()));
            tvCompany.setText(company.getCompany());
            tvCreditMgr.setText(String.format(Locale.CHINA, "%s:%s", company.getContacter(), company.getPhone()));

            if (status == OrderStatus.ORDER_CREDIT_PREPAY) {
                tvCreditBank = (TextView) findViewById(R.id.tv_creditBank);
                tvCreditName = (TextView) findViewById(R.id.tv_creditName);
                tvCreditNo = (TextView) findViewById(R.id.tv_creditNo);
                etPrepay = (EditText) findViewById(R.id.et_prepay);

                tvCreditBank.setText(company.getBank());
                tvCreditName.setText(company.getAccountName());
                tvCreditNo.setText(company.getAccountNo());
                btnSubmit.setText("提交");
            } else {
                llCreditBank = (LinearLayout) findViewById(R.id.ll_creditBank);
                llCreditName = (LinearLayout) findViewById(R.id.ll_creditName);
                llCreditNo = (LinearLayout) findViewById(R.id.ll_creditNo);
                llPrepay = (LinearLayout) findViewById(R.id.ll_prepay);

                llCreditBank.setVisibility(View.GONE);
                llCreditName.setVisibility(View.GONE);
                llCreditNo.setVisibility(View.GONE);
                llPrepay.setVisibility(View.GONE);
            }
        } else if (order.getAddtionalSrv() == AddOrderFragment.SRV_REGULATORY) {
            tvAddtionalSrv.setText("代收车款");
        } else {
            tvAddtionalSrv.setVisibility(View.INVISIBLE);
        }

        //显示位置和保险信息
        if (stubLocation.getVisibility() == View.VISIBLE) {
            llInsurance = (LinearLayout) findViewById(R.id.ll_insurance);
            llLocation = (LinearLayout) findViewById(R.id.ll_location);
            tvInsurance = (TextView) findViewById(R.id.tv_insurance);
            tvLocation = (TextView) findViewById(R.id.tv_location);

            //显示位置信息
            if (!order.getLocation().isEmpty()) {
                tvLocation.setText(order.getLocation());
            }

            //这两个号码代司机接车，做特殊处理
            if (order.getDriverPhone().equals("13651217220") || order.getDriverPhone().equals("13717811880")) {
                llLocation.setClickable(false);
                tvLocation.setSingleLine(false);
                tvLocation.setTextColor(getResources().getColor(R.color.gray_title));
            }

            //整版不显示保险
            if (order.getCarNum() >= ApiConfig.CAR_SIZE) {
                llInsurance.setVisibility(View.GONE);
                tvPdfDesc.setVisibility(View.GONE);
            } else {
                if (order.getNoNeedIns() != 0) {  //板车自带保险
                    tvInsurance.setText("板车自带保险");
                    llInsurance.setClickable(false);
                    tvInsurance.setTextColor(getResources().getColor(R.color.gray_title));
                } else {
                    //API 21以下版本提示用户下载PDF阅读器
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        tvPdfDesc = (TextView) findViewById(R.id.tv_pdf_desc);

                        tvPdfDesc.setVisibility(View.VISIBLE);
                        //PDF下载说明
                        tvPdfDesc.setText(Html.fromHtml(getString(R.string.pdf_view_desc)));
                        tvPdfDesc.setMovementMethod(LinkMovementMethod.getInstance());
                    }

                }
            }
        }

        //接车人照片
        if (stubReceiverPhoto.getVisibility() == View.VISIBLE) {
            OrderItem.ReceiverPhotoEntity receiverPhoto = order.getReceiverPhoto();
            //加载收车人照片
            if (receiverPhoto != null) {
                String url = receiverPhoto.getThumbnail();
                ivReceiverPhoto = (NetworkImageView) findViewById(R.id.iv_receiver_photo);
                ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
                ivReceiverPhoto.setImageUrl(url, mImageLoader);
            }

            if (status == OrderStatus.ORDER_RECEIVER_CONFIRM) {
                rgReceiverConfirm = (RadioGroup) findViewById(R.id.rg_receiverConfirm);
            } else {
                llReceiverConfirm = (LinearLayout) findViewById(R.id.ll_receiver_confirm);
                llReceiverConfirm.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 打开司机页面
     *
     * @param view
     */
    public void onSubmit(View view) {
        int status = order.getStatus();
        if (status == OrderStatus.ORDER_UNPAID) {
            carownerPay();
        } else if (status == OrderStatus.ORDER_RECEIVER_CONFIRM) {
            receiverConfirm();
        } else if (status == OrderStatus.ORDER_CREDIT_PREPAY) {
            creditPrepay();
        }
    }

    /**
     * 打开支付页面
     */
    private void carownerPay() {
        Intent it = new Intent(this, PayActivity.class);
        it.putExtra(PayActivity.PAY_ROLE, 0);
        Bundle bundle = new Bundle();
        bundle.putParcelable("order", order);
        it.putExtras(bundle);
        startActivity(it);
    }

    /**
     * 打开未知信息页面
     *
     * @param view
     */
    public void showLocation(View view) {
        Intent it = new Intent(this, MapActivity.class);
        it.putExtra("phone", order.getDriverPhone());
        startActivity(it);
    }

    /**
     * 打开接车人照片界面
     *
     * @param view
     */
    public void showReceiver(View view) {
        Intent it = new Intent(this, NetworkImageActivity.class);
        it.putExtra("url", order.getReceiverPhoto().getUrl());
        startActivity(it);
    }

    /**
     * 打开车辆照片界面
     *
     * @param view
     */
    public void showCarPhoto(View view) {
        Intent it = new Intent(this, CarPhotoActivity.class);
        it.putExtra("id", order.getId());
        startActivity(it);
    }

    /**
     * 打开保险页面
     *
     * @param view
     */
    public void showInsurance(View view) {
        Intent it;
        if (order.getTotalCarPrice() > 0)  //新版本
        {
            String filename = order.getOrderNo() + ".pdf";

            //修改为使用外置浏览器打开PDF文档
            String url = ApiConfig.api_url + "assets/insurance/" + filename;
            //API 21以上，系统内置PDF解析器
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it = new Intent(this, InsuranceActivity.class);
                it.putExtra("filename", filename);
                it.putExtra("url", ApiConfig.api_url + ApiConfig.INSURANCE_STATUS_URL + order.getId());
            } else {
                it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            }

        } else {
            it = new Intent(this, VinActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("order", order);
            it.putExtras(bundle);
        }

        startActivity(it);
    }

    /*
    * 提交车单
    */
    public void receiverConfirm() {
        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();

        //显示进度条，隐藏其它界面
        showProgress(true);

        if (rgReceiverConfirm.getCheckedRadioButtonId() == R.id.rb_correct) {
            params.put("confirm", "1");
        } else {
            params.put("confirm", "0");
        }

        //设置URl
        String url = ApiConfig.api_url + ApiConfig.RECEIVER_CONFIRM_URL + order.getId();

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
                        Toast.makeText(CDetailActivity.this, "收车人确认提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showProgress(false);

                        Helper.processVolleyErrorMsg(CDetailActivity.this, error);

                    }
                });

        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    /*
    * 提交车单
    */
    public void creditPrepay() {

        etPrepay.setError(null);
        if (order.getAddtionalSrv() != AddOrderFragment.SRV_CREDIT) {
            Toast.makeText(this, "非垫款发车订单，无法提交，请咨询客服", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etPrepay.getText().toString().isEmpty()) {
            //Toast.makeText(this, "请填写首付款额度", Toast.LENGTH_SHORT).show();
            etPrepay.setError("首付款额度不能为空");
            return;
        }

        if (Integer.valueOf(etPrepay.getText().toString()) != (order.getTotalCarPrice() - order.getQuota())) {
            //Toast.makeText(this, "首付款额度不对，无法提交", Toast.LENGTH_SHORT).show();
            etPrepay.setError("首付款额度不对，无法提交");
            return;
        }

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();

        //显示进度条，隐藏其它界面
        showProgress(true);

        params.put("prepayAmount", etPrepay.getText().toString());

        //设置URl
        String url = ApiConfig.api_url + ApiConfig.CREDIT_PREPAY_URL + order.getId();

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
                        //Toast.makeText(CDetailActivity.this, "预付款额度提交成功", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showProgress(false);

                        Helper.processVolleyErrorMsg(CDetailActivity.this, error);

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
            dialog = ProgressDialog.show(this, "正在提交...", "请您稍等");
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
