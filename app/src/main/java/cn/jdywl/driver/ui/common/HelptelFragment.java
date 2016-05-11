package cn.jdywl.driver.ui.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.common.HelptelAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.libsrc.recylerview.DividerItemDecoration;
import cn.jdywl.driver.libsrc.recylerview.EndlessRecyclerOnScrollListener;
import cn.jdywl.driver.model.HelptelPage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.component.InputDialogFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class HelptelFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = LogHelper.makeLogTag(HelptelFragment.class);

    @Bind(R.id.recyclerView_helptel)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    private boolean bReload = false;   //
    private boolean loading = false;   //是否正在加载
    protected HelptelAdapter mAdapter;

    private HelptelPage mData = new HelptelPage();

    private String city = "*";


    InputDialogFragment newFragment;


    public HelptelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rv = inflater.inflate(R.layout.fragment_helptel, container, false);
        ButterKnife.bind(this, rv);

        /*
         * 设置下拉刷新
         */
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

        /*
         * 设置RecyclerView
         */
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // 提高性能
        mRecyclerView.setHasFixedSize(true);
        //设置为线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置adapter
        mAdapter = new HelptelAdapter(mData.getData());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // 设置上拉加载
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            // 上拉加载更多
            public void onLoadMore(int current_page) {
                // do something...
                Toast.makeText(getActivity(), "正在加载下一页...", Toast.LENGTH_SHORT).show();
                loadMoreData(current_page);
            }
        });

        mSwipeLayout.setRefreshing(true);
        //加载数据
        loadData();

        return rv;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
        String url = ApiConfig.api_url + ApiConfig.HELPTEL_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page +
                "&city=" + URLEncoder.encode(city);

        GsonRequest<HelptelPage> myReq = new GsonRequest<HelptelPage>(Request.Method.GET,
                url,
                HelptelPage.class,
                null,
                new Response.Listener<HelptelPage>() {
                    @Override
                    public void onResponse(HelptelPage response) {
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

        String url = ApiConfig.api_url + ApiConfig.HELPTEL_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + current_page +
                "&city=" + city;

        GsonRequest<HelptelPage> myReq = new GsonRequest<HelptelPage>(Request.Method.GET,
                url,
                HelptelPage.class,
                null,
                new Response.Listener<HelptelPage>() {
                    @Override
                    public void onResponse(HelptelPage response) {
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
    public void onStop() {
        super.onStop();
        //取消volley 连接
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    void setRefreshEnabled(boolean enabled) {
        mSwipeLayout.setEnabled(enabled);
    }

    //根据城市搜索
    void searchCity(String input) {
        if (input.length() == 0) {
            city = "*";
        } else {
            city = input;
        }

        //先清空再从服务器获取
        mData.getData().clear();
        bReload = true;
        mAdapter.notifyDataSetChanged();

        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                /*
                if(newFragment == null) {
                    newFragment = InputDialogFragment.newInstance("带路电话查询", getString(R.string.hint_helptel));
                }
                newFragment.show(getActivity().getSupportFragmentManager(), TAG);
                */

                //设置Layout和margins等参数
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(Helper.dpToPx(getActivity(), 24), Helper.dpToPx(getActivity(), 24),
                        Helper.dpToPx(getActivity(), 24), Helper.dpToPx(getActivity(), 24));

                //向LinearLayout加入输入框
                final EditText et_city = new EditText(getActivity());
                et_city.setLayoutParams(params);
                et_city.setFocusable(true);
                //限定长度为20
                et_city.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                et_city.setHint("输入城市名");
                et_city.setTextSize(16);
                et_city.setTextColor(getResources().getColorStateList(R.color.gray_title));

                layout.addView(et_city);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("带路电话查询").setNegativeButton("取消", null);
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        city = et_city.getText().toString();

                        if (city.length() == 0) {
                            city = "*";
                        }

                        //先清空再从服务器获取
                        mData.getData().clear();
                        bReload = true;
                        mAdapter.notifyDataSetChanged();
                        loadData();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
