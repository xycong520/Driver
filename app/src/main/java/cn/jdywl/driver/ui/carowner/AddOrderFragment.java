package cn.jdywl.driver.ui.carowner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.common.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddOrderFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AddOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOrderFragment extends BaseFragment {
    private static String TAG = LogHelper.makeLogTag(AddOrderFragment.class);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int SELECT_ORIGIN = 0;
    public static final int SELECT_DESTINATION = 1;
    public static final int SELECT_CARTYPE = 2;
    public static final String B_ORIGIN = "B_ORIGIN";
    public static final String ORIGIN_CITY = "ORIGIN_CITY";

    public static final int SRV_NONE = 0; //无
    public static final int SRV_REGULATORY = 1; //代收车款
    public static final int SRV_CREDIT = 2; //垫资发运

    private boolean rgEnabled = true;

    @Bind(R.id.et_cartype)
    EditText etCartype;
    @Bind(R.id.et_carnum)
    EditText etCarnum;
    @Bind(R.id.et_carPrice)
    EditText etCarPrice;
    @Bind(R.id.tv_carPrice_desc)
    TextView tvCarPriceDesc;
    @Bind(R.id.et_origin)
    EditText etOrigin;
    @Bind(R.id.et_destination)
    EditText etDestination;
    @Bind(R.id.et_sendtime)
    EditText etSendtime;
    @Bind(R.id.tv_fetchAddr)
    TextView tvFetchAddr;
    @Bind(R.id.et_fetchAddr)
    EditText etFetchAddr;
    @Bind(R.id.stub_zhengban)
    ViewStub stubZhengban;
    @Bind(R.id.stub_sanche)
    ViewStub stubSanche;
    @Bind(R.id.rg_oldCar)
    RadioGroup rgOldCar;
    @Bind(R.id.rb_new)
    RadioButton rbNew;
    @Bind(R.id.rb_old)
    RadioButton rbOld;
    @Bind(R.id.rb_srvNone)
    RadioButton rbSrvNone;
    @Bind(R.id.rb_srvRegulatory)
    RadioButton rbSrvRegulatory;
    @Bind(R.id.rb_srvCredit)
    RadioButton rbSrvCredit;
    @Bind(R.id.rg_addtionalSrv)
    RadioGroup rgAddtionalSrv;
    @Bind(R.id.tv_addtionalSrv)
    TextView tvAddtionalSrv;
    @Bind(R.id.rl_addtionalSrv)
    RelativeLayout rlAddtionalSrv;
    @Bind(R.id.ib_help)
    Button ibHelp;

    //只用于整版的View
    EditText etExpectationPrice;
    EditText etCarBrand;

    //只用于散车的View
    EditText etReceiverName;
    EditText etReceiverPhone;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //运输线路价格
    PriceItem mPrice;

    private RouteItem mRoute;

    private OnAddOrderFragmentListener mListener;

    public AddOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOrderFragment newInstance(String param1, String param2) {
        AddOrderFragment fragment = new AddOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_order, container, false);
        ButterKnife.bind(this, view);

        /*
        ArrayAdapter spAdapter = ArrayAdapter.createFromResource(this, R.array.cartype_arrays, R.layout.spinner_item);
        spCartype.setAdapter(spAdapter);
        spCartype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateBill();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

        //SrvRadioGroupEnable(false);

        etCarnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if ((s.toString().length() > 0) && (Integer.valueOf(s.toString()) >= ApiConfig.CAR_SIZE)) {
                    stubZhengban.setVisibility(View.VISIBLE);
                    stubSanche.setVisibility(View.GONE);
                    tvFetchAddr.setText("装车地址");
                    etFetchAddr.setHint("输入装车地址");

                    //获取整版业务的View
                    etExpectationPrice = (EditText) view.findViewById(R.id.et_expectation_price);
                    etCarBrand = (EditText) view.findViewById(R.id.et_car_brand);

                    etReceiverName = null;
                    etReceiverPhone = null;
                } else {
                    stubZhengban.setVisibility(View.GONE);
                    stubSanche.setVisibility(View.VISIBLE);
                    tvFetchAddr.setText("取车地址");
                    etFetchAddr.setHint("输入上门取车地址");

                    //获取散车业务的View
                    etReceiverName = (EditText) view.findViewById(R.id.et_receiver_name);
                    etReceiverPhone = (EditText) view.findViewById(R.id.et_receiver_phone);

                    etExpectationPrice = null;
                    etCarBrand = null;
                }

                calculateBill();
            }
        });

        /*
        //轿车总价焦点改变时重新计算价格
        etCarPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String price = etCarPrice.getText().toString();
                    if (price.isEmpty()) {
                        Toast.makeText(AddOrderActivity.this, "轿车总价不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if ((Integer.valueOf(price) <= AppConst.MAX_TOTAL_PRICE)
                            && (Integer.valueOf(price) >= AppConst.MIN_TOTAL_PRICE)) {

                        calculateBill();
                    } else {
                        Toast.makeText(AddOrderActivity.this, getString(R.string.totalprice_hint), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        */

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
                        //Toast.makeText(AddOrderActivity.this, getString(R.string.totalprice_hint), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "轿车总价不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //选择始发地
        etOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), CityActivity.class);
                it.putExtra(B_ORIGIN, true);
                startActivityForResult(it, SELECT_ORIGIN);
            }
        });

        //选择目的地
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOrigin.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请先选择始发地", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent it = new Intent(getActivity(), CityActivity.class);
                it.putExtra(B_ORIGIN, false);
                it.putExtra(ORIGIN_CITY, etOrigin.getText().toString());
                startActivityForResult(it, SELECT_DESTINATION);
            }
        });

        //选择轿车类型
        etCartype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), CartypeSelectActivity.class);
                startActivityForResult(it, SELECT_CARTYPE);
            }
        });

        //选择启运日期
        etSendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                new DatePickerDialog(getActivity(),
                        // 绑定监听器
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etSendtime.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }
                        // 设置初始日期
                        , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ibHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_webview);
                WebView webView = (WebView) dialog.findViewById(R.id.webview);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });
                webView.loadUrl(ApiConfig.api_url+"mobile/service");
                dialog.setCancelable(true);
                dialog.setTitle("增值服务介绍");
                dialog.show();
                */

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                WebView wv = new WebView(getActivity());
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

        rgAddtionalSrv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_srvCredit) {
                    mListener.updateSubmitBtn("下一步");
                } else {
                    mListener.updateSubmitBtn("提交");
                }

                calculateBill();
            }
        });

        rgOldCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculateBill();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddOrderFragmentListener) {
            mListener = (OnAddOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == SELECT_ORIGIN) {
            // Make sure the request was successful
            if (resultCode == BaseActivity.RESULT_OK) {
                String city = data.getStringExtra("result");
                if (!city.equals(etOrigin.getText().toString())) {   //重新选择origin后需要清空destination

                    etOrigin.setText(city);
                    etOrigin.setError(null);
                    etDestination.setText("");
                    //价格清零
                    resetBill();
                }
            }
        } else if (requestCode == SELECT_DESTINATION) {
            // Make sure the request was successful
            if (resultCode == BaseActivity.RESULT_OK) {
                String city = data.getStringExtra("result");
                if (!city.equals(etDestination.getText().toString())) {   //重新选择destination后需要重新获取新的价格
                    etDestination.setText(city);
                    etDestination.setError(null);

                    //重新计算价格
                    resetBill();
                    calculateBill();

                    getRoutePrice();
                }
            }
        } else {
            // Make sure the request was successful
            if (resultCode == BaseActivity.RESULT_OK) {
                String cartype = data.getStringExtra("result");
                if (!cartype.equals(etCartype.getText().toString())) {   //重新选择cartype后需要重新获取新的价格
                    etCartype.setText(cartype);
                    etCartype.setError(null);

                    //重新计算价格
                    resetBill();
                    calculateBill();
                }
            }
        }
    }

    //根据始发地加载目的地
    private void calPrice() {

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
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        //获取分页信息
                        mPrice = response;
                        if (mListener != null) {
                            mListener.updatePrice(mPrice);
                        }

                        //如果是整板运输，设置默认心里价位为平台总价
                        String carnum = etCarnum.getText().toString();
                        if ((!carnum.isEmpty()) && Integer.valueOf(carnum) >= ApiConfig.CAR_SIZE && etExpectationPrice != null) {
                            etExpectationPrice.setHint(String.format("市场运价%d元", mPrice.getMarketPrice()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //返回错误统一处理
                        Helper.processVolleyErrorMsg(getActivity(), error);
                    }
                });


        myReq.setTag(TAG);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
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

    /*
    * 提交车单
    */
    Map<String, String> getParas() {

        //获取标单数据
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        String sendtime = etSendtime.getText().toString();
        String brand = etCartype.getText().toString();
        String carnum = etCarnum.getText().toString();
        String sender_addr = etFetchAddr.getText().toString();
        String carPrice = etCarPrice.getText().toString();

        //设置URl
        String url = ApiConfig.api_url + ApiConfig.SUBMIT_ORDER_URL;

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("sendtime", sendtime);
        params.put("brand", brand);
        params.put("car_num", carnum);
        params.put("street", sender_addr);
        params.put("totalCarPrice", carPrice);
        params.put("addtionalSrv", getAddtionalSrv());
        params.put("oldCar", getOldCar());

        if (mPrice != null) {
            params.put("market_price", String.valueOf(mPrice.getTotalBill()));
            //总预付款等于取车费+保险+增值服务费
            params.put("deposit", String.valueOf(mPrice.getDeposit() + mPrice.getInsurance() + mPrice.getSrvFee()));
            params.put("driverDeposit", String.valueOf(mPrice.getDriverDeposit()));
            params.put("driverBill", String.valueOf(mPrice.getMarketPrice()));
        }

        if (Integer.valueOf(carnum) >= ApiConfig.CAR_SIZE) {
            if (etExpectationPrice != null) {
                params.put("expectation_price", etExpectationPrice.getText().toString());
            } else {
                params.put("expectation_price", "0");
            }
            if (etCarBrand != null) {
                params.put("model", etCarBrand.getText().toString());
            }
        } else {
            if (etReceiverName != null) {
                params.put("receiver_name", etReceiverName.getText().toString());
            }
            if (etReceiverPhone != null) {
                params.put("receiver_phone", etReceiverPhone.getText().toString());
            }
        }

        return params;
    }

    //enable or disable增值服务radio group
    void SrvRadioGroupEnable(boolean enabled) {
        if (enabled != rgEnabled) {
            for (int i = 0; i < rgAddtionalSrv.getChildCount(); i++) {
                rgAddtionalSrv.getChildAt(i).setEnabled(enabled);
            }
            rgEnabled = enabled;
        }
    }

    //在运输区间变化是，需要重置价格
    void resetBill() {
        mPrice = null;

        if (mListener != null) {
            mListener.updatePrice(null);
        }
    }

    void calculateBill() {

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
            Toast.makeText(getActivity(), "轿车数量不能为0", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), getString(R.string.totalprice_hint), Toast.LENGTH_SHORT).show();
            etCarPrice.requestFocus();
            return;
        } else {
            etCarPrice.setError(null);
        }

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

        resetBill();
        //在线计算价格
        calPrice();
    }

    boolean verification() {
        // Reset errors.
        etCartype.setError(null);
        etSendtime.setError(null);
        etCarnum.setError(null);
        etCarPrice.setError(null);
        etFetchAddr.setError(null);
        etOrigin.setError(null);
        etDestination.setError(null);

        //etReceiverName.setError(null);
        //etReceiverPhone.setError(null);
        //etCarBrand.setError(null);

        String origin = etOrigin.getText().toString();
        if (origin.isEmpty()) {
            LogHelper.w(TAG, "始发地无效");
            etOrigin.setError("始发地无效");
            etOrigin.requestFocus();
            return false;
        }

        String destination = etDestination.getText().toString();
        if (destination.isEmpty()) {
            LogHelper.w(TAG, "目的地无效");
            etDestination.setError("目的地无效");
            etDestination.requestFocus();
            return false;
        }

        //校验轿车类型
        String cartype = etCartype.getText().toString();
        if (cartype.isEmpty()) {
            LogHelper.w(TAG, "轿车类型为空");
            etCartype.setError("轿车类型无效");
            return false;
        }

        String mCarnum = etCarnum.getText().toString();
        if (TextUtils.isEmpty(mCarnum)) {
            etCarnum.setError(getString(R.string.error_invalid_carnum));
            etCarnum.requestFocus();
            return false;
        }
        if (Integer.valueOf(mCarnum) > 100 || Integer.valueOf(mCarnum) <= 0) {
            etCarnum.setError("轿车数量必须在1到99台之间");
            etCarnum.requestFocus();
            return false;
        }

        String mTotalPrice = etCarPrice.getText().toString();
        if (TextUtils.isEmpty(mTotalPrice)) {
            etCarPrice.setError(getString(R.string.error_invalid_carprice));
            etCarPrice.requestFocus();
            return false;
        }

        if (Integer.valueOf(mTotalPrice) > AppConst.MAX_TOTAL_PRICE ||
                Integer.valueOf(mTotalPrice) < AppConst.MIN_TOTAL_PRICE) {
            etCarPrice.setError(getString(R.string.totalprice_hint));
            etCarPrice.requestFocus();
            return false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(etSendtime.getText().toString())) {
            etSendtime.setError(getString(R.string.error_invalid_sendtime));
            etSendtime.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etFetchAddr.getText().toString())) {
            etFetchAddr.setError(getString(R.string.error_invalid_senderAddr));
            etFetchAddr.requestFocus();
            return false;
        }

        if (Integer.valueOf(mCarnum) >= ApiConfig.CAR_SIZE) {
            if (TextUtils.isEmpty(etExpectationPrice.getText().toString())) {
                etExpectationPrice.setError(getString(R.string.error_invalid_price));
                etExpectationPrice.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(etCarBrand.getText().toString())) {
                etCarBrand.setError(getString(R.string.error_invalid_carmodel));
                etCarBrand.requestFocus();
                return false;
            }
        } else {
            if (TextUtils.isEmpty(etReceiverName.getText().toString())) {
                etReceiverName.setError(getString(R.string.error_invalid_receiverName));
                etReceiverName.requestFocus();
                return false;
            }
            if (TextUtils.isEmpty(etReceiverPhone.getText().toString())) {
                etReceiverPhone.setError(getString(R.string.error_invalid_receiverPhone));
                etReceiverPhone.requestFocus();
                return false;
            }

            //验证手机号是否有效
            if (!Helper.isPhone(etReceiverPhone.getText().toString())) {
                etReceiverPhone.setError(getString(R.string.error_invalid_phone));
                etReceiverPhone.requestFocus();
                return false;
            }
        }

        return true;
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
                        if (mRoute.getAddtionalSrv() == SRV_REGULATORY) {
                            tvAddtionalSrv.setVisibility(View.VISIBLE);
                            rlAddtionalSrv.setVisibility(View.VISIBLE);
                            rbSrvCredit.setVisibility(View.INVISIBLE);
                            //SrvRadioGroupEnable(true);
                        } else if (mRoute.getAddtionalSrv() == SRV_CREDIT) {
                            tvAddtionalSrv.setVisibility(View.VISIBLE);
                            rlAddtionalSrv.setVisibility(View.VISIBLE);
                            rbSrvCredit.setVisibility(View.VISIBLE);
                            //SrvRadioGroupEnable(true);
                        } else {
                            rlAddtionalSrv.setVisibility(View.GONE);
                            tvAddtionalSrv.setVisibility(View.GONE);
                            //SrvRadioGroupEnable(false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helper.processVolleyErrorMsg(getActivity(), error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddOrderFragmentListener {
        void updatePrice(PriceItem price);

        void updateSubmitBtn(String name);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
