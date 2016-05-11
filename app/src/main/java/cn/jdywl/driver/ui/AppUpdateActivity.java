package cn.jdywl.driver.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.ResponseEntry;
import cn.jdywl.driver.model.VersionItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public abstract class AppUpdateActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(AboutActivity.class);

    public static final String DOWNLOAD_APK_ID = "apkID";
    public static final String VERSION_CODE = "versionCode";
    public static final String JDY_PRE = "cn.jdywl.driver";

    private int versionCode;
    private String versionName;
    private VersionItem ver;
    private long myDownloadID = 0;
    boolean bNewVersion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查是否有新版本
        checkForUpdate();
    }

    /**
     * 检查更新
     */
    public void checkForUpdate() {

        readAppVersionCode();

        String url = ApiConfig.api_url + ApiConfig.VERSION_URL;

        GsonRequest<VersionItem> myReq = new GsonRequest<VersionItem>(Request.Method.GET,
                url,
                VersionItem.class,
                null,
                new Response.Listener<VersionItem>() {
                    @Override
                    public void onResponse(VersionItem response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        ver = response;

                        if (ver.getPlatform() != 0) {
                            LogHelper.e(TAG, "platform(%d)不是安卓", ver.getPlatform());
                            return;
                        }

                        //检测到新版本
                        if (ver.getVersionCode() > versionCode) {
                            bNewVersion = true;
                            updateNotify(bNewVersion, ver);
                        } else {
                            bNewVersion = false;
                            updateNotify(bNewVersion, ver);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(AppUpdateActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void readAppVersionCode() {
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            versionUpload(String.format("%s(%d)", versionName, versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            LogHelper.e(TAG, e.getMessage());
        }
    }

    private void versionUpload(String versionName) {
        String url = ApiConfig.api_url + ApiConfig.APPVERSION_URL;

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("apptype", "1");  //1: android app, 2: ios app
        params.put("version", versionName);

        GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.POST,
                url,
                ResponseEntry.class,
                params,
                new Response.Listener<ResponseEntry>() {
                    @Override
                    public void onResponse(ResponseEntry response) {

                        if (response == null) {
                            LogHelper.i(TAG, "appversion response为空");
                            return;
                        }

                        LogHelper.i(TAG, "版本号上传成功");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(AppUpdateActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    public void downloadAPK() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //只有获取到存储访问权限后才下载APP，要不然会FC
            Toast.makeText(this, "存储访问权限没被授权，下载新版APP失败。请前往微信公众号下载。", Toast.LENGTH_LONG).show();
            return;
        }

        String apk = "jindouyun_v" + ver.getVersionName() + ".apk";
        String url = ApiConfig.WEB_HOME + "assets/" + apk;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDescription("筋斗云轿车物流");
        request.setTitle(apk);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apk);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        myDownloadID = manager.enqueue(request);

        SharedPreferences settings = getSharedPreferences(JDY_PRE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(DOWNLOAD_APK_ID, myDownloadID);
        editor.putInt(VERSION_CODE, versionCode);
        editor.apply();

        Toast.makeText(this, "开始下载...", Toast.LENGTH_SHORT).show();
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }


    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public boolean hasNewVersion() {
        return bNewVersion;
    }

    public VersionItem getVertionItem() {
        return ver;
    }

    //设置是否enable refresh
    protected abstract void updateNotify(boolean bNewVersion, VersionItem ver);

}
