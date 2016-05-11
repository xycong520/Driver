package cn.jdywl.driver.push;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import java.util.ArrayList;
import java.util.List;

public class BdPushUtils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "error " + e.getMessage());
        }
        return apiKey;
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }

    // ��share preference��ʵ���Ƿ�󶨵Ŀ��ء���ionBind�ҳɹ�ʱ����true��unBind�ҳɹ�ʱ����false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        return "ok".equalsIgnoreCase(flag);
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static void setBindUserId(Context context, String userId){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_userid", userId);
        editor.commit();
    }

    public static String getBindUserId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("bind_userid", "");
    }

    public static void setBindChannelId(Context context, String channelid){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_channelid", channelid);
        editor.commit();
    }

    public static String getBindChannelId(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("bind_channelid", "");
    }


    // 打开富媒体列表界面
    public static void openRichMediaList(Context context) {
        // Push: 打开富媒体消息列表
        Intent sendIntent = new Intent();
        sendIntent.setClassName(context,
                "com.baidu.android.pushservice.richmedia.MediaListActivity");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }

    // 删除tag操作
    public static void deleteTags(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText textviewGid = new EditText(context);
        textviewGid.setHint("请输入多个标签，以英文逗号隔开");
        layout.addView(textviewGid);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setView(layout);
        builder.setPositiveButton("删除标签",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Push: 删除tag调用方式
                        List<String> tags = BdPushUtils.getTagsList(textviewGid
                                .getText().toString());
                        PushManager.delTags(context.getApplicationContext(), tags);
                    }
                });
        builder.show();
    }

    // 设置标签,以英文逗号隔开
    public static void setTags(final Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText textviewGid = new EditText(context);
        textviewGid.setHint("请输入多个标签，以英文逗号隔开");
        layout.addView(textviewGid);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setView(layout);
        builder.setPositiveButton("设置标签",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Push: 设置tag调用方式
                        List<String> tags = BdPushUtils.getTagsList(textviewGid
                                .getText().toString());
                        PushManager.setTags(context.getApplicationContext(), tags);
                    }

                });
        builder.show();
    }

    // 以apikey的方式绑定
    public static void initWithApiKey(Context context) {
        // Push: 无账号初始化，用api key绑定
        // checkApikey();
        PushManager.startWork(context.getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                BdPushUtils.getMetaValue(context, "com.baidu.cloudpush.API_KEY"));
    }

    // 解绑
    public static void unBindForApp(Context context) {
        // Push: 解绑
        PushManager.stopWork(context.getApplicationContext());
    }

    // 列举tag操作
    public static void showTags(Context context) {
        PushManager.listTags(context.getApplicationContext());
    }

}
