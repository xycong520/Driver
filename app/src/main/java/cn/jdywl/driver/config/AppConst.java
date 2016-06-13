package cn.jdywl.driver.config;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuwantao on 15-2-7.
 */
public class AppConst {

    //用户登录常量
    public static final String KEY_PREF_AUTH_ISLOGIN = "pref_key_auth_islogin";
    public static final String KEY_PREF_AUTH_PHONE = "pref_key_auth_username";
    public static final String KEY_PREF_AUTH_PASSWORD = "pref_key_auth_password";
    public static final String KEY_PREF_AUTH_NAME = "pref_key_auth_name";
    public static final String KEY_PREF_AUTH_LOGINTIME = "pref_key_auth_logintime";
    public static final String KEY_PREF_AUTH_ROLES_DRIVER = "pref_key_auth_roles_driver";
    public static final String KEY_PREF_AUTH_ROLES_MASTER = "pref_key_auth_roles_master";

    //百度push常量
    public static final String KEY_PREF_PUSH_APPID = "pref_key_push_appid";
    public static final String KEY_PREF_PUSH_USERID = "pref_key_push_userId";
    public static final String KEY_PREF_PUSH_CHANNELID = "pref_key_push_channelId";

    //轿车类型
    public static final int CAR_BIGSUV = 0;
    public static final int CAR_SUV = 1;
    public static final int CAR_CAR = 2;

    //用户输入轿车总价的限制，最小1万，最大5000万
    public static final int MIN_TOTAL_PRICE = 10000;
    public static final int MAX_TOTAL_PRICE = 50000000;


    //状态码
    public static final int USER_INEXIST = 10400;//用户不存在

    public static ProgressDialog dialog;

    public static void showDialog(Context mContext) {
        if (dialog == null) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("加载中，请稍等");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface d) {
                    dialog = null;
                }
            });
        } else {
            return;
        }
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static String getCurTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
}


