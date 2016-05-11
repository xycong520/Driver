package cn.jdywl.driver.ui.common;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.helper.LogHelper;

public class NetworkImageActivity extends BaseActivity {
    private static final String TAG = LogHelper.makeLogTag(NetworkImageActivity.class);

    @Bind(R.id.iv_receiver_photo)
    NetworkImageView ivReceiverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_image);
        ButterKnife.bind(this);

        setupToolbar();

        //加载收车人照片
        String url = getIntent().getStringExtra("url");
        ivReceiverPhoto = (NetworkImageView) findViewById(R.id.iv_receiver_photo);
        ImageLoader mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        ivReceiverPhoto.setImageUrl(url, mImageLoader);

    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
