package cn.jdywl.driver.ui.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.carowner.COrderRvAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.libsrc.recylerview.EndlessRecyclerOnScrollListener;
import cn.jdywl.driver.model.OrderPage;
import cn.jdywl.driver.network.GsonRequest;


public class RMainFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = LogHelper.makeLogTag(RMainFragment.class);

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    private boolean bReload = false;
    private boolean loading = false;   //是否正在加载
    private OrderPage mData = new OrderPage();
    private COrderRvAdapter mAdapter;

    private String phone;

    public RMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fragment响应menu点击事件
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rmain, container, false);
        ButterKnife.bind(this, view);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

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
        mAdapter = new COrderRvAdapter(mData.getData(), COrderRvAdapter.FROM_RECEIVER);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // 设置上拉加载
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            // 上拉加载更多
            public void onLoadMore(int current_page) {
                // do something...
                Toast.makeText(getActivity(), "正在加载下一页...", Toast.LENGTH_SHORT).show();
                loadMoreData(current_page);
            }
        });


        if (AppConfig.isLogin()) {
            phone = AppConfig.phone;
        } else {
            phone = "*";
        }

        swipeContainer.setRefreshing(true);
        //加载数据
        loadData();

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                openSearchDialog();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void openSearchDialog() {
        //设置Layout和margins等参数
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Helper.dpToPx(getActivity(), 24), Helper.dpToPx(getActivity(), 24),
                Helper.dpToPx(getActivity(), 24), Helper.dpToPx(getActivity(), 24));

        //向LinearLayout加入输入框
        final EditText et_phone = new EditText(getActivity());
        et_phone.setLayoutParams(params);
        et_phone.setFocusable(true);
        //限定长度为20
        et_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        et_phone.setHint("输入收车人手机号");
        et_phone.setTextSize(16);
        et_phone.setTextColor(getResources().getColorStateList(R.color.gray_title));
        et_phone.setInputType(InputType.TYPE_CLASS_PHONE);

        layout.addView(et_phone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("位置和保险查询").setNegativeButton("取消", null);
        builder.setView(layout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                phone = et_phone.getText().toString();

                if (phone.length() == 0) {
                    phone = "*";
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
        String url = ApiConfig.api_url + ApiConfig.RECEIVER_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page +
                "&phone=" + phone;

        GsonRequest<OrderPage> myReq = new GsonRequest<OrderPage>(Request.Method.GET,
                url,
                OrderPage.class,
                null,
                new Response.Listener<OrderPage>() {
                    @Override
                    public void onResponse(OrderPage response) {
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 返回错误也要取消refresh
                        swipeContainer.setRefreshing(false);
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
            return;
        }

        String url = ApiConfig.api_url + ApiConfig.RECEIVER_URL +
                "&page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + current_page +
                "&phone=" + phone;

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
    public void onStop() {
        super.onStop();
        //取消volley 连接
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    void setRefreshEnabled(boolean enabled) {
        swipeContainer.setEnabled(enabled);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
