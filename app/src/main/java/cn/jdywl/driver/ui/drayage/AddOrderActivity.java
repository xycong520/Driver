package cn.jdywl.driver.ui.drayage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.model.PriceItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public class AddOrderActivity extends BaseActivity implements AddOrderFragment.OnAddOrderFragmentListener {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(AddOrderActivity.class);

    @Bind(R.id.tv_totalBill)
    TextView tvTotalBill;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.submit_button)
    Button submitButton;

    ProgressDialog dialog;
    AddOrderFragment orderFragment;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ButterKnife.bind(this);
        setupToolbar();
        id = getIntent().getIntExtra("id", -1);

        // Create a new Fragment to be placed in the activity layout
        orderFragment = new AddOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        orderFragment.setArguments(bundle);
        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, orderFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        // Update your UI here.
                        updateSubmitBtn("提交");
                    }
                });
    }

    /*
    * 提交车单
    */
    public void submitOrder(View view) {
        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();

        //校验常规订单内容
        if (!orderFragment.verification()) {
            LogHelper.w(TAG, "订单提交校验失败");
            return;
        }


        //显示进度条，隐藏其它界面
        showProgress(true);

        //设置URl
        String url = ApiConfig.api_url + ApiConfig.SDRIVERS_DRAYAGE_URL;

        //添加order参数
        params.putAll(orderFragment.getParas());


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
                        Toast.makeText(AddOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showProgress(false);

                        Helper.processVolleyErrorMsg(AddOrderActivity.this, error);

                    }
                });

        myReq.setTag(TAG);
        myReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    /**
     * Shows the progress UI
     */
    public void showProgress(final boolean show) {

        if (show) {
            dialog = ProgressDialog.show(this, "正在提交...", "订单提交进行中");
        } else {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_order, menu);
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

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

    @Override
    public void updatePrice(PriceItem price) {
        if (price != null) {
            tvTotalBill.setText(String.format("总费用：%.2f元", price.getCharge() ));
//            tvPrice.setText(String.format("运费%1$d元", price.getCharge()));
        } else {
            tvTotalBill.setText("总费用：0元");
            tvPrice.setText("");
        }
    }

    @Override
    public void updateSubmitBtn(String name) {
        submitButton.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            orderFragment.onActivityResult(requestCode, resultCode, data);
//            orderFragment.setAddress(data.getStringExtra("address"),data.getStringExtra("addressX"),data.getStringExtra("addressY"));
        }
    }
}
