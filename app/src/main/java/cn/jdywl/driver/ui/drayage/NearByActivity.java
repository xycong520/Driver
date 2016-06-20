package cn.jdywl.driver.ui.drayage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.JindouyunApplication;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.BeanGPS;
import cn.jdywl.driver.model.BeanNearDriver;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.stage.BaiduMapUtilByRacer;
import cn.jdywl.driver.ui.stage.ILocationWatcher;

/**
 * Created by xycong on 2016/6/13.
 */
public class NearByActivity extends BaseActivity implements ILocationWatcher {
    private static String TAG = LogHelper.makeLogTag(NearByActivity.class);


    @Bind(R.id.btn_submit)
    Button btAdd;
    @Bind(R.id.mMapView)
    MapView mMapView;
    List<BeanNearDriver> mData = new ArrayList<>();

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);
        setupToolbar();
        id = getIntent().getIntExtra("id", -1);
        AppConst.showDialog(this);
        location();
        init();
    }

    private void init() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearByActivity.this, AddOrderActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
            }
        });
    }

    private void loadNearBy() {
        String url = ApiConfig.api_url + ApiConfig.SDRIVERS_NEARBY_URL
                + "&city=" + city + "&longitude=" + lon + "&latitude=" + lat;
        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Receive Location
                        AppConst.dismiss();
                        if (response != null) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    mData.add(new BeanNearDriver(response.getJSONObject(i)));
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
                        //移动到自己的当前位子
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                lat, lon, R.drawable.icon_point,
                                mBaiduMap, 0, true);
                        for (int i = 0; i < mData.size(); i++) {
                            BeanNearDriver driver = mData.get(i);
                            View view = getLayoutInflater().inflate(R.layout.layout_orerlay, null);
                            TextView tvName = (TextView) view.findViewById(R.id.tvName);
                            tvName.setText(driver.getName() + "\n" + driver.getPhone());
                            if (driver.getGps().size()>0){
                                BeanGPS gps = driver.getGps().get(0);
                                if (gps == null) {
                                    continue;
                                }
                                BaiduMapUtilByRacer.addOverlay(tvName, gps.getLatitude(), gps.getLongitude(), mBaiduMap);
                            }else{
                                continue;
                            }

//                            BaiduMapUtilByRacer.addOverlay( R.drawable.icon_point, driver.getGps().get(0).getLatitude(), driver.getGps().get(0).getLongitude(), mBaiduMap);
                        }
//                        BaiduMapUtilByRacer.setZoom(12, mBaiduMap);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                AppConst.dismiss();
                Helper.processVolleyErrorMsg(NearByActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    public void location() {
        ((JindouyunApplication) getApplication()).setmLocationWatcher(this);
        LocationClient mLocationClient = ((JindouyunApplication) getApplication()).mLocationClient;
        mLocationClient.requestLocation();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
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
        loadNearBy();
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
