package cn.jdywl.driver.ui.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.common.TitleDetailLvAdapter;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.model.TitleDetailItem;

public class VinActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(VinActivity.class);

    @Bind(R.id.lv_vin)
    ListView lvVin;

    private TitleDetailLvAdapter mAdapter;

    private List<TitleDetailItem> mList = new ArrayList<TitleDetailItem>();
    private OrderItem order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vin);
        ButterKnife.bind(this);
        setupToolbar();

        //获取商品描述和支付信息
        order = getIntent().getParcelableExtra("order");

        for (int i = 0; i < order.getCarNum(); i++) {
            switch (i) {
                case 0:
                    mList.add(new TitleDetailItem("车架号", order.getVin1()));
                    break;
                case 1:
                    mList.add(new TitleDetailItem("车架号", order.getVin1()));
                    break;
                case 2:
                    mList.add(new TitleDetailItem("车架号", order.getVin3()));
                    break;
                case 3:
                    mList.add(new TitleDetailItem("车架号", order.getVin4()));
                    break;
                case 4:
                    mList.add(new TitleDetailItem("车架号", order.getVin5()));
                    break;
                case 5:
                    mList.add(new TitleDetailItem("车架号", order.getVin6()));
                    break;
                case 6:
                    mList.add(new TitleDetailItem("车架号", order.getVin7()));
                    break;
                case 7:
                    mList.add(new TitleDetailItem("车架号", order.getVin8()));
                    break;
                case 8:
                    mList.add(new TitleDetailItem("车架号", order.getVin9()));
                    break;
                default:
                    mList.add(new TitleDetailItem("车架号", order.getOrderNo()));
                    break;
            }

        }

        mAdapter = new TitleDetailLvAdapter(this, R.layout.simple_dropdown_item, mList);
        lvVin.setAdapter(mAdapter);

        lvVin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String filename = order.getOrderNo() + "-" + mList.get(position).getDetail() + ".pdf";
                /*
                Intent it = new Intent(VinActivity.this, InsuranceActivity.class);


                it.putExtra("filename",filename);
                startActivity(it);
                */

                //修改为使用外置浏览器打开PDF文档
                String url = ApiConfig.api_url + "assets/insurance/" + filename;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vin, menu);
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

        return super.onOptionsItemSelected(item);
    }
    */

    /**
     * 打开司机页面
     *
     * @param view
     */
    public void downloadPdfReader(View view) {
        //修改为使用外置浏览器打开PDF文档
        String url = "https://ardownload2.adobe.com/pub/adobe/reader/android/15.x/15.2.2/arm/AdobeReader.apk";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
