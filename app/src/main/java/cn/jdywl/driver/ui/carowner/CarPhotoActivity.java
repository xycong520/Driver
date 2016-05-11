package cn.jdywl.driver.ui.carowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.carowner.CarPhotosAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.CarPhotosEntity;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;
import cn.jdywl.driver.ui.common.NetworkImageActivity;

public class CarPhotoActivity extends BaseActivity {
    private static final String TAG = LogHelper.makeLogTag(CarPhotoActivity.class);

    public final int SPAN_COUNT = 2;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    private List<String> mlist = new ArrayList<String>();
    private List<String> mUrlList = new ArrayList<String>();
    protected CarPhotosAdapter mAdapter;

    @Bind(R.id.rv_carphoto)
    RecyclerView rvCarphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photo);
        ButterKnife.bind(this);

        setupToolbar();

        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            getCarPhotos(id);
        }
    }

    //设置RecyclerView
    private void setupRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // 提高性能
        rvCarphoto.setHasFixedSize(true);

        //布局为GridView
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String url = mlist.get(position);
                //非HTTP开头则为Header
                if (url.startsWith("http:")) {
                    return 1;
                } else {//Header
                    return SPAN_COUNT;
                }
            }
        });
        rvCarphoto.setLayoutManager(mLayoutManager);

        //设置adapter
        mAdapter = new CarPhotosAdapter(mlist);
        mAdapter.setOnItemClickListener(new CarPhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent it = new Intent(CarPhotoActivity.this, NetworkImageActivity.class);
                it.putExtra("url", mUrlList.get(pos));
                CarPhotoActivity.this.startActivity(it);
            }
        });

        rvCarphoto.setAdapter(mAdapter);

        rvCarphoto.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void getCarPhotos(int id) {
        progressbar.setVisibility(View.VISIBLE);
        String url = ApiConfig.api_url + ApiConfig.CARPHOTO_URL + id;

        GsonRequest<CarPhotosEntity> myReq = new GsonRequest<CarPhotosEntity>(Request.Method.GET,
                url,
                CarPhotosEntity.class,
                null,
                new Response.Listener<CarPhotosEntity>() {
                    @Override
                    public void onResponse(CarPhotosEntity response) {

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        int count = 0, urlCount = 0;

                        for (CarPhotosEntity.VinsEntity vin : response.getVins()) {
                            mlist.add(count++, vin.getVin());
                            mUrlList.add(urlCount++, vin.getVin());
                            for (CarPhotosEntity.VinsEntity.ImagesEntity photo : vin.getImages()) {
                                mlist.add(count++, photo.getThumbnail());
                                mUrlList.add(urlCount++, photo.getUrl());
                            }
                        }

                        progressbar.setVisibility(View.GONE);

                        //数据获取之后再初始化RecyclerView
                        setupRecyclerView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressbar.setVisibility(View.GONE);
                        // 返回错误也要取消refresh
                        Helper.processVolleyErrorMsg(CarPhotoActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
