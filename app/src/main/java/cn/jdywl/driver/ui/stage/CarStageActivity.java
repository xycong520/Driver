package cn.jdywl.driver.ui.stage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.stage.CarStageAdapter;
import cn.jdywl.driver.adapter.stage.CommonAdapter;
import cn.jdywl.driver.adapter.stage.ViewHolder;
import cn.jdywl.driver.app.JindouyunApplication;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.libsrc.recylerview.EndlessRecyclerOnScrollListener;
import cn.jdywl.driver.model.CarStagePage;
import cn.jdywl.driver.model.Stage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.LoginActivity;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by Administrator on 2016/5/6.
 */
public class CarStageActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener, ILocationWatcher {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(CarStageActivity.class);

    public static final String REFRESH_ACTION = "CarRefreshAction";
    List<String> citys;
    List<String> provinceItemList;
    @Bind(R.id.tvNear)
    TextView tvNear;
    @Bind(R.id.tvPro)
    TextView tvPro;
    @Bind(R.id.tvCity)
    TextView tvCity;
    @Bind(R.id.rv_market)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.mMapView)
    MapView mapView;
    private BaiduMap mBaiduMap;
    @Bind(R.id.tvShowMap)
    TextView tvShowMap;
    private boolean bReload = false;   //
    private boolean loading = false;   //是否正在加载

    protected CarStageAdapter mAdapter;

    private CarStagePage mData = new CarStagePage();

    boolean isMapView = false;
    private BeanLocation mLocationBean;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carstage);
        setupToolbar();

        ButterKnife.bind(this);

        if (!AppConfig.isLogin()) {
            Intent it = new Intent(this, LoginActivity.class);
            startActivityForResult(it, LoginActivity.LOGIN);
        }

        /*
         * 设置下拉刷新
         */
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        setupRecyclerView(mRecyclerView);
        //注册local Broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mRefreshReceiver, new IntentFilter(CarStageActivity.REFRESH_ACTION));
        init();
    }

    boolean isProvinceSelect = false;

    private void init() {
        tvNear.setVisibility(View.GONE);
        tvPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProvinceSelect = true;
                showPw(v, tvPro, provinceItemList);
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCity();
            }
        });
        tvShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMapView = !isMapView;
                if (isMapView) {
                    tvShowMap.setText("显示列表");
                    mBaiduMap = mapView.getMap();
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
//                    mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
//                    mBaiduMap.setOnMapClickListener(mapOnClickListener);
                    mBaiduMap.getUiSettings().setZoomGesturesEnabled(true);// 缩放手势
                    // 开启定位图层
                    mBaiduMap.setMyLocationEnabled(true);
                    location();
                } else {
                    tvShowMap.setText("显示地图");
                }
                mapView.setVisibility(mapView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mRecyclerView.setVisibility(mRecyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LoginActivity.LOGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //登录成功
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //登录不成功，finish
        finish();
    }


    public void setRefreshEnabled(boolean enabled) {
        Intent intent = new Intent(REFRESH_ACTION);
        intent.putExtra("enabled", enabled);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

    //设置RecyclerView
    private void setupRecyclerView(RecyclerView recyclerView) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // 提高性能
        recyclerView.setHasFixedSize(true);
        //设置为线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        //设置adapter
        mAdapter = new CarStageAdapter(mData.getData());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positon = (int) v.getTag();
                Intent i = new Intent(CarStageActivity.this, StageInfoActivity.class);
                i.putExtra(StageInfoActivity.ID, mData.getData().get(positon).getId());
                i.putExtra(StageInfoActivity.PAC, mData.getData().get(positon).getProvince() + " " + mData.getData().get(positon).getCity());
                i.putExtra(StageInfoActivity.ADDRESS, mData.getData().get(positon).getAddress());
                i.putExtra(StageInfoActivity.MASTER, mData.getData().get(positon).getMaster());
                i.putExtra(StageInfoActivity.STAGENAME, mData.getData().get(positon).getStation());
                i.putExtra(StageInfoActivity.CENTER, mData.getData().get(positon).getOperation_center());
                startActivity(i);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // 设置上拉加载
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            // 上拉加载更多
            public void onLoadMore(int currentPage) {
                // do something...
                Toast.makeText(CarStageActivity.this, "正在加载下一页...", Toast.LENGTH_SHORT).show();
                loadMoreData(currentPage);
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        bReload = true;
        mSwipeLayout.setRefreshing(true);
        location();
    }


    @Override
    public void onStop() {
        super.onStop();
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    @Override
    public void onRefresh() {
        bReload = true;
        loadData();

    }

    private void loadProvinces() {
        String url = ApiConfig.api_url + ApiConfig.STAGE_PROVINCES_URL;
        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("省份：", response.toString());
                        provinceItemList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                provinceItemList.add(response.getJSONObject(i).getString("province"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                mSwipeLayout.setRefreshing(false);
                bReload = false;

                Helper.processVolleyErrorMsg(CarStageActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void loadCity() {
        if (citys!=null && citys.size()>0){
            showPw(tvCity, tvCity, citys);
            return;
        }
        String url = ApiConfig.api_url + ApiConfig.STAGE_CITY_URL +
                "&province=" + URLEncoder.encode(tvPro.getText().toString());
        AppConst.showDialog(this);

        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppConst.dismiss();
                        citys = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                citys.add(response.getJSONObject(i).getString("city"));
                            }
                            showPw(tvCity, tvCity, citys);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                AppConst.dismiss();
                isProvinceSelect = false;
                Helper.processVolleyErrorMsg(CarStageActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void loadData() {
        if (bReload) {
            mData.setCurrentPage(0);
        }
        int page = mData.getCurrentPage() + 1;
        String url = ApiConfig.api_url + ApiConfig.STAGE_URL +
                "&province=" + URLEncoder.encode(tvPro.getText().toString()) +
                "&city=" + URLEncoder.encode(tvCity.getText().toString()) +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page;
        AppConst.showDialog(this);
        GsonRequest<CarStagePage> myReq = new GsonRequest<CarStagePage>(Request.Method.GET,
                url,
                CarStagePage.class,
                null,
                new Response.Listener<CarStagePage>() {
                    @Override
                    public void onResponse(CarStagePage response) {
                        AppConst.dismiss();
                        // 取消refresh
                        mSwipeLayout.setRefreshing(false);
                        if (bReload) {
                            mData.getData().clear();
                            bReload = false;
                        }
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
//                        mData.add(response);
                        mData.setTotal(response.getTotal());
                        mData.setPerPage(response.getPerPage());
                        mData.setCurrentPage(response.getCurrentPage());
                        mData.setLastPage(response.getLastPage());
                        mData.setFrom(response.getFrom());
                        mData.setTo(response.getTo());
                        mData.getData().addAll(response.getData());
                        Log.i("CarStageActivity", mData.getData().size() + "");
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消refresh
                        mSwipeLayout.setRefreshing(false);
                        AppConst.dismiss();
                        bReload = false;

                        Helper.processVolleyErrorMsg(CarStageActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    private void loadMoreData(int current_page) {

        if (loading) {  //如果正在加载，直接返回
            return;
        }

        //到达尾页，直接返回
        if (current_page > mData.getData().size()) {
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.api_url + ApiConfig.STAGE_URL +
                "&province=" +
                "&city=" +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + current_page;

        GsonRequest<CarStagePage> myReq = new GsonRequest<CarStagePage>(Request.Method.GET,
                url,
                CarStagePage.class,
                null,
                new Response.Listener<CarStagePage>() {
                    @Override
                    public void onResponse(CarStagePage response) {
                        loading = false;
                        mSwipeLayout.setRefreshing(true);
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        mData.setTotal(response.getTotal());
                        mData.setPerPage(response.getPerPage());
                        mData.setCurrentPage(response.getCurrentPage());
                        mData.setLastPage(response.getLastPage());
                        mData.setFrom(response.getFrom());
                        mData.setTo(response.getTo());

                        mData.getData().addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消正在加载
                        loading = false;
                        mSwipeLayout.setRefreshing(true);
                        Helper.processVolleyErrorMsg(CarStageActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //取消local broadcast的注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
    }

    private BroadcastReceiver mRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(CarStageActivity.REFRESH_ACTION)) {
                boolean enabled = intent.getBooleanExtra("enabled", true);
                mSwipeLayout.setEnabled(enabled);
            }

        }
    };

    public void location() {
        ((JindouyunApplication) getApplication()).setmLocationWatcher(this);
        LocationClient mLocationClient = ((JindouyunApplication) getApplication()).mLocationClient;
        mLocationClient.requestLocation();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }


    String lat,lon;
    @Override
    public void onLocationSuccess(BDLocation location) {
        if (!isMapView) {
            tvPro.setText(location.getProvince().replace("省",""));
            tvCity.setText(location.getCity().replace("市",""));
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
            if (mData.getData().size()<1){
                loadData();
            }else {
                mSwipeLayout.setRefreshing(false);
            }
            if (provinceItemList == null){
                loadProvinces();
            }
            return;
        }
        //Receive Location
        if (mMarker != null) {
            mMarker.remove();
        } else {
            mBaiduMap.clear();
        }
        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                location.getLatitude(),
                location.getLongitude(), R.drawable.icon_point,
                mBaiduMap, 0, true);
        for (int i = 0; i < mData.getData().size(); i++) {
            Stage stage = mData.getData().get(i);
            BaiduMapUtilByRacer.addOverlay(R.drawable.icon_point, stage.getLatitude(), stage.getLongitude(), mBaiduMap);
        }
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    static PopupWindow popupWindow;

    public void showPw(View v, final TextView tv, final List<String> string) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_lv, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, tv.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });
            ListView listView = (ListView) view.findViewById(R.id.lv);
            listView.setAdapter(new CommonAdapter<String>(v.getContext(), string, R.layout.item_text) {
                @Override
                public void convert(ViewHolder helper, String item) {
                    helper.setText(R.id.tvName, item);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    bReload = true;
                    if (isProvinceSelect && !tv.getText().toString().equals(string.get(i))) {
                        citys = null;
                        tvCity.setText("");
                    }
                    tv.setText(string.get(i));
                    loadData();
                    popupWindow.dismiss();

                }
            });
            popupWindow.showAsDropDown(v, 0, 0);
        }
    }

}
