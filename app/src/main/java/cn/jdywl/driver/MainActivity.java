package cn.jdywl.driver;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.LocationClient;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.adapter.HomeGvAdapter;
import cn.jdywl.driver.app.JindouyunApplication;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderPage;
import cn.jdywl.driver.model.StatsItem;
import cn.jdywl.driver.model.VersionItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.push.BdPushUtils;
import cn.jdywl.driver.service.LocalIntentService;
import cn.jdywl.driver.ui.AppUpdateActivity;
import cn.jdywl.driver.ui.LoginActivity;
import cn.jdywl.driver.ui.UserActivity;
import cn.jdywl.driver.ui.carowner.CMainActivity;
import cn.jdywl.driver.ui.common.HelptelActivity;
import cn.jdywl.driver.ui.common.PriceQueryActivity;
import cn.jdywl.driver.ui.common.RMainActivity;
import cn.jdywl.driver.ui.common.SupportActivity;
import cn.jdywl.driver.ui.driver.DMainActivity;
import cn.jdywl.driver.ui.stage.CarStageActivity;
import cn.jdywl.driver.ui.stage.DriverOrderActivity;
import cn.jdywl.driver.ui.stage.MyAcceptOrderActivity;
import cn.jdywl.driver.ui.stage.StageManagerActivity;

public class MainActivity extends AppUpdateActivity {
    private static final String TAG = LogHelper.makeLogTag(MainActivity.class);
    private static final int PERMISSIONS_REQUEST_LOCATION = 0;
    private static final int PERMISSIONS_REQUEST_PHONE = 1;
    private static final int PERMISSIONS_REQUEST_STORAGE = 2;

    public static final String ACTION_LOGIN_OK = "loginok";

    //设置RecycleView的span为4
    public final int SPAN_COUNT = 4;
    public final static int MENU_COUNT = 7;
    public static int STAGE_COUNT = 4;

    protected Integer[] mTitleId = {
            R.string.home_title_carowner, R.string.home_title_driver,
            R.string.home_title_receiver, R.string.home_title_user,
            R.string.home_title_price, R.string.home_title_helptel,
            R.string.home_title_support
    };

    protected Integer[] mImgId = {
            R.drawable.home_tile_carowner, R.drawable.home_tile_driver,
            R.drawable.home_tile_receiver, R.drawable.home_tile_user,
            R.drawable.home_tile_price, R.drawable.home_tile_helptel,
            R.drawable.home_tile_support
    };
    protected Integer[] mStageID = {
            R.string.home_title_stage1, R.string.home_title_stage2
            , R.string.home_title_stage3, R.string.home_title_stage4
    };
    protected Integer[] mStageImgID = {
            R.drawable.home_title_fast_train, R.drawable.home_tile_stage
            , R.drawable.home_title_fast_train, R.drawable.home_tile_stage
    };


    @Bind(R.id.rv_home)
    RecyclerView rvHome;

    protected HomeGvAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private OrderPage mData = new OrderPage();
    StatsItem stats;

    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        registerReceiver(onDownloadComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        //registerReceiver(onNotificationClick,new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

        if (AppConfig.isLogin())  //用户登录时才初始化百度push
        {
            //Android M以上版本需要动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //申请用户权限
                requestLocationPermission();
                requestPhonePermission();
                requestStoragePermission();
            } else {

                initPush(); //M以下版本默认全部获取权限，因此立即初始化
            }
        }

        setupRecyclerView(rvHome);

