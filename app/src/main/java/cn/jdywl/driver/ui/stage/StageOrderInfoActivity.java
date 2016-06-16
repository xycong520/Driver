package cn.jdywl.driver.ui.stage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.config.OrderStatus;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.StageOrderItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by Administrator on 2016/5/12.
 */
public class StageOrderInfoActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(StageOrderInfoActivity.class);
    public static final int FROM_SPENDING = 1;//待拖运
    public static final int FROM_STRANSPORTING = 2;//运输中
    public static final int FROM_STODOS = 3;//已完成
    public static final int FROM_SMARKET = 4;//拖运市场
    public static final int FROM_SORDER = 5;//我的承运
    public static final int FROM_SHISTORY_ORDER = 6;//承运历史
    int form = 4;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tvStatus)
    TextView tvStatus;
    @Bind(R.id.tvPrice)
    TextView tvPrice;
    @Bind(R.id.tvBX)
    TextView tvBX;
    @Bind(R.id.tvPTP)
    TextView tvPTP;
    @Bind(R.id.tvStar)
    TextView tvStar;
    @Bind(R.id.tvEnd)
    TextView tvEnd;
    @Bind(R.id.tvTotal)
    TextView tvTotal;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.btn_submit)
    Button btSub;
    @Bind(R.id.layoutLocation)
    LinearLayout layoutLocation;
    StageOrderItem order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = getIntent().getParcelableExtra("order");
        setContentView(R.layout.activity_orderinfo);
        setupToolbar();
        ButterKnife.bind(this);
        form = getIntent().getIntExtra("from", 4);
        init();
    }

    private void init() {
        tvOrderNo.setText(order.getOrder_no());
        tvDate.setText(order.getSendtime());
        tvStatus.setText(OrderStatus.getDesc(order.getStatus()));
        tvName.setText(order.getReceiver_name());
        tvPhone.setText(order.getReceiver_phone());
        tvEnd.setText(order.getTo_address());
        tvStar.setText(order.getFrom_address());
        tvPTP.setText(order.getOrigin() + "-" + order.getDestination());
        tvBX.setText("￥：" + order.getInsurance());
        tvPrice.setText("￥：" + order.getCar_price());
        tvTotal.setText("￥：" + order.getCharge());
        if (order.getStatus() == OrderStatus.ORDER_TRADING) {
            layoutLocation.setVisibility(View.VISIBLE);
            tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StageOrderInfoActivity.this, CarLocationActivity.class);
                    intent.putExtra("id", order.getId());
                    startActivity(intent);
                }
            });
        } else {
            layoutLocation.setVisibility(View.GONE);
        }
        switch (form) {
            case FROM_SPENDING:
                btSub.setText("取消订单");
                break;
            case FROM_STRANSPORTING:
                btSub.setVisibility(View.GONE);
                break;
            case FROM_STODOS:
                btSub.setVisibility(View.GONE);
                break;
            case FROM_SMARKET:
                break;
            case FROM_SORDER:
                btSub.setText("完成订单");
                break;
            case FROM_SHISTORY_ORDER:
                btSub.setVisibility(View.GONE);
                break;
        }
        btSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });

    }

    private void accept() {
        String url = "";
        if (form == FROM_SMARKET) {
            url = ApiConfig.api_url + ApiConfig.STAGE_ORDER_URL.replace("/", "/" + order.getId() + "/");
        } else if (form == FROM_SPENDING) {
            url = ApiConfig.api_url + ApiConfig.STAGE_CANCEL_URL.replace("/", "/" + order.getId() + "/");
        } else if (form == FROM_SORDER) {
            url = ApiConfig.api_url + ApiConfig.STAGE_FINISH_URL.replace("/", "/" + order.getId() + "/");
        }
        AppConst.showDialog(this);
        GsonRequest<StageOrderItem> myReq = new GsonRequest<StageOrderItem>(Request.Method.PUT,
                url,
                StageOrderItem.class,
                null,
                new Response.Listener<StageOrderItem>() {
                    @Override
                    public void onResponse(StageOrderItem response) {
                        AppConst.dismiss();
                        if (form == FROM_SMARKET) {
                            Toast.makeText(StageOrderInfoActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
                        } else if (form == FROM_SPENDING) {
                            Toast.makeText(StageOrderInfoActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                        } else if (form == FROM_SORDER) {
                            Toast.makeText(StageOrderInfoActivity.this, "完成订单成功", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConst.dismiss();
                        Helper.processVolleyErrorMsg(StageOrderInfoActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(StageOrderInfoActivity.this).addToRequestQueue(myReq);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setRefreshEnabled(boolean enabled) {

    }

    @Override
    protected void cancelVolleyRequest(RequestQueue queue) {

    }
}
