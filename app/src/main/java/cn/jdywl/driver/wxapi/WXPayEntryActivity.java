package cn.jdywl.driver.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.MainActivity;
import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.ui.carowner.CMainActivity;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.common.PayActivity;
import cn.jdywl.driver.ui.driver.DMainActivity;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = LogHelper.makeLogTag(WXPayEntryActivity.class);

    public static final String PAY_RESULT = "PayResult";  //0: 支付成功，-1：支付失败， -2：超时无法支付,-3:unkonwn

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.btn_back)
    Button btnBack;

    private IWXAPI api;

    int payRole = -1;
    int payResult = -3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        ButterKnife.bind(this);
        setupToolbar();

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);

        Intent it = getIntent();

        payRole = it.getIntExtra(PayActivity.PAY_ROLE, -1);

        payResult = it.getIntExtra(PAY_RESULT,-3);

        switch (payResult)
        {
            case -1:
                tvResult.setText("支付失败，请稍后再试。");
                break;
            case -2:
                tvResult.setText("已经超过支付时间，无法继续支付！");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogHelper.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        /**
         * 回调中errCode值列表：
         * 0	成功	展示成功页面
         * -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
         * -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
         */
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            Toast.makeText(this, getString(R.string.pay_result_callback_msg, "错误码:" + String.valueOf(resp.errCode)), Toast.LENGTH_SHORT).show();

            switch (resp.errCode) {
                case 0:
                    break;
                case -1:
                    tvResult.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvResult.setText("支付失败，请稍后再试。");
                    break;
                case -2:
                    tvResult.setTextColor(getResources().getColor(R.color.colorHighlight));
                    tvResult.setText("支付被主动取消");
                    break;
            }
        }
    }

    /**
     * 打开个人信息页面
     *
     * @param view
     */
    public void BackToMain(View view) {
        Intent it;
        switch (payRole) {
            case 0:  //货主支付
                it = new Intent(this, CMainActivity.class);
                break;
            case 1: //司机支付
                it = new Intent(this, DMainActivity.class);
                break;
            default:  //其它
                it = new Intent(this, MainActivity.class);
                break;
        }

        startActivity(it);
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}