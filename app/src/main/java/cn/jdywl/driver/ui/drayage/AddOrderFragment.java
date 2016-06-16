package cn.jdywl.driver.ui.drayage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

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
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.PriceItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseFragment;
import cn.jdywl.driver.ui.stage.BaiduMapUtilByRacer;
import cn.jdywl.driver.ui.stage.GetAddressInMap;

public class AddOrderFragment extends BaseFragment {
    private static String TAG = LogHelper.makeLogTag(AddOrderFragment.class);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final int SELECT_ORIGIN = 0;
    public static final int SELECT_DESTINATION = 1;
    public static final int GETCAR_ADDRESS = 2;
    public static final int TO_ADDRESS = 3;
    public static final int SELECT_CARTYPE = 2;
    public static final String B_ORIGIN = "B_ORIGIN";
    public static final String ORIGIN_CITY = "ORIGIN_CITY";


    @Bind(R.id.et_getCarAddress)
    EditText etGetCarAddress;
    @Bind(R.id.et_destination)
    EditText etDestination;
    @Bind(R.id.et_sendtime)
    EditText etSendtime;
    @Bind(R.id.et_fetchName)
    EditText etFetchName;
    @Bind(R.id.et_fetchPhone)
    EditText etFetchPhone;
    boolean isGetCarAddress;

    // TODO: Rename and change types of parameters
    String fLat, fLon, tLat, tLon;
    String city;
    int id;

    //运输线路价格
    PriceItem mPrice;


    private OnAddOrderFragmentListener mListener;

    public AddOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_drayage_order, container, false);
        ButterKnife.bind(this, view);
        //选择提车地
        etGetCarAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGetCarAddress = true;
                startActivityForResult(new Intent(getActivity(), GetAddressInMap.class), GETCAR_ADDRESS);
            }
        });
        //选择目的地
        etDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGetCarAddress = false;
                startActivityForResult(new Intent(getActivity(), GetAddressInMap.class), TO_ADDRESS);
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
            city = data.getStringExtra("cityName");
            fLat = data.getStringExtra("addressX");
            fLon = data.getStringExtra("addressY");
        } else if (requestCode == TO_ADDRESS && resultCode == Activity.RESULT_OK) {
            etDestination.setText(data.getStringExtra("address"));
            tLat = data.getStringExtra("addressX");
            tLon = data.getStringExtra("addressY");
            //重新计算价格
            resetBill();
            getRoutePrice();
        }
    }




    /*
    * 提交车单
    */
    Map<String, String> getParas() {

        //获取标单数据
        String origin = etGetCarAddress.getText().toString();
        String destination = etDestination.getText().toString();
        String sendtime = etSendtime.getText().toString();
        String sender_name = etFetchName.getText().toString();
        String sender_phone = etFetchPhone.getText().toString();
        String distance = BaiduMapUtilByRacer.getDistanceWithUtil(fLat,fLon,tLat,tLon);

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("station_id", String.valueOf(id));
        params.put("origin", origin);
        params.put("from_longitude", fLon);
        params.put("from_latitude", fLat);
        params.put("destination", destination);
        params.put("to_longitude", tLon);
        params.put("to_latitude", tLat);
        params.put("sendtime", sendtime);
        params.put("distance", distance);
        params.put("receiver_name", sender_name);
        params.put("receiver_phone", sender_phone);

        return params;
    }

    //在运输区间变化是，需要重置价格
    void resetBill() {
        mPrice = null;
        if (mListener != null) {
            mListener.updatePrice(null);
        }
    }


    boolean verification() {
        // Reset errors.
        etSendtime.setError(null);
        etFetchName.setError(null);
        etFetchPhone.setError(null);
        etGetCarAddress.setError(null);
        etDestination.setError(null);

        //etReceiverName.setError(null);
        //etReceiverPhone.setError(null);
        //etCarBrand.setError(null);

        String origin = etGetCarAddress.getText().toString();
        if (origin.isEmpty()) {
            LogHelper.w(TAG, "提车地无效");
            etGetCarAddress.setError("提车地无效");
            etGetCarAddress.requestFocus();
            return false;
        }

        String destination = etDestination.getText().toString();
        if (destination.isEmpty()) {
            LogHelper.w(TAG, "目的地无效");
            etDestination.setError("目的地无效");
            etDestination.requestFocus();
            return false;
        }


        // Check for a valid email address.
        if (TextUtils.isEmpty(etSendtime.getText().toString())) {
            etSendtime.setError(getString(R.string.error_invalid_sendtime));
            etSendtime.requestFocus();
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
        if ((etGetCarAddress.getText().toString().isEmpty()) || (etDestination.getText().toString().isEmpty())) {
            LogHelper.i(TAG, "提车地或者目的地为空，提车地：%s，目的地：%s", etGetCarAddress.getText().toString(), etDestination.getText().toString());
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.SDRIVERS_PRICE_URL
                + "&city=" + URLEncoder.encode(city.replace("市",""))
                + "&distance=" + URLEncoder.encode(BaiduMapUtilByRacer.getDistanceWithUtil(fLat,fLon,tLat,tLon));

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
                        mPrice = response;
                        if (mListener != null) {
                            mListener.updatePrice(mPrice);
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
