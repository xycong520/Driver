package cn.jdywl.driver.ui.stage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BaiduMapUtilByRacer
 * @Description: 百度地圖工具類
 * @date 2014-10-31 下午2:38:44
 */
public class BaiduMapUtilByRacer {

    /**
     * @param view
     * @return Bitmap
     * @throws
     * @Title: getBitmapFromView
     * @Description: 通過view得到位圖
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    /**
     * @param lat
     * @param lon
     * @param mBaiduMap
     * @param distance
     * @param isMoveTo
     * @return Marker
     * @throws
     * @Title: showMarkerByResource
     * @Description: 通過resource來顯示Marker
     */
    public static Marker showMarkerByResource(double lat, double lon,
                                              int resource, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory
                .fromResource(resource);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }
    public static Marker showMarkerByResource(double lat, double lon,
                                              View resource, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory
                .fromView(resource);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    /**
     * @param lat
     * @param lon
     * @param mBitmap
     * @param mBaiduMap
     * @param distance
     * @param isMoveTo
     * @return Marker
     * @throws
     * @Title: showMarkerByBitmap
     * @Description: 通過bitmap來顯示Marker
     */
    public static Marker showMarkerByBitmap(double lat, double lon,
                                            Bitmap mBitmap, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory.fromBitmap(mBitmap);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    /**
     * @param mMarker
     * @param resourceId
     * @return void
     * @throws
     * @Title: updateMarkerIcon
     * @Description:
     */
    public static void updateMarkerIcon(Marker mMarker, int resourceId) {
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(resourceId);
        mMarker.setIcon(bd);
    }

    /**
     * @param lat
     * @param lon
     * @param mView
     * @param mBaiduMap
     * @param distance
     * @param isMoveTo
     * @return Marker
     * @throws
     * @Title: showMarkerByView
     * @Description: 通過view來顯示Marker
     */
    public static Marker showMarkerByView(double lat, double lon, View mView,
                                          BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory.fromView(mView);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    /**
     * @param lat
     * @param lon
     * @param mBitmap
     * @param mBaiduMap
     * @param distance
     * @param isMoveTo
     * @param listener
     * @return InfoWindow
     * @throws
     * @Title: showInfoWindowByBitmap
     * @Description: 通過bitmap來顯示InfoWindow
     */
    public static InfoWindow showInfoWindowByBitmap(double lat, double lon,
                                                    Bitmap mBitmap, BaiduMap mBaiduMap, int distance, boolean isMoveTo,
                                                    OnInfoWindowClickListener listener) {
        InfoWindow mInfoWindow = new InfoWindow(mBitmap == null ? null
                : BitmapDescriptorFactory.fromBitmap(mBitmap), new LatLng(lat,
                lon), distance, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return mInfoWindow;
    }

    /**
     * @param lat
     * @param lon
     * @param mView
     * @param mBaiduMap
     * @param distance
     * @param isMoveTo
     * @param listener
     * @return InfoWindow
     * @throws
     * @Title: showPopByView
     * @Description: 通過view來顯示InfoWindow
     */
    public static InfoWindow showInfoWindowByView(double lat, double lon,
                                                  View mView, BaiduMap mBaiduMap, int distance, boolean isMoveTo,
                                                  OnInfoWindowClickListener listener) {
        InfoWindow mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(mView), new LatLng(lat, lon),
                distance, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return mInfoWindow;
    }

    /**
     * @param lat
     * @param lon
     * @param mBaiduMap
     * @return void
     * @throws
     * @Title: moveToTarget
     * @Description:移動到該點
     */
    public static void moveToTarget(double lat, double lon, BaiduMap mBaiduMap) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
                lat, lon)));
    }

