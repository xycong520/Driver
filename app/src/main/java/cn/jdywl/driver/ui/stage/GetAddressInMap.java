package cn.jdywl.driver.ui.stage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.stage.AroundPoiAdapter;
import cn.jdywl.driver.adapter.stage.SearchPoiAdapter;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.ui.common.BaseActivity;


/**
 * Created by xycong on 2016/1/24.
 */
public class GetAddressInMap extends BaseActivity {
    private int mChoseItemPos;
    private BeanLocation mLocationBean;
    // 定位poi地名信息数据源
    private List<PoiInfo> aroundPoiList;
    private AroundPoiAdapter mAroundPoiAdapter;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;
    // 搜索当前城市poi数据源
    private static List<BeanLocation> searchPoiList;
    private SearchPoiAdapter mSearchPoiAdapter;
    // 標識
    public static final int SHOW_MAP = 0;
    private static final int SHOW_SEARCH_RESULT = 1;


    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private EditText etMLCityPoi;
    private TextView tvShowLocation;
    private LinearLayout llMLMain;
    private ListView lvAroundPoi, lvSearchPoi;
    private Button btMapZoomIn, btMapZoomOut;
    private ImageButton ibMLLocate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getcaraddress);
        ButterKnife.bind(this);
        setupToolbar();
        init();
        locate();
        iniEvent();
    }

    private void init() {
        ibMLLocate = (ImageButton) findViewById(R.id.ibMLLocate);
        etMLCityPoi = (EditText) findViewById(R.id.etMLCityPoi);
        tvShowLocation = (TextView) findViewById(R.id.tvShowLocation);
        lvAroundPoi = (ListView) findViewById(R.id.lvPoiList);
        lvSearchPoi = (ListView) findViewById(R.id.lvMLCityPoi);
        btMapZoomIn = (Button) findViewById(R.id.btMapZoomIn);
        btMapZoomOut = (Button) findViewById(R.id.btMapZoomOut);
        llMLMain = (LinearLayout) findViewById(R.id.llMLMain);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mMapView);
        BaiduMapUtilByRacer.goneMapViewChild(mMapView, true, true);
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        mBaiduMap.setOnMapClickListener(mapOnClickListener);
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);// 缩放手势
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }


    public void locate() {
        BaiduMapUtilByRacer.locateByBaiduMap(this, 2000,
                new BaiduMapUtilByRacer.LocateListener() {
                    @Override
                    public void onLocateSucceed(BeanLocation locationBean) {
                        mLocationBean = locationBean;
                        if (mMarker != null) {
                            mMarker.remove();
                        } else {
                            mBaiduMap.clear();
                        }
                        mMarker = BaiduMapUtilByRacer.showMarkerByResource(
                                locationBean.getLatitude(),
                                locationBean.getLongitude(), R.drawable.icon_point,
                                mBaiduMap, 0, true);
                    }

                    @Override
                    public void onLocateFiled() {
                    }

                    @Override
                    public void onLocating() {
                    }
                });
    }

    private void iniEvent() {
        etMLCityPoi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etMLCityPoi.getText().toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                }
            }
        });
        etMLCityPoi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before,
                                      int count) {
                if (cs.toString().trim().length() > 0) {
                    getPoiByPoiSearch();
                } else {
                    if (searchPoiList != null) {
                        searchPoiList.clear();
                    }
                    showMapOrSearch(SHOW_MAP);
                    hideSoftinput(GetAddressInMap.this);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ibMLLocate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                locate();
            }
        });
        btMapZoomIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomInMapView(mMapView);
            }
        });
        btMapZoomOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.zoomOutMapView(mMapView);
            }
        });
        lvAroundPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                isCanUpdateMap = false;
                mChoseItemPos = position;
                // go2LocationSettingAct(position);
            }
        });

        lvSearchPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // Geo搜索
                // mGeoCoder.geocode(new GeoCodeOption().city(
                // searchPoiList.get(arg2).getCity()).address(
                // searchPoiList.get(arg2).getLocName()));
                hideSoftinput(GetAddressInMap.this);
                isCanUpdateMap = false;
                BaiduMapUtilByRacer.moveToTarget(searchPoiList.get(arg2)
                                .getLatitude(), searchPoiList.get(arg2).getLongitude(),
                        mBaiduMap);
                // tvShowLocation.setText(searchPoiList.get(arg2).getLocName());
                // 反Geo搜索
                reverseGeoCode(new LatLng(
                        searchPoiList.get(arg2).getLatitude(), searchPoiList
                        .get(arg2).getLongitude()), false);
                showMapOrSearch(SHOW_MAP);
            }
        });
    }
