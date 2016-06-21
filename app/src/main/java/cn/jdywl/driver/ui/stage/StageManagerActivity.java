package cn.jdywl.driver.ui.stage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.Marker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.stage.CarStageAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.libsrc.recylerview.EndlessRecyclerOnScrollListener;
import cn.jdywl.driver.model.Stage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by Administrator on 2016/5/6.
 */
public class StageManagerActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(StageManagerActivity.class);

    public static final String REFRESH_ACTION = "StageManagerRefreshAction";
    @Bind(R.id.rv_market)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    private boolean bReload = false;   //
    private boolean loading = false;   //是否正在加载

    protected CarStageAdapter mAdapter;

    private List<Stage> mData = new ArrayList<>();

    boolean isMapView = false;
    private BeanLocation mLocationBean;
    // 搜索模块，也可去掉地图模块独立使用
    private Marker mMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stagemanager);
        setupToolbar();

        ButterKnife.bind(this);

        /*
         * 设置下拉刷新
         */
        mSwipeLayout.setEnabled(true);
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        setupRecyclerView(mRecyclerView);

        //注册local Broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mRefreshReceiver, new IntentFilter(StageManagerActivity.REFRESH_ACTION));

        init();
    }

    private void init() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        int position = requestCode;
        if (resultCode == Activity.RESULT_OK && data != null) {
            setLocation(position, data.getStringExtra("addressX"), data.getStringExtra("addressY"), data.getStringExtra("address"));
        }
    }

    private void setLocation(final int i, String lat, String lon, String Address) {
        AppConst.showDialog(this);
        String position = ApiConfig.STAGE_POSOTION_URL.replace("/", "/" + mData.get(i).getId() + "/");
        String url = ApiConfig.api_url + position;
        Map<String, String> params = new HashMap<String, String>();
        params.put("longitude", lon);
        params.put("latitude", lat);
        params.put("position", Address);
        params.put("address", Address);
        params.put("gathertime", AppConst.getCurTime());
        GsonRequest<Stage> myReq = new GsonRequest<Stage>(Request.Method.PUT,
                url,
                Stage.class,
                params,
                new Response.Listener<Stage>() {
                    @Override
                    public void onResponse(Stage response) {
                        AppConst.dismiss();
                        mData.set(i, response);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                AppConst.dismiss();
                mSwipeLayout.setRefreshing(false);
                bReload = false;

                Helper.processVolleyErrorMsg(StageManagerActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    public void setRefreshEnabled(boolean enabled) {
        Intent intent = new Intent(REFRESH_ACTION);
        intent.putExtra("enabled", enabled);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

    //设置RecyclerView
    private void setupRecyclerView(RecyclerView recyclerView) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // 提高性能
        recyclerView.setHasFixedSize(true);
        //设置为线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        //设置adapter
        mAdapter = new CarStageAdapter(mData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
//                if (mData.get(position).getLatitude() == 0 || TextUtils.isEmpty(mData.get(position).getPosition())) {
                startActivityForResult(new Intent(StageManagerActivity.this, GetAddressInMap.class), position);
//                } else {
//                    //打开详情
//
//                }
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // 设置上拉加载
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            // 上拉加载更多
            public void onLoadMore(int currentPage) {
                // do something...
                Toast.makeText(StageManagerActivity.this, "正在加载下一页...", Toast.LENGTH_SHORT).show();
                loadMoreData(currentPage);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bReload = true;
        mSwipeLayout.setRefreshing(true);
        loadData();
    }


    @Override
    public void onStop() {
        super.onStop();
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    @Override
    public void onRefresh() {
        bReload = true;
        loadData();
    }

    private void loadData() {
        String url = ApiConfig.api_url + ApiConfig.STAGE_OFMASTER_URL;
        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mSwipeLayout.setRefreshing(false);
                        mData.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Gson gson = new Gson();
                                Stage stage = gson.fromJson(response.getJSONObject(i).toString(), Stage.class);
                                mData.add(stage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                mSwipeLayout.setRefreshing(false);
                bReload = false;

                Helper.processVolleyErrorMsg(StageManagerActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }


    private void loadMoreData(int current_page) {

        if (loading) {  //如果正在加载，直接返回
            return;
        }

        //到达尾页，直接返回
        if (current_page > mData.size()) {
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.STAGE_OFMASTER_URL;

        GsonRequest<Stage> myReq = new GsonRequest<Stage>(Request.Method.GET,
                url,
                Stage.class,
                null,
                new Response.Listener<Stage>() {
                    @Override
                    public void onResponse(Stage response) {
                        loading = false;

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
//                        mData.setTotal(response.getTotal());
//                        mData.setPerPage(response.getPerPage());
//                        mData.setCurrentPage(response.getCurrentPage());
//                        mData.setLastPage(response.getLastPage());
//                        mData.setFrom(response.getFrom());
//                        mData.setTo(response.getTo());
//
//                        mData.getData().addAll(response.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消正在加载
                        loading = false;

                        Helper.processVolleyErrorMsg(StageManagerActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        //取消local broadcast的注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshReceiver);
    }

    private BroadcastReceiver mRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StageManagerActivity.REFRESH_ACTION)) {
                boolean enabled = intent.getBooleanExtra("enabled", true);
                mSwipeLayout.setEnabled(enabled);
            }

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
    }


}
