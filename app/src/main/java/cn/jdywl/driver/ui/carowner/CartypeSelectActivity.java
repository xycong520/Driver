package cn.jdywl.driver.ui.carowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.ui.common.BaseActivity;

public class CartypeSelectActivity extends BaseActivity {
    public static final String TAG = LogHelper.makeLogTag(CartypeSelectActivity.class);
    public static final String CARTYPE_SELECT = "cartype";

    @Bind(R.id.tv_car)
    TextView tvCar;
    @Bind(R.id.tv_suv)
    TextView tvSuv;
    @Bind(R.id.tv_bigsuv)
    TextView tvBigsuv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartype_select);
        ButterKnife.bind(this);
        setupToolbar();
    }

    /**
     * 选择启运日期
     *
     * @param view
     */
    public void selectCartype(View view) {
        String[] str_array = getResources().getStringArray(R.array.cartype_arrays);
        String cartype;
        switch (view.getId()) {
            case R.id.tv_car: {
                cartype = str_array[2];
                break;
            }
            case R.id.tv_suv: {
                cartype = str_array[1];
                break;
            }
            case R.id.tv_bigsuv: {
                cartype = str_array[0];
                break;
            }
            default: {
                cartype = "车型无效";
            }
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", cartype);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
