package cn.jdywl.driver.ui.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.driver.DOrderRvAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.libsrc.recylerview.EndlessRecyclerOnScrollListener;
import cn.jdywl.driver.model.OrderPage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DOrderFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = LogHelper.makeLogTag(DOrderFragment.class);

    @Bind(R.id.rv_dorder)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    private boolean bReload = false;   //
    private boolean loading = false;   //是否正在加载

    protected DOrderRvAdapter mAdapter;

    private OrderPage mData = new OrderPage();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static DOrderFragment newInstance() {
        return new DOrderFragment();
    }

    public DOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rv = inflater.inflate(R.layout.fragment_dorder, container, false);
        ButterKnife.bind(this, rv);

        /*
         * 设置下拉刷新
         */
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        /*
         * 设置RecyclerView
         */

        setupRecyclerView(mRecyclerView);

        //注册local Broadcast
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mRefreshReceiver, new IntentFilter(DMainActivity.REFRESH_ACTION));

        //加载数据
        //loadData();

        return rv;
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
        mAdapter = new DOrderRvAdapter(mData.getData());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // 设置上拉加载
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            // 上拉加载更多
            public void onLoadMore(int currentPage) {
                // do something...
                Toast.makeText(getActivity(), "正在加载下一页...", Toast.LENGTH_SHORT).show();
                loadMoreData(currentPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        bReload = true;
        loadData();
    }

    private void loadData() {
        if (bReload) {
            mData.setCurrentPage(0);
        }
        int page = mData.getCurrentPage() + 1;
        String url = ApiConfig.api_url + ApiConfig.DRIVER_ORDER_URL +
                "page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page;

        GsonRequest<OrderPage> myReq = new GsonRequest<OrderPage>(Request.Method.GET,
                url,
                OrderPage.class,
                null,
                new Response.Listener<OrderPage>() {
                    @Override
                    public void onResponse(OrderPage response) {

                        // 取消refresh
                        mSwipeLayout.setRefreshing(false);
                        if (bReload) {
                            mData.getData().clear();
                            bReload = false;
                        }
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        mData.setTotal(response.getTotal());
                        mData.setPerPage(response.getPerPage());
                        mData.setCurrentPage(response.getCurrentPage());
                        mData.setLastPage(response.getLastPage());
                        mData.setFrom(response.getFrom());
                        mData.setTo(response.getTo());
                        mData.getData().addAll(response.getData());

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消refresh
                        mSwipeLayout.setRefreshing(false);
                        bReload = false;

                        Helper.processVolleyErrorMsg(getActivity(), error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
    }


    private void loadMoreData(int current_page) {

        if (loading) {  //如果正在加载，直接返回
            return;
        }

        //到达尾页，直接返回
        if (current_page > mData.getLastPage()) {
            LogHelper.i(TAG, "已经到达最后一页");
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.DRIVER_ORDER_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + current_page;

        GsonRequest<OrderPage> myReq = new GsonRequest<OrderPage>(Request.Method.GET,
                url,
                OrderPage.class,
                null,
                new Response.Listener<OrderPage>() {
                    @Override
                    public void onResponse(OrderPage response) {
                        loading = false;

                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        mData.setTotal(response.getTotal());
                        mData.setPerPage(response.getPerPage());
                        mData.setCurrentPage(response.getCurrentPage());
                        mData.setLastPage(response.getLastPage());
                        mData.setFrom(response.getFrom());
                        mData.setTo(response.getTo());

                        mData.getData().addAll(response.getData());

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消正在加载
                        loading = false;

                        Helper.processVolleyErrorMsg(getActivity(), error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
    }

    @Override
    public void onResume() {
        super.onResume();
        bReload = true;
        loadData();
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        //取消local broadcast的注册
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRefreshReceiver);
    }


    private BroadcastReceiver mRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DMainActivity.REFRESH_ACTION)) {
                boolean enabled = intent.getBooleanExtra("enabled", true);
                mSwipeLayout.setEnabled(enabled);
            }

        }
    };

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
