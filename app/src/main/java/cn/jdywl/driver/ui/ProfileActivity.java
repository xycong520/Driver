package cn.jdywl.driver.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.common.TitleDetailLvAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.Profile;
import cn.jdywl.driver.model.TitleDetailItem;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public class ProfileActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(ProfileActivity.class);
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.lv_profile)
    ListView lvProfile;

    private Profile profile;

    private List<TitleDetailItem> mList = new ArrayList<TitleDetailItem>();
    private TitleDetailLvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupToolbar();

        mAdapter = new TitleDetailLvAdapter(this, 0, mList);
        lvProfile.setAdapter(mAdapter);
        lvProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //设置Layout和margins等参数
                        LinearLayout layout = new LinearLayout(ProfileActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(Helper.dpToPx(ProfileActivity.this, 24), Helper.dpToPx(ProfileActivity.this, 24),
                                Helper.dpToPx(ProfileActivity.this, 24), Helper.dpToPx(ProfileActivity.this, 24));

                        //向LinearLayout加入输入框
                        final EditText et_name = new EditText(ProfileActivity.this);
                        et_name.setLayoutParams(params);
                        et_name.setFocusable(true);
                        //限定长度为20
                        et_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        et_name.setHint("在此输入姓名，最少两个汉字");
                        et_name.setTextSize(16);
                        et_name.setTextColor(getResources().getColorStateList(R.color.gray_title));

                        layout.addView(et_name);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle("修改姓名").setNegativeButton("取消", null);
                        builder.setView(layout);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String newname = et_name.getText().toString();
                                if (newname.length() < 2) {
                                    Toast.makeText(ProfileActivity.this, "姓名无效，最少两个汉字", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                updateProfile(newname);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    default:
                        break;
                }
            }
        });

        loadProfile();
    }

    private void loadProfile() {
        String url = ApiConfig.api_url + ApiConfig.CAROWNER_PROFILE_URL;

        GsonRequest<Profile> myReq = new GsonRequest<Profile>(Request.Method.GET,
                url,
                Profile.class,
                null,
                new Response.Listener<Profile>() {
                    @Override
                    public void onResponse(Profile response) {

                        //mInError = false;
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        profile = response;
                        TitleDetailItem name = new TitleDetailItem("姓名", profile.getName());
                        mList.add(name);
                        TitleDetailItem phone = new TitleDetailItem("手机号", profile.getPhone());
                        mList.add(phone);

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(ProfileActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void updateProfile(String name) {
        String url = ApiConfig.api_url + ApiConfig.CAROWNER_UPDATE_PROFILE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);

        GsonRequest<Profile> myReq = new GsonRequest<Profile>(Request.Method.PUT,
                url,
                Profile.class,
                params,
                new Response.Listener<Profile>() {
                    @Override
                    public void onResponse(Profile response) {

                        //mInError = false;
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        profile = response;

                        mList.clear();
                        TitleDetailItem name = new TitleDetailItem("姓名：", profile.getName());
                        mList.add(name);
                        TitleDetailItem phone = new TitleDetailItem("手机号：", profile.getPhone());
                        mList.add(phone);
                        mAdapter.notifyDataSetChanged();

                        //登录成功,且姓名有效
                        AppConfig.name = profile.getName();

                        // 存储姓名
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(AppConst.KEY_PREF_AUTH_NAME, AppConfig.name);
                        editor.commit(); //需要立即生效

                        Toast.makeText(ProfileActivity.this, "姓名修改成功", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(ProfileActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
