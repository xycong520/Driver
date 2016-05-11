package cn.jdywl.driver.ui.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.PriceItem;
import cn.jdywl.driver.model.RouteItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.carowner.AddOrderFragment;
import cn.jdywl.driver.ui.carowner.CartypeSelectActivity;
import cn.jdywl.driver.ui.carowner.CityActivity;

public class PriceQueryActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(PriceQueryActivity.class);

    @Bind(R.id.et_cartype)
    EditText etCartype;
    @Bind(R.id.et_carnum)
    EditText etCarnum;
    @Bind(R.id.et_carPrice)
    EditText etCarPrice;
    @Bind(R.id.et_origin)
    EditText etOrigin;
    @Bind(R.id.et_destination)
    EditText etDestination;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.tv_marketprice)
    TextView tvMarketprice;
    @Bind(R.id.tv_deposit_desc)
    TextView tvDepositDesc;
    @Bind(R.id.tv_deposit)
    TextView tvDeposit;
    @Bind(R.id.tv_insurance)
    TextView tvInsurance;
    @Bind(R.id.tv_totalBill)
    TextView tvTotalBill;
    @Bind(R.id.rl_price)
    RelativeLayout rlPrice;
    @Bind(R.id.rb_new)
    RadioButton rbNew;
    @Bind(R.id.rb_old)
    RadioButton rbOld;
    @Bind(R.id.rg_oldCar)
    RadioGroup rgOldCar;
    @Bind(R.id.rb_srvNone)
    RadioButton rbSrvNone;
    @Bind(R.id.rb_srvRegulatory)
    RadioButton rbSrvRegulatory;
    @Bind(R.id.rb_srvCredit)
    RadioButton rbSrvCredit;
    @Bind(R.id.rg_addtionalSrv)
    RadioGroup rgAddtionalSrv;
    @Bind(R.id.ib_help)
    ImageButton ibHelp;
    @Bind(R.id.ll_addtionalSrv)
    LinearLayout llAddtionalSrv;

    private RouteItem mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_query);
        ButterKnife.bind(this);
        setupToolbar();

        etCarnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0) {
                    if (Integer.valueOf(s.toString()) >= ApiConfig.CAR_SIZE) {
                        tvDepositDesc.setText("服务费");

                    } else {
                        tvDepositDesc.setText("服务费(含取车费)");
                    }

                    calculateBill();
                } else {
                    resetBill();
                }
            }
        });

        etCarPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0) {
                    if ((Integer.valueOf(s.toString()) <= AppConst.MAX_TOTAL_PRICE)
                            && (Integer.valueOf(s.toString()) >= AppConst.MIN_TOTAL_PRICE)) {

                        calculateBill();
                    } else {
                        resetBill();
                    }

                }
            }
        });

        rgAddtionalSrv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateBill();
            }
        });

        rgOldCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateBill();
            }
        });

        ibHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PriceQueryActivity.this);
                WebView wv = new WebView(PriceQueryActivity.this);
                wv.loadUrl(ApiConfig.api_url + ApiConfig.WEB_ADDTIONAL_SERVICE);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);

                alert.setPositiveButton("关闭", null);
                alert.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_support, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_call:
                final boolean telAvailable = Helper.isIntentAvailable(this, Intent.ACTION_DIAL);
                if (telAvailable) {
                    it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006501387"));
                    startActivity(it);
                } else {
                    Toast.makeText(this, "未安装电话应用，无法拨打电话", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == AddOrderFragment.SELECT_ORIGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("result");
                if (!city.equals(etOrigin.getText().toString())) {   //重新选择origin后需要清空destination

                    etOrigin.setText(city);
                    etOrigin.setError(null);
                    etDestination.setText("");
                    //价格清零
                    resetBill();
                }
            }
        } else if (requestCode == AddOrderFragment.SELECT_DESTINATION) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("result");
                if (!city.equals(etDestination.getText().toString())) {   //重新选择destination后需要重新获取新的价格
                    etDestination.setText(city);
                    etDestination.setError(null);

                    //重新计算价格
                    calculateBill();

                    getRoutePrice();
                }
            }
        } else {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String cartype = data.getStringExtra("result");
                if (!cartype.equals(etCartype.getText().toString())) {   //重新选择cartype后需要重新获取新的价格
                    etCartype.setText(cartype);
                    etCartype.setError(null);

                    //重新计算价格
                    calculateBill();
                }
            }
        }
    }

    /**
     * 选择轿车类型
     *
     * @param view
     */
    public void selectCartype(View view) {
        Intent it = new Intent(this, CartypeSelectActivity.class);
        startActivityForResult(it, AddOrderFragment.SELECT_CARTYPE);
    }

    /**
     * 选择始发地
     *
     * @param view
     */
    public void selectOrigin(View view) {
        Intent it = new Intent(this, CityActivity.class);
        it.putExtra(AddOrderFragment.B_ORIGIN, true);
        startActivityForResult(it, AddOrderFragment.SELECT_ORIGIN);
    }

    /**
     * 选择目的地
     *
     * @param view
     */
    public void selectDestination(View view) {
        if (etOrigin.getText().toString().isEmpty()) {
            Toast.makeText(this, "请先选择始发地", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(this, CityActivity.class);
        it.putExtra(AddOrderFragment.B_ORIGIN, false);
        it.putExtra(AddOrderFragment.ORIGIN_CITY, etOrigin.getText().toString());
        startActivityForResult(it, AddOrderFragment.SELECT_DESTINATION);
    }

    //在运输区间变化是，需要重置价格
    void resetBill() {
        tvDeposit.setText("");
        tvTotalBill.setText("");
        tvInsurance.setText("");
        tvMarketprice.setText("");
    }

    void calculateBill() {

        //先复位再计算
        resetBill();

        String origin = etOrigin.getText().toString();
        if (origin.isEmpty()) {
            LogHelper.w(TAG, "始发地无效");
            return;
        }

        String destination = etDestination.getText().toString();
        if (destination.isEmpty()) {
            LogHelper.w(TAG, "目的地无效");
            return;
        }

        //校验轿车类型
        String cartype = etCartype.getText().toString();
        if (cartype.isEmpty()) {
            LogHelper.w(TAG, "轿车类型为空");
            etCartype.setError("轿车类型无效");
            etCartype.requestFocus();
            return;
        }

        //校验car num
        String carNumString = etCarnum.getText().toString();
        if (carNumString.isEmpty() || !Helper.isInteger(carNumString)) {
            return;
        }
        int car_num = Integer.valueOf(carNumString);
        if (car_num > 100) {
            etCarnum.setError("不能大于100台");
            return;
        } else if (car_num == 0) {
            Toast.makeText(this, "轿车数量不能为0", Toast.LENGTH_SHORT).show();
        } else {
            etCarnum.setError(null);
        }

        //校验轿车总价
        String totalPriceString = etCarPrice.getText().toString();
        if (totalPriceString.isEmpty()) {
            return;
        }
        int totalPrice = Integer.valueOf(totalPriceString);
        if (totalPrice > AppConst.MAX_TOTAL_PRICE || totalPrice < AppConst.MIN_TOTAL_PRICE) {
            //etCarPrice.setError(getString(R.string.totalprice_hint));
            Toast.makeText(PriceQueryActivity.this, getString(R.string.totalprice_hint), Toast.LENGTH_SHORT).show();
            etCarPrice.requestFocus();
            return;
        } else {
            etCarPrice.setError(null);
        }

        //在线计算价格
        calPrice();
    }

    //根据始发地加载目的地
    private void calPrice() {
        showProgress(true);
        String url = ApiConfig.api_url + ApiConfig.PRICE_QUERY
                + "&brand=" + URLEncoder.encode(etCartype.getText().toString())
                + "&car_num=" + etCarnum.getText().toString()
                + "&totalCarPrice=" + etCarPrice.getText().toString()
                + "&origin=" + URLEncoder.encode(etOrigin.getText().toString())
                + "&destination=" + URLEncoder.encode(etDestination.getText().toString())
                + "&oldCar=" + getOldCar()
                + "&addtionalSrv=" + getAddtionalSrv();

        GsonRequest<PriceItem> myReq = new GsonRequest<PriceItem>(Request.Method.GET,
                url,
                PriceItem.class,
                null,
                new Response.Listener<PriceItem>() {
                    @Override
                    public void onResponse(PriceItem response) {
                        showProgress(false);
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        //取车费和增值服务费
                        tvDeposit.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%d元", response.getDeposit() + response.getSrvFee()));
                        tvTotalBill.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%d元", response.getTotalBill()));
                        tvInsurance.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%d元", response.getInsurance()));
                        tvMarketprice.setText(String.format(Locale.SIMPLIFIED_CHINESE, "%d元", response.getMarketPrice()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //返回错误统一处理
                        showProgress(false);
                        Toast.makeText(PriceQueryActivity.this, "价格查询失败，请检查您的网络", Toast.LENGTH_SHORT).show();
                        Helper.processVolleyErrorMsg(PriceQueryActivity.this, error);
                    }
                });


        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private String getAddtionalSrv() {
        if (rgAddtionalSrv.getCheckedRadioButtonId() == R.id.rb_srvCredit) {
            return "2";
        } else if (rgAddtionalSrv.getCheckedRadioButtonId() == R.id.rb_srvRegulatory) {
            return "1";
        } else {
            return "0";
        }
    }

    private String getOldCar() {
        if (rgOldCar.getCheckedRadioButtonId() == R.id.rb_old) {
            return "1";
        } else {
            return "0";
        }
    }

    void getRoutePrice() {
        if ((etOrigin.getText().toString().isEmpty()) || (etDestination.getText().toString().isEmpty())) {
            LogHelper.i(TAG, "始发地或者目的地为空，始发地：%s，目的地：%s", etOrigin.getText().toString(), etDestination.getText().toString());
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.ROUTE_PRICE_URL
                + "&origin=" + URLEncoder.encode(etOrigin.getText().toString())
                + "&destination=" + URLEncoder.encode(etDestination.getText().toString());

        GsonRequest<RouteItem> myReq = new GsonRequest<RouteItem>(Request.Method.GET,
                url,
                RouteItem.class,
                null,
                new Response.Listener<RouteItem>() {
                    @Override
                    public void onResponse(RouteItem response) {
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        mRoute = response;

                        //默认选中的增值服务为NONE
                        rbSrvNone.setChecked(true);

                        //控制增值服务类型的显示和隐藏
                        if (mRoute.getAddtionalSrv() == AddOrderFragment.SRV_REGULATORY) {
                            llAddtionalSrv.setVisibility(View.VISIBLE);
                            rbSrvCredit.setVisibility(View.INVISIBLE);
                        } else if (mRoute.getAddtionalSrv() == AddOrderFragment.SRV_CREDIT) {
                            llAddtionalSrv.setVisibility(View.VISIBLE);
                            rbSrvCredit.setVisibility(View.VISIBLE);
                        } else {
                            llAddtionalSrv.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.processVolleyErrorMsg(PriceQueryActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        rlPrice.setVisibility(show ? View.GONE : View.VISIBLE);
        rlPrice.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rlPrice.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressbar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
