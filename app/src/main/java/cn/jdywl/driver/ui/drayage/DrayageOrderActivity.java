package cn.jdywl.driver.ui.drayage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.ui.LoginActivity;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by xycong on 2016/5/7.
 */
public class DrayageOrderActivity extends BaseActivity {

    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(DrayageOrderActivity.class);

    public static final String REFRESH_ACTION = "SRefreshAction";

    SectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmain);
        setupToolbar();

        ButterKnife.bind(this);

        if (!AppConfig.isLogin()) {
            Intent it = new Intent(this, LoginActivity.class);
            startActivityForResult(it, LoginActivity.LOGIN);
        }

        // 设置viewpager和tab
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mSectionsPagerAdapter);

        tabs.setupWithViewPager(viewpager);
        fab.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LoginActivity.LOGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //登录成功
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //登录不成功，finish
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return DPendingFragment.newInstance();
                case 1:
                    return DTransportingFragment.newInstance();
                case 2:
                    return DTodosFragment.newInstance();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "待拖运";
                case 1:
                    return "运输中";
                case 2:
                    return "已完成";
            }
            return null;
        }
    }

    public void setRefreshEnabled(boolean enabled) {
        Intent intent = new Intent(REFRESH_ACTION);
        intent.putExtra("enabled", enabled);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
