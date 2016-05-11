package cn.jdywl.driver.ui.carowner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.carowner.CreditCompanyAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.CreditCompanyItem;
import cn.jdywl.driver.model.CreditCompanyPage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreditCompanyFragment extends BaseFragment {
    private static String TAG = LogHelper.makeLogTag(CreditCompanyFragment.class);

    private CreditCompanyPage mData = new CreditCompanyPage();
    protected CreditCompanyAdapter mAdapter;

    @Bind(R.id.rv_creditCompany)
    RecyclerView rvCreditCompany;

    public CreditCompanyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_company, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView(rvCreditCompany);

        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //设置RecyclerView
    private void setupRecyclerView(RecyclerView recyclerView) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // 提高性能
        recyclerView.setHasFixedSize(true);
        //设置为线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置adapter
        mAdapter = new CreditCompanyAdapter(mData.getData());
        mAdapter.setOnItemClickListener(new CreditCompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CreditCompanyItem item) {
                Intent returnIntent = new Intent();

                Bundle bundle = new Bundle();
                bundle.putParcelable("company", item);
                returnIntent.putExtras(bundle);

                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
        });
        mAdapter.setOnBtnClickListener(new CreditCompanyAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(CreditCompanyItem item) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                WebView wv = new WebView(getActivity());
                wv.loadUrl(ApiConfig.api_url + ApiConfig.WEB_CREDIT_COMPANY + item.getId());
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                alert.setView(wv);

                alert.setPositiveButton("关闭", null);
                alert.show();
            }
        });
        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void loadData() {
        int page = 0;
        String url = ApiConfig.api_url + ApiConfig.CREDITCOMPANY_URL +
                "page_size=" + ApiConfig.PAGE_SIZE +
                "&page=" + page;

        GsonRequest<CreditCompanyPage> myReq = new GsonRequest<CreditCompanyPage>(Request.Method.GET,
                url,
                CreditCompanyPage.class,
                null,
                new Response.Listener<CreditCompanyPage>() {
                    @Override
                    public void onResponse(CreditCompanyPage response) {
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
                        Helper.processVolleyErrorMsg(getActivity(), error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
