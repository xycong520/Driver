package cn.jdywl.driver.ui.stage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import cn.jdywl.driver.ui.carowner.CityActivity;
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
    public static final int GETCAR_ADDRESS = 2;
    public static final int TO_ADDRESS = 3;
    public static final int SELECT_CARTYPE = 2;
    public static final String B_ORIGIN = "B_ORIGIN";
    public static final String ORIGIN_CITY = "ORIGIN_CITY";

    public static final int SRV_NONE = 0; //无
    public static final int SRV_REGULATORY = 1; //代收车款
    public static final int SRV_CREDIT = 2; //垫资发运

    private boolean rgEnabled = true;

    @Bind(R.id.tvBx)
    TextView tvBX;
    @Bind(R.id.et_carPrice)
    EditText etCarPrice;
    @Bind(R.id.et_origin)
    EditText etOrigin;
    @Bind(R.id.et_destination)
    EditText etDestination;
    @Bind(R.id.et_sendtime)
    EditText etSendtime;
    @Bind(R.id.et_fetchAddr)
    EditText etFetchAddr;
    @Bind(R.id.et_fetchName)
    EditText etFetchName;
    @Bind(R.id.et_fetchPhone)
    EditText etFetchPhone;
    @Bind(R.id.rg_buy)
    RadioGroup rgBuy;
    @Bind(R.id.et_getCarAddress)
    EditText etGetCarAddress;
    boolean isGetCarAddress;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String fLat, fLon, tLat, tLon;

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
        final View view = inflater.inflate(R.layout.fragment_add_stage_order, container, false);
        ButterKnife.bind(this, view);
        etGetCarAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGetCarAddress = true;
                startActivityForResult(new Intent(getActivity(), GetAddressInMap.class), GETCAR_ADDRESS);
            }
        });
        etFetchAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGetCarAddress = false;
                startActivityForResult(new Intent(getActivity(), GetAddressInMap.class), TO_ADDRESS);
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
                it.putExtra("isExpress", true);
                getActivity().startActivityForResult(it, SELECT_ORIGIN);
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
                it.putExtra("isExpress", true);
                it.putExtra(ORIGIN_CITY, etOrigin.getText().toString());
                startActivityForResult(it, SELECT_DESTINATION);
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


        rgBuy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getOldCar();
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

        if (requestCode == GETCAR_ADDRESS && resultCode == Activity.RESULT_OK) {
            etGetCarAddress.setText(data.getStringExtra("address"));
            fLat = data.getStringExtra("addressX");
            fLon = data.getStringExtra("addressY");
        } else if (requestCode == TO_ADDRESS && resultCode == Activity.RESULT_OK) {
            etFetchAddr.setText(data.getStringExtra("address"));
            tLat = data.getStringExtra("addressX");
            tLon = data.getStringExtra("addressY");
        }
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
            }
        }
    }

    //根据始发地加载目的地
    private void calPrice() {

        String url = ApiConfig.api_url + ApiConfig.STAGE_PRICE_URL
                + "&car_price=" + etCarPrice.getText().toString()
                + "&origin=" + URLEncoder.encode(etOrigin.getText().toString())
                + "&destination=" + URLEncoder.encode(etDestination.getText().toString());

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
                            tvBX.setText(mPrice.getInsurance() + "元");
                            mListener.updatePrice(mPrice);
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


    private int getOldCar() {
        if (rgBuy.getCheckedRadioButtonId() == R.id.rb_yes) {
            if (mPrice != null && mListener != null) {
                mPrice.setInsurance(Integer.valueOf(tvBX.getText().toString().replace("元", "")));
                mListener.updatePrice(mPrice);
            }
            return 1;
        } else {
            if (mPrice != null && mListener != null) {
                mPrice.setInsurance(0);
                mListener.updatePrice(mPrice);
            }
            return 0;
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
        String sender_addr = etFetchAddr.getText().toString();
        String sender_name = etFetchName.getText().toString();
        String sender_phone = etFetchPhone.getText().toString();
        String carPrice = etCarPrice.getText().toString();

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("sendtime", sendtime);
        params.put("car_price", carPrice);
        params.put("from_longitude", fLon);
        params.put("from_latitude", fLat);
        params.put("from_address", etGetCarAddress.getText().toString());
        params.put("to_longitude", tLon);
        params.put("to_latitude", tLat);
        params.put("to_address", sender_addr);
        params.put("receiver_name", sender_name);
        params.put("receiver_phone", sender_phone);
        params.put("ifIns", String.valueOf(getOldCar()));

        return params;
    }

    //在运输区间变化是，需要重置价格
    void resetBill() {
        mPrice = null;

        if (mListener != null) {
            mListener.updatePrice(null);
        }
    }

    void calculateBill() {


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
        etSendtime.setError(null);
        tvBX.setError(null);
        etCarPrice.setError(null);
        etFetchAddr.setError(null);
        etFetchName.setError(null);
        etFetchPhone.setError(null);
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
        if (TextUtils.isEmpty(etFetchName.getText().toString())) {
            etFetchName.setError("请输入收车人姓名");
            etFetchName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etFetchPhone.getText().toString())) {
            etFetchPhone.setError("请输入收车人电话");
            etFetchPhone.requestFocus();
            return false;
        }

        //验证手机号是否有效
        if (!Helper.isPhone(etFetchPhone.getText().toString())) {
            etFetchPhone.setError(getString(R.string.error_invalid_phone));
            etFetchPhone.requestFocus();
            return false;
        }

        return true;
    }

    void getRoutePrice() {
        if ((etOrigin.getText().toString().isEmpty()) || (etDestination.getText().toString().isEmpty())) {
            LogHelper.i(TAG, "始发地或者目的地为空，始发地：%s，目的地：%s", etOrigin.getText().toString(), etDestination.getText().toString());
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.STAGE_ROUTE_URL
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

                        if (mRoute.getExpress_price() <= 0) {
                            Toast.makeText(getActivity(), "不支持该目的地,请重新选择", Toast.LENGTH_SHORT).show();
                            etDestination.setText("");
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

    public void setAddress(String address, String lat, String lon) {
        if (isGetCarAddress) {
            etGetCarAddress.setText(address);
            fLat = lat;
            fLon = lon;
        } else {
            etFetchAddr.setText(address);
            tLat = lat;
            tLon = lon;
        }
    }


}
