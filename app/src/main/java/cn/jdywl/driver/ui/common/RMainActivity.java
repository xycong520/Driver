package cn.jdywl.driver.ui.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;

import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.LogHelper;

public class RMainActivity extends BaseActivity {

    private static String TAG = LogHelper.makeLogTag(RMainActivity.class);

    RMainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmain);
        ButterKnife.bind(this);
        setupToolbar();

        fragment = (RMainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setRefreshEnabled(boolean enabled) {
        fragment.setRefreshEnabled(enabled);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
