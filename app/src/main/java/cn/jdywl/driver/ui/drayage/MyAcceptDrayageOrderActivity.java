package cn.jdywl.driver.ui.drayage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
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
import cn.jdywl.driver.ui.driver.DMainActivity;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MyAcceptDrayageOrderActivity extends BaseActivity {

    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(DMainActivity.class);

    public static final String REFRESH_ACTION = "MyAcceptOrderActivityAction";

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmain);
        setupToolbar();

        ButterKnife.bind(this);

        if (!AppConfig.isLogin()) {
            Intent it = new Intent(MyAcceptDrayageOrderActivity.this, LoginActivity.class);
            startActivityForResult(it, LoginActivity.LOGIN);
        }

        // 设置viewpager和tab
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mSectionsPagerAdapter);

        tabs.setupWithViewPager(viewpager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dmain, menu);
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
            case R.id.action_search:
                //Intent it = new Intent(DMainActivity.this, SearchOrderActivity.class);
                //startActivity(it);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LoginActivity.LOGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //登录成功
                Toast.makeText(MyAcceptDrayageOrderActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
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
                    return DMarketFragment.newInstance();
                case 1:
                    return DOrderFragment.newInstance();
                case 2:
                    return DHistoryOrderFragment.newInstance();
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
                    return getString(R.string.title_driver1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_driver2).toUpperCase(l);
                case 2:
                    return "历史承运";
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

