package cn.jdywl.driver.ui.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.ui.SettingsActivity;


public class SupportActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //现实加载进度条，必须在setContentView之前调用
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);

        //设置toolbar作为actionbar
        setSupportActionBar(toolbar);

        // Set up the action bar.
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent it = getIntent();
        String url = it.getStringExtra("url");

        webview_init(url);
    }

    /*
     * WebView初始化，开启JavaScript功能
     */
    private void webview_init(String url) {

        WebSettings webSettings = webview.getSettings();
        //设置使用够执行JS脚本
        webSettings.setJavaScriptEnabled(true);
        //设置使支持缩放
        //webSettings.setBuiltInZoomControls(true);

        //设置webChromeClient，现实进度条
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                //Make the bar disappear after URL is loaded, and changes string to Loading...
                SupportActivity.this.setTitle("正在加载...");
                SupportActivity.this.setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    SupportActivity.this.setTitle(R.string.title_hotline);
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //tel:开头直接拨打电话
                if (url.startsWith("tel:")) {
                    final boolean telAvailable = Helper.isIntentAvailable(SupportActivity.this, Intent.ACTION_DIAL);
                    if (telAvailable) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(intent);
                    } else {
                        Toast.makeText(SupportActivity.this, "未安装电话应用，无法拨打电话", Toast.LENGTH_SHORT).show();
                    }

                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    view.loadUrl(url);// 使用当前WebView处理跳转
                }
                return true; //true表示此事件在此处被处理，不需要再广播
            }

            @Override   //转向错误时的处理
            @TargetApi(Build.VERSION_CODES.M)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // 打印错误信息
                Toast.makeText(SupportActivity.this, "Oh no! " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });

        //加载网址
        webview.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_support, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                it = new Intent(this, SettingsActivity.class);
                startActivity(it);
                return true;
            case R.id.action_call:
                final boolean telAvailable = Helper.isIntentAvailable(this, Intent.ACTION_DIAL);
                if (telAvailable) {
                    it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006501387"));
                    startActivity(it);
                } else {
                    Toast.makeText(this, "未安装电话应用，无法拨打电话", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
