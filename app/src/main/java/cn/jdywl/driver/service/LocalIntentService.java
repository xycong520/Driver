package cn.jdywl.driver.service;

import android.app.IntentService;
import android.content.Intent;

import com.baidu.location.LocationClient;

import cn.jdywl.driver.app.JindouyunApplication;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class LocalIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_START_LOCATION = "cn.jdywl.driver.service.action.START_LOCATION";

    private LocationClient mLocationClient;

    public LocalIntentService() {
        super("LocalIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_LOCATION.equals(action)) {
                mLocationClient = ((JindouyunApplication) getApplication()).mLocationClient;
                if (!mLocationClient.isStarted()) {
                    mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                }
            }
        }
    }


}
