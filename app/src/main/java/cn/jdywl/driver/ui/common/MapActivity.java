package cn.jdywl.driver.ui.common;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.LocationEntry;
import cn.jdywl.driver.network.GsonRequest;

public class MapActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LogHelper.makeLogTag(MapActivity.class);

    protected static final int RELOCATION = 0;

    @Bind(R.id.map_container)
    LinearLayout mapContainer;

    private MyLocationConfiguration config;
    private MyLocationConfiguration unenableConfig;
    private BitmapDescriptor myLocationMarker;
    private BitmapDescriptor myLocationMarkerUnenable;
    private ImageView mBackButton = null;

    private MapView mMapView = null;
    private BaiduMap mMap = null;
    private CoordinateConverter converter = null;

    private String mMessage = "";
    private String mDatetime = "";
    private String mPhone = "";
    private boolean mIsNeareatPos;

    Timer timer;
    TimerTask task;

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null && PushUtils.ACTION_NEED_REFRESH_LIST.equals(intent.getAction())) {
//                // TODO 根据push获取到的数据，更新地图
//                double[] d = new double[]{31.222184, 121.503966};
//                try {
//                    updateMyLocation(d);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        setupToolbar();

//        mMapView = (MapView) findViewById(R.id.bmapView);
//        IntentFilter filter = new IntentFilter(
//                PushUtils.ACTION_NEED_REFRESH_LIST);
//        registerReceiver(receiver, filter);

        mPhone = getIntent().getStringExtra("phone");

        BaiduMapOptions mapOpt = new BaiduMapOptions();
        MapStatus.Builder builder = new MapStatus.Builder();
        //builder.zoom(30);
        mapOpt.zoomControlsEnabled(true);
        mapOpt.mapStatus(builder.build());
        mMapView = new MapView(this, mapOpt);
        mapContainer.addView(mMapView);
        mMap = mMapView.getMap();
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        myLocationMarker = BitmapDescriptorFactory.fromResource(R.drawable.map_car_enable);
        myLocationMarkerUnenable = BitmapDescriptorFactory.fromResource(R.drawable.map_car_unenable);
        config = new MyLocationConfiguration(LocationMode.FOLLOWING, true, myLocationMarker);
        unenableConfig = new MyLocationConfiguration(LocationMode.FOLLOWING, true, myLocationMarkerUnenable);
        mMap.setMyLocationConfigeration(config);
        mMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            public void onMapClick(LatLng latLng) {
                mMap.hideInfoWindow();
            }

            public boolean onMapPoiClick(MapPoi mapPoi) {
                return true;
            }
        });
        mMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {
            public boolean onMyLocationClick() {
                InfoWindow mInfoWindow;
                TextView location = new TextView(getApplicationContext());
                //location.setBackgroundColor(0xe8e8e8);
                location.setBackgroundResource(R.drawable.map_info_bg);
                location.setPadding(30, 20, 30, 50);

                location.setText(Html.fromHtml(String.format("<b>%s</b> - %s<br>%s", mPhone, mDatetime, mMessage)));

                final LatLng ll = new LatLng(mMap.getLocationData().latitude, mMap.getLocationData().longitude);
                Point p = mMap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng llInfo = mMap.getProjection().fromScreenLocation(p);
                mInfoWindow = new InfoWindow(location, llInfo, 0);

                mMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });


        //test
        //timer.schedule(task,500,1000*60);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getLocation(getIntent().getStringExtra("phone"));
            }
            super.handleMessage(msg);
        }

    };

    private class SendTimerTask extends TimerTask {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        String action = intent.getAction();
        if (action != null) {
            /*
            if (action.equals(ACTION_LOGIN_OK)) {

            }
            */
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onResume();

        //开启定时器，获取位置信息
        timer = new Timer();
        task = new SendTimerTask();
        timer.schedule(task, 500, 1000 * 60);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();

        //关闭定时器和任务
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.map_title_left_image:
//                finish();
//                break;

            default:
                break;
        }
    }

    private void updateMyLocation(double[] d) throws RemoteException {
        mMap.setMyLocationConfigeration(mIsNeareatPos ? config : unenableConfig);

        LatLng desLatLng = convert(new LatLng(d[0], d[1]));
        MyLocationData locData = new MyLocationData.Builder().latitude(desLatLng.latitude).longitude(desLatLng.longitude).build();
        mMap.setMyLocationData(locData);
        MapStatusUpdate status = MapStatusUpdateFactory.newLatLngZoom(desLatLng, 30);
        mMap.setMapStatus(status);

    }

    private LatLng convert(LatLng src) {
        if (converter == null) {
            converter = new CoordinateConverter();
        }
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(src);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    private void getLocation(String phone) {
        String url = ApiConfig.api_url + ApiConfig.LOCATION_URL + "&phone=" + phone;

        LogHelper.i("Map", "getLocation--" + url);

        // Request a JSON response from the provided URL.
        GsonRequest<LocationEntry> myReq = new GsonRequest<LocationEntry>(Request.Method.GET,
                //"http://validate.jsontest.com/?json={'key':'value'}",
                url,
                LocationEntry.class,
                null,
                new Response.Listener<LocationEntry>() {
                    @Override
                    public void onResponse(LocationEntry response) {
//                        showProgress(false);

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        double la = Double.parseDouble(response.getLatitude());
                        double lo = Double.parseDouble(response.getLongitude());
                        double[] d = new double[]{la, lo};
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = format.parse(response.getUpdatedAt());
                            Date now = new Date();

                            mIsNeareatPos = Math.abs(date.getTime() - now.getTime()) <= 1000 * 60 * 60 * 24;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mDatetime = response.getUpdatedAt();
                        mMessage = response.getPosition();
                        try {
                            updateMyLocation(d);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "位置获取失败"/* + error.getMessage()*/, Toast.LENGTH_SHORT).show();
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