//    private void go2LocationSettingAct(int position) {
//        Intent intent = new Intent(AddCorLocationActivity.this,
//                LocationSettingAct.class);
//        PoiInfo poiInfo = aroundPoiList.get(position);
//        if (poiInfo == null) {
//            Constant.showToast("地址获取中，请稍候");
//            return;
//        }
//        intent.putExtra("platform", getIntent().getStringExtra("platform"));
//        intent.putExtra("selectedAddress", poiInfo.address + poiInfo.name);
//        intent.putExtra("lat", poiInfo.location.latitude);
//        intent.putExtra("lon", poiInfo.location.longitude);
//        startActivity(intent);
//        finish();
//    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void setRefreshEnabled(boolean enabled) {

    }

    @Override
    protected void cancelVolleyRequest(RequestQueue queue) {

    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (llMLMain.getVisibility() == View.GONE) {
            showMapOrSearch(SHOW_MAP);
        } else {
            this.finish();
        }
    }

    BaiduMap.OnMapClickListener mapOnClickListener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         *
         * @param point
         *            点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            hideSoftinput(GetAddressInMap.this);
        }

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param poi
         *            点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return false;
        }
    };
    private boolean isCanUpdateMap = true;
    BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status
         *            地图状态改变开始时的地图状态
         */
        public void onMapStatusChangeStart(MapStatus status) {
        }

        /**
         * 地图状态变化中
         *
         * @param status
         *            当前地图状态
         */
        public void onMapStatusChange(MapStatus status) {
        }

        /**
         * 地图状态改变结束
         *
         * @param status
         *            地图状态改变结束后的地图状态
         */
        public void onMapStatusChangeFinish(MapStatus status) {
            if (isCanUpdateMap) {
                LatLng ptCenter = new LatLng(status.target.latitude,
                        status.target.longitude);
                // 反Geo搜索
                reverseGeoCode(ptCenter, true);
            } else {
                isCanUpdateMap = true;
            }
        }
    };


    /**
     * 隐藏软键盘
     */
    private void hideSoftinput(Context mContext) {
        InputMethodManager manager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(etMLCityPoi.getWindowToken(), 0);
        }
    }

    // 刷新热门地名列表界面的adapter
    private void updatePoiListAdapter(List<PoiInfo> list, int index) {
        AppConst.dismiss();
        lvAroundPoi.setVisibility(View.VISIBLE);
        if (mAroundPoiAdapter == null) {
            mAroundPoiAdapter = new AroundPoiAdapter(GetAddressInMap.this, list);
            lvAroundPoi.setAdapter(mAroundPoiAdapter);
        } else {
            mAroundPoiAdapter.setNewList(list, index);
        }
    }

    // 刷新当前城市兴趣地点列表界面的adapter
    private void updateCityPoiListAdapter() {
        if (mSearchPoiAdapter == null) {
            mSearchPoiAdapter = new SearchPoiAdapter(GetAddressInMap.this, searchPoiList);
            lvSearchPoi.setAdapter(mSearchPoiAdapter);
        } else {
            mSearchPoiAdapter.notifyDataSetChanged();
        }
        showMapOrSearch(SHOW_SEARCH_RESULT);
    }

    // 显示地图界面亦或搜索结果界面
    private void showMapOrSearch(int index) {
        if (index == SHOW_SEARCH_RESULT) {
            llMLMain.setVisibility(View.GONE);
            lvSearchPoi.setVisibility(View.VISIBLE);
        } else {
            lvSearchPoi.setVisibility(View.GONE);
            llMLMain.setVisibility(View.VISIBLE);
            if (searchPoiList != null) {
                searchPoiList.clear();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mLocationBean = null;
        lvAroundPoi = null;
        lvSearchPoi = null;
        btMapZoomIn.setBackgroundResource(0);
        btMapZoomIn = null;
        btMapZoomOut.setBackgroundResource(0);
        btMapZoomOut = null;
        ibMLLocate.setImageBitmap(null);
//        ibMLLocate.setImageResource(0);
        ibMLLocate = null;
        if (aroundPoiList != null) {
            aroundPoiList.clear();
            aroundPoiList = null;
        }
        mAroundPoiAdapter = null;
        if (searchPoiList != null) {
            searchPoiList.clear();
            searchPoiList = null;
        }
        mSearchPoiAdapter = null;
        if (mBaiduMap != null) {
            mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.destroyDrawingCache();
            mMapView.onDestroy();
            mMapView = null;
        }
        if (etMLCityPoi != null) {
            etMLCityPoi.setBackgroundResource(0);
            etMLCityPoi = null;
        }
        mMarker = null;
        super.onDestroy();
        System.gc();
    }

    public void getPoiByPoiSearch() {
        String keyName = etMLCityPoi.getText().toString();
        BaiduMapUtilByRacer.getPoiByPoiSearch(mLocationBean.getCity(),
                keyName, 0,
                new BaiduMapUtilByRacer.PoiSearchListener() {

                    @Override
                    public void onGetSucceed(List<BeanLocation> locationList,
                                             PoiResult res) {
                        if (etMLCityPoi.getText().toString().trim().length() > 0) {
                            if (searchPoiList == null) {
                                searchPoiList = new ArrayList();
                            }
                            searchPoiList.clear();
                            searchPoiList.addAll(locationList);
                            updateCityPoiListAdapter();
                        }
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(GetAddressInMap.this, "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void reverseGeoCode(LatLng ll, final boolean isShowTextView) {
        AppConst.showDialog(GetAddressInMap.this);
        BaiduMapUtilByRacer.getPoisByGeoCode(ll.latitude, ll.longitude,
                new BaiduMapUtilByRacer.GeoCodePoiListener() {

                    @Override
                    public void onGetSucceed(BeanLocation locationBean,
                                             List<PoiInfo> poiList) {
                        AppConst.dismiss();
                        mLocationBean = (BeanLocation) locationBean.clone();
                        // Toast.makeText(
                        // mContext,
                        // mLocationBean.getProvince() + "-"
                        // + mLocationBean.getCity() + "-"
                        // + mLocationBean.getDistrict() + "-"
                        // + mLocationBean.getStreet() + "-"
                        // + mLocationBean.getStreetNum(),
                        // Toast.LENGTH_SHORT).show();
                        String chosenLocation = mLocationBean.getProvince()
                                +  mLocationBean.getCity()
                                + mLocationBean.getDistrict()
                                + mLocationBean.getStreet()
                                + mLocationBean.getStreetNum();
                        tvShowLocation.setText(chosenLocation);
                        // mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        // .newLatLng(new LatLng(locationBean
                        // .getLatitude(), locationBean
                        // .getLongitude())));
                        // if (isShowTextView) {
                        // tvShowLocation.setText(locationBean.getLocName());
                        // }
                        if (aroundPoiList == null) {
                            aroundPoiList = new ArrayList();
                        }
                        aroundPoiList.clear();
                        if (poiList != null) {
                            aroundPoiList.addAll(poiList);
                        } else {
                            Toast.makeText(GetAddressInMap.this,"该点周边没有热点",Toast.LENGTH_SHORT).show();
                        }
                        updatePoiListAdapter(aroundPoiList, -1);
                    }

                    @Override
                    public void onGetFailed() {
                        Toast.makeText(GetAddressInMap.this,"抱歉，未能找到结果",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ok, menu);
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
            case R.id.action_ok:
                Intent intent = new Intent();
                if (mChoseItemPos!=0){
                    intent.putExtra("address", tvShowLocation.getText().toString()+this.aroundPoiList.get(mChoseItemPos).name);
                }else{
                    intent.putExtra("address", tvShowLocation.getText().toString());
                }
                intent.putExtra("addressX", String.valueOf(mLocationBean.getLatitude()));
                intent.putExtra("addressY", String.valueOf(mLocationBean.getLongitude()));
                setResult(RESULT_OK,intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