        mLocationClient = ((JindouyunApplication) getApplication()).mLocationClient;
    }

    //初始化百度push
    public void initPush() {

        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        //！！ 请将AndroidManifest.xml 128 api_key 字段值修改为自己的 api_key 方可使用 ！！
        //！！ ATTENTION：You need to modify the value of api_key to your own at row 128 in AndroidManifest.xml to use this Demo !!
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                BdPushUtils.getMetaValue(MainActivity.this, "com.baidu.cloudpush.API_KEY"));
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        /*
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier(
                        "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        cBuilder.setNotificationSound(Uri.withAppendedPath(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, cBuilder);
        */
    }

    void setupRecyclerView(RecyclerView rv) {
        //布局为GridView
        mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position >= 1 && position <= MENU_COUNT) {
                    return 1;
                } else if (position == MENU_COUNT + 1) {
                    return SPAN_COUNT;
                } else if (position > MENU_COUNT + 1 && position <= MENU_COUNT + 1 + STAGE_COUNT) {
                    return 1;
                } else {
                    return SPAN_COUNT;
                }
            }
        });

        rv.setLayoutManager(mLayoutManager);

        mAdapter = new HomeGvAdapter(mTitleId, mImgId, mStageID, mStageImgID, mData.getData());
        rv.setAdapter(mAdapter);

        rv.setHasFixedSize(true);
        //rv.addItemDecoration(new DividerGridItemDecoration(this));

        /*
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_tile_space);
        rv.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        */

        mAdapter.setOnItemClickLitener(new HomeGvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    return;
                }
                if (position >= 1 && position <= MENU_COUNT) {
                    switch (position) {
                        case 1:
                            OpenCMainActivity();
                            break;
                        case 2:
                            OpenDMainActivity();
                            break;
                        case 3:
                            OpenRMainActivity();
                            break;
                        case 4:
                            OpenUserActivity();
                            break;
                        case 5:
                            OpenPriceQueryActivity();
                            break;
                        case 6:
                            OpenHelptelActivity();
                            break;
                        case 7:
                            OpenSupportActivity();
                            break;
                    }
                    return;
                }
                if (position > MENU_COUNT + 1 && position <= MENU_COUNT + 1 + STAGE_COUNT) {
                    switch (position) {
                        case MENU_COUNT + 2:
                            OpenCarStageActivity();
                            break;
                        case MENU_COUNT + 3:
                            OpenStageManagerActivity();
                            break;
                        case MENU_COUNT + 4:
                            OpenDriverOrderActivity();
                            break;
                        case MENU_COUNT + 5:
                            OpenMyAcceptOrderActivity();
                            break;

                    }
                    return;
                }

                if (position == MENU_COUNT + 1 + STAGE_COUNT + 1) {
                    OpenDMainActivity();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    void requestLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                LogHelper.i(TAG, "解释位置权限");
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("位置访问权限未开启");
                builder.setMessage("为了使用轿车在途位置跟踪功能，需要您允许位置访问权限\n")
                        .setPositiveButton("前往开启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .setNegativeButton("仍然拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //权限申请被用户拒绝后停止定位
                                if (mLocationClient.isStarted()) {
                                    mLocationClient.stop();
                                }
                            }
                        });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //权限申请被用户拒绝后停止定位
                        if (mLocationClient.isStarted()) {
                            mLocationClient.stop();
                        }
                    }
                });

                // Create the AlertDialog object and return it
                builder.create().show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);

                // PERMISSIONS_REQUEST_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                LogHelper.i(TAG, "申请位置权限");
            }
        } else { //获取location权限之后再开启百度定位

            //启动后台service
            Intent mIntent = new Intent(this.getApplicationContext(), LocalIntentService.class);
            mIntent.setAction(LocalIntentService.ACTION_START_LOCATION);
            //mIntent.setPackage(getPackageName());
            startService(mIntent);
        }
    }

    void requestPhonePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                LogHelper.i(TAG, "解释读取手机状态权限");
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("读取手机状态权限未开启");
                builder.setMessage("为了第一时间接收我们的通知消息，需要您允许读取手机状态权限\n")
                        .setPositiveButton("前往开启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_PHONE_STATE},
                                        PERMISSIONS_REQUEST_PHONE);
                            }
                        })
                        .setNegativeButton("仍然拒绝", null);

                // Create the AlertDialog object and return it
                builder.create().show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_PHONE);

                // PERMISSIONS_REQUEST_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                LogHelper.i(TAG, "申请读取手机状态权限");
            }
        } else {  //开启read phone权限之后再开启百度push

            initPush();
        }
    }

    void requestStoragePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                LogHelper.i(TAG, "解释存储权限");
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("存储权限未开启");
                builder.setMessage("为了正常查看电子保单，需要您允许存储权限\n")
                        .setPositiveButton("前往开启", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_STORAGE);
                            }
                        })
                        .setNegativeButton("仍然拒绝", null);

                // Create the AlertDialog object and return it
                builder.create().show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_STORAGE);

                // PERMISSIONS_REQUEST_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                LogHelper.i(TAG, "申请位置权限");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LogHelper.i(TAG, "访问位置授权成功。");
                    if (!mLocationClient.isStarted()) {
                        mLocationClient.start();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    LogHelper.i(TAG, "访问位置授权失败。");
                    Toast.makeText(MainActivity.this, "您拒绝了位置访问权限，在途位置功能无法使用。", Toast.LENGTH_SHORT).show();
                    //权限申请被用户拒绝后停止定位
                    if (mLocationClient.isStarted()) {
                        mLocationClient.stop();
                    }
                }
                return;
            }
            case PERMISSIONS_REQUEST_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LogHelper.i(TAG, "读取手机状态授权成功。");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    LogHelper.i(TAG, "读取手机状态授权失败。");
                    Toast.makeText(MainActivity.this, "您拒绝了读取手机状态权限，无法在第一时间接收通知。", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LogHelper.i(TAG, "存储权限授权成功。");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    LogHelper.i(TAG, "存储权限授权失败。");
                    Toast.makeText(MainActivity.this, "您拒绝了存储访问权限，查看电子保单功能可能无法使用。", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AppConfig.isLogin()) {
            Intent it = new Intent(this, LoginActivity.class);
            startActivityForResult(it, LoginActivity.LOGIN);
        }


        //加载订单和统计
        loadStats();
        loadOrder();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LoginActivity.LOGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //登录成功
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                if (!getMasterRoles().equals("station_master")) {
                    mStageID = removeElement(mStageID, 1);
                    mStageImgID = removeElement(mStageImgID, 1);
                    STAGE_COUNT -= 1;
                }
                //Android M以上版本需要动态申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //申请用户权限
                    requestLocationPermission();
                    requestPhonePermission();
                    requestStoragePermission();
                } else {
                    initPush(); //M以下版本默认全部获取权限，因此立即初始化
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        String action = intent.getAction();
        if (action != null) {
            if (action.equals(ACTION_LOGIN_OK)) {

            }
        }
    }

    /**
     * 打开货主页面
     */
    public void OpenCMainActivity() {
        Intent it = new Intent(this, CMainActivity.class);
        startActivity(it);
    }

    /**
     * 打开司机页面
     */
    public void OpenDMainActivity() {
        Intent it = new Intent(this, DMainActivity.class);
        startActivity(it);
    }

    /**
     * 打开车驿站页面
     */
    public void OpenCarStageActivity() {
        Intent it = new Intent(this, CarStageActivity.class);
        startActivity(it);
    }
    /**
     * 打开驿站管理页面
     */
    public void OpenStageManagerActivity() {
        Intent it = new Intent(this, StageManagerActivity.class);
        startActivity(it);
    }
    /**
     * 打开我的承运
     */
    public void OpenMyAcceptOrderActivity() {
        Intent it = new Intent(this, MyAcceptOrderActivity.class);
        startActivity(it);
    }
    /**
     * 打开车小板运输
     */
    public void OpenDriverOrderActivity() {
        Intent it = new Intent(this, DriverOrderActivity.class);
        startActivity(it);
    }

    /**
     * 打开价格查询页面
     */
    public void OpenPriceQueryActivity() {
        Intent it = new Intent(this, PriceQueryActivity.class);
        startActivity(it);
    }


    /**
     * 打开收车人订单查询页面
     */
    public void OpenRMainActivity() {
        Intent it = new Intent(this, RMainActivity.class);
        startActivity(it);
    }

    /**
     * 打开个人中心
     */
    public void OpenUserActivity() {
        Intent it = new Intent(this, UserActivity.class);
        startActivity(it);
    }

    /**
     * 打开客服页面
     */
    public void OpenSupportActivity() {
        Intent it = new Intent(this, SupportActivity.class);
        it.putExtra("url", ApiConfig.WEB_HOME + "about");
        startActivity(it);
    }


    /**
     * 打开带路电话页面
     */
    public void OpenHelptelActivity() {
        Intent it = new Intent(this, HelptelActivity.class);
        startActivity(it);
    }

    //下载完成或者用户点击通知栏时开始安装apk
    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            try {
                Bundle extras = intent.getExtras();
                SharedPreferences mSharedPref = getSharedPreferences(JDY_PRE, Context.MODE_PRIVATE);
                long myDownloadID = mSharedPref.getLong(DOWNLOAD_APK_ID, 0);
                int version = mSharedPref.getInt(VERSION_CODE, 1);
                mSharedPref.edit().remove(DOWNLOAD_APK_ID).apply();

                long downloadCompletedId = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
                DownloadManager.Query q = new DownloadManager.Query();

                if (myDownloadID == downloadCompletedId) {
                    q.setFilterById(downloadCompletedId);
                    DownloadManager mManager = (DownloadManager) MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor c = mManager.query(q);
                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
                            promptInstall.setDataAndType(
                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/jindouyun_v" + getVertionItem().getVersionName() + ".apk")),
                                    "application/vnd.android.package-archive");
                            promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(promptInstall);
                        }
                    }
                    c.close();
                }
            } finally {

            }
        }
    };

    /*
    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {

        }
    };
    */


    private void loadOrder() {
        String url = ApiConfig.api_url + ApiConfig.MARKET_ORDER_URL +
                "&page_size=" + 5 +
                "&page=" + 1;

        GsonRequest<OrderPage> myReq = new GsonRequest<OrderPage>(Request.Method.GET,
                url,
                OrderPage.class,
                null,
                new Response.Listener<OrderPage>() {
                    @Override
                    public void onResponse(OrderPage response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        //先清除，再添加
                        mData.getData().clear();

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

                        Helper.processVolleyErrorMsg(MainActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void loadStats() {
        String url = ApiConfig.api_url + ApiConfig.STATS;

        GsonRequest<StatsItem> myReq = new GsonRequest<StatsItem>(Request.Method.GET,
                url,
                StatsItem.class,
                null,
                new Response.Listener<StatsItem>() {
                    @Override
                    public void onResponse(StatsItem response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        stats = response;

                        mAdapter.setTotalCount(stats.getSumcars());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(MainActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    public void updateNotify(boolean bNewVersion, VersionItem ver) {
        if (bNewVersion) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("检测到新版本(v" + ver.getVersionName() + ")");
            builder.setMessage(ver.getDesc())
                    .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            downloadAPK();
                        }
                    })
                    .setNegativeButton("稍后再说", null);
            // Create the AlertDialog object and return it
            builder.create().show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //取消volley 连接
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private Integer[] removeElement(Integer[] array, int removeIndex) {
        Integer[] newArray = new Integer[array.length - 1];
        boolean isRemove = false;
        for (int i = 0; i < array.length; i++) {
            if (i == removeIndex) {
                isRemove = true;
                continue;
            } else {
                if (isRemove) {
                    newArray[i - 1] = array[i];
                } else {
                    newArray[i] = array[i];
                }
            }
        }
        return newArray;
    }

    public String getMasterRoles() {
        String roles = "";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        roles = sharedPref.getString(AppConst.KEY_PREF_AUTH_ROLES_MASTER, "");

        return roles;
    }
}
