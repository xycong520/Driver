package cn.jdywl.driver.ui.stage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.BeanGPS;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by xycong on 2016/6/13.
 */
public class CarLocationActivity extends BaseActivity implements ILocationWatcher {
    private static String TAG = LogHelper.makeLogTag(CarLocationActivity.class);


    @Bind(R.id.btn_submit)
    Button btAdd;
    @Bind(R.id.mMapView)
    MapView mMapView;
    List<BeanGPS> mData = new ArrayList<>();

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);
        setupToolbar();
        id = getIntent().getIntExtra("id", -1);
        init();
    }

    private void init() {
        btAdd.setVisibility(View.GONE);
        loadCarLocation();
    }

    private void loadCarLocation() {
        String url = ApiConfig.api_url + "express/"+id+"/gpses";

        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Receive Location
                        if (response != null) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    mData.add(new Gson().fromJson(response.getJSONObject(i).toString(),BeanGPS.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        mBaiduMap = mMapView.getMap();
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
                        mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
                        // 开启定位图层
                        mBaiduMap.setMyLocationEnabled(true);
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            mBaiduMap.clear();
                        }

                        for (int i = 0; i < mData.size(); i++) {
                            BeanGPS gps = mData.get(i);
                            if (gps == null) {
                                continue;
                            }
                            BaiduMapUtilByRacer.addOverlay( R.drawable.icon_point, lat =gps.getLatitude(), lon =gps.getLongitude(), mBaiduMap);
                        }
                        //移动到最后的位子
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                lat, lon, R.drawable.icon_point,
                                mBaiduMap, 0, true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                Helper.processVolleyErrorMsg(CarLocationActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    private BaiduMap mBaiduMap;
    double lat, lon;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;
    String city;

    @Override
    public void onLocationSuccess(BDLocation location) {
        city = location.getCity();
        lat = location.getLatitude();
        lon = (location.getLongitude());
        loadCarLocation();
    }

    @Override
    protected void setRefreshEnabled(boolean enabled) {

    }

    @Override
    protected void cancelVolleyRequest(RequestQueue queue) {

    }

    @Override
    protected void onDestroy() {
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        mMarker = null;
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
