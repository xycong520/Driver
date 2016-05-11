package cn.jdywl.driver.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lenovo on 2015/3/23.
 */
public class BootStartReceiver extends BroadcastReceiver {
    //static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, LocalIntentService.class);
        mIntent.setAction(LocalIntentService.ACTION_START_LOCATION);
        //mIntent.setData(Uri.parse(dataUrl));
        //mIntent.setPackage(context.getPackageName());
        context.startService(mIntent);
    }
}
