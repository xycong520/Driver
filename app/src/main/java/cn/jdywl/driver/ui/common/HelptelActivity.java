package cn.jdywl.driver.ui.common;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.ui.component.InputDialogFragment;

public class HelptelActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        InputDialogFragment.OnFragmentInteractionListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    HelptelFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helptel);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        fragment = (HelptelFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        // Set up the action bar.
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_helptel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            fragment.setRefreshEnabled(true);
        } else {
            fragment.setRefreshEnabled(false);
        }
    }

    @Override
    public void onDialogPositiveClick(String input) {
        fragment.searchCity(input);
    }

}
