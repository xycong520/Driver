package cn.jdywl.driver.config;

import android.text.TextUtils;

public class AppConfig {

    //用户登录信息，phone作为用户名
    public static String name;
    public static String phone;
    public static String password;
    public static String roles[];
    public static boolean bLogin = false;
    public static long logintime;   //登录时间，seconds since January 1

    //API URL
    public static String api_url;


    public static boolean isLogin() {
        long now = System.currentTimeMillis() / 1000;
        //登录时间30天有效
        return (bLogin && ((now - logintime) < (30 * 24 * 3600)) && !TextUtils.isEmpty(name));
    }
}
