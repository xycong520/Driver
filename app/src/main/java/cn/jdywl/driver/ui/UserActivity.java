package cn.jdywl.driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.MainActivity;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.Profile;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.carowner.CHistoryActivity;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.driver.DHistoryActivity;

public class UserActivity extends BaseActivity {

    public static final String TAG = LogHelper.makeLogTag(UserActivity.class);
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        setupToolbar();

        loadProfile();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvName.setText(getString(R.string.profile_name, AppConfig.name));
        tvPhone.setText(getString(R.string.profile_phone, AppConfig.phone));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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

                        tvName.setText(getString(R.string.profile_name, response.getName()));
                        tvPhone.setText(getString(R.string.profile_phone, response.getPhone()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Helper.processVolleyErrorMsg(UserActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    /**
     * 打开个人信息页面
     *
     * @param view
     */
    public void OpenProfileActivity(View view) {
        Intent it = new Intent(this, ProfileActivity.class);
        startActivity(it);
    }

    /**
     * 打开货主历史订单页面
     *
     * @param view
     */
    public void OpenCHistoryActivity(View view) {
        Intent it = new Intent(this, CHistoryActivity.class);
        startActivity(it);
    }

    /**
     * 打开司机历史
     *
     * @param view
     */
    public void OpenDHistoryActivity(View view) {
        Intent it = new Intent(this, DHistoryActivity.class);
        startActivity(it);
    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void Logout(View view) {

        LoginActivity.logout(this);

        //跳转到用户登录界面
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