    /**
     * @param mLatLng
     * @param mBaiduMap
     * @return void
     * @throws
     * @Title: moveToTarget
     * @Description:移動到該點
     */
    public static void moveToTarget(LatLng mLatLng, BaiduMap mBaiduMap) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
    }

    /**
     * 设置地图缩放比例
     *
     * @param zoomLevel 15.0f
     * @param mBaiduMap
     */
    public static void setZoom(float zoomLevel, BaiduMap mBaiduMap) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(zoomLevel));
    }

    /**
     * 放大地圖
     *
     * @param mMapView
     */
    public static void zoomInMapView(MapView mMapView) {
        try {
            BaiduMapUtilByRacer.setZoom(mMapView.getMap().getMapStatus().zoom + 1, mMapView.getMap());
        } catch (NumberFormatException e) {
        }
    }

    /**
     * 缩小地圖
     *
     * @param mMapView
     */
    public static void zoomOutMapView(MapView mMapView) {
        try {
            BaiduMapUtilByRacer.setZoom(mMapView.getMap().getMapStatus().zoom - 1, mMapView.getMap());
        } catch (NumberFormatException e) {
        }
    }

    /**
     * 隐藏百度logo或百度自帶的缩放键
     *
     * @param mMapView
     * @param goneLogo
     * @param goneZoomControls
     */
    public static void goneMapViewChild(MapView mMapView, boolean goneLogo, boolean goneZoomControls) {
        int count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ImageView && goneLogo) { // 隐藏百度logo
                child.setVisibility(View.GONE);
            }
            if (child instanceof ZoomControls && goneZoomControls) { // 隐藏百度的縮放按鍵
                child.setVisibility(View.GONE);
            }
        }
    }


    public static LocationClient mLocationClient = null;
    public static LocationClientOption option = null;
    public static LocateListener mLocateListener = null;
    public static MyLocationListenner mMyLocationListenner = null;
    public static int locateTime = 500;

    public interface LocateListener {
        void onLocateSucceed(BeanLocation locationBean);

        void onLocateFiled();

        void onLocating();

    }

    /**
     * @param mContext
     * @param time     大於1000既會間隔定位
     * @param listener
     * @return void
     * @throws
     * @Title: startLocate
     * @Description: 定位
     */
    public static void locateByBaiduMap(Context mContext, int time, LocateListener listener) {
        // if (mLocationClient == null) {
        mLocateListener = listener;
        locateTime = time;
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext);
        }
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mMyLocationListenner == null) {
            mMyLocationListenner = new MyLocationListenner();
        }
        mLocationClient.registerLocationListener(mMyLocationListenner);
        if (option == null) {
            option = new LocationClientOption();
            option.setOpenGps(true);// 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(time);
            option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
            // option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        }
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        // } else {
        // Log.i("huan", "requestLocation()");
        // mLocationClient.requestLocation();
        // }
    }

    /**
     * @author 李焕儿
     * @ClassName: MyLocationListenner
     * @Description: 定位SDK监听函数
     * @date 2014-10-31 下午3:18:34
     */
    public static class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mLocateListener != null) {
                mLocateListener.onLocating();
            }
            // map view 销毁后不在处理新接收的位置
            if (location == null || location.getProvince() == null
                    || location.getCity() == null || mLocateListener == null) {
                if (mLocateListener != null) {
                    mLocateListener.onLocateFiled();
                }
                if (locateTime < 1000) {
                    stopAndDestroyLocate();
                }
                return;
            }
            BeanLocation mLocationBean = new BeanLocation();
            mLocationBean.setProvince(location.getProvince());
            mLocationBean.setCity(location.getCity());
            mLocationBean.setDistrict(location.getDistrict());
            mLocationBean.setStreet(location.getStreet());
            mLocationBean.setLatitude(location.getLatitude());
            mLocationBean.setLongitude(location.getLongitude());
            mLocationBean.setTime(location.getTime());
            mLocationBean.setLocType(location.getLocType());
            mLocationBean.setRadius(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                mLocationBean.setSpeed(location.getSpeed());
                mLocationBean.setSatellite(location.getSatelliteNumber());
                mLocationBean.setDirection(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                mLocationBean.setLocName(location.getStreet());
                // 运营商信息
                mLocationBean.setOperationers(location.getOperators());
            }
            mLocationBean.setAddStr(location.getAddrStr());
            if (mLocateListener != null) {
                mLocateListener.onLocateSucceed(mLocationBean);
            }
            stopAndDestroyLocate();
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * @return void
     * @throws
     * @Title: stopAndDestroyLocate
     * @Description: 停止及置空mLocationClient
     */
    public static void stopAndDestroyLocate() {
        locateTime = 500;
        if (mLocationClient != null) {
            if (mMyLocationListenner != null) {
                mLocationClient.unRegisterLocationListener(mMyLocationListenner);
            }
            mLocationClient.stop();
        }
        mMyLocationListenner = null;
        mLocateListener = null;
        mLocationClient = null;
        option = null;
    }

    /**
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @return String
     * @throws
     * @Title: getDistance
     * @Description: 獲取距離 Stirng類型的
     */
    public static String getDistanceWithUtil(double mLat1, double mLon1,
                                             double mLat2, double mLon2) {
        if ((Double) mLat1 instanceof Double
                && (Double) mLon1 instanceof Double
                && (Double) mLat2 instanceof Double
                && (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
                && mLat2 != 0 && mLon2 != 0) {
            float distance = (float) DistanceUtil.getDistance(new LatLng(mLat1,
                    mLon1), new LatLng(mLat2, mLon2));
            return addUnit(distance);
        } else {
            return "0M";
        }
    }

    /**
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @return int
     * @throws
     * @Title: getDistanceForInteger
     * @Description: 獲取距離 int類型的
     */
    public static int getDistanceWithoutUtil(double mLat1, double mLon1,
                                             double mLat2, double mLon2) {
        if ((Double) mLat1 instanceof Double
                && (Double) mLon1 instanceof Double
                && (Double) mLat2 instanceof Double
                && (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
                && mLat2 != 0 && mLon2 != 0) {
            return (int) DistanceUtil.getDistance(new LatLng(mLat1, mLon1),
                    new LatLng(mLat2, mLon2));
        } else {
            return 0;
        }
    }

    /**
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @return String
     * @throws
     * @Title: getDistance
     * @Description: 獲取距離 Stirng類型的
     */
    public static String getDistanceWithUtil(String mLat1, String mLon1,
                                             String mLat2, String mLon2) {
        if (mLat1 != null && !mLat1.equals("") && !mLat1.equals("null")
                && !mLat1.equals("0") && mLon1 != null && !mLon1.equals("")
                && !mLon1.equals("null") && !mLon1.equals("0") && mLat2 != null
                && !mLat2.equals("") && !mLat2.equals("null")
                && !mLat2.equals("0") && mLon2 != null && !mLon2.equals("")
                && !mLon2.equals("null") && !mLon2.equals("0")) {
            float distance = (float) DistanceUtil.getDistance(
                    new LatLng(Double.valueOf(mLat1), Double.valueOf(mLon1)),
                    new LatLng(Double.valueOf(mLat2), Double.valueOf(mLon2)));
            return addUnit(distance);
        } else {
            return "0M";
        }
    }

    /**
     * @param distance
     * @return String
     * @throws
     * @Title: addUnit
     * @Description: 為距離添加單位的
     */
    public static String addUnit(float distance) {
        if (distance == 0) {
            return "0M";
        } else {
            if (distance > 1000) {
                distance = distance / 1000;
                distance = (float) ((double) Math.round(distance * 100) / 100);
                return distance + "KM";
            } else {
                distance = (float) ((double) Math.round(distance * 100) / 100);
                return distance + "M";
            }
        }
    }

    /**
     * @Title: NaviByWeb
     * @Description: 通過web導航
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @param mContext
     * @return
     * @return boolean
     * @throws
     *//*
    public static boolean NaviByWebBaidu(double mLat1, double mLon1,
			double mLat2, double mLon2, Context mContext) {
		if ((Double) mLat1 instanceof Double
				&& (Double) mLon1 instanceof Double
				&& (Double) mLat2 instanceof Double
				&& (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
				&& mLat2 != 0 && mLon2 != 0) {
			LatLng pt1 = new LatLng(mLat1, mLon1);
			LatLng pt2 = new LatLng(mLat2, mLon2);
			// 构建 导航参数
			NaviPara para = new NaviPara();
			para.startPoint = pt1;
			para.endPoint = pt2;
			// BaiduMapNavigation.op
			BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
			return true;
		} else {
			return false;
		}
	}*/

    /**
     * @Title: NaviByWeb
     * @Description: 通過web導航
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @param mContext
     * @return
     * @return boolean
     * @throws
     *//*
    public static boolean NaviByWebBaidu(String mLat1, String mLon1,
			String mLat2, String mLon2, Context mContext) {
		if (mLat1 != null && !mLat1.equals("") && !mLat1.equals("null")
				&& !mLat1.equals("0") && mLon1 != null && !mLon1.equals("")
				&& !mLon1.equals("null") && !mLon1.equals("0") && mLat2 != null
				&& !mLat2.equals("") && !mLat2.equals("null")
				&& !mLat2.equals("0") && mLon2 != null && !mLon2.equals("")
				&& !mLon2.equals("null") && !mLon2.equals("0")) {
			LatLng pt1 = new LatLng(Double.valueOf(mLat1),
					Double.valueOf(mLon1));
			LatLng pt2 = new LatLng(Double.valueOf(mLat2),
					Double.valueOf(mLon2));
			// 构建 导航参数
			 NaviPara para = new NaviPara();
			 para.startPoint = pt1;
			 para.endPoint = pt2;
			 BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
			return true;
		} else {
			return false;
		}
	}

	*/
    /**
     * @Title: NaviByBaidu
     * @Description: 通過百度客戶端導航
     * @param mLat1
     * @param mLon1
     * @param mLat2
     * @param mLon2
     * @param mContext
     * @return
     * @return boolean
     * @throws
     *//*
	public static boolean NaviByBaidu(double mLat1, double mLon1, double mLat2,
			double mLon2, final Context mContext) {
		if ((Double) mLat1 instanceof Double
				&& (Double) mLon1 instanceof Double
				&& (Double) mLat2 instanceof Double
				&& (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
				&& mLat2 != 0 && mLon2 != 0) {
			LatLng pt1 = new LatLng(mLat1, mLon1);
			LatLng pt2 = new LatLng(mLat2, mLon2);
			// 构建 导航参数
			NaviPara para = new NaviPara();
			para.startPoint = pt1;
			para.startName = "从这里开始";
			para.endPoint = pt2;
			para.endName = "到这里结束";
			try {
				BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
			} catch (BaiduMapAppNotSupportNaviException e) {
				e.printStackTrace();
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// BaiduMapNavigation.getLatestBaiduMapApp(mContext);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
			return true;
		} else {
			return false;
		}
	}*/

    // poi搜索
    public static SuggestionSearch mSuggestionSearch = null;
    public static SuggestionsGetListener mSuggestionsGetListener = null;

    public interface SuggestionsGetListener {
        void onGetSucceed(List<BeanLocation> searchPoiList);

        void onGetFailed();
    }

    /**
     * @param cityName
     * @param keyName
     * @param listener
     * @return void
     * @throws
     * @Title: getSuggestion
     * @Description: 通過關鍵字及城市名搜索對應地區 city城市名-district區名-locName熱點名
     */
    public static void getSuggestion(String cityName, String keyName,
                                     SuggestionsGetListener listener) {
        mSuggestionsGetListener = listener;
        if (cityName == null || keyName == null) {
            if (mSuggestionsGetListener != null) {
                mSuggestionsGetListener.onGetFailed();
            }
            destroySuggestion();
            return;
        }
        if (mSuggestionSearch == null) {
            // 初始化搜索模块，注册事件监听
            mSuggestionSearch = SuggestionSearch.newInstance();
        }
        mSuggestionSearch
                .setOnGetSuggestionResultListener(new MySuggestionListener());
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(keyName.toString()).city(cityName));
    }

    /**
     * @author 李焕儿
     * @ClassName: MySuggestionListener
     * @Description: 關鍵字搜索監聽回調
     * @date 2014-11-11 下午1:52:40
     */
    public static class MySuggestionListener implements
            OnGetSuggestionResultListener {

        @Override
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                if (mSuggestionsGetListener != null) {
                    mSuggestionsGetListener.onGetFailed();
                }
                destroySuggestion();
                return;
            }
            List<BeanLocation> searchPoiList = new ArrayList<BeanLocation>();
            for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                if (info.key != null) {
                    BeanLocation cityPoi = new BeanLocation();
                    cityPoi.setCity(info.city);
                    cityPoi.setDistrict(info.district);
                    cityPoi.setLocName(info.key);
                    searchPoiList.add(cityPoi);
                }
            }
            if (mSuggestionsGetListener != null) {
                mSuggestionsGetListener.onGetSucceed(searchPoiList);
            }
            destroySuggestion();
        }
    }

    /**
     * @return void
     * @throws
     * @Title: destroySuggestion
     * @Description: 銷毀及置空搜索相關對象
     */
    public static void destroySuggestion() {
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
            mSuggestionSearch = null;
        }
        mSuggestionsGetListener = null;
    }

    public static GeoCoder mGeoCoder = null;
    public static GeoCodeListener mGeoCodeListener = null;

    public interface GeoCodeListener {
        void onGetSucceed(BeanLocation BeanLocation);

        void onGetFailed();
    }

    /**
     * @param mLocationBean
     * @param listener
     * @return void
     * @throws
     * @Title: getLocationByGeoCode
     * @Description: 獲取坐標通過geo搜索
     */
    public static void getLocationByGeoCode(BeanLocation mLocationBean,
                                            GeoCodeListener listener) {
        mGeoCodeListener = listener;
        if (mLocationBean == null || mLocationBean.getCity() == null
                || mLocationBean.getLocName() == null) {
            if (mGeoCodeListener != null) {
                mGeoCodeListener.onGetFailed();
            }
            destroyGeoCode();
            return;
        }
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGeoCodeListener());
        // Geo搜索
        mGeoCoder.geocode(new GeoCodeOption().city(mLocationBean.getCity())
                .address(mLocationBean.getLocName()));
    }

    public static GeoCodePoiListener mGeoCodePoiListener = null;

    public interface GeoCodePoiListener {
        void onGetSucceed(BeanLocation locationBean, List<PoiInfo> poiList);

        void onGetFailed();
    }

    /**
     * @param lat
     * @param lon
     * @param listener
     * @return void
     * @throws
     * @Title: getPoiByGeoCode
     * @Description: 根据经纬度获取周边热点名
     */
    public static void getPoisByGeoCode(double lat, double lon,
                                        GeoCodePoiListener listener) {
        mGeoCodePoiListener = listener;
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGeoCodeListener());
        // 反Geo搜索
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(lat, lon)));
    }

    /**
     * @author 李焕儿
     * @ClassName: MyGeoCodeListener
     * @Description: geo搜索的回調
     * @date 2014-11-11 下午2:18:25
     */
    public static class MyGeoCodeListener implements
            OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                if (mGeoCodeListener != null) {
                    mGeoCodeListener.onGetFailed();
                }
                if (mGeoCodePoiListener != null) {
                    mGeoCodePoiListener.onGetFailed();
                }
                destroyGeoCode();
                return;
            }
            // 反Geo搜索
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(result
                    .getLocation()));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            BeanLocation mLocationBean = new BeanLocation();
            mLocationBean.setProvince(result.getAddressDetail().province);
            mLocationBean.setCity(result.getAddressDetail().city);
            mLocationBean.setDistrict(result.getAddressDetail().district);
            mLocationBean.setLocName(result.getAddressDetail().street);
            mLocationBean.setStreet(result.getAddressDetail().street);
            mLocationBean.setStreetNum(result.getAddressDetail().streetNumber);
            mLocationBean.setLatitude(result.getLocation().latitude);
            mLocationBean.setLongitude(result.getLocation().longitude);
            if (mGeoCodeListener != null) {
                mGeoCodeListener.onGetSucceed(mLocationBean);
            }
            if (mGeoCodePoiListener != null) {
                mGeoCodePoiListener.onGetSucceed(mLocationBean,
                        result.getPoiList());
            }
            destroyGeoCode();
        }
    }

    /**
     * @return void
     * @throws
     * @Title: destroyGeoCode
     * @Description: 銷毀及置空geo搜索相關對象
     */
    public static void destroyGeoCode() {
        if (mGeoCoder != null) {
            mGeoCoder.destroy();
            mGeoCoder = null;
        }
        mGeoCodeListener = null;
        mGeoCodePoiListener = null;
    }

    public static PoiSearch mPoiSearch = null;
    public static PoiSearchListener mPoiSearchListener = null;

    public interface PoiSearchListener {
        void onGetSucceed(List<BeanLocation> locationList, PoiResult res);

        void onGetFailed();
    }

    /**
     * @param cityName
     * @param keyName
     * @param pageNum
     * @param listener
     * @return void
     * @throws
     * @Title: getPoiByPoiSearch
     * @Description: 通過關鍵字及城市名搜索對應地區
     * city城市名-addstr地址-locName熱點名-latlon经纬度-uid百度自定义id
     */
    public static void getPoiByPoiSearch(String cityName, String keyName,
                                         int pageNum, PoiSearchListener listener) {
        mPoiSearchListener = listener;
        if (cityName == null || keyName == null) {
            if (mPoiSearchListener != null) {
                mPoiSearchListener.onGetFailed();
            }
            destroyPoiSearch();
            return;
        }
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
        mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName)
                .keyword(keyName).pageNum(pageNum));
    }

    public static PoiDetailSearchListener mPoiDetailSearchListener = null;

    public interface PoiDetailSearchListener {
        void onGetSucceed(BeanLocation locationBean);

        void onGetFailed();
    }

    /**
     * @param uid
     * @param listener
     * @return void
     * @throws
     * @Title: getPoiDetailByPoiSearch
     * @Description: 获得所谓的详细信息
     */
    public static void getPoiDetailByPoiSearch(String uid,
                                               PoiDetailSearchListener listener) {
        mPoiDetailSearchListener = listener;
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(uid));
    }

    /**
     * @author 李焕儿
     * @ClassName: MyPoiSearchListener
     * @Description: poisearch搜索的回调
     * @date 2014-11-22 下午4:57:34
     */
    public static class MyPoiSearchListener implements
            OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                if (mPoiDetailSearchListener != null) {
                    mPoiDetailSearchListener.onGetFailed();
                }
                destroyPoiSearch();
                return;
            }
            BeanLocation mLocationBean = new BeanLocation();
            mLocationBean.setLocName(result.getName());
            mLocationBean.setAddStr(result.getAddress());
            mLocationBean.setLatitude(result.getLocation().latitude);
            mLocationBean.setLongitude(result.getLocation().longitude);
            mLocationBean.setUid(result.getUid());
            if (mPoiDetailSearchListener != null) {
                mPoiDetailSearchListener.onGetSucceed(mLocationBean);
            }
            destroyPoiSearch();
        }

        @Override
        public void onGetPoiResult(PoiResult res) {
            if (res == null
                    || res.error == SearchResult.ERRORNO.RESULT_NOT_FOUND
                    || res.getAllPoi() == null) {
                if (mPoiSearchListener != null) {
                    mPoiSearchListener.onGetFailed();
                }
                destroyPoiSearch();
                return;
            }
            List<BeanLocation> searchPoiList = new ArrayList<BeanLocation>();
            if (res.getAllPoi() != null) {
                for (PoiInfo info : res.getAllPoi()) {
                    BeanLocation cityPoi = new BeanLocation();
                    cityPoi.setAddStr(info.address);
                    cityPoi.setCity(info.city);
                    cityPoi.setLatitude(info.location.latitude);
                    cityPoi.setLongitude(info.location.longitude);
                    cityPoi.setUid(info.uid);
                    cityPoi.setLocName(info.name);
                    searchPoiList.add(cityPoi);
                    Log.i("huan", "lat==" + info.location.latitude + "--lon=="
                            + info.location.longitude + "--热点名==" + info.name);
                }
            }
            if (mPoiSearchListener != null) {
                mPoiSearchListener.onGetSucceed(searchPoiList, res);
            }
            destroyPoiSearch();
        }
    }

    /**
     * @return void
     * @throws
     * @Title: destroyPoiSearch
     * @Description: 銷毀及置空poisearch搜索相關對象
     */
    public static void destroyPoiSearch() {
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
        mPoiSearchListener = null;
        mPoiDetailSearchListener = null;
    }

    public static void addOverlay(int iconID, double lat, double lon,
                                  BaiduMap mBaiduMap) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(iconID);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }
    public static void addOverlay(View view, double lat, double lon,
                                  BaiduMap mBaiduMap) {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(view);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }


}
