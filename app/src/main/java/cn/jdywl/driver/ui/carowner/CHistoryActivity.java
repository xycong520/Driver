package cn.jdywl.driver.ui.carowner;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.common.OrderHistoryAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderPage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public class CHistoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = LogHelper.makeLogTag(CHistoryActivity.class);
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lv_history)
    ListView lvHistory;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private OrderHistoryAdapter mAdapter;
    private boolean bReload = false;
    private boolean loading = false;   //是否正在加载
    private OrderPage mData = new OrderPage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chistory);
        ButterKnife.bind(this);
        setupToolbar();

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        mAdapter = new OrderHistoryAdapter(this, 0, mData.getData());
        lvHistory.setAdapter(mAdapter);
        lvHistory.setOnScrollListener(new EndlessScrollListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        swipeContainer.setRefreshing(true);
    }

    private void loadData() {
        if (bReload) {
            mData.setCurrentPage(0);
        }
        long page = mData.getCurrentPage() + 1;
        String url = ApiConfig.api_url + ApiConfig.CAROWNER_HISTORY_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page;

        GsonRequest<OrderPage> myReq = new GsonRequest<OrderPage>(Request.Method.GET,
                url,
                OrderPage.class,
                null,
                new Response.Listener<OrderPage>() {
                    @Override
                    public void onResponse(OrderPage response) {

                        //mInError = false;

                        // 取消refresh
                        swipeContainer.setRefreshing(false);
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

                        if (response.getTotal() == 0) {
                            Toast.makeText(CHistoryActivity.this, "历史订单为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消refresh
                        swipeContainer.setRefreshing(false);
                        bReload = false;

                        //mInError = true;

                        Helper.processVolleyErrorMsg(CHistoryActivity.this, error);
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
        if (current_page > mData.getLastPage()) {
            LogHelper.i(TAG, "已经到达最后一页");
            return;
        }

        int page = mData.getCurrentPage() + 1;
        String url = ApiConfig.api_url + ApiConfig.CAROWNER_HISTORY_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page;

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
                        Helper.processVolleyErrorMsg(CHistoryActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    @Override
    public void onRefresh() {
        bReload = true;
        loadData();
    }

    /**
     * Detects when user is close to the end of the current page and starts loading the next page
     * so the user will not have to wait (that much) for the next entries.
     *
     * @author Ognyan Bankov
     */
    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        // how many entries earlier to start loading next page
        private int visibleThreshold = 5;
        private int currentPage = 1;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;

                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.

                currentPage++;

                loadMoreData(currentPage);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }


        public int getCurrentPage() {
            return currentPage;
        }
    }


    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
