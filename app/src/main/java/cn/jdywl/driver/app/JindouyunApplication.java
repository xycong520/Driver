/**
 * Copyright 2013 Ognyan Bankov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.jdywl.driver.app;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.squareup.leakcanary.LeakCanary;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jdywl.driver.R;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.ResponseEntry;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.service.LocalIntentService;
import cn.jdywl.driver.ui.SettingsActivity;
import cn.jdywl.driver.ui.stage.ILocationWatcher;

/**
 * 主Application，完成百度定位、百度推送、Volley的初始化等工作
 * <p/>
 * 所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * <p/>
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */
//@ReportsCrashes(formUri = "http://120.25.147.20/api/public/crashreport?XDEBUG_SESSION_START=PHPSTORM")
@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://120.25.147.20:5984/acra-jindouyun/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "jdy",
        formUriBasicAuthPassword = "TGYD1phs"
)
public class JindouyunApplication extends Application {
    private static final String TAG = LogHelper.makeLogTag(JindouyunApplication.class);

    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Battery_Saving;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private ILocationWatcher mLocationWatcher;

    public Vibrator mVibrator;
    /*
        tempcoor="gcj02";//国家测绘局标准
        tempcoor="bd09ll";//百度经纬度标准
        tempcoor="bd09";//百度墨卡托标准
    */
    private String tempcoor = "bd09ll";

    public void setmLocationWatcher(ILocationWatcher mLocationWatcher) {
        this.mLocationWatcher = mLocationWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        //检查内存泄露
        LeakCanary.install(this);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);

        //初始化配置信息
        initConfig();

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        initLocation();

        //Android M以下版本直接启动百度定位service，M以上版本需要获取location的授权后才会开启，逻辑放在MainActivity中处理
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //启动后台service
            Intent mIntent = new Intent(this.getApplicationContext(), LocalIntentService.class);
            mIntent.setAction(LocalIntentService.ACTION_START_LOCATION);
            //mIntent.setPackage(getPackageName());
            startService(mIntent);
        }
    }

    //初始化配置信息
    private void initConfig() {
        // Calling this during onCreate() ensures that your application is properly initialized with default settings,
        // which your application might need to read in order to determine some behaviors .

        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /*
         * 获取APP配置选项
         */

        //认证信息
        AppConfig.bLogin = sharedPref.getBoolean(AppConst.KEY_PREF_AUTH_ISLOGIN, false);
        AppConfig.phone = sharedPref.getString(AppConst.KEY_PREF_AUTH_PHONE, "");
        AppConfig.password = sharedPref.getString(AppConst.KEY_PREF_AUTH_PASSWORD, "");
        AppConfig.name = sharedPref.getString(AppConst.KEY_PREF_AUTH_NAME, "");
        AppConfig.logintime = sharedPref.getLong(AppConst.KEY_PREF_AUTH_LOGINTIME, 0);

        //获取API URL
        AppConfig.api_url = sharedPref.getString(SettingsActivity.KEY_PREF_API_URL, "");
    }


    //初始化百度地图定位
    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(3600000);//设置发起定位请求的间隔时间为一个小时，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        //option.setOpenGps(true);//可选，默认false,设置是否使用gps
        //option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }


    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            List<Poi> list = location.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            LogHelper.i(TAG, sb.toString());
            if (mLocationWatcher!=null){
                mLocationWatcher.onLocationSuccess(location);
            }else{
                //将位置推送到服务器
                uploadLocation(location);
            }
        }


    }

    void uploadLocation(BDLocation location) {
        int errorCode = location.getLocType();

        if (errorCode != BDLocation.TypeGpsLocation
                && errorCode != BDLocation.TypeNetWorkLocation
                && errorCode != BDLocation.TypeCacheLocation) {
            LogHelper.i(TAG, "位置信息无效");
            return;
        }

        if (!AppConfig.isLogin()) {
            LogHelper.i(TAG, "用户未登录");
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.LOCATION_URL;
        LogHelper.i(TAG, "uploadLocation--" + url);

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("longitude", String.valueOf(location.getLongitude()));
        params.put("latitude", String.valueOf(location.getLatitude()));
        params.put("position", location.getAddrStr() == null ? "位置为空" : location.getAddrStr());
        params.put("gathertime", location.getTime() == null ? Helper.getDateTime() : location.getTime());

//            LogHelper.e("sss",location.getAddrStr());

        // Request a JSON response from the provided URL.
        GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.POST,
                //"http://validate.jsontest.com/?json={'key':'value'}",
                url,
                ResponseEntry.class,
                params,
                new Response.Listener<ResponseEntry>() {
                    @Override
                    public void onResponse(ResponseEntry response) {

                        //mLocationClient.stop();

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        if (response.getStatusCode() == 0) {

                            //wwt:暂时去掉，避免引起用户注意
                            LogHelper.i(TAG, "位置上传成功");
                        } else {
                            //Toast.makeText(getApplicationContext(), "上传失败:" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            LogHelper.i(TAG, "位置上传异常");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogHelper.i(TAG, "位置上传失败");
                        //mLocationClient.stop();
                        //Toast.makeText(getApplicationContext(), "上传失败-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }



}
