package cn.jdywl.driver.ui.stage;

import android.os.Bundle;

import com.android.volley.RequestQueue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by xycong on 2016/6/18.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    ProgressWebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        String data = getIntent().getStringExtra("data");
        setupToolbar();
        //        try {

            webView.loadDataWithBaseURL(null,data, "text/html", "utf-8", null);//webView.loadData(URLEncoder.encode(data, "utf-8"), "text/html", "utf-8");
//            webView.loadData(URLEncoder.encode(data, "utf-8"), "text/html", "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            webView.loadUrl(getIntent().getStringExtra("url"));
//            e.printStackTrace();
//        }
    }

    @Override
    protected void setRefreshEnabled(boolean enabled) {

    }

    @Override
    protected void cancelVolleyRequest(RequestQueue queue) {

    }
}
