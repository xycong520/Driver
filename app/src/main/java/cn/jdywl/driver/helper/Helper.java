package cn.jdywl.driver.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.DisplayMetrics;

import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import cn.jdywl.driver.R;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.ui.LoginActivity;

/**
 * Created by wuwantao on 15-2-7.
 */
public class Helper {
    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
    */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    // 使用正则表达式验证手机号码是否合法
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^\\s*(15\\d{9}|13[0-9]\\d{8}|18\\d{9}|17\\d{9}|14\\d{9})\\s*$");
        return pattern.matcher(phone).matches();
    }

    // 显示volley的错误信息
    public static void processVolleyErrorMsg(Context mContext, VolleyError error) {

        if(error == null)
        {
            LogHelper.e(mContext.toString(), "VolleyError为空");
            return;
        }
        //如果返回码是401，未授权，则重新登录
        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
            // 退出登录，删除用户认证信息
            LoginActivity.logout(mContext);

            //跳转到用户登录界面
            Intent it = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(it);
        }

        //Toast.makeText(mContext, "无法获取信息", Toast.LENGTH_SHORT).show();
    }

    // 使用正则表达式验证手机号码是否合法
    public static String getCarTypeByid(Context c, int code) {

        if(code > AppConst.CAR_CAR)
        {
            return "车型错误";
        }

        String[] carType = c.getResources().getStringArray(R.array.cartype_arrays);

        return carType[code];
    }

    public static int dpToPx(Context c, int dp) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context c, int px) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static String getDateTime() {
        //获取当前时间
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return dff.format(new Date());
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
